package com.jc.sgtasec.web;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.jc.sgtasec.model.TipoAtencion;
import com.jc.sgtasec.service.IHandlerExceptionService;
import com.jc.sgtasec.service.ITipoAtencionService;
import com.jc.sgtasec.web.dto.TipoAtencionDto;

@Controller
public class TipoAtencionController {

	private Logger logger = LogManager.getLogger(getClass());
	private ITipoAtencionService tipoAtencionService;
	private IHandlerExceptionService handlerExceptionService;

	public TipoAtencionController(ITipoAtencionService tipoAtencionService, IHandlerExceptionService handlerExceptionService) {
		super();
		this.tipoAtencionService = tipoAtencionService;
		this.handlerExceptionService = handlerExceptionService;
	}

	@GetMapping("/tipo_atencion")
	public String listTipoAtencion(Model model) {
		List<TipoAtencionDto> listTipoAtencionDto = new ArrayList<TipoAtencionDto>();

		for (TipoAtencion tipoAtencion : tipoAtencionService.getAllTipoAtencions()) {
			listTipoAtencionDto.add(tipoAtencionService.mapperToDTO(tipoAtencion));
		}

		model.addAttribute("tipo_atenciones", listTipoAtencionDto);
		return "tipo_atencion/tipo_atenciones";
	}

	@GetMapping("/tipo_atencion/nuevo")
	public String createTipoAtencionForm(Model model) {
		TipoAtencionDto tipoAtencionDto = new TipoAtencionDto();
		model.addAttribute("tipoAtencion", tipoAtencionDto);
		return "tipo_atencion/crear_tipo_atencion";
	}

	@PostMapping("/tipo_atencion")
	public String saveTipoAtencion(@ModelAttribute("tipoAtencion") @Valid TipoAtencionDto tipoAtencionDto,
			BindingResult result, Model model) {
		try {

			if (result.hasErrors()) {
				return "tipo_atencion/crear_tipo_atencion";
			}

			TipoAtencion tipoAtencion = tipoAtencionService.mapperToEntity(tipoAtencionDto);
			tipoAtencionService.saveTipoAtencion(tipoAtencion);
			return "redirect:/tipo_atencion";
		} catch (DataIntegrityViolationException ex) {
			logger.error(ex.getMessage());
			model.addAttribute("error", handlerExceptionService.customizeException(ex, "/tipo_atencion"));
			return "error/error";
		} catch (Exception e) {
			logger.error(e.getMessage());
			model.addAttribute("error", handlerExceptionService.customizeException(e, "/tipo_atencion"));
			return "error/error";
		}
	}

	@GetMapping("/tipo_atencion/editar/{id}")
	public String editTipoAtencionForm(@PathVariable Long id, Model model) {
		try {

			TipoAtencionDto tipoAtencionDto = tipoAtencionService
					.mapperToDTO(tipoAtencionService.getTipoAtencionById(id));
			model.addAttribute("tipoAtencion", tipoAtencionDto);
			return "tipo_atencion/editar_tipo_atencion";
		} catch (DataIntegrityViolationException ex) {
			logger.error(ex.getMessage());
			model.addAttribute("error", handlerExceptionService.customizeException(ex, "/tipo_atencion"));
			return "error/error";
		} catch (Exception e) {
			logger.error(e.getMessage());
			model.addAttribute("error", handlerExceptionService.customizeException(e, "/tipo_atencion"));
			return "error/error";
		}
	}

	@PostMapping("/tipo_atencion/{id}")
	public String updateTipoAtencion(@PathVariable Long id,
			@ModelAttribute("tipoAtencion") @Valid TipoAtencionDto tipoAtencionDto, BindingResult result, Model model) {
		System.out.println("Long id: " + id);

		try {

			if (result.hasErrors()) {
				System.out.println("result.hasErrors: " + id);

				return "tipo_atencion/editar_tipo_atencion";
			}

			// get from database by id
			TipoAtencion existingTipoAtencion = tipoAtencionService.getTipoAtencionById(id);
			existingTipoAtencion.setNombre(tipoAtencionDto.getNombre());
			existingTipoAtencion.setTiempoAtencion(tipoAtencionDto.getTiempoAtencion());
			// save updated object
			tipoAtencionService.updateTipoAtencion(existingTipoAtencion);
			return "redirect:/tipo_atencion";
		} catch (DataIntegrityViolationException ex) {
			logger.error(ex.getMessage());
			model.addAttribute("error", handlerExceptionService.customizeException(ex, "/tipo_atencion"));
			return "error/error";
		} catch (Exception e) {
			logger.error(e.getMessage());
			model.addAttribute("error", handlerExceptionService.customizeException(e, "/tipo_atencion"));
			return "error/error";
		}
	}

	@GetMapping("/tipo_atencion/{id}")
	public String deleteTipoAtencion(@PathVariable Long id, Model model) {
		try {
			tipoAtencionService.deleteTipoAtencionById(id);
			return "redirect:/tipo_atencion";
		} catch (DataIntegrityViolationException ex) {
			logger.error(ex.getMessage());
			model.addAttribute("error", handlerExceptionService.customizeException(ex, "/tipo_atencion"));
			return "error/error";
		} catch (Exception e) {
			logger.error(e.getMessage());
			model.addAttribute("error", handlerExceptionService.customizeException(e, "/tipo_atencion"));
			return "error/error";
		}
	}
}
