package com.jc.sgtasec.web;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jc.sgtasec.config.CustomProperties;
import com.jc.sgtasec.model.Alerta;
import com.jc.sgtasec.model.Atencion;
import com.jc.sgtasec.service.IAlertaService;
import com.jc.sgtasec.service.IAtencionService;
import com.jc.sgtasec.service.ILlamadaService;
import com.jc.sgtasec.web.dto.AtencionDto;
import com.jc.sgtasec.web.dto.EmailDto;
import com.jc.sgtasec.web.dto.RestAtencionDto;

@RestController
public class WSController {
	
	private Logger logger = LogManager.getLogger(getClass());
	
	@Autowired
	private IAtencionService atencionService;
	
	@Autowired	
	private CustomProperties customProperties;

	@Autowired
	private IAlertaService alertaService;
	
	@Autowired
	private ILlamadaService llamadaService;
	
	public WSController() {
		super();
	}
	

	@MessageMapping("/atencion")
	@SendTo("/topic/greetings")
	public RestAtencionDto getAtencionByEmail(@Payload String email) {	
		RestAtencionDto restAtencionDto = new RestAtencionDto();	
		
		if (email.equalsIgnoreCase("email_vacio")) {
			restAtencionDto.setEmailRecibido(email);
			return restAtencionDto;
		}
		
		
		ObjectMapper mapper = new ObjectMapper();
		EmailDto emailDto = new EmailDto();
		
		try {
			emailDto = mapper.readValue(email, EmailDto.class);
			Thread.sleep(600);
			
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
			
		Collection<Alerta> alertas = new ArrayList<Alerta>();
		restAtencionDto.setEmailRecibido(emailDto.getEmail());		
		Atencion atencion = atencionService.findByClienteEmail(emailDto.getEmail());
				
		if (atencion != null) {				
			AtencionDto atencionDto = atencionService.mapperToDTO(atencion);
			atencionDto.setContadorLlamados(llamadaService.findByAtencion(atencion).size());			
			restAtencionDto.setAtencion(atencionDto);		
			restAtencionDto.setFechaCreacion(LocalDateTime.now());
			restAtencionDto.setCantidadMaximaLlamadas(customProperties.getCantidadMaximaLlamadas());
			
			Long idAtencion = atencion.getId();
			restAtencionDto.setTiempoEstimadoParaAtencion(atencionService.tiempoEstimadoParaAtencion(idAtencion));
						
			for (Alerta alerta : alertaService.getAllAlertas()) {
					alertas.add(alerta);
			}	
			
			restAtencionDto.setAlertas(alertas);					
			logger.debug("getAtencionByEmail(@Payload String email) : " + emailDto.getEmail());
		}
		
		return restAtencionDto;
	}
	
	
}
