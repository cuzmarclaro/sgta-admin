package com.jc.sgtasec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jc.sgtasec.model.Alerta;

@Repository
public interface IAlertaRepository extends JpaRepository<Alerta, Long> {

}
