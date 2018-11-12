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
import com.qutap.dash.customException.RequirementException;
import com.qutap.dash.domain.ModuleDomain;
import com.qutap.dash.domain.ProjectInfoDomain;
import com.qutap.dash.domain.RequirementDomain;


@Repository
@Transactional
public class RequirementDaoImp implements RequirementDao {
	Logger log = LoggerFactory.getLogger(ModuleDaoImpl.class);
	@Autowired
	MongoTemplate mongoTemplate;

	@Override
	public Response saveRequirement(List<RequirementDomain> requirementDomainList, String moduleId,
			String projectId) {
		Response response = Utils.getResponseObject("Adding requirement Details");
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("moduleList.moduleId").is(moduleId)
					.andOperator(Criteria.where("projectId").is(projectId)));
			ProjectInfoDomain projectInfo = mongoTemplate.findOne(query, ProjectInfoDomain.class);
			List<ModuleDomain> modList = projectInfo.getModuleList();
			ModuleDomain mod = projectInfo.getModuleList().stream()
					.filter(i -> i.getModuleId().equalsIgnoreCase(moduleId)).collect(Collectors.toList()).get(0);
			Update update = new Update();
			if (mod.getRequirementList() == null) {
				// update.set("moduleList.requirementList", requirementDomainList);
				mod.setRequirementList(requirementDomainList);
				update.set("moduleList", modList);
				mongoTemplate.upsert(query, update, "projectInfo");

			} else {
				for (RequirementDomain requirementDomain : requirementDomainList) {
					List<RequirementDomain> requirementList = mod.getRequirementList();
					if (!(requirementList.stream()
							.anyMatch(i -> i.getRequirementName().equalsIgnoreCase(requirementDomain.getRequirementName())))) {
						requirementList.add(requirementDomain);
						update.set("moduleList", modList);
						mongoTemplate.upsert(query, update, "projectInfo");
					} else {
						throw new RequirementException(requirementDomain.getRequirementName()+"is already present in database");
					}
					
				}
			}

