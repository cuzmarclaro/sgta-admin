package com.jc.sgtasec.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.jc.sgtasec.model.Rol;
import com.jc.sgtasec.repository.IRolRepository;

public class RolServiceImpl implements IRolService{
	
	private Logger logger = LogManager.getLogger(getClass());
	private Authentication auth;
	private IRolRepository rolRepository;

	public RolServiceImpl(IRolRepository rolRepository) {
		super();
		this.rolRepository = rolRepository;
	}
	
	@Override
	public List<Rol> getAllRols() {
		return rolRepository.findAll();
	}

	@Override
	public Rol saveRol(Rol rol) {
		this.auth = SecurityContextHolder.getContext().getAuthentication();
		logger.info("Usuario: " + auth.getName());
		logger.info("saveRol(Rol rol): " + rol.toString());	
		return rolRepository.save(rol);
	}

	@Override
	public Rol getRolById(Long id) {
		return rolRepository.findById(id).get();
	}

	@Override
	public Rol updateRol(Rol rol) {
		this.auth = SecurityContextHolder.getContext().getAuthentication();
		logger.info("Usuario: " + auth.getName());
		logger.info("updateRol(Rol rol): " + rol.toString());	
		return rolRepository.save(rol);
	}

	@Override
	public void deleteRolById(Long id) {
		this.auth = SecurityContextHolder.getContext().getAuthentication();
		logger.info("Usuario: " + auth.getName());
		logger.info("deleteRolById(Long id): " + id);	
		rolRepository.deleteById(id);
	}

}
