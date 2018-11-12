package com.qutap.dash.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties("qutap")
public class ReadQutapProperties {
	
	private String excelPath;
	private String excecutionPath;

	public String getExcecutionPath() {
		return excecutionPath;
	}

	public void setExcecutionPath(String excecutionPath) {
		this.excecutionPath = excecutionPath;
	}

	public String getExcelPath() {
		return excelPath;
	}

	public void setExcelPath(String excelPath) {
		this.excelPath = excelPath;
	}
	
	

}
