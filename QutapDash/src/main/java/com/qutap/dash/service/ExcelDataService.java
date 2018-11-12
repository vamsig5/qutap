package com.qutap.dash.service;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import com.qutap.dash.commonUtils.Response;
import com.qutap.dash.model.TestCaseModel;
import com.qutap.dash.model.TestStepModel;

public interface ExcelDataService {

	public Response readExcelData(MultipartFile multipartFile);


}
