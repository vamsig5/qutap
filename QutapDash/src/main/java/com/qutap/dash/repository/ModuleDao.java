package com.qutap.dash.repository;

import java.util.List;

import com.qutap.dash.commonUtils.Response;
import com.qutap.dash.domain.ModuleDomain;
import com.qutap.dash.domain.ProjectInfoDomain;

public interface ModuleDao {

	Response saveModule(List<ModuleDomain> moduleDomain,String projectId);
	
	ModuleDomain getModuleByName(String moduleName, String projectName);
	
	ModuleDomain getModuleById(String moduleId, String projectName);
	
	Response updateModule(ModuleDomain moduleDomain, String projectId);
	
	Response deleteModule(String moduleId,String projectId);

	List<ModuleDomain> getModuleList(String projectId);

	Response deleteAllModule(String projectId);
	




}
