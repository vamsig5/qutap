package com.qutap.dash.model;

import java.io.Serializable;

import com.qutap.dash.domain.TestCaseDomain;

public class InputData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AgentMeta agentMeta;
	private String apiVersion;
	private String plugin;
	private TestCaseDomain testCaseDomain;
	private String service;
	private String txnId;
	
	

	@Override
	public String toString() {
		return "InputData [agentMeta=" + agentMeta + ", apiVersion=" + apiVersion + ", plugin=" + plugin
				+ ", testCaseDomain=" + testCaseDomain + ", service=" + service + ", txnId=" + txnId + "]";
	}

	public TestCaseDomain getTestCaseDomain() {
		return testCaseDomain;
	}

	public void setTestCaseDomain(TestCaseDomain testCaseDomain) {
		this.testCaseDomain = testCaseDomain;
	}

	public AgentMeta getAgentMeta() {
		return agentMeta;
	}

	public void setAgentMeta(AgentMeta agentMeta) {
		this.agentMeta = agentMeta;
	}

	

	public String getApiVersion() {
		return apiVersion;
	}

	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}

	public String getPlugin() {
		return plugin;
	}

	public void setPlugin(String plugin) {
		this.plugin = plugin;
	}


	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}

}
