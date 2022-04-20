package com.jc.sgtasec.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.googlecode.jmapper.JMapper;
import com.jc.sgtasec.model.Atencion;
import com.jc.sgtasec.repository.IAtencionRepository;
import com.jc.sgtasec.web.dto.AtencionDto;

@Service
public class AtencionServiceImpl implements IAtencionService {

	private Logger logger = LogManager.getLogger(getClass());
	private Authentication auth;
	private IAtencionRepository atencionRepository;
	private JMapper<Atencion, AtencionDto> mapperToEntity;
	private JMapper<AtencionDto, Atencion> mapperToDTO;

	public AtencionServiceImpl(IAtencionRepository atencionRepository) {
		super();
		this.atencionRepository = atencionRepository;
		this.mapperToEntity = new JMapper<>(Atencion.class, AtencionDto.class);
		this.mapperToDTO = new JMapper<>(AtencionDto.class, Atencion.class);
	}
	
	@Override
	public List<Atencion> getAllAtenciones() {
		return atencionRepository.findAll();
	}

	@Override
	public Atencion saveAtencion(Atencion atencion) {
		
		this.auth = SecurityContextHolder.getContext().getAuthentication();
		logger.info("Usuario: " + auth.getName());
		logger.info("saveAtencion(Atencion atencion) " + atencion.toString());
		
		return atencionRepository.save(atencion);
	}

	@Override
	public Atencion getAtencionById(Long id) {
		return atencionRepository.findById(id).get();
	}

	@Override
	public Atencion updateAtencion(Atencion atencion) {
				
		this.auth = SecurityContextHolder.getContext().getAuthentication();
		logger.info("Usuario: " + auth.getName());
		logger.info("updateAtencion(Atencion atencion) " + atencion.toString());
		
		return atencionRepository.save(atencion);
	}

	@Override
	public void deleteAtencionById(Long id) {
		
		this.auth = SecurityContextHolder.getContext().getAuthentication();
		logger.info("Usuario: " + auth.getName());
		logger.info("deleteAtencionById(Long id) " + id);
		
		atencionRepository.deleteById(id);		
	}

	@Override
	public Atencion mapperToEntity(AtencionDto source) {
		Atencion atencion = new Atencion();
		atencion = mapperToEntity.getDestination(source);		
		return atencion;
	}

	@Override
	public AtencionDto mapperToDTO(Atencion source) {
		AtencionDto alertaDto = new AtencionDto();
		alertaDto = mapperToDTO.getDestination(source);		
		return alertaDto;
	}
	
	@Override
	public List<AtencionDto> getListDTO(List<Atencion> lista) {
		List<AtencionDto> listDTO = new ArrayList<AtencionDto>();		
		
		for (Atencion atencion : getAllAtenciones()) {
			listDTO.add(mapperToDTO(atencion));
		}
		return listDTO;
	}	

	@Override
	public List<Atencion> getAtencionesConCantidadDeLlamadas() {
		List<Atencion> lista = atencionRepository.listaAtencionesConLlamadas();
		List<Atencion> listaSalida = new ArrayList<Atencion>();	
			
			for (int i = 0; i < lista.size() ; i++) {				
				if (listaSalida.contains(lista.get(i))) {					
					lista.get(i).setContadorLlamados(lista.get(i).getContadorLlamados() + 1);					
				} else {					
					listaSalida.add(lista.get(i));
				}
			}				
		
		return listaSalida;
	}

	@Override
	public Atencion findByClienteEmail(String email) {
		return atencionRepository.findByClienteEmail(email);
	}

	@Override
	public Long tiempoEstimadoParaAtencion(Long idAtencion) {
		return atencionRepository.tiempoEstimadoParaAtencion(idAtencion);
	}





}
