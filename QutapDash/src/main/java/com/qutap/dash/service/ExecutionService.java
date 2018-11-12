package com.qutap.dash.service;

import java.util.List;

import com.qutap.dash.commonUtils.Response;

public interface ExecutionService {

	public List<Response> executeData(String testScenarioName, String requirementName, String moduleName, String projectName);

	public List<Response> executeAllTestCases(String projectId);

}
