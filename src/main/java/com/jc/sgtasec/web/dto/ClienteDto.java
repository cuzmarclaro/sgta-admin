package com.jc.sgtasec.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import com.googlecode.jmapper.annotations.JGlobalMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JGlobalMap
public class ClienteDto {


	private Long id;
	
	@NotEmpty(message = "{NotEmpty.ClienteDto.nombre}")
	private String nombre;
	
	@NotEmpty(message = "{NotEmpty.ClienteDto.apellidoPaterno}")
	private String apellidoPaterno;
	
	private String apellidoMaterno;
	
	@NotEmpty(message = "{NotEmpty.ClienteDto.email}")
	@Pattern(regexp="^(.+)@(\\S+)$", message="{Pattern.ClienteDto.email}")
	private String email;
	
	@NotEmpty(message = "{NotEmpty.ClienteDto.rut}")
	@Pattern(regexp = "^\\d{7,8}-[0-9kK]{1}$", message = "{Pattern.ClienteDto.rut}")
	private String rut;
}
