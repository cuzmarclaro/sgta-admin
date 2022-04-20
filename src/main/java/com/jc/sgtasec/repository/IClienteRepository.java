package com.jc.sgtasec.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.jc.sgtasec.model.Cliente;

@Repository
public interface IClienteRepository extends JpaRepository<Cliente, Long>  {
	
	@Query(value="SELECT id, apellido_paterno, apellido_materno, email, nombre, rut FROM clientes where id NOT IN "
	+ "(SELECT id_cliente FROM atenciones, turnos WHERE atenciones.id_turno = turnos.id and turnos.estado IN (1,2))", nativeQuery = true) 
	List<Cliente> getAllClientesSinTurnoActivo();

}
