package com.jc.sgtasec.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageExceptionDto {
	
	private String customizedMessage;
	private String exceptionMessage;
	

}
