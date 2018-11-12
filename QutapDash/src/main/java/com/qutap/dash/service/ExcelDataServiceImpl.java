package com.qutap.dash.service;




import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.bson.types.ObjectId;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.qutap.dash.commonUtils.DateUtility;
import com.qutap.dash.commonUtils.Response;
import com.qutap.dash.commonUtils.StatusCode;
import com.qutap.dash.config.LoadConfiguration;
import com.qutap.dash.config.ReadQutapProperties;
import com.qutap.dash.customException.TestCaseException;
import com.qutap.dash.domain.ModuleDomain;
import com.qutap.dash.domain.ProjectInfoDomain;
import com.qutap.dash.domain.RequirementDomain;
import com.qutap.dash.domain.TestCaseDomain;
import com.qutap.dash.domain.TestScenarioDomain;
import com.qutap.dash.domain.TestStepDomain;
import com.qutap.dash.model.AgentMeta;
import com.qutap.dash.model.InputData;
import com.qutap.dash.model.TestCaseModel;
import com.qutap.dash.repository.ExcelDataDao;


@Service
public class ExcelDataServiceImpl implements ExcelDataService{
	


Logger log= LoggerFactory.getLogger(ExcelDataServiceImpl.class);
	
	@Autowired
	ExcelDataDao excelDataDao;
	
	@Autowired
	ReadQutapProperties readProperties;
	

	Response response=new Response();
	
	private static long txnId;
	private static final String TEST_CASE_END = "TC_END";

