package com.jc.sgtasec.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jc.sgtasec.service.IHandlerExceptionService;
import com.jc.sgtasec.service.IUsuarioService;
import com.jc.sgtasec.web.dto.UsuarioDto;

@Controller
@RequestMapping("/registro")
public class UsuarioRegistrationController {

	private Logger logger = LogManager.getLogger(getClass());
	private IUsuarioService usuarioService;
	private IHandlerExceptionService handlerExceptionService;

	public UsuarioRegistrationController(IUsuarioService usuarioService, IHandlerExceptionService handlerExceptionService) {
		super();
		this.usuarioService = usuarioService;
		this.handlerExceptionService = handlerExceptionService;
	}

	@ModelAttribute("usuario")
	public UsuarioDto usuarioDto() {
		return new UsuarioDto();
	}

	@GetMapping
	public String showRegistrationForm() {
		return "usuarios/registro";
	}

	@PostMapping
	public String registerUserAccount(@ModelAttribute("usuario") UsuarioDto usuarioDto, Model model) {
		try {
			usuarioService.save(usuarioDto);
			return "redirect:/registro?success";
		} catch (DataIntegrityViolationException ex) {
			logger.error(ex.getMessage());
			model.addAttribute("error", handlerExceptionService.customizeException(ex, "/registro"));
			return "error/error";
		} catch (Exception e) {
			logger.error(e.getMessage());
			model.addAttribute("error", handlerExceptionService.customizeException(e, "/registro"));
			return "error/error";
		}
	}
}
