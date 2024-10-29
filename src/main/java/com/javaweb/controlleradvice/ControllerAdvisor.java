package com.javaweb.controlleradvice;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.javaweb.dto.ErrorDetailDTO;
import com.sun.media.sound.InvalidDataException;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {
	@ExceptionHandler(ArithmeticException.class)
	public ResponseEntity<ErrorDetailDTO> handleArithmeticException(ArithmeticException ex){
		ErrorDetailDTO errorDetailDTO = new ErrorDetailDTO();
		errorDetailDTO.setError(ex.getMessage());
		List<String> details = new ArrayList<String>();
		details.add("A number can't be divided by 0");
		errorDetailDTO.setDetail(details);
		return new ResponseEntity<>(errorDetailDTO, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(InvalidDataException.class)
	public ResponseEntity<ErrorDetailDTO> handleInvalidDataException(InvalidDataException ex){
		ErrorDetailDTO errorDetailDTO = new ErrorDetailDTO();
		errorDetailDTO.setError(ex.getMessage());
		List<String> details = new ArrayList<String>();
		details.add("Can't be blanked");
		errorDetailDTO.setDetail(details);
		return new ResponseEntity<>(errorDetailDTO, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	}
