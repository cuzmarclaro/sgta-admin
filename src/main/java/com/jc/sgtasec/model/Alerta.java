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
@Table(name = "alertas")
@JGlobalMap
public class Alerta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "color", unique = true, nullable = false, length = 50)
	private String color;

	@Column(name = "color_html", length = 50)
	private String colorHtml;

	@Column(name = "duracion_desde", nullable = false)
	private Integer duracionDesde;

	@Column(name = "duracion_hasta", nullable = false)
	private Integer duracionHasta;

	@Column(name = "descripcion", nullable = false, length = 255)
	private String descripcion;

	@Column(name = "fecha_creacion", nullable = false)
	private LocalDateTime fechaCreacion;
}
