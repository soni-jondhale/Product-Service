package com.jbk.exception;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String,String> MethodArgumentNotValidException( MethodArgumentNotValidException mx)
	{
		Map<String,String> error=new HashMap<>();
		List<FieldError>fieldError=mx.getFieldErrors();
		for (FieldError fieldErrors:fieldError) {
		String fieldname=fieldErrors.getField();
		String msg=fieldErrors.getDefaultMessage();
			error.put(fieldname, msg);
		}
		return error;
		
	}
	@ExceptionHandler(ResourceAlreadyExistException.class)
	public CustomExceptionResponse xx(ResourceAlreadyExistException resmsg,HttpServletRequest request) {
		String msg=resmsg.getMessage();
		String timeStamp=new SimpleDateFormat("yyyy-MM-dd-HH:MM:SS").format(Calendar.getInstance().getTime());
		String path=request.getRequestURI();
		CustomExceptionResponse response=new CustomExceptionResponse(msg,timeStamp,path);
		return response;
	}
	@ExceptionHandler(ResourceNotExistException.class)
	public CustomExceptionResponse xx(ResourceNotExistException resmsg,HttpServletRequest request) {
		String msg=resmsg.getMessage();
		String timeStamp=new SimpleDateFormat("yyyy-MM-dd-HH:MM:SS").format(Calendar.getInstance().getTime());
		String path=request.getRequestURI();
		CustomExceptionResponse response=new CustomExceptionResponse(msg,timeStamp,path);
		return response;
	}
	@ExceptionHandler(SomethingWentWrong.class)
	public CustomExceptionResponse somethingWentWrong(SomethingWentWrong resmsg,HttpServletRequest request) {
		String msg=resmsg.getMessage();
		String timeStamp=new SimpleDateFormat("yyyy-MM-dd-HH:MM:SS").format(Calendar.getInstance().getTime());
		String path=request.getRequestURI();
		CustomExceptionResponse response=new CustomExceptionResponse(msg,timeStamp,path);
		return response;
	}
	
}
