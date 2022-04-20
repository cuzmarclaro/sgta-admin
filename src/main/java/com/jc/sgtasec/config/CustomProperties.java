package com.jc.sgtasec.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import lombok.Getter;
import lombok.Setter;

@Component
@Getter 
@Setter 
public class CustomProperties {
	
	@Value("${tiempo.minimo.inicial}")
	private Long tiempoMinimoInicial;
	
	@Value("${cantidad.maxima.llamadas}")
	private Long cantidadMaximaLlamadas;
	
	@Value("${minutos.para.siguiente.llamada}")
	private Long minutosParaSiguienteLlamada;

}
