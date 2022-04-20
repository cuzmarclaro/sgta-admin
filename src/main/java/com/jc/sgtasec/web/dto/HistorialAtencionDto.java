package com.jc.sgtasec.web.dto;

import java.time.LocalDateTime;
import com.googlecode.jmapper.annotations.JMap;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class HistorialAtencionDto {

	@JMap
	private Long id;
	@JMap
	private Long idAtencion;
	@JMap
	private LocalDateTime fechaCreacionAtencion;
	@JMap
	private String apellidoPaternoCliente;
	@JMap
	private String apellidoMaternoCliente;
	@JMap
	private String nombreCliente;
	@JMap
	private String emailCliente;
	@JMap
	private String rutCliente;
	@JMap
	private String nombreTipoAtencion;
	@JMap
	private int tiempoAtencion;
	@JMap
	private String turnoAtencion;
	@JMap
	private LocalDateTime fechaCreacionLlamada;
	
	private String tiempoEsperaParaLlamada;
}
