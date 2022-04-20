package com.jc.sgtasec.service;

import java.util.List;
import com.jc.sgtasec.model.TipoAtencion;
import com.jc.sgtasec.web.dto.TipoAtencionDto;

public interface ITipoAtencionService {

	List<TipoAtencion> getAllTipoAtencions();

	TipoAtencion saveTipoAtencion(TipoAtencion tipoAtencion);

	TipoAtencion getTipoAtencionById(Long id);

	TipoAtencion updateTipoAtencion(TipoAtencion tipoAtencion);

	void deleteTipoAtencionById(Long id);
	
	TipoAtencion mapperToEntity(TipoAtencionDto source);
	
	TipoAtencionDto mapperToDTO(TipoAtencion source);
	
	List<TipoAtencionDto> getListTipoAtencionDTO(List<TipoAtencion> lista);
}
