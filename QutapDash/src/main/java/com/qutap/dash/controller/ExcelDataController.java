package com.qutap.dash.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.qutap.dash.commonUtils.Response;
import com.qutap.dash.service.ExcelDataService;


@RestController
@RequestMapping("/Qutap")
public class ExcelDataController {
	
	Logger log= LoggerFactory.getLogger(ExcelDataController.class);
	
	@Autowired
	ExcelDataService excelDataService;
	
	
	@PostMapping("/excel")
	public Response readExcelData(@RequestParam("file") MultipartFile multipartFile,HttpServletRequest req) throws IOException { 
		log.info("url of the application"+req.getRequestURL().toString());
		Response response=excelDataService.readExcelData(multipartFile);
		response.setUrl(req.getRequestURL().toString());
		return response;		
	}	
	


}
