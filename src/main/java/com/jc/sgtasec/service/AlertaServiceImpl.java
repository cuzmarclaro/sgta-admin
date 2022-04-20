package com.jc.sgtasec.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.googlecode.jmapper.JMapper;
import com.jc.sgtasec.model.Alerta;
import com.jc.sgtasec.repository.IAlertaRepository;
import com.jc.sgtasec.web.dto.AlertaDto;

@Service
public class AlertaServiceImpl implements IAlertaService {
	
	private Logger logger = LogManager.getLogger(getClass());
	private Authentication auth;
	private IAlertaRepository alertaRepository;
	private JMapper<Alerta, AlertaDto> mapperToEntity;
	private JMapper<AlertaDto, Alerta> mapperToDTO;
	
	public AlertaServiceImpl(IAlertaRepository alertaRepository) {
		super();
		this.alertaRepository = alertaRepository;
		this.mapperToEntity = new JMapper<>(Alerta.class, AlertaDto.class);
		this.mapperToDTO = new JMapper<>(AlertaDto.class, Alerta.class);
	}

	@Override
	public List<Alerta> getAllAlertas() {
		return alertaRepository.findAll();
	}

	@Override
	public Alerta saveAlerta(Alerta alerta) {
		this.auth = SecurityContextHolder.getContext().getAuthentication();
		logger.info("Usuario: " + auth.getName());
		logger.info("saveAlerta(Alerta alerta) " + alerta.toString());
		return alertaRepository.save(alerta);
	}

	@Override
	public Alerta getAlertaById(Long id) {
		return alertaRepository.findById(id).get();
	}

	@Override
	public Alerta updateAlerta(Alerta alerta) {
		this.auth = SecurityContextHolder.getContext().getAuthentication();
		logger.info("Usuario: " + auth.getName());
		logger.info("updateAlerta(Alerta alerta) " + alerta.toString());
		return alertaRepository.save(alerta);
	}

	@Override
	public void deleteAlertaById(Long id) {
		this.auth = SecurityContextHolder.getContext().getAuthentication();
		logger.info("Usuario: " + auth.getName());
		logger.info("deleteAlertaById(Long id) " + id);
		alertaRepository.deleteById(id);		
	}

	@Override
	public Alerta mapperToEntity(AlertaDto source) {
		Alerta alerta = new Alerta();
		alerta = mapperToEntity.getDestination(source);		
		return alerta;
	}

	@Override
	public AlertaDto mapperToDTO(Alerta source) {
		AlertaDto alertaDto = new AlertaDto();
		alertaDto = mapperToDTO.getDestination(source);		
		return alertaDto;
	}

	@Override
	public List<AlertaDto> getListDTO(List<Alerta> lista) {
		List<AlertaDto> listDTO = new ArrayList<AlertaDto>();
		
		for (Alerta alerta : getAllAlertas()) {
			listDTO.add(mapperToDTO(alerta));
		}
		return listDTO;
	}


}
