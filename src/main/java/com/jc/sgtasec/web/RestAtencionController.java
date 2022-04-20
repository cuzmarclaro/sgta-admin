package com.jc.sgtasec.web;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.jc.sgtasec.config.CustomProperties;
import com.jc.sgtasec.model.Alerta;
import com.jc.sgtasec.model.Atencion;
import com.jc.sgtasec.service.IAlertaService;
import com.jc.sgtasec.service.IAtencionService;
import com.jc.sgtasec.service.ILlamadaService;
import com.jc.sgtasec.web.dto.AtencionDto;
import com.jc.sgtasec.web.dto.RestAtencionDto;

@RestController
public class RestAtencionController {
	
	private Logger logger = LogManager.getLogger(getClass());
	
	@Autowired
	private IAtencionService atencionService;
	
	@Autowired	
	private CustomProperties customProperties;

	@Autowired
	private IAlertaService alertaService;
	
	@Autowired
	private ILlamadaService llamadaService;
	
	public RestAtencionController() {
		super();
	}
	
	@GetMapping("/api/atenciones/{email}")
	public RestAtencionDto getAtencionByEmail(@PathVariable String email) {	
		RestAtencionDto restAtencionDto = new RestAtencionDto();				
		Collection<Alerta> alertas = new ArrayList<Alerta>();
		restAtencionDto.setEmailRecibido(email);		
		Atencion atencion = atencionService.findByClienteEmail(email);
				
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
			logger.debug("getAtencionByEmail(@PathVariable String email) : " + email);
		}
		
		return restAtencionDto;
	}		
}