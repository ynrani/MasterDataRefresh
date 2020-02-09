package com.TDMData.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.TDMData.constant.AppConstant;
import com.TDMData.constant.MessageConstant;
import com.TDMData.exception.BaseException;
import com.TDMData.model.DTO.DbConnectionsDTO;
import com.TDMData.service.DatabaseTableManipulationService;
import com.TDMData.service.DbConnectionService;
import com.TDMData.service.ProfilerCreateService;
import com.TDMData.service.TableExtractionDetailsService;
import com.TDMData.service.UploadService;


@Controller
public class DatabaseCreationController {
	@Resource(name = MessageConstant.DB_CON_SERVICE)
	private DbConnectionService dbConnectionService;

	@Resource(name = MessageConstant.UPLOAD_SERVICE)
	private UploadService uploadService;

	@Resource(name = "profilerCreateService")
	private ProfilerCreateService profilerCreateService;

	@Autowired
	private TableExtractionDetailsService tableExtractionDetailsService;

	@Autowired
	private DatabaseTableManipulationService databaseTableManipulationService;
	
	private static final Logger logger = Logger.getLogger(DatabaseCreationController.class);
	
	
	
	@RequestMapping(value = AppConstant.DB_CON, method = RequestMethod.POST)
	public String conPOST(
			@RequestParam(value = AppConstant.TEST_CON, required = false) String testCon,
			@RequestParam(value = AppConstant.CREATE_CON, required = false) String saveCon,
			@ModelAttribute(MessageConstant.DB_CON_CTO) DbConnectionsDTO dbConnectionsDTO,
			ModelMap modelmap, HttpServletRequest request, HttpServletResponse response)
			throws BaseException {
		logger.info(MessageConstant.DB_CON_CTLR + MessageConstant.DB_CON_POST
				+ MessageConstant.LOG_INFO_PARAMS_YES);
		String pingSuccessMsg = null;
		String saveSuccessMsg = null;
		String strConnectName = null;
		try {
			dbConnectionsDTO.setUserId((String) request.getSession().getAttribute(
					AppConstant.SESSION_UID));
			//going to check connection name
			//if(StringUtils.isNotEmpty(dbConnectionsDTO.getConId()))
			strConnectName = dbConnectionService.checkConnectionName(dbConnectionsDTO);
			if(StringUtils.isEmpty(strConnectName)){
			if (null != testCon) {
				pingSuccessMsg = dbConnectionService.testConnection(dbConnectionsDTO);
			} else if (null != saveCon) {
				dbConnectionsDTO = dbConnectionService.saveConnection(dbConnectionsDTO);
				saveSuccessMsg = MessageConstant.SUCCESS_SAVE_MSG;
			}
			}else{
				pingSuccessMsg = MessageConstant.CONNECTION_NAME_EXIST;
			}
			

		} catch (BaseException baseEx) {
			pingSuccessMsg = MessageConstant.PING_FAIL_MSG;
			saveSuccessMsg = MessageConstant.SAVE_FAIL_MSG;
			logger.error(MessageConstant.DB_CON_CTLR + MessageConstant.DB_CON_POST
					+ MessageConstant.LOG_INFO_PARAMS_YES + baseEx);
			if (null != baseEx.getErrorCode() || baseEx.getErrorCode().equalsIgnoreCase("null")) {
				// String exceptionMsg = passcodes and get msg from properties file by passing key
				// as
				// baseEx.getErrorCode();
				if (baseEx.getErrorCode().startsWith("")) {
					modelmap.addAttribute(AppConstant.STATUS, pingSuccessMsg);
					modelmap.addAttribute(AppConstant.SAVE_STS, saveSuccessMsg);
					modelmap.addAttribute(AppConstant.BTN, true);
					return AppConstant.DB_CON_VIEW;
				}
			}
			// responseMsg = passcodes and get msg from properties file by passing key as
			// baseEx.getErrorCode();
			modelmap.addAttribute(AppConstant.STATUS, pingSuccessMsg);
			modelmap.addAttribute(AppConstant.SAVE_STS, saveSuccessMsg);
			modelmap.addAttribute(AppConstant.BTN, true);
			return AppConstant.DB_CON_VIEW;
		}
		if (null != saveCon && StringUtils.isEmpty(strConnectName)) {
			return AppConstant.DB_CON_LIST_RED;
		}
		modelmap.addAttribute(AppConstant.STATUS, pingSuccessMsg);
		modelmap.addAttribute(AppConstant.SAVE_STS, saveSuccessMsg);
		modelmap.addAttribute(AppConstant.BTN, false);
		return AppConstant.DB_CON_VIEW;

	}
	
	@RequestMapping(value = "/fetchConditionRelationTabs",  method = RequestMethod.GET)
	public @ResponseBody String fetchConditionRelationTabs(@RequestParam(required = true, value = "reqVals") String reqVals,@RequestParam("databaseId") String databaseId,@RequestParam("conditionFlag") String conditionFlag){
		//String response= AppConstant.FAILED+"# Error occurred while creating profile";
		try{
		if(StringUtils.isNotEmpty(reqVals)){
			
			// Requiremetns  = url,userName,password,lastpassedTabs
			
			DbConnectionsDTO dbDto = dbConnectionService.getConnectionDetails(Long.parseLong(databaseId));
			String strResposne = profilerCreateService.fetchConditionRelationalTabs(dbDto,reqVals,conditionFlag);
			return strResposne;
		}
		} catch (BaseException baseEx) {
		}
		return "";
	}
	
