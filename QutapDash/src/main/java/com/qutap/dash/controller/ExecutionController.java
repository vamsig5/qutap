package com.qutap.dash.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qutap.dash.commonUtils.Response;
import com.qutap.dash.service.ExecutionService;

@RestController
@RequestMapping("/Qutap")
public class ExecutionController {
	
	Logger log = LoggerFactory.getLogger(ExecutionController.class);

	@Autowired
	ExecutionService executionService;

	
	
	@PostMapping("/execution/{testScenarioName}/{requirementName}/{moduleName}/{projectName}")
	public List<Response> executeData(@PathVariable String testScenarioName, @PathVariable String requirementName,@PathVariable String moduleName,@PathVariable String projectName, HttpServletRequest req) {
		log.info("url of the application" + req.getRequestURL().toString());
		List<Response> response = executionService.executeData(testScenarioName, requirementName, moduleName, projectName);
	//	response.setUrl(req.getRequestURL().toString());
		return response;
	}
	
	@GetMapping("execute/{projectId}")
	public List<Response> executeTestCases(@PathVariable String projectId,HttpServletRequest req){
		log.info("url of the application" + req.getRequestURL().toString());
		List<Response> response = executionService.executeAllTestCases(projectId); 
		return response;
	}
	
}
