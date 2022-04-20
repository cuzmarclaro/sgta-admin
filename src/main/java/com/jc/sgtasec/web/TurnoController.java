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
import com.jc.sgtasec.model.Turno;
import com.jc.sgtasec.service.IHandlerExceptionService;
import com.jc.sgtasec.service.ITurnoService;
import com.jc.sgtasec.web.dto.TurnoDto;

@Controller
public class TurnoController {

	private Logger logger = LogManager.getLogger(getClass());
	private ITurnoService turnoService;
	private IHandlerExceptionService handlerExceptionService;

	public TurnoController(ITurnoService turnoService, IHandlerExceptionService handlerExceptionService) {
		super();
		this.turnoService = turnoService;
		this.handlerExceptionService = handlerExceptionService;
	}

	@GetMapping("/turnos")
	public String listTurnos(Model model) {
		List<TurnoDto> listTurnoDto = new ArrayList<TurnoDto>();

		for (Turno turno : turnoService.getAllTurnos()) {
			listTurnoDto.add(turnoService.mapperToDTO(turno));
		}

		model.addAttribute("turnos", listTurnoDto);
		return "turnos/turnos";
	}

	@GetMapping("/turnos/nuevo")
	public String createTurnoForm(Model model) {
		TurnoDto turnoDto = new TurnoDto();
		model.addAttribute("turno", turnoDto);
		return "turnos/crear_turno";
	}

	@PostMapping("/turnos")
	public String saveTurno(@ModelAttribute("turno") @Valid TurnoDto turnoDto, BindingResult result, Model model) {
		try {

			if (result.hasErrors()) {
				return "turnos/crear_turno";
			}

			Turno turno = turnoService.mapperToEntity(turnoDto);
			turnoService.saveTurno(turno);
			return "redirect:/turnos";
		} catch (DataIntegrityViolationException ex) {
			logger.error(ex.getMessage());
			model.addAttribute("error", handlerExceptionService.customizeException(ex, "/turnos"));
			return "error/error";
		} catch (Exception e) {
			logger.error(e.getMessage());
			model.addAttribute("error", handlerExceptionService.customizeException(e, "/turnos"));
			return "error/error";
		}
	}

	@GetMapping("/turnos/editar/{id}")
	public String editTurnoForm(@PathVariable Long id, Model model) {

		TurnoDto turnoDto = turnoService.mapperToDTO(turnoService.getTurnoById(id));
		model.addAttribute("turno", turnoDto);
		return "turnos/editar_turno";
	}

	@PostMapping("/turnos/{id}")
	public String updateTurno(@PathVariable Long id, @ModelAttribute("turno") @Valid TurnoDto turnoDto,
			BindingResult result, Model model) {
		try {

			if (result.hasErrors()) {
				return "turnos/editar_turno";
			}

			Turno existingTurno = turnoService.getTurnoById(id);
			existingTurno.setTurnoAtencion(turnoDto.getTurnoAtencion());
			existingTurno.setEstado(turnoDto.getEstado());
			turnoService.updateTurno(existingTurno);
			return "redirect:/turnos";
		} catch (DataIntegrityViolationException ex) {
			logger.error(ex.getMessage());
			model.addAttribute("error", handlerExceptionService.customizeException(ex, "/turnos"));
			return "error/error";
		} catch (Exception e) {
			logger.error(e.getMessage());
			model.addAttribute("error", handlerExceptionService.customizeException(e, "/turnos"));
			return "error/error";
		}
	}

	@GetMapping("/turnos/{id}")
	public String deleteTurno(@PathVariable Long id, Model model) {
		try {
			turnoService.deleteTurnoById(id);
			return "redirect:/turnos";
		} catch (DataIntegrityViolationException ex) {
			logger.error(ex.getMessage());
			model.addAttribute("error", handlerExceptionService.customizeException(ex, "/turnos"));
			return "error/error";
		} catch (Exception e) {
			logger.error(e.getMessage());
			model.addAttribute("error", handlerExceptionService.customizeException(e, "/turnos"));
			return "error/error";
		}
	}

}
