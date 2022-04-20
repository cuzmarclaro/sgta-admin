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
import com.jc.sgtasec.model.Cliente;
import com.jc.sgtasec.service.IClienteService;
import com.jc.sgtasec.service.IHandlerExceptionService;
import com.jc.sgtasec.web.dto.ClienteDto;

@Controller
public class ClienteController {

	private Logger logger = LogManager.getLogger(getClass());

	private IClienteService clienteService;
	private IHandlerExceptionService handlerExceptionService;

	public ClienteController(IClienteService clienteService, IHandlerExceptionService handlerExceptionService) {
		super();
		this.clienteService = clienteService;
		this.handlerExceptionService = handlerExceptionService;
	}

	@GetMapping("/clientes")
	public String listClientes(Model model) {
		List<ClienteDto> listClientesDto = new ArrayList<ClienteDto>();

		for (Cliente cliente : clienteService.getAllClientes()) {
			listClientesDto.add(clienteService.mapperToDTO(cliente));
		}

		model.addAttribute("clientes", listClientesDto);
		return "clientes/clientes";
	}

	@GetMapping("/clientes/nuevo")
	public String createClienteForm(Model model) {
		ClienteDto clienteDto = new ClienteDto();
		model.addAttribute("cliente", clienteDto);
		return "clientes/crear_cliente";
	}

	@PostMapping("/clientes")
	public String saveCliente(@ModelAttribute("cliente") @Valid ClienteDto clienteDto, BindingResult result,
			Model model) {
		try {

			if (result.hasErrors()) {
				return "clientes/crear_cliente";
			}

			Cliente cliente = clienteService.mapperToEntity(clienteDto);
			clienteService.saveCliente(cliente);
			return "redirect:/clientes";
		} catch (DataIntegrityViolationException ex) {
			logger.error(ex.getMessage());
			model.addAttribute("error", handlerExceptionService.customizeException(ex, "/clientes"));
			return "error/error";
		} catch (Exception e) {
			logger.error(e.getMessage());
			model.addAttribute("error", handlerExceptionService.customizeException(e, "/clientes"));
			return "error/error";
		}
	}

	@GetMapping("/clientes/editar/{id}")
	public String editClienteForm(@PathVariable Long id, Model model) {

		ClienteDto clienteDto = clienteService.mapperToDTO(clienteService.getClienteById(id));
		model.addAttribute("cliente", clienteDto);
		return "clientes/editar_cliente";
	}

	@PostMapping("/clientes/{id}")
	public String updateCliente(@PathVariable Long id, @ModelAttribute("cliente") @Valid ClienteDto clienteDto,
			BindingResult result, Model model) {
		try {

			if (result.hasErrors()) {
				return "clientes/editar_cliente";
			}

			// get Cliente from database by id
			Cliente existingCliente = clienteService.getClienteById(id);
			// existingCliente.setId(id);
			existingCliente.setNombre(clienteDto.getNombre());
			existingCliente.setApellidoPaterno(clienteDto.getApellidoPaterno());
			existingCliente.setApellidoMaterno(clienteDto.getApellidoMaterno());
			existingCliente.setEmail(clienteDto.getEmail());
			existingCliente.setRut(clienteDto.getRut());
			// save updated Cliente object
			clienteService.updateCliente(existingCliente);
			return "redirect:/clientes";
		} catch (DataIntegrityViolationException ex) {
			logger.error(ex.getMessage());
			model.addAttribute("error", handlerExceptionService.customizeException(ex, "/clientes"));
			return "error/error";
		} catch (Exception e) {
			logger.error(e.getMessage());
			model.addAttribute("error", handlerExceptionService.customizeException(e, "/clientes"));
			return "error/error";
		}
	}

	@GetMapping("/clientes/{id}")
	public String deleteCliente(@PathVariable Long id, Model model) {
		try {
			clienteService.deleteClienteById(id);
			return "redirect:/clientes";
		} catch (DataIntegrityViolationException ex) {
			logger.error(ex.getMessage());
			model.addAttribute("error", handlerExceptionService.customizeException(ex, "/clientes"));
			return "error/error";
		} catch (Exception e) {
			logger.error(e.getMessage());
			model.addAttribute("error", handlerExceptionService.customizeException(e, "/clientes"));
			return "error/error";
		}
	}
}