package com.qutap.dash.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qutap.dash.commonUtils.Response;
import com.qutap.dash.commonUtils.Utils;
import com.qutap.dash.config.LoadConfiguration;
import com.qutap.dash.config.ReadQutapProperties;
import com.qutap.dash.config.TestTransport;
import com.qutap.dash.customException.ExecutionException;
import com.qutap.dash.domain.ModuleDomain;
import com.qutap.dash.domain.ProjectInfoDomain;
import com.qutap.dash.domain.RequirementDomain;
import com.qutap.dash.domain.TestCaseDomain;
import com.qutap.dash.domain.TestScenarioDomain;
import com.qutap.dash.model.AgentMeta;
import com.qutap.dash.model.InputData;
import com.qutap.dash.repository.ExcelDataDao;
import com.qutap.dash.repository.ExecutionDao;
import com.qutap.dash.repository.ProjectInfoDao;
import com.qutap.dash.repository.TestScenarioDao;

@Service
public class ExecutionServiceImpl implements ExecutionService {

	Logger log = LoggerFactory.getLogger(ExecutionServiceImpl.class);

	@Autowired
	ExecutionDao executionDao;
	
	@Autowired
	ProjectInfoDao projectInfoDao;

	@Autowired
	ExcelDataDao excelDataDao;

	@Autowired
	ReadQutapProperties readQutapProperties;
	
	@Autowired
	TestScenarioDao testScenarioDao;
	


	List<Response> executionResponseList = new ArrayList<>();

	private static long txnId;

	@Override
	public List<Response> executeData(String testScenarioName, String requirementName, String moduleName, String projectName) {
		Response response=new Response();
		JSONObject resJSON = null;
		JSONObject json = null;
		LoadConfiguration loadConfig = new LoadConfiguration();
		JSONObject pluginObject = null;
		try {
			pluginObject = new JSONObject(loadConfig.getProperties());
		} catch (Exception e) {
			log.error("error in loading plugin", e);
			throw new ExecutionException("error in loading plugin");
		}
		InputData input = new InputData();
		input.setService("QuTapService");
		input.setApiVersion("10.2");

		AgentMeta agentMeta = new AgentMeta();
		agentMeta.setASync("false");
		agentMeta.setLogLevel("debug");
		agentMeta.setResultType("LOG");
		input.setAgentMeta(agentMeta);
	

		try {
			TestScenarioDomain testScenarioDomain=testScenarioDao.getTestScenario(testScenarioName, requirementName, moduleName, projectName);
		List<TestCaseDomain> testCaseDomainList	=testScenarioDomain.getTestCaseList();
	for(TestCaseDomain testCaseDomain:testCaseDomainList)
	{
			if (testCaseDomain.getExcecuteOrSkip().equalsIgnoreCase("Y")) {
				
				input.setTestCaseDomain(testCaseDomain);
				txnId++;
				input.setTxnId(txnId + "");
				String runner = testCaseDomain.getRunnerType();
				String plugin = null;
				for (String string : pluginObject.keySet()) {
					if (string.equals(runner)) {
						plugin = pluginObject.getString(runner);
					}
				}
				input.setPlugin(plugin);
				json = new JSONObject(input);
				resJSON = TestTransport.postRequestExec(readQutapProperties.getExcecutionPath(), json);
				response = executionDao.saveExecutionData(Utils.getObject(resJSON.toString()));
			}
			if (testCaseDomain.getExcecuteOrSkip().equalsIgnoreCase("N")) {
				System.out.println("TestCase data Skipped" + testCaseDomain);
				response=new Response();

			}

			executionResponseList.add(response);
		}
	
		}
		catch (Exception e) {
			log.error("error in executing the test case",e);
			throw new ExecutionException("error in executing the test case");
		}
		System.out.println(response);

		return executionResponseList;

	}

	@Override
	public List<Response> executeAllTestCases(String projectId) {

		try {

			ProjectInfoDomain projectInfo = projectInfoDao.getProjectInfoById(projectId);
			List<ModuleDomain> modList = projectInfo.getModuleList();
			for (ModuleDomain moduleDomain : modList) {

				List<RequirementDomain> reqList = moduleDomain.getRequirementList();
				for (RequirementDomain requirementDomain : reqList) {

					List<TestScenarioDomain> testSceList = requirementDomain.getTestScenarioList();
					for (TestScenarioDomain testScenarioDomain : testSceList) {
						String projectName = projectInfo.getProjectName();
						String moduleName = moduleDomain.getModuleName();
						String requirementName = requirementDomain.getRequirementName();
						String testScenarioName = testScenarioDomain.getTestScenarioName();

						List<Response> responses = executeData(testScenarioName, requirementName, moduleName,
								projectName);
						for (Response response : responses) {
							executionResponseList.add(response);
						}

					}

				}

			}
			return executionResponseList;
		} catch (Exception e) {
			log.error("error in executing the test case", e);
			throw new ExecutionException("error in executing the test case");
		}
	}
}
	
	
	



