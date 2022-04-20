package com.jc.sgtasec.web.dto;

import javax.validation.constraints.NotEmpty;

import com.googlecode.jmapper.annotations.JGlobalMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JGlobalMap
public class RolDto {

	private Long id;
	
	@NotEmpty(message = "{NotEmpty.RolDto.name}")
	private String name;
}
