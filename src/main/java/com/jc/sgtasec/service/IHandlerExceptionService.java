package com.jc.sgtasec.service;

import org.springframework.dao.DataIntegrityViolationException;

import com.jc.sgtasec.web.dto.MessageExceptionDto;

public interface IHandlerExceptionService {

	MessageExceptionDto customizeException(DataIntegrityViolationException ex, String enlaceVolver);

	MessageExceptionDto customizeException(Exception ex, String enlaceVolver);
}
