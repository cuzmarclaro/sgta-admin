package com.jc.sgtasec.service;

import java.util.List;

import com.jc.sgtasec.model.Alerta;
import com.jc.sgtasec.web.dto.AlertaDto;

public interface IAlertaService {
	List<Alerta> getAllAlertas();
	
	Alerta saveAlerta(Alerta alerta);
	
	Alerta getAlertaById(Long id);
	
	Alerta updateAlerta(Alerta alerta);
	
	void deleteAlertaById(Long id);
	
	Alerta mapperToEntity(AlertaDto source);
	
	AlertaDto mapperToDTO(Alerta source);
	
	List<AlertaDto> getListDTO(List<Alerta> lista);

}
