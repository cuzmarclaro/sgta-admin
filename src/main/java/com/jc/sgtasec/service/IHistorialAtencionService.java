package com.jc.sgtasec.service;

import java.util.List;

import com.jc.sgtasec.model.HistorialAtencion;
import com.jc.sgtasec.web.dto.HistorialAtencionDto;

public interface IHistorialAtencionService {
	
	List<HistorialAtencion> getAllHistorialAtenciones();
	
	HistorialAtencion saveHistorialAtencion(HistorialAtencion historialAtencion);

	HistorialAtencion getHistorialAtencionById(Long id);

	HistorialAtencion updateHistorialAtencion(HistorialAtencion historialAtencion);

	void deleteHistorialAtencionById(Long id);
	
	HistorialAtencion mapperToEntity(HistorialAtencionDto source);
	
	HistorialAtencionDto mapperToDTO(HistorialAtencion source);
	
	List<HistorialAtencionDto> getListDTO(List<HistorialAtencion> lista);

}