	@RequestMapping(value = AppConstant.DB_CON, method = RequestMethod.GET)
	public String conGet(@RequestParam(value = AppConstant.ID, required = false) String id,
			@ModelAttribute(MessageConstant.DB_CON_CTO) DbConnectionsDTO dbConnectionsDTO,
			ModelMap modelmap, HttpServletRequest request, HttpServletResponse response) {
		logger.info(MessageConstant.DB_CON_CTLR + MessageConstant.DB_CON_GET
				+ MessageConstant.LOG_INFO_PARAMS_YES);
		try {
			if (null != id) {
				dbConnectionsDTO = dbConnectionService.savedConnection(id);
				modelmap.addAttribute(AppConstant.BTN, false);
			} else {
				modelmap.addAttribute(AppConstant.BTN, true);
			}

		} catch (BaseException baseEx) {

			logger.error(MessageConstant.DB_CON_CTLR + MessageConstant.DB_CON_GET
					+ MessageConstant.LOG_INFO_PARAMS_YES + baseEx);
			if (null != baseEx.getErrorCode() || baseEx.getErrorCode().equalsIgnoreCase("null")) {
				
				if (baseEx.getErrorCode().startsWith("")) {

					modelmap.addAttribute(AppConstant.BTN, true);
					return AppConstant.DB_CON_VIEW;
				}
			}
			// responseMsg = passcodes and get msg from properties file by passing key as
			// baseEx.getErrorCode();
			modelmap.addAttribute(AppConstant.BTN, true);
			modelmap.addAttribute(MessageConstant.DB_CON_CTO, dbConnectionsDTO);
			return AppConstant.DB_CON_VIEW;
		}
		modelmap.addAttribute(com.TDMData.constant.MessageConstant.DB_CON_CTO, dbConnectionsDTO);
		return AppConstant.DB_CON_VIEW;
	}
	
	@RequestMapping(value = AppConstant.DB_CON_LIST, method = RequestMethod.GET)
	public String connectionsDashboard(
			@RequestParam(value = AppConstant.PAGE, required = false) String page, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) throws BaseException {
		logger.info(MessageConstant.DB_CON_CTLR + MessageConstant.DB_CON_DASH
				+ MessageConstant.LOG_INFO_PARAMS_SEPC + page);

		Long totalRecords = 0L;
		com.TDMData.util.PaginationUtil pagenation = new com.TDMData.util.PaginationUtil();
		int recordsperpage = 10;
		try {
			int offSet = pagenation.getOffset(request, recordsperpage);
			/*totalRecords = dbConnectionService.connectionsDashboardCount((String) request
					.getSession().getAttribute(AppConstant.SESSION_UID));*/
			List<DbConnectionsDTO> dbConnectionsDTOs = dbConnectionService.connectionsDashboard(
					offSet, recordsperpage, true,
					(String) request.getSession().getAttribute(AppConstant.SESSION_UID));
			pagenation.paginate(totalRecords, request, (double) recordsperpage, recordsperpage);
			int noOfPages = (int) Math.ceil(totalRecords.doubleValue() / recordsperpage);
			request.setAttribute(AppConstant.NO_OF_PAGES, noOfPages);
			model.addAttribute(AppConstant.DB_CONN_DTLS, dbConnectionsDTOs);
			logger.info(MessageConstant.DB_CON_CTLR + MessageConstant.DB_CON_DASH
					+ MessageConstant.LOG_INFO_RETURN);
			return AppConstant.DB_CON_LIST_VIEW;
		} catch (BaseException baseEx) {
			logger.error(MessageConstant.DB_CON_CTLR + MessageConstant.DB_CON_DASH
					+ MessageConstant.LOG_ERROR_EXCEPTION + baseEx);
			if (null != baseEx.getErrorCode() || baseEx.getErrorCode().equalsIgnoreCase("null")) {
				// String exceptionMsg = passcodes and get msg from properties file by passing key
				// as
				// baseEx.getErrorCode();
				if (baseEx.getErrorCode().startsWith("")) {
					return AppConstant.DB_CON_LIST_VIEW;
				}
			}
			// responseMsg = passcodes and get msg from properties file by passing key as
			// baseEx.getErrorCode();
			return AppConstant.DB_CON_LIST_VIEW;
		}
	}
	
	@RequestMapping(value = AppConstant.DB_CON_DELETE, method = RequestMethod.GET)
	public String deleteConnection(
			@RequestParam(value = AppConstant.ID, required = false) String conId,
			ModelMap modelmap, HttpServletRequest request, HttpServletResponse response) {
		logger.info(MessageConstant.DB_CON_CTLR + MessageConstant.DB_CON_DELETE
				+ MessageConstant.LOG_INFO_PARAMS_YES);
		boolean sts = false;
		try {
			if (null != conId) {
				sts = dbConnectionService.deleteConnection(conId);
			}
			logger.info(MessageConstant.DB_CON_CTLR + MessageConstant.DB_CON_DELETE
					+ MessageConstant.LOG_INFO_RETURN + sts);
		} catch (BaseException baseEx) {

			logger.error(MessageConstant.DB_CON_CTLR + MessageConstant.DB_CON_DELETE
					+ MessageConstant.LOG_INFO_PARAMS_YES + baseEx);
			if (null != baseEx.getErrorCode() || baseEx.getErrorCode().equalsIgnoreCase("null")) {
				// String exceptionMsg = passcodes and get msg from properties file by passing key
				// as
				// baseEx.getErrorCode();
				if (baseEx.getErrorCode().startsWith("")) {
					return AppConstant.DB_CON_LIST_RED;
				}
			}
			// responseMsg = passcodes and get msg from properties file by passing key as
			// baseEx.getErrorCode();
			return AppConstant.DB_CON_LIST_RED;
		}
		return AppConstant.DB_CON_LIST_RED;
	}
}
