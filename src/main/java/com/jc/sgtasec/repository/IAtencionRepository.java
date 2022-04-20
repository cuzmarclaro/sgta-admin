package com.jc.sgtasec.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.jc.sgtasec.model.Atencion;

@Repository
public interface IAtencionRepository extends JpaRepository<Atencion, Long> {
	
		
	Atencion findByClienteEmail(String email);
	
	@Query(value="select ate.id, ate.fecha_creacion, ate.id_cliente, ate.id_tipo_atencion, "
			+ "ate.id_turno, lla.fecha_creacion fecha_creacion_llamada, count(lla.id_atencion) "
			+ "contador_llamados from atenciones ate "
			+ "left join llamadas lla on ate.id = lla.id_atencion group by "
			+ "ate.id, ate.fecha_creacion, ate.id_cliente, ate.id_tipo_atencion, ate.id_turno, "
			+ "lla.fecha_creacion order by ate.id_turno, lla.fecha_creacion DESC", nativeQuery = true)
			List<Atencion> listaAtencionesConLlamadas();
	
	@Query(value="select SUM(tipo_atencion.tiempo_atencion) tiempo_estimado_en_minutos "
			+ "from atenciones, tipo_atencion, turnos "
			+ "where atenciones.id_tipo_atencion = tipo_atencion.id  "
			+ "and atenciones.id_turno = turnos.id "
			+ "and turnos.estado in (1,2) "
			+ "and atenciones.id_turno < (select id_turno from atenciones where id = ?1)", nativeQuery = true) 
	Long tiempoEstimadoParaAtencion(Long idAtencion);
}
