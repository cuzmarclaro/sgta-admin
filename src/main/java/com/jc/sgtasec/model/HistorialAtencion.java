package com.jc.sgtasec.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import com.googlecode.jmapper.annotations.JGlobalMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "historial_atenciones")
@JGlobalMap
public class HistorialAtencion {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; 
	
	@Column(name = "id_atencion")
	private Long idAtencion; 
	
	@Column(name = "fecha_creacion_atencion")
	private LocalDateTime fechaCreacionAtencion; 
	
	@Column(name = "apellido_paterno_cliente", length = 50)
	private String apellidoPaternoCliente; 
	
	@Column(name = "apellido_materno_cliente", length = 50)
	private String apellidoMaternoCliente; 
	
	@Column(name = "nombre_cliente", length = 50)
	private String nombreCliente;
	
	@Column(name = "email_cliente", length = 50)
	private String emailCliente;
	
	@Column(name = "rut_cliente", length = 50)
	private String rutCliente; 
	
	@Column(name = "nombre_tipo_atencion", length = 50)
	private String nombreTipoAtencion; 
	
	@Column(name = "tiempo_atencion")
	private int tiempoAtencion; 
	
	@Column(name = "turno_atencion", length = 50)
	private String turnoAtencion; 
	
	@Column(name = "fecha_creacion_llamada")
	private LocalDateTime fechaCreacionLlamada; 

}
