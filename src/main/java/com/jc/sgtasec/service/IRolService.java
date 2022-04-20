package com.jc.sgtasec.service;

import java.util.List;
import com.jc.sgtasec.model.Rol;

public interface IRolService {
	
	List<Rol> getAllRols();

	Rol saveRol(Rol rol);

	Rol getRolById(Long id);

	Rol updateRol(Rol rol);

	void deleteRolById(Long id);

}
