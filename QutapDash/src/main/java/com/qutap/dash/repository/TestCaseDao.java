package com.qutap.dash.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.qutap.dash.commonUtils.Response;
import com.qutap.dash.domain.TestCaseDomain;


@Repository
public interface TestCaseDao {

	TestCaseDomain getTestCaseById(String testCaseId,String testScenarioId, String requirementId, String moduleId,
			String projectId);
	 List<TestCaseDomain> getTestCaseList(String testScenarioId, String requirementId, String moduleId,
			String projectId);
	Response deleteTestCase(String testCaseId, String testScenarioId, String requirementId, String moduleId,
			String projectId);
	Response updateTestCase(TestCaseDomain testCaseDomain, String testScenarioId, String requirementId, String moduleId,
			String projectId);
	Response deleteAllTestCase(String testScenarioId, String requirementId, String moduleId,
			String projectId);
}
