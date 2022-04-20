package com.jc.sgtasec.web.dto;

import java.time.LocalDateTime;
import com.googlecode.jmapper.annotations.JMap;
import com.jc.sgtasec.model.Atencion;
import com.jc.sgtasec.model.TipoAtencion;
import com.jc.sgtasec.model.Turno;
import com.jc.sgtasec.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LlamadaDto {
	
	@JMap
	private Long id;
	
	@JMap("usuario")
	private Usuario usuario;
	
	@JMap("atencion")
	private Atencion atencion;
	
	@JMap("fechaCreacion")
	private LocalDateTime fechaCreacion;
	
	private Turno turno;
	
	private TipoAtencion tipoAtencion;
}
