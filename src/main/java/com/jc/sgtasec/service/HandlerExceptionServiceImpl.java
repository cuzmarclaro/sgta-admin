package com.jc.sgtasec.service;

import java.sql.SQLException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import com.jc.sgtasec.web.dto.MessageExceptionDto;

@Service
public class HandlerExceptionServiceImpl implements IHandlerExceptionService {

	@Override
	public MessageExceptionDto customizeException(DataIntegrityViolationException ex, String enlaceVolver) {
		MessageExceptionDto messageExceptionDto = new MessageExceptionDto();
		Throwable rootCause = com.google.common.base.Throwables.getRootCause(ex);

		if (rootCause instanceof SQLException) {

			/*
			 * Manejar las SQLException, según la getSQLState asociada.
			 */
			if ("23505".equals(((SQLException) rootCause).getSQLState())) {
				messageExceptionDto.setCustomizedMessage(
						"El registro que intenta guardar, tiene un atributo único; ya registrado en el sistema. "
								+ "<a href=\""+ enlaceVolver +"\">Volver</a>");
			}
			
            if ("23503".equals(((SQLException) rootCause).getSQLState())) {
            	messageExceptionDto.setCustomizedMessage(
            			"El registro que intenta eliminar, aún está en uso en otra tabla del sistema. "
            			+ "<a href=\""+ enlaceVolver +"\">Volver</a>");
            }
			
		}

		messageExceptionDto.setExceptionMessage(ex.getRootCause().getMessage());
		return messageExceptionDto;
	}

	@Override
	public MessageExceptionDto customizeException(Exception ex, String enlaceVolver) {
		MessageExceptionDto messageExceptionDto = new MessageExceptionDto();
		Throwable rootCause = com.google.common.base.Throwables.getRootCause(ex);

		if (rootCause instanceof Exception) {
			messageExceptionDto.setCustomizedMessage(
					"Ha ocurrido un error inesperado en el sistema. <a href=\""+ enlaceVolver +"\">Volver</a>");
		}
		
		messageExceptionDto.setExceptionMessage(ex.getMessage());
		return messageExceptionDto;
	}
}