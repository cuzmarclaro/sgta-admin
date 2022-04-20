package com.jc.sgtasec.web;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.jc.sgtasec.config.CustomProperties;
import com.jc.sgtasec.model.Atencion;
import com.jc.sgtasec.model.HistorialAtencion;
import com.jc.sgtasec.model.Llamada;
import com.jc.sgtasec.model.Turno;
import com.jc.sgtasec.service.IAtencionService;
import com.jc.sgtasec.service.IClienteService;
import com.jc.sgtasec.service.IHandlerExceptionService;
import com.jc.sgtasec.service.IHistorialAtencionService;
import com.jc.sgtasec.service.ILlamadaService;
import com.jc.sgtasec.service.ITipoAtencionService;
import com.jc.sgtasec.service.ITurnoService;
import com.jc.sgtasec.service.IUsuarioService;
import com.jc.sgtasec.web.dto.AtencionDto;
import com.jc.sgtasec.web.dto.ClienteDto;
import com.jc.sgtasec.web.dto.TipoAtencionDto;

@Controller
public class AtencionController {

	private Logger logger = LogManager.getLogger(getClass());
	private Authentication auth;

	private IAtencionService atencionService;
	private IClienteService clienteService;
	private ITipoAtencionService tipoAtencionService;
	private ITurnoService turnoService;
	private ILlamadaService llamadaService;
	private IUsuarioService usuarioService;
	private CustomProperties customProperties;
	private IHistorialAtencionService historialAtencionService;
	private IHandlerExceptionService handlerExceptionService;
	
	@Autowired
	private SimpMessagingTemplate webSocket;
	
	public AtencionController(IAtencionService atencionService, IClienteService clienteService,
			ITipoAtencionService tipoAtencionService, ITurnoService turnoService, ILlamadaService llamadaService,
			IUsuarioService usuarioService, CustomProperties customProperties, IHistorialAtencionService historialAtencionService, 
			IHandlerExceptionService handlerExceptionService) {
		super();
		this.atencionService = atencionService;
		this.clienteService = clienteService;
		this.tipoAtencionService = tipoAtencionService;
		this.turnoService = turnoService;
		this.llamadaService = llamadaService;
		this.usuarioService = usuarioService;
		this.customProperties = customProperties;
		this.historialAtencionService = historialAtencionService;
		this.handlerExceptionService = handlerExceptionService;
	}

	@GetMapping("/atenciones")
	public String listAtenciones(Model model) {

		List<AtencionDto> listAtencionesDto = new ArrayList<AtencionDto>();

		for (Atencion atencion : atencionService.getAtencionesConCantidadDeLlamadas()) {
			listAtencionesDto.add(atencionService.mapperToDTO(atencion));
		}
		model.addAttribute("atenciones", listAtencionesDto);
		return "atenciones/atenciones";
	}

	@GetMapping("/atenciones/nuevo")
	public String createAtencionForm(Model model) {
		AtencionDto atencionDto = new AtencionDto();
		List<ClienteDto> listClientesDto = clienteService.getAllClientesDtoSinTurnoActivo();
		List<TipoAtencionDto> listTipoAtencionDto = tipoAtencionService
				.getListTipoAtencionDTO(tipoAtencionService.getAllTipoAtencions());
		model.addAttribute("atencion", atencionDto);
		model.addAttribute("clientes", listClientesDto);
		model.addAttribute("tipoAtenciones", listTipoAtencionDto);
		return "atenciones/crear_atencion";
	}

	@PostMapping("/atenciones")
	public String saveAtencion(@ModelAttribute("atencion") @Valid AtencionDto atencionDto, BindingResult result,
			Model model) {
		try {
			if (result.hasErrors()) {
				return "atenciones/crear_atencion";
			}

			Turno turno = new Turno();
			turno = turnoService.getTurnoDisponible();
			atencionDto.setTurno(turno);
			atencionDto.setFechaCreacion(LocalDateTime.now());
			Atencion atencion = atencionService.mapperToEntity(atencionDto);
			atencionService.saveAtencion(atencion);
			/*			
			* Estado Turno 			
			* 0 Disponible, Turno disponible para asignar a una atención.
			* 1 Asignado, Turno asignado a una atención.
			* 2 En atención, Turno siendo atendido. 
			* 3 Finalizado, Turno con cuya atención finalizo.
			* 4 Descartado, Turno descartado de los pendientes para atender.
			*/
			turno.setEstado(1);
			turnoService.saveTurno(turno);
			
			// webSocket avisar cambios a cliente en método saveAtencion
			webSocket.convertAndSend("/topic/greetings", new String("email_vacio"));
			
			return "redirect:/atenciones";
		} catch (DataIntegrityViolationException ex) {
			logger.error(ex.getMessage());
			model.addAttribute("error", handlerExceptionService.customizeException(ex, "/atenciones"));
			return "error/error";
		} catch (Exception e) {
			logger.error(e.getMessage());
			model.addAttribute("error", handlerExceptionService.customizeException(e, "/atenciones"));
			return "error/error";
		}
	}

