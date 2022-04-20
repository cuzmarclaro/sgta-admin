package com.jc.sgtasec.web;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.jc.sgtasec.model.HistorialAtencion;
import com.jc.sgtasec.service.IHistorialAtencionService;
import com.jc.sgtasec.web.dto.HistorialAtencionDto;

@Controller
public class HistorialAtencionController {

	private IHistorialAtencionService historialAtencionService;
	
	public HistorialAtencionController(IHistorialAtencionService historialAtencionService) {
		super();
		this.historialAtencionService = historialAtencionService;
	}
	
	@GetMapping("/historial")
	public String listHistorial(Model model) {
		
		List<HistorialAtencionDto> listHistorialAtencionesDto = new ArrayList<HistorialAtencionDto>();
		
		for (HistorialAtencion historialAtencion : historialAtencionService.getAllHistorialAtenciones()) {
			listHistorialAtencionesDto.add(historialAtencionService.mapperToDTO(historialAtencion));
		}		
		model.addAttribute("historialAtenciones", listHistorialAtencionesDto);		
		return "historial/historial";
	}
	
	
}
