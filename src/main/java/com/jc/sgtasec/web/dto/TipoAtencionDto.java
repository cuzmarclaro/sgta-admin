package com.jc.sgtasec.web.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.googlecode.jmapper.annotations.JGlobalMap;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JGlobalMap
public class TipoAtencionDto {

	private Long id;
	
	@NotEmpty(message = "{NotEmpty.TipoAtencionDto.nombre}")
	private String nombre;
	
	@NotNull(message = "{NotNull.TipoAtencionDto.tiempoAtencion}")
	private int tiempoAtencion;
}
