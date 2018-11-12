package com.qutap.dash.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.qutap.dash.commonUtils.Response;
import com.qutap.dash.commonUtils.StatusCode;
import com.qutap.dash.commonUtils.Utils;

import com.qutap.dash.customException.TestStepException;

import com.qutap.dash.model.TestStepModel;
import com.qutap.dash.service.TestStepService;

@RestController
@RequestMapping("/Qutap")
public class TestStepController {
	Logger log = LoggerFactory.getLogger(TestCaseController.class);
	
	@Autowired
	TestStepService testStepService;
	
	@GetMapping("/getTestStepById/{testStepsId}/{testCaseId}/{testScenarioId}/{requirementId}/{moduleId}/{projectId}")
	public @ResponseBody String getTestStepById(@PathVariable String testStepsId,@PathVariable String testCaseId,@PathVariable String testScenarioId,@PathVariable String requirementId, @PathVariable String moduleId,
			@PathVariable String projectId, HttpServletRequest req) throws IOException {
		Response response = Utils.getResponseObject("getting testCase details data");
		try {
			log.info("url of the application" + req.getRequestURL().toString());
			TestStepModel testStepModel  = testStepService.getTestStepById(testStepsId, testCaseId, testScenarioId, requirementId, moduleId, projectId);
			response.setStatus(StatusCode.SUCCESS.name());
			response.setUrl(req.getRequestURL().toString());
			response.setData(testStepModel);

		} catch (Exception e) {
			log.error("error in getting tsetStep details", e);
			throw new TestStepException("error in getting tsetStep details");

		}
		return (String) Utils.getJson(response);
	}
	
	@GetMapping("/listOfTestSteps/{testCaseId}/{testScenarioId}/{requirementId}/{moduleId}/{projectId}")
	public @ResponseBody String getTestStepList(@PathVariable String testCaseId,@PathVariable String testScenarioId,@PathVariable String requirementId,@PathVariable String moduleId, @PathVariable String projectId,
			HttpServletRequest req) throws IOException {
		Response response = Utils.getResponseObject("getting list of test step list details data");
		try {
			List<TestStepModel> testStepModelList=testStepService.getTestStepList(testCaseId, testScenarioId, requirementId, moduleId, projectId);
			response.setUrl(req.getRequestURL().toString());
			response.setData(testStepModelList);

		} catch (Exception e) {
			log.error("error in getting tsetStepList details", e);
			throw new TestStepException("error in getting tsetStepList details");

		}
		return (String) Utils.getJson(response);
	}
	
	@DeleteMapping("/deleteTestStep/{testStepsId}/{testCaseId}/{testScenarioId}/{requirementId}/{moduleId}/{projectId}")
	public Response deleteTestStep(@PathVariable String testStepsId,@PathVariable String testCaseId,@PathVariable String testScenarioId, @PathVariable String requirementId,
			@PathVariable String moduleId, @PathVariable String projectId, HttpServletRequest req) {
		try {
			
		
		log.info("url of the application" + req.getRequestURL().toString());
		Response response = testStepService.deleteTestStep(testStepsId, testCaseId, testScenarioId, requirementId, moduleId, projectId);
		response.setUrl(req.getRequestURL().toString());
		return response;
		} catch (Exception e) {
			log.error("error in deleting tsetStep details", e);
			throw new TestStepException("error in deleting tsetStep details");

		}
		
	}
	@PutMapping("/updateTestStep/{testCaseId}/{testScenarioId}/{requirementId}/{moduleId}/{projectId}")
	public Response updateTestStep(@RequestBody TestStepModel testStepModel,@PathVariable String testCaseId,@PathVariable String testScenarioId,
			@PathVariable String requirementId, @PathVariable String moduleId, @PathVariable String projectId,
			HttpServletRequest req) {
		try {
		Response response = Utils.getResponseObject("updating testSceanrio details data");
		log.info("url of the application" + req.getRequestURL().toString());
		response= testStepService.updateTestStep(testStepModel, testCaseId, testScenarioId, requirementId, moduleId, projectId);
		response.setUrl(req.getRequestURL().toString());
		
		return response;
		} catch (Exception e) {
			log.error("error in updating tsetStep details", e);
			throw new TestStepException("error in updating tsetStep details");

		}
		
	}

}
