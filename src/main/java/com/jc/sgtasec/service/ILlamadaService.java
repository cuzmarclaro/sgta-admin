package com.jc.sgtasec.service;

import java.util.List;

import com.jc.sgtasec.model.Atencion;
import com.jc.sgtasec.model.Llamada;
import com.jc.sgtasec.web.dto.LlamadaDto;

public interface ILlamadaService {
	
	List<Llamada> getAllLlamadas();
			
	List<Llamada> findByAtencion(Atencion atencion);
	
	List<Llamada> listaLlamadasConTurnos();
	
	Llamada saveLlamada(Llamada llamada);

	Llamada getLlamadaById(Long id);
	
	Llamada updateLlamada(Llamada llamada);

	void deleteLlamadaById(Long id);
	
	void deleteByAtencion(Atencion atencion);
					
	Llamada mapperToEntity(LlamadaDto source);
	
	LlamadaDto mapperToDTO(Llamada source);
	
	List<LlamadaDto> getListDTO(List<Llamada> lista);
}
