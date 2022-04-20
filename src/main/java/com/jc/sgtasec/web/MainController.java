package com.jc.sgtasec.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

	Logger logger = LogManager.getLogger(getClass());
	private Authentication auth;

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/")
	public String home() {
		this.auth = SecurityContextHolder.getContext().getAuthentication();
		logger.info("Usuario: " + auth.getName());
		logger.info("Ingreso a pagina index ");
		return "index";
	}
}
