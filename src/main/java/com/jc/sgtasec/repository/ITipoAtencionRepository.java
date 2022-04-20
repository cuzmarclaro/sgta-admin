package com.jc.sgtasec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.jc.sgtasec.model.TipoAtencion;

@Repository
public interface ITipoAtencionRepository extends JpaRepository<TipoAtencion, Long> {

	
}
