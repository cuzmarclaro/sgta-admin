package com.jc.sgtasec.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.googlecode.jmapper.JMapper;
import com.jc.sgtasec.model.Cliente;
import com.jc.sgtasec.repository.IClienteRepository;
import com.jc.sgtasec.web.dto.ClienteDto;

@Service
public class ClienteServiceImpl implements IClienteService {
	
	private Logger logger = LogManager.getLogger(getClass());
	private Authentication auth;
	private IClienteRepository clienteRepository;	
	private JMapper<Cliente, ClienteDto> mapperToEntity;
	private JMapper<ClienteDto, Cliente> mapperToDTO;
	
	public ClienteServiceImpl(IClienteRepository clienteRepository) {
		super();
		this.clienteRepository = clienteRepository;
		this.mapperToEntity = new JMapper<>(Cliente.class, ClienteDto.class);
		this.mapperToDTO = new JMapper<>(ClienteDto.class, Cliente.class);
	}

	@Override
	public List<Cliente> getAllClientes() {
		return clienteRepository.findAll();
	}
	

	@Override
	public Cliente saveCliente(Cliente cliente) {
		this.auth = SecurityContextHolder.getContext().getAuthentication();
		logger.info("Usuario: " + auth.getName());
		logger.info("saveCliente(Cliente cliente) " + cliente.toString());
		return clienteRepository.save(cliente);
	}

	@Override
	public Cliente getClienteById(Long id) {
		return clienteRepository.findById(id).get();
	}

	@Override
	public Cliente updateCliente(Cliente cliente) {
		this.auth = SecurityContextHolder.getContext().getAuthentication();
		logger.info("Usuario: " + auth.getName());
		logger.info("updateCliente(Cliente cliente) " + cliente.toString());
		return clienteRepository.save(cliente);
	}

	@Override
	public void deleteClienteById(Long id) {
		this.auth = SecurityContextHolder.getContext().getAuthentication();
		logger.info("Usuario: " + auth.getName());
		logger.info("deleteClienteById(Long id) " + id);
		clienteRepository.deleteById(id);
		
	}

	@Override
	public Cliente mapperToEntity(ClienteDto source) {
		Cliente cliente = new Cliente();
		cliente = mapperToEntity.getDestination(source);		
		return cliente;
	}

	@Override
	public ClienteDto mapperToDTO(Cliente source) {
		ClienteDto clienteDTO = new ClienteDto();
		clienteDTO = mapperToDTO.getDestination(source);		
		return clienteDTO;
	}

	@Override
	public List<ClienteDto> getListClientesDTO(List<Cliente> lista) {
		List<ClienteDto> listDTO = new ArrayList<ClienteDto>();
		
		for (Cliente cliente : getAllClientes()) {
			listDTO.add(mapperToDTO(cliente));
		}
		return listDTO;
	}

	@Override
	public List<ClienteDto> getAllClientesDtoSinTurnoActivo() {
		List<ClienteDto> listDTO = new ArrayList<ClienteDto>();
		
		for (Cliente cliente : clienteRepository.getAllClientesSinTurnoActivo()) {
			listDTO.add(mapperToDTO(cliente));
		}
		return listDTO;
	}
}
