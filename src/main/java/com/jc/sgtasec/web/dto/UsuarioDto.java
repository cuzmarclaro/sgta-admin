package com.jc.sgtasec.web.dto;

import java.util.Collection;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import com.googlecode.jmapper.annotations.JGlobalMap;
import com.jc.sgtasec.model.Rol;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data 
@AllArgsConstructor 
@NoArgsConstructor
@JGlobalMap
public class UsuarioDto {

	private Long id;
	
	@NotEmpty(message = "{NotEmpty.UsuarioDto.nombre}")
	private String nombre;
	
	@NotEmpty(message = "{NotEmpty.UsuarioDto.apellidoPaterno}")
	private String apellidoPaterno;
	
	private String apellidoMaterno;
	
	@NotEmpty(message = "{NotEmpty.UsuarioDto.email}")
	@Pattern(regexp="^(.+)@(\\S+)$", message="{Pattern.UsuarioDto.email}")
	private String email;
	
	@NotEmpty(message = "{NotEmpty.UsuarioDto.password}")
	private String password;
	
	@NotEmpty(message = "{NotEmpty.UsuarioDto.rut}")
	@Pattern(regexp = "^\\d{7,8}-[0-9kK]{1}$", message = "{Pattern.UsuarioDto.rut}")
	private String rut;
	
	private Collection<Rol> roles;	
}
