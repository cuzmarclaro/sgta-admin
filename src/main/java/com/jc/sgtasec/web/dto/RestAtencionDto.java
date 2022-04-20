package com.jc.sgtasec.web.dto;

import java.time.LocalDateTime;
import java.util.Collection;

import com.jc.sgtasec.model.Alerta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestAtencionDto {
	
	private String emailRecibido;
	
	private AtencionDto atencion;
	
	private LocalDateTime fechaCreacion;
	
	private Collection<Alerta> alertas;
	
	private Long cantidadMaximaLlamadas;
	
	private Long tiempoEstimadoParaAtencion;

}
