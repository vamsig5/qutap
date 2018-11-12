package com.qutap.dash.repository;

import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import com.qutap.dash.commonUtils.Response;
import com.qutap.dash.domain.ProjectInfoDomain;

public interface ExecutionDao {

	Response saveExecutionData(Object testExecutedResult);
  
}
