package com.jc.sgtasec.service;

import java.util.List;

import com.jc.sgtasec.model.Cliente;
import com.jc.sgtasec.web.dto.ClienteDto;

public interface IClienteService {

	List<Cliente> getAllClientes();
	

	Cliente saveCliente(Cliente cliente);

	Cliente getClienteById(Long id);

	Cliente updateCliente(Cliente cliente);

	void deleteClienteById(Long id);
	
	Cliente mapperToEntity(ClienteDto source);
	
	ClienteDto mapperToDTO(Cliente source);
	
	List<ClienteDto> getListClientesDTO(List<Cliente> lista);
	
	List<ClienteDto> getAllClientesDtoSinTurnoActivo();
}
