package com.jc.sgtasec.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.jc.sgtasec.model.Turno;

@Repository
public interface ITurnoRepository extends JpaRepository<Turno, Long> {
	
	Turno findByTurnoAtencion(String turnoAtencion);
	
	List<Turno> findByEstadoOrderByTurnoAtencionAsc(int estado);
	
	@Query(value="SELECT ID, ESTADO, TURNO_ATENCION FROM TURNOS WHERE ESTADO = 0 ORDER BY ID LIMIT 1", nativeQuery = true)
	Turno getDisponible();

}