	@GetMapping("/atenciones/{id}")
	public String deleteAtencion(@PathVariable Long id, Model model) {
		try {

			Atencion existingAtencion = atencionService.getAtencionById(id);
			List<Llamada> llamadas = llamadaService.findByAtencion(existingAtencion);

			for (Llamada llamada : llamadas) {
				if (llamada.getAtencion().getId() == id) {
					llamadaService.deleteLlamadaById(llamada.getId());
				}
			}

			Turno turno = turnoService.getTurnoById(existingAtencion.getTurno().getId());
			/*			
			* Estado Turno 			
			* 0 Disponible, Turno disponible para asignar a una atención.
			* 1 Asignado, Turno asignado a una atención.
			* 2 En atención, Turno siendo atendido. 
			* 3 Finalizado, Turno con cuya atención finalizo.
			* 4 Descartado, Turno descartado de los pendientes para atender.
			*/
			turno.setEstado(4);
			turnoService.saveTurno(turno);

			atencionService.deleteAtencionById(id);
			
			// webSocket avisar cambios a cliente en método deleteAtencion
			webSocket.convertAndSend("/topic/greetings", new String("email_vacio"));
			
			return "redirect:/atenciones";
		} catch (DataIntegrityViolationException ex) {
			logger.error(ex.getMessage());
			model.addAttribute("error", handlerExceptionService.customizeException(ex, "/atenciones"));
			return "error/error";
		} catch (Exception e) {
			logger.error(e.getMessage());
			model.addAttribute("error", handlerExceptionService.customizeException(e, "/atenciones"));
			return "error/error";
		}
	}

	@GetMapping("/atenciones/llamar/{id}")
	public String realizarLlamadoParaAtencion(@PathVariable Long id, Model model) {
		try {

			Atencion existingAtencion = atencionService.getAtencionById(id);
			Long cantidad = customProperties.getCantidadMaximaLlamadas();
			List<Llamada> llamadas = llamadaService.findByAtencion(existingAtencion);

			if (llamadas.size() < cantidad) {
				Llamada llamada = new Llamada();
				llamada.setFechaCreacion(LocalDateTime.now());
				llamada.setAtencion(existingAtencion);
				this.auth = SecurityContextHolder.getContext().getAuthentication();
				llamada.setUsuario(usuarioService.getUsuarioByEmail(auth.getName()));
				llamadaService.saveLlamada(llamada);
			}
			
			// webSocket avisar cambios a cliente en método realizarLlamadoParaAtencion
			webSocket.convertAndSend("/topic/greetings", new String("email_vacio"));

			return "redirect:/atenciones";
		} catch (DataIntegrityViolationException ex) {
			logger.error(ex.getMessage());
			model.addAttribute("error", handlerExceptionService.customizeException(ex, "/atenciones"));
			return "error/error";
		} catch (Exception e) {
			logger.error(e.getMessage());
			model.addAttribute("error", handlerExceptionService.customizeException(e, "/atenciones"));
			return "error/error";
		}
	}

