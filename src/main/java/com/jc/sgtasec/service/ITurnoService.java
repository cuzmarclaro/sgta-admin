package com.jc.sgtasec.service;

import java.util.List;

import com.jc.sgtasec.model.Turno;
import com.jc.sgtasec.web.dto.TurnoDto;

public interface ITurnoService {
	
	List<Turno> getAllTurnos();

	Turno saveTurno(Turno turno);

	Turno getTurnoById(Long id);

	Turno updateTurno(Turno turno);

	void deleteTurnoById(Long id);
	
	Turno getTurnoDisponible();
	
	Turno mapperToEntity(TurnoDto source);
	
	TurnoDto mapperToDTO(Turno source);
	
	List<TurnoDto> getListDTO(List<Turno> lista);
	
}
