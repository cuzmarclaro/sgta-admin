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
public class TurnoDto {

	private Long id;
	
	@NotEmpty(message = "{NotEmpty.TurnoDto.turnoAtencion}")
	private String turnoAtencion;
	
	@NotNull(message = "{NotNull.TurnoDto.estado}")
	private int estado;
}
