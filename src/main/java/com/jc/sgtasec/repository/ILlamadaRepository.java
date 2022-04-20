package com.jc.sgtasec.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.jc.sgtasec.model.Atencion;
import com.jc.sgtasec.model.Llamada;

@Repository
public interface ILlamadaRepository extends JpaRepository<Llamada, Long> {
	
	List<Llamada> findByAtencion(Atencion atencion);
	
	void deleteByAtencion(Atencion atencion);
	
	@Query(value="SELECT tu.turno_atencion turno, "
			+ "ll.id, ll.fecha_creacion, "
			+ "at.id id_atencion, " 
			+ "us.id id_usuario, "
			+ "us.nombre, "
			+ "us.apellido_paterno, " 
			+ "us.apellido_materno "
			+ "FROM llamadas ll "
			+ "LEFT JOIN atenciones at ON ll.id_atencion=at.id "
			+ "LEFT JOIN usuarios us ON ll.id_usuario=us.id "
			+ "LEFT JOIN turnos tu ON tu.id=at.id_turno "
			+ "ORDER BY tu.id, ll.fecha_creacion", nativeQuery = true)
	List<Llamada> listaLlamadasConTurnos();

	
}