			response.setStatus(StatusCode.SUCCESS.name());
			response.setData(mod);
			return response;
		} catch (Exception e) {
			log.error("error in saving requirement detail", e);
			throw new RequirementException("error in saving requirement detail");
		}
	}

	@Override
	public RequirementDomain getRequirement(String requirementName, String moduleName, String projectName) {
		Response response = Utils.getResponseObject("getting  requirement Details");
		try {

			Query query = new Query();
			query.addCriteria(Criteria.where("moduleList.requirementList.requirementName").is(requirementName)
					.andOperator(Criteria.where("moduleList.moduleName").is(moduleName)));
			ProjectInfoDomain projectInfo = mongoTemplate.findOne(query, ProjectInfoDomain.class);
			ModuleDomain mod = projectInfo.getModuleList().stream()
					.filter(i -> i.getModuleName().equalsIgnoreCase(moduleName)).collect(Collectors.toList()).get(0);
			RequirementDomain requirementDomain = mod.getRequirementList().stream()
					.filter(i -> i.getRequirementName().equalsIgnoreCase(requirementName)).collect(Collectors.toList())
					.get(0);

			response.setStatus(StatusCode.SUCCESS.name());
			response.setData(requirementDomain);
			return requirementDomain;
		} catch (Exception e) {
			log.error("error in getting Requirement detail", e);
			throw new RequirementException("error in getting requirement detail");
		}
	}
	
	@Override
	public List<RequirementDomain> getRequirementList(String moduleId, String projectId) {
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("moduleList.moduleId").is(moduleId).andOperator(Criteria.where("projectId").is(projectId)));
			
			ProjectInfoDomain projectInfo = mongoTemplate.findOne(query, ProjectInfoDomain.class);
			
			
			ModuleDomain moduleDomain = projectInfo.getModuleList().stream().filter(i->i.getModuleId().equalsIgnoreCase(moduleId)).collect(Collectors.toList()).get(0);
			
			
			
			return moduleDomain.getRequirementList();
			
		} catch (Exception e) {
			log.error("error in getting list of requirement details", e);
			throw new RequirementException("error in getting list of requirement details");
		}
	}

	@Override
	public Response updateRequirement(RequirementDomain requirementDomain, String moduleId, String projectId) {
		Response response = Utils.getResponseObject("updating Module Details");
		try {
			String requirementId = requirementDomain.getRequirementId();
			Query query = new Query();
			query.addCriteria(Criteria.where("moduleList.requirementList.requirementId").is(requirementId)
					.andOperator(Criteria.where("moduleList.moduleId").is(moduleId)));
			ProjectInfoDomain projectInfo = mongoTemplate.findOne(query, ProjectInfoDomain.class, "projectInfo");
			ModuleDomain mod = projectInfo.getModuleList().stream()
					.filter(i -> i.getModuleId().equalsIgnoreCase(moduleId)).collect(Collectors.toList()).get(0);
			RequirementDomain requirement = mod.getRequirementList().stream()
					.filter(i -> i.getRequirementId().equalsIgnoreCase(requirementId)).collect(Collectors.toList())
					.get(0);

			if (!(requirement.getRequirementName().equalsIgnoreCase(requirementDomain.getRequirementName()))
					&& requirementDomain.getRequirementName() != null) {
				requirement.setRequirementName(requirementDomain.getRequirementName());

			}
			if (!(requirement.getRequirementDescription()
					.equalsIgnoreCase(requirementDomain.getRequirementDescription()))
					&& requirementDomain.getRequirementDescription() != null) {
				requirement.setRequirementDescription(requirementDomain.getRequirementDescription());

			}
			if (!(requirement.getRequirementCases().equalsIgnoreCase(requirementDomain.getRequirementCases()))
					&& requirementDomain.getRequirementCases() != null) {
				requirement.setRequirementCases(requirementDomain.getRequirementCases());

			}
			List<ModuleDomain> modList = projectInfo.getModuleList();

			Update update = new Update();
			update.set("moduleList", modList);
			mongoTemplate.updateMulti(query, update, "projectInfo");
			response.setStatus(StatusCode.SUCCESS.name());
			response.setData(requirement);
			return response;
		} catch (Exception e) {
			log.error("error in updating requirement detail", e);
			throw new ModuleException("error in updating requirement detail");
		}
	}

	@Override
	public Response deleteRequirement(String requirementId, String moduleId, String projectId) {
		Response response = Utils.getResponseObject("deleting  requirement Details");
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("moduleList.requirementList.requirementId").is(requirementId)
					.andOperator(Criteria.where("moduleList.moduleId").is(moduleId)));
			ProjectInfoDomain projectInfo = mongoTemplate.findOne(query, ProjectInfoDomain.class);
			ModuleDomain mod = projectInfo.getModuleList().stream()
					.filter(i -> i.getModuleId().equalsIgnoreCase(moduleId)).collect(Collectors.toList()).get(0);
			mod.getRequirementList().removeIf(i -> i.getRequirementId().equalsIgnoreCase(requirementId));
			Update update = new Update();

			List<ModuleDomain> modList = projectInfo.getModuleList();
			System.out.println(modList);
			update.set("moduleList", modList);
			mongoTemplate.updateMulti(query, update, "projectInfo");

			response.setStatus(StatusCode.SUCCESS.name());
			response.setData(mod);
			return response;
		} catch (Exception e) {
			log.error("error in deleting Requirement detail", e);
			throw new RequirementException("error in deleting requirement detail");
		}
	}

	@Override
	public RequirementDomain getRequirementById(String requirementId, String moduleId, String projectId) {
		Response response = Utils.getResponseObject("getting requirement Details");
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("moduleList.requirementList.requirementId").is(requirementId)
					.andOperator(Criteria.where("moduleList.moduleId").is(moduleId)));
			ProjectInfoDomain projectInfo = mongoTemplate.findOne(query, ProjectInfoDomain.class);
			ModuleDomain module = projectInfo.getModuleList().stream()
					.filter(i -> i.getModuleId().equalsIgnoreCase(moduleId)).collect(Collectors.toList()).get(0);
			RequirementDomain requirementDomain = module.getRequirementList().stream().filter(i->i.getRequirementId().equalsIgnoreCase(requirementId)).collect(Collectors.toList()).get(0); 
			return requirementDomain;
		} catch (Exception e) {
			log.error("error in getting requirement detail", e);
			throw new RequirementException("error in getting requirement detail");
		}
	}

	@Override
	public Response deleteAllRequirement(String moduleId, String projectId) {
		Response response = Utils.getResponseObject("deleting All requirement Details");
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("moduleList.moduleId").is(moduleId));
			ProjectInfoDomain projectInfo = mongoTemplate.findOne(query, ProjectInfoDomain.class);
			ModuleDomain mod = projectInfo.getModuleList().stream()
					.filter(i -> i.getModuleId().equalsIgnoreCase(moduleId)).collect(Collectors.toList()).get(0);
			mod.setRequirementList(null);
			Update update = new Update();

			List<ModuleDomain> modList = projectInfo.getModuleList();
			System.out.println(modList);
			update.set("moduleList", modList);
			mongoTemplate.updateMulti(query, update, "projectInfo");

			response.setStatus(StatusCode.SUCCESS.name());
			response.setData(mod);
			return response;
		} catch (Exception e) {
			log.error("error in deleting all Requirement detail", e);
			throw new RequirementException("error in deleting all requirement detail");
		}
	}



}
