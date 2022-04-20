package com.jc.sgtasec.web.dto;

import java.time.LocalDateTime;

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
public class AlertaDto {

	private Long id;
	
	@NotEmpty(message = "{NotEmpty.AlertaDto.color}")
	private String color;
	private String colorHtml;
	@NotNull(message = "{NotNull.AlertaDto.duracionDesde}")
	private Integer duracionDesde;
	@NotNull(message = "{NotNull.AlertaDto.duracionHasta}")
	private Integer duracionHasta;
	@NotEmpty(message = "{NotEmpty.AlertaDto.descripcion}")
	private String descripcion;
	private LocalDateTime fechaCreacion;
}
