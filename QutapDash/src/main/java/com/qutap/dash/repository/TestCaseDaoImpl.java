package com.qutap.dash.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.qutap.dash.commonUtils.Response;
import com.qutap.dash.commonUtils.StatusCode;
import com.qutap.dash.commonUtils.Utils;
import com.qutap.dash.customException.ModuleException;
import com.qutap.dash.customException.TestCaseException;
import com.qutap.dash.customException.TestScenarioException;
import com.qutap.dash.domain.ModuleDomain;
import com.qutap.dash.domain.ProjectInfoDomain;
import com.qutap.dash.domain.RequirementDomain;
import com.qutap.dash.domain.TestCaseDomain;
import com.qutap.dash.domain.TestScenarioDomain;

@Repository
@Transactional
public class TestCaseDaoImpl implements TestCaseDao{

	Logger log = LoggerFactory.getLogger(ModuleDaoImpl.class);
	@Autowired
	MongoTemplate mongoTemplate;

	@Override
	public TestCaseDomain getTestCaseById(String testCaseId,String testScenarioId, String requirementId, String moduleId,
			String projectId) {
		Response response = Utils.getResponseObject("getting testCase Details");
		try {
			
			Query query = new Query();
			query.addCriteria(Criteria.where("moduleList.requirementList.testScenarioList.testCaseList.tsetCaseId").is(testCaseId)
					.andOperator(Criteria.where("moduleList.requirementList.testScenarioList.testScenarioId").is(testScenarioId)));
			ProjectInfoDomain projectInfo = mongoTemplate.findOne(query, ProjectInfoDomain.class);
			
			ModuleDomain module = projectInfo.getModuleList().stream()
					.filter(i -> i.getModuleId().equalsIgnoreCase(moduleId)).collect(Collectors.toList()).get(0);
			RequirementDomain requirementDomain = module.getRequirementList().stream().filter(i->i.getRequirementId().equalsIgnoreCase(requirementId)).collect(Collectors.toList()).get(0); 
			TestScenarioDomain testScenarioDomain=requirementDomain.getTestScenarioList().stream().filter(i->i.getTestScenarioId().equalsIgnoreCase(testScenarioId)).collect(Collectors.toList()).get(0);
			return testScenarioDomain.getTestCaseList().stream().filter(i->i.getTestCaseId().equalsIgnoreCase(testCaseId)).collect(Collectors.toList()).get(0);
		} catch (Exception e) {
			log.error("error in getting testCase detail", e);
			throw new  TestCaseException("error in getting testCase detail");
		}
	}

	@Override
	public List<TestCaseDomain> getTestCaseList(String testScenarioId, String requirementId, String moduleId,
			String projectId) {
		
		Response response = Utils.getResponseObject("getting testCaseList Details");
		try {
			
			Query query = new Query();
			query.addCriteria(Criteria.where("moduleList.requirementList.testScenarioList.testScenarioId").is(testScenarioId)
					.andOperator(Criteria.where("moduleList.requirementList.requirementId").is(requirementId)));
			ProjectInfoDomain projectInfo = mongoTemplate.findOne(query, ProjectInfoDomain.class);
			
			ModuleDomain module = projectInfo.getModuleList().stream()
					.filter(i -> i.getModuleId().equalsIgnoreCase(moduleId)).collect(Collectors.toList()).get(0);
			RequirementDomain requirementDomain = module.getRequirementList().stream().filter(i->i.getRequirementId().equalsIgnoreCase(requirementId)).collect(Collectors.toList()).get(0); 
			TestScenarioDomain testScenarioDomain=requirementDomain.getTestScenarioList().stream().filter(i->i.getTestScenarioId().equalsIgnoreCase(testScenarioId)).collect(Collectors.toList()).get(0);
			return testScenarioDomain.getTestCaseList();
		} catch (Exception e) {
			log.error("error in getting testcaseList detail", e);
			throw new  TestCaseException("error in getting testCaselist detail");
		}
	}

	@Override
	public Response deleteTestCase(String testCaseId, String testScenarioId, String requirementId, String moduleId,
			String projectId) {
		Response response = Utils.getResponseObject("deleting testCase Details");
		
		try {
			
			Query query = new Query();
			query.addCriteria(Criteria.where("moduleList.requirementList.testScenarioList.testCaseList.testCaseId").is(testCaseId)
					.andOperator(Criteria.where("moduleList.requirementList.testScenarioList.testScenarioId").is(testScenarioId)));
			ProjectInfoDomain projectInfo = mongoTemplate.findOne(query, ProjectInfoDomain.class);
			
			ModuleDomain module = projectInfo.getModuleList().stream()
					.filter(i -> i.getModuleId().equalsIgnoreCase(moduleId)).collect(Collectors.toList()).get(0);
			RequirementDomain requirementDomain = module.getRequirementList().stream().filter(i->i.getRequirementId().equalsIgnoreCase(requirementId)).collect(Collectors.toList()).get(0); 
			TestScenarioDomain testScenarioDomain=requirementDomain.getTestScenarioList().stream().filter(i->i.getTestScenarioId().equalsIgnoreCase(testScenarioId)).collect(Collectors.toList()).get(0);
			testScenarioDomain.getTestCaseList().removeIf(i->i.getTestCaseId().equalsIgnoreCase(testCaseId));
			Update update = new Update();
			List<ModuleDomain> modList = projectInfo.getModuleList();
			
			System.out.println(modList);
			update.set("moduleList", modList);
			mongoTemplate.updateMulti(query, update, "projectInfo");

			response.setStatus(StatusCode.SUCCESS.name());
			response.setData(testScenarioDomain);
			return response;
		} catch (Exception e) {
			log.error("error in deleting testCase detail", e);
			throw new  TestCaseException("error in delating testCase detail");
		}
	}

