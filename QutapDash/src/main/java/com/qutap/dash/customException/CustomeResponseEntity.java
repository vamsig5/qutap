package com.qutap.dash.customException;

import java.util.Date;

import javax.security.auth.message.callback.PrivateKeyCallback.Request;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.support.HttpRequestHandlerServlet;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.qutap.dash.commonUtils.ErrorObject;
import com.qutap.dash.commonUtils.Response;
import com.qutap.dash.commonUtils.StatusCode;

@ControllerAdvice
@RestController
public class CustomeResponseEntity extends ResponseEntityExceptionHandler{
	
	
	
	@ExceptionHandler(Exception.class)
	public ErrorObject handleAllException(Exception ex,WebRequest req){
		
		ErrorObject errorObject=new ErrorObject();
		errorObject.setErrorMessage(ex.getMessage());	
		errorObject.setDate(new Date());	
		errorObject.setCode(HttpStatus.INTERNAL_SERVER_ERROR);		
		return errorObject;	
	}
	
	@ExceptionHandler(ProjectInfoException.class)
	public ErrorObject HandleUserNotFound(Exception ex,WebRequest req,HttpServletRequest request){
		ErrorObject errorObject=new ErrorObject();
		errorObject.setCode(HttpStatus.NOT_FOUND);	
		errorObject.setErrorMessage(ex.getMessage());
		errorObject.setMessage(ex.getMessage());
		errorObject.setDate(new Date());
		errorObject.setUrl(request.getRequestURL().toString());
		errorObject.setStatus(StatusCode.FAILURE.name());
		return errorObject;		
	}
	
	@ExceptionHandler(TestCaseException.class)
	public ErrorObject HandleTestCaseNotFound(Exception ex,WebRequest req,HttpServletRequest request){
		ErrorObject errorObject=new ErrorObject();
		errorObject.setCode(HttpStatus.NOT_FOUND);	
		errorObject.setErrorMessage(ex.getMessage());
		errorObject.setMessage(ex.getMessage());
		errorObject.setDate(new Date());
		errorObject.setUrl(request.getRequestURL().toString());
		errorObject.setStatus(StatusCode.FAILURE.name());
		return errorObject;		
	}
	
	@ExceptionHandler(ModuleException.class)
	public ErrorObject HandleModuleNotFound(Exception ex,WebRequest req,HttpServletRequest request){
		ErrorObject errorObject=new ErrorObject();
		errorObject.setCode(HttpStatus.NOT_FOUND);	
		errorObject.setErrorMessage(ex.getMessage());
		errorObject.setMessage(ex.getMessage());
		errorObject.setDate(new Date());
		errorObject.setUrl(request.getRequestURL().toString());
		errorObject.setStatus(StatusCode.FAILURE.name());
		return errorObject;		
	}
	
	@ExceptionHandler(RequirementException.class)
	public ErrorObject HandleRequirementNotFound(Exception ex,WebRequest req,HttpServletRequest request){
		ErrorObject errorObject=new ErrorObject();
		errorObject.setCode(HttpStatus.NOT_FOUND);	
		errorObject.setErrorMessage(ex.getMessage());
		errorObject.setMessage(ex.getMessage());
		errorObject.setDate(new Date());
		errorObject.setUrl(request.getRequestURL().toString());
		errorObject.setStatus(StatusCode.FAILURE.name());
		return errorObject;		
	}

	@ExceptionHandler(ExecutionException.class)
	public ErrorObject HandleExecutionNotFound(Exception ex,WebRequest req,HttpServletRequest request){
		ErrorObject errorObject=new ErrorObject();
		errorObject.setCode(HttpStatus.NOT_FOUND);	
		errorObject.setErrorMessage(ex.getMessage());
		errorObject.setMessage(ex.getMessage());
		errorObject.setDate(new Date());
		errorObject.setUrl(request.getRequestURL().toString());
		errorObject.setStatus(StatusCode.FAILURE.name());
		return errorObject;		
	}
	@ExceptionHandler(TestScenarioException.class)
	public ErrorObject HandleTestScenarioNotFound(Exception ex,WebRequest req,HttpServletRequest request){
		ErrorObject errorObject=new ErrorObject();
		errorObject.setCode(HttpStatus.NOT_FOUND);	
		errorObject.setErrorMessage(ex.getMessage());
		errorObject.setMessage(ex.getMessage());
		errorObject.setDate(new Date());
		errorObject.setUrl(request.getRequestURL().toString());
		errorObject.setStatus(StatusCode.FAILURE.name());
		return errorObject;		
	}
	@ExceptionHandler(TestStepException.class)
	public ErrorObject HandleTestStepNotFound(Exception ex,WebRequest req,HttpServletRequest request){
		ErrorObject errorObject=new ErrorObject();
		errorObject.setCode(HttpStatus.NOT_FOUND);	
		errorObject.setErrorMessage(ex.getMessage());
		errorObject.setMessage(ex.getMessage());
		errorObject.setDate(new Date());
		errorObject.setUrl(request.getRequestURL().toString());
		errorObject.setStatus(StatusCode.FAILURE.name());
		return errorObject;		
	}


}
