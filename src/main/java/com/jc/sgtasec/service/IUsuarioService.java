package com.jc.sgtasec.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.jc.sgtasec.model.Usuario;
import com.jc.sgtasec.web.dto.UsuarioDto;

public interface IUsuarioService extends UserDetailsService {

	Usuario save(UsuarioDto usuarioDto);
	
	Usuario resetPassword(Usuario usuario);
	
	boolean recoveryAcces(UsuarioDto usuarioDto);
	
	boolean esUsuarioAdministrador(Long id);
	
	List<Usuario> getAllUsuarios();

	Usuario saveUsuario(Usuario usuario);

	Usuario getUsuarioById(Long id);
	
	Usuario getUsuarioByEmail(String email);

	Usuario updateUsuario(Usuario usuario);

	void deleteUsuarioById(Long id);
	
	Usuario mapperToEntity(UsuarioDto source);
	
	UsuarioDto mapperToDTO(Usuario source);
	
	List<UsuarioDto> getListDTO(List<Usuario> lista);
}