	@Override
	public Response updateTestCase(TestCaseDomain testCaseDomain, String testScenarioId, String requirementId,
			String moduleId, String projectId) {
		Response response = Utils.getResponseObject("updating TestCase Details");
		try {
			String testCaseId= testCaseDomain.getTestCaseId();
					
			Query query = new Query();
			query.addCriteria(Criteria.where("moduleList.requirementList.testScenarioList.testCaseList.testCaseId").is(testCaseId));
			ProjectInfoDomain projectInfo = mongoTemplate.findOne(query, ProjectInfoDomain.class, "projectInfo");
			ModuleDomain mod = projectInfo.getModuleList().stream()
					.filter(i -> i.getModuleId().equalsIgnoreCase(moduleId)).collect(Collectors.toList()).get(0);
			RequirementDomain requirement = mod.getRequirementList().stream()
					.filter(i -> i.getRequirementId().equalsIgnoreCase(requirementId)).collect(Collectors.toList())
					.get(0);
			
			TestScenarioDomain testSceDomain = requirement.getTestScenarioList().stream()
					.filter(i -> i.getTestScenarioId().equalsIgnoreCase(testScenarioId))
					.collect(Collectors.toList()).get(0);
			TestCaseDomain testCasDomain=testSceDomain.getTestCaseList().stream().filter(i->i.getTestCaseId().equalsIgnoreCase(testCaseId)).collect(Collectors.toList()).get(0);

			if (!(testCasDomain.getTestCaseName().equalsIgnoreCase(testCaseDomain.getTestCaseName()))
					&& testCaseDomain.getTestCaseName() != null) {
				
				testCasDomain.setTestCaseName(testCaseDomain.getTestCaseName());

			}
			if (!(testCasDomain.getTestCasePriority().equalsIgnoreCase(testCaseDomain.getTestCasePriority()))
					&& testCaseDomain.getTestCasePriority()!= null) {
				
				testCasDomain.setTestCasePriority(testCaseDomain.getTestCasePriority());

			}
			if (!(testCasDomain.getTestCaseDesciption().equalsIgnoreCase(testCaseDomain.getTestCaseDesciption()))
					&& testCaseDomain.getTestCaseDesciption()!= null) {
				
				testCasDomain.setTestCaseDesciption(testCaseDomain.getTestCaseDesciption());

			}
			if (!(testCasDomain.getTestCaseCategory().equalsIgnoreCase(testCaseDomain.getTestCaseCategory()))
					&& testCaseDomain.getTestCaseCategory()!= null) {
				
				testCasDomain.setTestCaseCategory(testCaseDomain.getTestCaseCategory());

			}
			
			List<ModuleDomain> modList = projectInfo.getModuleList();
			Update update = new Update();
			update.set("moduleList", modList);
			mongoTemplate.updateMulti(query, update, "projectInfo");
			response.setStatus(StatusCode.SUCCESS.name());
			response.setData(testCasDomain);
			return response;
		} catch (Exception e) {
			log.error("error in updating testingCase detail", e);
			throw new  TestCaseException("error in updating testCase detail");
			
		}
	}

	@Override
	public Response deleteAllTestCase(String testScenarioId, String requirementId, String moduleId, String projectId) {
			Response response = Utils.getResponseObject("deleting testCase Details");
		
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("moduleList.requirementList.testScenarioList.testScenarioId").is(testScenarioId));
					
			ProjectInfoDomain projectInfo = mongoTemplate.findOne(query, ProjectInfoDomain.class);
			
			ModuleDomain module = projectInfo.getModuleList().stream()
					.filter(i -> i.getModuleId().equalsIgnoreCase(moduleId)).collect(Collectors.toList()).get(0);
			RequirementDomain requirementDomain = module.getRequirementList().stream().filter(i->i.getRequirementId().equalsIgnoreCase(requirementId)).collect(Collectors.toList()).get(0); 
			TestScenarioDomain testScenarioDomain=requirementDomain.getTestScenarioList().stream().filter(i->i.getTestScenarioId().equalsIgnoreCase(testScenarioId)).collect(Collectors.toList()).get(0);
			testScenarioDomain.setTestCaseList(null);
			Update update = new Update();
			List<ModuleDomain> modList = projectInfo.getModuleList();
			
			System.out.println(modList);
			update.set("moduleList", modList);
			mongoTemplate.updateMulti(query, update, "projectInfo");

			response.setStatus(StatusCode.SUCCESS.name());
			response.setData(testScenarioDomain);
			return response;
		} catch (Exception e) {
			log.error("error in deleting testCase detail", e);
			throw new  TestCaseException("error in delating testCase detail");
		}
	}

}
