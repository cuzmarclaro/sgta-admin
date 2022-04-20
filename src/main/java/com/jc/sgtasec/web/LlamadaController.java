package com.jc.sgtasec.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.jc.sgtasec.model.Llamada;
import com.jc.sgtasec.service.IHandlerExceptionService;
import com.jc.sgtasec.service.ILlamadaService;
import com.jc.sgtasec.web.dto.LlamadaDto;

@Controller
public class LlamadaController {

	private Logger logger = LogManager.getLogger(getClass());
	private ILlamadaService llamadaService;	
	private IHandlerExceptionService handlerExceptionService;

	public LlamadaController(ILlamadaService llamadaService, IHandlerExceptionService handlerExceptionService) {
		super();
		this.llamadaService = llamadaService;
		this.handlerExceptionService = handlerExceptionService;
	}

	@GetMapping("/llamadas")
	public String listaLlamadasConTurnos(Model model) {
		List<LlamadaDto> listLlamadasDto = new ArrayList<LlamadaDto>();

		for (Llamada llamada : llamadaService.listaLlamadasConTurnos()) {
			LlamadaDto llamadaDto = llamadaService.mapperToDTO(llamada);
			llamadaDto.setTurno(llamada.getAtencion().getTurno());
			llamadaDto.setTipoAtencion(llamada.getAtencion().getTipoAtencion());
			listLlamadasDto.add(llamadaDto);
		}

		model.addAttribute("llamadas", listLlamadasDto);
		return "llamadas/llamadas";
	}

	@GetMapping("/llamadas/nuevo")
	public String createLlamadaForm(Model model) {
		LlamadaDto llamadaDto = new LlamadaDto();
		model.addAttribute("llamada", llamadaDto);
		return "llamadas/crear_llamada";
	}

	public void saveLlamada(Llamada llamada, Model model) {
		try {
			
			llamadaService.saveLlamada(llamada);
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
	}

	@GetMapping("/llamadas/editar/{id}")
	public String editLlamadaForm(@PathVariable Long id, Model model) {

		LlamadaDto llamadaDto = llamadaService.mapperToDTO(llamadaService.getLlamadaById(id));
		model.addAttribute("llamada", llamadaDto);
		return "llamadas/editar_llamada";
	}

	@PostMapping("/llamadas/{id}")
	public String updateLlamada(@PathVariable Long id, @ModelAttribute("llamada") LlamadaDto llamadaDto, Model model) {
		try {

			Llamada existingLlamada = llamadaService.getLlamadaById(id);
			existingLlamada.setAtencion(llamadaDto.getAtencion());
			existingLlamada.setUsuario(llamadaDto.getUsuario());
			existingLlamada.setFechaCreacion(llamadaDto.getFechaCreacion());
			llamadaService.updateLlamada(existingLlamada);
			return "redirect:/llamadas";
		} catch (DataIntegrityViolationException ex) {
			logger.error(ex.getMessage());
			model.addAttribute("error", handlerExceptionService.customizeException(ex, "/llamadas"));
			return "error/error";
		} catch (Exception e) {
			logger.error(e.getMessage());
			model.addAttribute("error", handlerExceptionService.customizeException(e, "/llamadas"));
			return "error/error";
		}
	}

	@GetMapping("/llamadas/{id}")
	public String deleteLlamada(@PathVariable Long id, Model model) {
		try {
			llamadaService.deleteLlamadaById(id);
			return "redirect:/llamadas";
		} catch (DataIntegrityViolationException ex) {
			logger.error(ex.getMessage());
			model.addAttribute("error", handlerExceptionService.customizeException(ex, "/llamadas"));
			return "error/error";
		} catch (Exception e) {
			logger.error(e.getMessage());
			model.addAttribute("error", handlerExceptionService.customizeException(e, "/llamadas"));
			return "error/error";
		}
	}
}
