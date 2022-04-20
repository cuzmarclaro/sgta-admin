package com.jc.sgtasec.model;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import com.googlecode.jmapper.annotations.JGlobalMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "atenciones")
@JGlobalMap
public class Atencion {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne
	@JoinColumn(name = "id_turno", referencedColumnName = "id")
	private Turno turno;

	@OneToOne
	@JoinColumn(name = "id_cliente", referencedColumnName = "id")
	private Cliente cliente;
	
	@OneToOne
	@JoinColumn(name = "id_tipo_atencion", referencedColumnName = "id")
	private TipoAtencion tipoAtencion;	
	
	@Column(name = "contador_llamados")
	private long contadorLlamados;
	
	@Column(name = "fecha_creacion", nullable = false)
	private LocalDateTime fechaCreacion;

	@Column(name = "fecha_creacion_llamada")
	private LocalDateTime fechaCreacionLlamada;
	
}
