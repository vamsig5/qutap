package com.qutap.dash.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.qutap.dash.commonUtils.Response;
import com.qutap.dash.domain.TestScenarioDomain;

@Repository
public interface TestScenarioDao {
	Response saveTestScenario(List<TestScenarioDomain> testScenarioDomain,String requirementId,String moduleId,String projectId);

	TestScenarioDomain getTestScenario(String TestScenarioName,String requirementName, String moduleName, String projectName);

	Response deleteTestScenario(String TestScenarioName,String requirementName, String moduleName,String projectName);

	Response updateTestScenario(TestScenarioDomain testScenarioDomain,String requirementId, String ModuleId, String projectId);

	public List<TestScenarioDomain> getTestScenarioList(String requirementId, String moduleId, String projectId);

	TestScenarioDomain getTestScenarioById(String testScenarioId, String requirementId, String moduleId,
			String projectId);

	Response deleteAllTestScenario(String requirementId, String moduleId, String projectId);
}
