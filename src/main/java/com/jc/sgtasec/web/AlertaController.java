package com.jc.sgtasec.web;

import java.time.LocalDateTime;
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
import com.jc.sgtasec.model.Alerta;
import com.jc.sgtasec.service.IAlertaService;
import com.jc.sgtasec.service.IHandlerExceptionService;
import com.jc.sgtasec.web.dto.AlertaDto;

@Controller
public class AlertaController {

	private Logger logger = LogManager.getLogger(getClass());
	private IAlertaService alertaService;
	private IHandlerExceptionService handlerExceptionService;

	public AlertaController(IAlertaService alertaService, IHandlerExceptionService handlerExceptionService) {
		super();
		this.alertaService = alertaService;
		this.handlerExceptionService = handlerExceptionService;
	}

	@GetMapping("/alertas")
	public String listAlertas(Model model) {

		try {
			List<AlertaDto> listAlertaDtos = new ArrayList<AlertaDto>();

			for (Alerta alerta : alertaService.getAllAlertas()) {
				listAlertaDtos.add(alertaService.mapperToDTO(alerta));
			}
			model.addAttribute("alertas", listAlertaDtos);
			return "alertas/alertas";
		} catch (Exception e) {
			logger.error(e.getMessage());
			model.addAttribute("error", handlerExceptionService.customizeException(e, "/alertas"));
			return "error/error";
		}
	}

	@GetMapping("/alertas/nuevo")
	public String createAlertaForm(Model model) {

		try {
			AlertaDto alertaDto = new AlertaDto();
			model.addAttribute("alerta", alertaDto);
			return "alertas/crear_alerta";
		} catch (Exception e) {
			logger.error(e.getMessage());
			model.addAttribute("error", handlerExceptionService.customizeException(e, "/alertas"));
			return "error/error";
		}
	}

	@PostMapping("/alertas")
	public String saveAlerta(@ModelAttribute("alerta") @Valid AlertaDto alertaDto, BindingResult result, Model model) {

		try {
			if (result.hasErrors()) {
				return "alertas/crear_alerta";
			}

			Alerta alerta = alertaService.mapperToEntity(alertaDto);
			alerta.setFechaCreacion(LocalDateTime.now());
			alertaService.saveAlerta(alerta);
			return "redirect:/alertas";
		} catch (DataIntegrityViolationException ex) {
			logger.error(ex.getMessage());
			model.addAttribute("error", handlerExceptionService.customizeException(ex, "/alertas"));
			return "error/error";
		} catch (Exception e) {
			logger.error(e.getMessage());
			model.addAttribute("error", handlerExceptionService.customizeException(e, "/alertas"));
			return "error/error";
		}
	}

	@GetMapping("/alertas/editar/{id}")
	public String editAlertaForm(@PathVariable Long id, Model model) {

		try {
			AlertaDto alertaDto = alertaService.mapperToDTO(alertaService.getAlertaById(id));
			model.addAttribute("alerta", alertaDto);
			return "alertas/editar_alerta";
		} catch (DataIntegrityViolationException ex) {
			logger.error(ex.getMessage());
			model.addAttribute("error", handlerExceptionService.customizeException(ex, "/alertas"));
			return "error/error";
		} catch (Exception e) {
			logger.error(e.getMessage());
			model.addAttribute("error", handlerExceptionService.customizeException(e, "/alertas"));
			return "error/error";
		}
	}

	@PostMapping("/alertas/{id}")
	public String updateAlerta(@PathVariable Long id, @ModelAttribute("alerta") @Valid AlertaDto alertaDto,
			BindingResult result, Model model) {

		try {
			if (result.hasErrors()) {
				return "alertas/editar_alerta";
			}
			// get Alerta from database by id
			Alerta existingAlerta = alertaService.getAlertaById(id);
			// existingAlerta.setId(id);
			existingAlerta.setColor(alertaDto.getColor());
			existingAlerta.setColorHtml(alertaDto.getColorHtml());
			existingAlerta.setDuracionDesde(alertaDto.getDuracionDesde());
			existingAlerta.setDuracionHasta(alertaDto.getDuracionHasta());
			existingAlerta.setDescripcion(alertaDto.getDescripcion());
			existingAlerta.setFechaCreacion(LocalDateTime.now());

			// save updated Alerta object
			alertaService.updateAlerta(existingAlerta);
			return "redirect:/alertas";
		} catch (DataIntegrityViolationException ex) {
			logger.error(ex.getMessage());
			model.addAttribute("error", handlerExceptionService.customizeException(ex, "/alertas"));
			return "error/error";
		} catch (Exception e) {
			logger.error(e.getMessage());
			model.addAttribute("error", handlerExceptionService.customizeException(e, "/alertas"));
			return "error/error";
		}
	}

	@GetMapping("/alertas/{id}")
	public String deleteAlerta(@PathVariable Long id, Model model) {

		try {
			alertaService.deleteAlertaById(id);
			return "redirect:/alertas";
		} catch (DataIntegrityViolationException ex) {
			logger.error(ex.getMessage());
			model.addAttribute("error", ex.getRootCause().getMessage());
			return "error/error";
		} catch (Exception e) {
			logger.error(e.getMessage());
			model.addAttribute("error", e.getMessage());
			return "error/error";
		}
	}
}
