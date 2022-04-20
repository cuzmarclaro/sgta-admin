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
import com.googlecode.jmapper.annotations.JMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "llamadas")
public class Llamada {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JMap
	private Long id;
	
	@OneToOne
	@JoinColumn(name = "id_usuario", referencedColumnName = "id")
	@JMap("usuario")
	private Usuario usuario;
	
	@OneToOne
	@JoinColumn(name = "id_atencion", referencedColumnName = "id")
	@JMap("atencion")
	private Atencion atencion;
	
	@Column(name = "fecha_creacion")
    @JMap("fechaCreacion")
	private LocalDateTime fechaCreacion;	

}