	@GetMapping("/atenciones/atender/{id}")
	public String realizarAccionAtenderParaUnaAtencion(@PathVariable Long id, Model model) {

		try {

			Atencion existingAtencion = atencionService.getAtencionById(id);

			Turno turno = turnoService.getTurnoById(existingAtencion.getTurno().getId());
			/*			
			* Estado Turno 			
			* 0 Disponible, Turno disponible para asignar a una atención.
			* 1 Asignado, Turno asignado a una atención.
			* 2 En atención, Turno siendo atendido. 
			* 3 Finalizado, Turno con cuya atención finalizo.
			* 4 Descartado, Turno descartado de los pendientes para atender.
			*/
			turno.setEstado(2);
			turnoService.saveTurno(turno);
			
			// webSocket avisar cambios a cliente en método realizarAccionAtenderParaUnaAtencion
			webSocket.convertAndSend("/topic/greetings", new String("email_vacio"));

			return "redirect:/atenciones";
		} catch (DataIntegrityViolationException ex) {
			logger.error(ex.getMessage());
			model.addAttribute("error", handlerExceptionService.customizeException(ex, "/atenciones"));
			return "error/error";
		} catch (Exception e) {
			logger.error(e.getMessage());
			model.addAttribute("error", handlerExceptionService.customizeException(e, "/atenciones"));
			return "error/error";
		}
	}

	@GetMapping("/atenciones/finalizar/{id}")
	public String finalizarAtencion(@PathVariable Long id, Model model) {

		try {

			Atencion existingAtencion = atencionService.getAtencionById(id);

			Turno turno = turnoService.getTurnoById(existingAtencion.getTurno().getId());
			/*			
			* Estado Turno 			
			* 0 Disponible, Turno disponible para asignar a una atención.
			* 1 Asignado, Turno asignado a una atención.
			* 2 En atención, Turno siendo atendido. 
			* 3 Finalizado, Turno con cuya atención finalizo.
			* 4 Descartado, Turno descartado de los pendientes para atender.
			*/
			turno.setEstado(3);
			turnoService.saveTurno(turno);

			// webSocket avisar cambios a cliente en método finalizarAtencion
			webSocket.convertAndSend("/topic/greetings", new String("email_vacio"));
			
			return "redirect:/atenciones";
		} catch (DataIntegrityViolationException ex) {
			logger.error(ex.getMessage());
			model.addAttribute("error", handlerExceptionService.customizeException(ex, "/atenciones"));
			return "error/error";
		} catch (Exception e) {
			logger.error(e.getMessage());
			model.addAttribute("error", handlerExceptionService.customizeException(e, "/atenciones"));
			return "error/error";
		}
	}

	@GetMapping("/atenciones/archivar/{id}")
	 @Transactional
	public String archivarAtencion(@PathVariable Long id, Model model) {

		try {
			Atencion existingAtencion = atencionService.getAtencionById(id);
		
			// guardar en historial la atención 
			HistorialAtencion historialAtencion = new HistorialAtencion();
			
			historialAtencion.setIdAtencion(existingAtencion.getId());
			historialAtencion.setFechaCreacionAtencion(existingAtencion.getFechaCreacion());
			historialAtencion.setApellidoPaternoCliente(existingAtencion.getCliente().getApellidoPaterno());
			historialAtencion.setApellidoMaternoCliente(existingAtencion.getCliente().getApellidoMaterno());
			historialAtencion.setNombreCliente(existingAtencion.getCliente().getNombre());
			historialAtencion.setEmailCliente(existingAtencion.getCliente().getEmail());
			historialAtencion.setRutCliente(existingAtencion.getCliente().getRut());
			historialAtencion.setNombreTipoAtencion(existingAtencion.getTipoAtencion().getNombre());
			historialAtencion.setTiempoAtencion(existingAtencion.getTipoAtencion().getTiempoAtencion());
			historialAtencion.setTurnoAtencion(existingAtencion.getTurno().getTurnoAtencion());
						
			List<Llamada> llamadas = llamadaService.findByAtencion(existingAtencion);
			logger.info("llamada.getFechaCreacion: " + llamadas);

			for (Llamada llamada : llamadas) {
				
				historialAtencion.setFechaCreacionLlamada(llamada.getFechaCreacion());
			}			
			
			historialAtencionService.saveHistorialAtencion(historialAtencion);
			llamadaService.deleteByAtencion(existingAtencion);
			atencionService.deleteAtencionById(id);

			return "redirect:/atenciones";
		} catch (DataIntegrityViolationException ex) {
			logger.error(ex.getMessage());
			model.addAttribute("error", handlerExceptionService.customizeException(ex, "/atenciones"));
			return "error/error";
		} catch (Exception e) {
			logger.error(e.getMessage());
			model.addAttribute("error", handlerExceptionService.customizeException(e, "/atenciones"));
			return "error/error";
		}
	}

}
