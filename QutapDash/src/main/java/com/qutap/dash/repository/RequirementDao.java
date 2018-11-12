package com.qutap.dash.repository;

import java.util.List;



import com.qutap.dash.commonUtils.Response;
import com.qutap.dash.domain.RequirementDomain;
import com.qutap.dash.model.RequirementModel;

 public interface RequirementDao {
	Response saveRequirement(List<RequirementDomain> requirementDomain,String projectId,String moduleId);

	RequirementDomain getRequirement(String requirementName, String moduleName, String projectName);
	
	public List<RequirementDomain> getRequirementList(String moduleId,String projectId);

	Response deleteRequirement(String requirementId, String moduleId,String projectId);

	Response updateRequirement(RequirementDomain requirementDomain, String ModuleId, String projectId);

	RequirementDomain getRequirementById(String requirementId, String moduleId, String projectId);

	Response deleteAllRequirement(String moduleId, String projectId);
}