	@Override
	public Response readExcelData(MultipartFile multipartFile) {
		
		try {
			try {
				if (readProperties.getExcelPath() != null && !"".equals(readProperties.getExcelPath().trim())) {
					File dirFileObj = FileUtils.getFile(readProperties.getExcelPath());
					if (!dirFileObj.exists()) {
						FileUtils.forceMkdir(dirFileObj);
					}
					byte[] bytes = multipartFile.getBytes();
					Path path = Paths.get(readProperties.getExcelPath() + "/" + multipartFile.getOriginalFilename());
					Files.write(path, bytes);
				}
			} catch (IOException e) {
				log.error("error in creating directory",e);
				throw new TestCaseException("error in creating dirctory");
			}

			Workbook workbook = WorkbookFactory.create(multipartFile.getInputStream());
			int numberOfSheets = workbook.getNumberOfSheets();
			String testCaseValue;
			TestCaseDomain testCaseDomain=null;
			ProjectInfoDomain projectInfoDomain= new ProjectInfoDomain();
			ModuleDomain moduleDomain=null;
			
			
			RequirementDomain requirementDomain=null;
			TestScenarioDomain testScenarioDomain=null;
			
			Row row;
			TestStepDomain testStepDomain;
			DataFormatter dataFormatter = new DataFormatter();
			

			for (int i = 0; i < numberOfSheets; i++) {
				Sheet sheet = workbook.getSheetAt(i);
				Iterator<Row> rowIterator = sheet.iterator();
				while (rowIterator.hasNext()) {
					row = rowIterator.next();
					int rownum = row.getRowNum();
					if (rownum >= 6) {
						if (!((dataFormatter.formatCellValue(row.getCell(0)).isEmpty()))) {
						 
						 
						 
						
						
						    projectInfoDomain.setProjectId(new ObjectId().toString());
							projectInfoDomain.setProjectName(dataFormatter.formatCellValue(row.getCell(0)));
							projectInfoDomain.setProjectDescription(dataFormatter.formatCellValue(row.getCell(1)));
							projectInfoDomain.setModuleList(new ArrayList<ModuleDomain>());
							projectInfoDomain.setCreatedDate(DateUtility.getDate(new Date(), DateUtility.FORMAT_1));
						}
							if (!((dataFormatter.formatCellValue(row.getCell(2)).isEmpty()))) {
								moduleDomain = new ModuleDomain(); 
								moduleDomain.setModuleId(new ObjectId().toString());
								moduleDomain.setModuleName(dataFormatter.formatCellValue(row.getCell(2)));
								moduleDomain.setModuleDescription(dataFormatter.formatCellValue(row.getCell(3)));
								moduleDomain.setRequirementList(new ArrayList<RequirementDomain>());
								moduleDomain.setCreatedDate(DateUtility.getDate(new Date(), DateUtility.FORMAT_1));
								projectInfoDomain.getModuleList().add(moduleDomain);
								
							}
							if (!((dataFormatter.formatCellValue(row.getCell(4)).isEmpty()))) {
								requirementDomain= new RequirementDomain();
							requirementDomain.setRequirementId(new ObjectId().toString());
							requirementDomain.setRequirementName(dataFormatter.formatCellValue(row.getCell(4)));
							requirementDomain.setRequirementCases(dataFormatter.formatCellValue(row.getCell(5)));
							requirementDomain.setRequirementDescription(dataFormatter.formatCellValue(row.getCell(6)));
							requirementDomain.setTestScenarioList(new ArrayList<TestScenarioDomain>());
							requirementDomain.setCreatedDate(DateUtility.getDate(new Date(), DateUtility.FORMAT_1));
							moduleDomain.getRequirementList().add(requirementDomain);
							}
							if (!((dataFormatter.formatCellValue(row.getCell(7)).isEmpty()))) {
								 testScenarioDomain= new TestScenarioDomain();
						    testScenarioDomain.setTestScenarioId(new ObjectId().toString());		 
							testScenarioDomain.setTestScenarioName(dataFormatter.formatCellValue(row.getCell(7)));
							testScenarioDomain.setTestScenarioDescription(dataFormatter.formatCellValue(row.getCell(8)));
							testScenarioDomain.setTestCaseList(new ArrayList<TestCaseDomain>());
							testScenarioDomain.setCreatedDate(DateUtility.getDate(new Date(), DateUtility.FORMAT_1));
							requirementDomain.getTestScenarioList().add(testScenarioDomain);
							}
							if (!((dataFormatter.formatCellValue(row.getCell(9)).isEmpty()))) {
								 testCaseDomain = new TestCaseDomain();
							
							testCaseDomain.setTestScenarioName(dataFormatter.formatCellValue(row.getCell(7)));
							testCaseDomain.setTestCaseId(dataFormatter.formatCellValue(row.getCell(9)));
							testCaseDomain.setTestCaseName(dataFormatter.formatCellValue(row.getCell(10)));
							testCaseDomain.setTestCaseCategory(dataFormatter.formatCellValue(row.getCell(11)));
							testCaseDomain.setTestCasePriority(dataFormatter.formatCellValue(row.getCell(12)));
							testCaseDomain.setTestCaseTag(dataFormatter.formatCellValue(row.getCell(13)));
							testCaseDomain.setTestCaseDesciption(dataFormatter.formatCellValue(row.getCell(14)));
							testCaseDomain.setPositiveOrNegative(dataFormatter.formatCellValue(row.getCell(15)));
							testCaseDomain.setRunnerType(dataFormatter.formatCellValue(row.getCell(16)));
							testCaseDomain.setExcecuteOrSkip(dataFormatter.formatCellValue(row.getCell(19)));
							testCaseDomain.setCreatedDate(DateUtility.getDate(new Date(), DateUtility.FORMAT_1));
							testCaseDomain.setTestStepList(new ArrayList<>());
							testScenarioDomain.getTestCaseList().add(testCaseDomain);
							}
							if (!((dataFormatter.formatCellValue(row.getCell(17)).isEmpty()))) {
							while (rowIterator.hasNext()) {
								row = rowIterator.next();

								if (dataFormatter.formatCellValue(row.getCell(17)).equals(TEST_CASE_END)) {
									break;
								}
								testStepDomain = new TestStepDomain();
								testStepDomain.setTestStepsId(new ObjectId().toString());
								
								testStepDomain.setAction(dataFormatter.formatCellValue(row.getCell(18)));							
								testStepDomain.setDependency(dataFormatter.formatCellValue(row.getCell(20)));
								testStepDomain.setParamGroupObject(dataFormatter.formatCellValue(row.getCell(21)));
								testStepDomain.setStepParam(dataFormatter.formatCellValue(row.getCell(22)));
								testStepDomain.setExpectedResult(dataFormatter.formatCellValue(row.getCell(24)));
								testStepDomain.setActualResult(dataFormatter.formatCellValue(row.getCell(25)));
								testStepDomain.setParamGroupId(dataFormatter.formatCellValue(row.getCell(26)));
								testStepDomain.setCreatedDate(DateUtility.getDate(new Date(),DateUtility.FORMAT_1 ));
								testCaseValue = dataFormatter.formatCellValue(row.getCell(23));
								List<String> testParamData = new ArrayList<String>();
								if (testCaseValue.equals("")) {
									testStepDomain.setTestParamData(testParamData);
								} else {
									for (String eachValue : testCaseValue.split(",")) {
										testParamData.add(eachValue);
									}
									testStepDomain.setTestParamData(testParamData);
								}
								testCaseDomain.getTestStepList().add(testStepDomain);
							}
							}

						
					}

				}
				
					response = excelDataDao.saveExcelData(projectInfoDomain);
				
			}
						

			
			
		} catch (Exception e) {
			log.error("error in saving the excelData",e);
			throw new TestCaseException("error in saving the excelData");
		}
		return response;
	}

	
	
	
}
		
