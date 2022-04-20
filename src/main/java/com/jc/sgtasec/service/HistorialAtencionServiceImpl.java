package com.jc.sgtasec.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.googlecode.jmapper.JMapper;
import com.jc.sgtasec.model.HistorialAtencion;
import com.jc.sgtasec.repository.IHistorialAtencionRepository;
import com.jc.sgtasec.web.dto.HistorialAtencionDto;

@Service
public class HistorialAtencionServiceImpl implements IHistorialAtencionService {
	
	private Logger logger = LogManager.getLogger(getClass());
	private Authentication auth;
	private IHistorialAtencionRepository atencionRepository;
	private JMapper<HistorialAtencion, HistorialAtencionDto> mapperToEntity;
	private JMapper<HistorialAtencionDto, HistorialAtencion> mapperToDTO;
	private DifferenceBetweenTwoDate differenceBetweenTwoDate;

	public HistorialAtencionServiceImpl(IHistorialAtencionRepository atencionRepository, DifferenceBetweenTwoDate differenceBetweenTwoDate) {
		super();
		this.atencionRepository = atencionRepository;
		this.differenceBetweenTwoDate = differenceBetweenTwoDate;
		this.mapperToEntity = new JMapper<>(HistorialAtencion.class, HistorialAtencionDto.class);
		this.mapperToDTO = new JMapper<>(HistorialAtencionDto.class, HistorialAtencion.class);
		
	}

	@Override
	public List<HistorialAtencion> getAllHistorialAtenciones() {
		return atencionRepository.findAll();
	}

	@Override
	public HistorialAtencion saveHistorialAtencion(HistorialAtencion historialAtencion) {
		this.auth = SecurityContextHolder.getContext().getAuthentication();
		logger.info("Usuario: " + auth.getName());
		logger.info("saveHistorialAtencion(HistorialAtencion historialAtencion) " + historialAtencion.toString());
		return atencionRepository.save(historialAtencion);
	}

	@Override
	public HistorialAtencion getHistorialAtencionById(Long id) {
		return atencionRepository.findById(id).get();
	}

	@Override
	public HistorialAtencion updateHistorialAtencion(HistorialAtencion historialAtencion) {
		this.auth = SecurityContextHolder.getContext().getAuthentication();
		logger.info("Usuario: " + auth.getName());
		logger.info("updateHistorialAtencion(HistorialAtencion historialAtencion) " + historialAtencion.toString());
		return atencionRepository.save(historialAtencion);
	}

	@Override
	public void deleteHistorialAtencionById(Long id) {
		this.auth = SecurityContextHolder.getContext().getAuthentication();
		logger.info("Usuario: " + auth.getName());
		logger.info("deleteHistorialAtencionById(Long id) " + id);
		atencionRepository.deleteById(id);		
	}

	@Override
	public HistorialAtencion mapperToEntity(HistorialAtencionDto source) {
		HistorialAtencion historialAtencion = new HistorialAtencion();
		historialAtencion = mapperToEntity.getDestination(source);
		return historialAtencion;
	}

	@Override
	public HistorialAtencionDto mapperToDTO(HistorialAtencion source) {
		HistorialAtencionDto historialAtencionDto = new HistorialAtencionDto();
		historialAtencionDto = mapperToDTO.getDestination(source);
		
		LocalDateTime fechaCreacionAtencion = historialAtencionDto.getFechaCreacionAtencion();
		LocalDateTime fechaCreacionLlamada = historialAtencionDto.getFechaCreacionLlamada();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		String formatFechaCreacionAtencion = "";
		String formatFechaCreacionLlamada = "";
        
        if(historialAtencionDto.getFechaCreacionAtencion() != null) {
        	formatFechaCreacionAtencion = fechaCreacionAtencion.format(formatter);
        }
    	
        if(historialAtencionDto.getFechaCreacionLlamada() != null) {
        	formatFechaCreacionLlamada = fechaCreacionLlamada.format(formatter);
        }

        if((historialAtencionDto.getFechaCreacionLlamada() != null) && (historialAtencionDto.getFechaCreacionAtencion() != null)) {
        	historialAtencionDto.setTiempoEsperaParaLlamada(differenceBetweenTwoDate.findDifference(formatFechaCreacionAtencion, formatFechaCreacionLlamada));
        }
		
		
		return historialAtencionDto;
	}

	@Override
	public List<HistorialAtencionDto> getListDTO(List<HistorialAtencion> lista) {
		List<HistorialAtencionDto> listDTO = new ArrayList<HistorialAtencionDto>();
		
		for (HistorialAtencion historialAtencion : getAllHistorialAtenciones()) {
			listDTO.add(mapperToDTO.getDestination(historialAtencion));			
		}
		
		return listDTO;
	}

}
