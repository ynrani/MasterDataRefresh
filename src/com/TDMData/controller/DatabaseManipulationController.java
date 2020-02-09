package com.TDMData.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.TDMData.constant.AppConstant;
import com.TDMData.constant.MessageConstant;
import com.TDMData.exception.BaseException;
import com.TDMData.exception.ServiceException;
import com.TDMData.model.DTO.DataTableExportImportDTO;
import com.TDMData.model.DTO.DbConnectionsDTO;
import com.TDMData.service.DatabaseTableManipulationService;
import com.TDMData.service.DbConnectionService;
import com.TDMData.service.ProfilerCreateService;
import com.TDMData.service.TableExtractionDetailsService;
import com.TDMData.service.UploadService;
import com.TDMData.util.PaginationUtil;

/**
 * @author Sushil Birajdar
 * subirajd
 *
 */

@Controller
public class DatabaseManipulationController {

	private static Logger logger = Logger
			.getLogger(DatabaseManipulationController.class);

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
	
	/*@RequestMapping(value="createSchemaForUpload",method=RequestMethod.POST)
	@ResponseBody
	public String createSchemaForUpload(HttpServletRequest request,@ModelAttribute("model") DataTableExportImportDTO dbCon) {
		
		
		System.out.println("Inside Create schema file:: ");
		
		File file = null;
		String targetId = "";
		try {
			System.out.println(dbConnectionsDto.getId());
			System.out.println(dbConnectionsDto.getDatabaseFile());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return "weldone ";
	}*/
	@RequestMapping(value = "loadTablesIntoDatabase", method = RequestMethod.POST)
	public String saveFlatFileConnectionWithFile(HttpServletRequest request, @ModelAttribute("dbConnectionsDto") DataTableExportImportDTO dataTableExportImportdto) {
		
		logger.info(MessageConstant.DB_EXTRACT_CNTRL
				+ MessageConstant.DB_EXTRACT_CNTRL_SAVE
				+ MessageConstant.LOG_INFO_PARAMS_SEPC );
		
		String redirectPage = "dataTableLoad";
		try {
			
			databaseTableManipulationService
					.loadDatabaseLatable(dataTableExportImportdto);
			
			List<DbConnectionsDTO> dbConnectionsDTOs = dbConnectionService
			.connectionsDashboard(
					0,
					0,
					true,
					(String) request.getSession().getAttribute(
							AppConstant.SESSION_UID));
			dataTableExportImportdto.setDbConnectionsDtoList(dbConnectionsDTOs);
			
			dataTableExportImportdto.setCreateSchema(null);
			logger.info(MessageConstant.DB_EXTRACT_CNTRL
					+ MessageConstant.DB_EXTRACT_SAVE_SUCCESS_CNTRL
					+ MessageConstant.LOG_INFO_PARAMS_SEPC );

		} catch (Exception e) {
			e.printStackTrace();
		}
		return redirectPage;
	}

	@RequestMapping(value = "dataTableLoad", method = RequestMethod.GET)
	public String getDataTableload(
			@RequestParam(value = AppConstant.PAGE, required = false) String page,
			ModelMap model,
			HttpServletRequest request,
			HttpServletResponse response,
			@ModelAttribute("dbConnectionsDto") DataTableExportImportDTO dbConnectionsDtoList)
			throws BaseException {

		logger.info(MessageConstant.DB_MANIPULAT_CTRL
				+ MessageConstant.DB_CON_DASH
				+ MessageConstant.LOG_INFO_PARAMS_SEPC + page);

		PaginationUtil pagenation = new PaginationUtil();
		int recordsperpage = 10;
		try {
			int offSet = pagenation.getOffset(request, recordsperpage);
			List<DbConnectionsDTO> dbConnectionsDTOs = dbConnectionService
					.connectionsDashboard(
							offSet,
							recordsperpage,
							true,
							(String) request.getSession().getAttribute(
									AppConstant.SESSION_UID));
			logger.info(MessageConstant.DB_MANIPULAT_CTRL
					+ MessageConstant.DB_CON_DASH
					+ MessageConstant.LOG_INFO_RETURN);
			dbConnectionsDtoList.setDbConnectionsDtoList(dbConnectionsDTOs);
			return "dataTableLoad";
		} catch (BaseException baseEx) {
			logger.error(MessageConstant.DB_MANIPULAT_CTRL
					+ MessageConstant.DB_CON_DASH
					+ MessageConstant.LOG_ERROR_EXCEPTION + baseEx);
			if (null != baseEx.getErrorCode()
					|| baseEx.getErrorCode().equalsIgnoreCase("null")) {

				if (baseEx.getErrorCode().startsWith("")) {
					return "dataTableLoad";
				}
			}
		}
		return "dataTableLoad";
	}

	@RequestMapping(value = "getTablesFromDatabase", method = RequestMethod.GET)
	@ResponseBody
	public String getTablesFromDatabase(
			@RequestParam("databaseName") String database) {

		String responceText = "";
		JSONArray jsonArray = new JSONArray();
		List<String> tableList = null;

		if (null != database) {
			try {
				DbConnectionsDTO dbDto = dbConnectionService
						.getConnectionDetails(Long.parseLong(database));
				tableList = uploadService.getAllTable(dbDto);

				for (String table : tableList) {
					// responceText = responceText+table+"\",\"";
					jsonArray.put(table);
				}
				JSONObject json = new JSONObject();
				json.put("dataTable", jsonArray);
				responceText = json.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return responceText;
	}

	private List<String> parseJson(String str) {
		List<String> strList = new ArrayList<String>();
		try {
			org.json.JSONObject json = new org.json.JSONObject(str);
			JSONArray jsonA = new JSONArray(json.get("dataTable")+"");
			for(int i = 0; i<jsonA.length();i++){ 
				strList.add((String) jsonA.get(i));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return strList;
		
	}
	@RequestMapping(value = "editExtractionDetails", method = RequestMethod.GET)
	public String editExtractionDetails(
			@ModelAttribute("dbConnectionsDto") DataTableExportImportDTO dbConnectionsDto,
			HttpServletRequest request) {

		try {
			tableExtractionDetailsService
					.selectTableExtractionDetailById(dbConnectionsDto);

			String tblList[] = dbConnectionsDto.getTableExtractDetails().getTableList().split(",");
			String tableUnselectedStr = getTablesFromDatabase(""+dbConnectionsDto.getTableExtractDetails().getDbConnectionDto().getConId());
			
			List<String> tableUnselectedList = parseJson(tableUnselectedStr);
			for(String str : tblList) {
				dbConnectionsDto.getSelectedTableList().add(str);
				tableUnselectedList.remove(str);
			}
			
			dbConnectionsDto.setUnSelectedTableList(tableUnselectedList);

			PaginationUtil pagenation = new PaginationUtil();
			int recordsperpage = 10;
			int offSet = pagenation.getOffset(request, recordsperpage);
			
			List<DbConnectionsDTO> dbConnectionsDTOs = dbConnectionService
					.connectionsDashboard(offSet,recordsperpage,true,(String) request.getSession().getAttribute(
									AppConstant.SESSION_UID));
			
			dbConnectionsDto.setDbConnectionsDtoList(dbConnectionsDTOs);
			return "dataTableExport";

		} catch (ServiceException e) {
			
			e.printStackTrace();
		}
		return "dataTableExport";
	}

	@RequestMapping(value = "deleteExtractionDetails", method = RequestMethod.GET)
	public String deleteExtractionDetails(
			@RequestParam("id") String extractId,
			@ModelAttribute("dataTableExportImportDTO") DataTableExportImportDTO dataTableExportImportDTO) {

		dataTableExportImportDTO.getTableExtractDetails().setId(
				dataTableExportImportDTO.getId());
		try {
			tableExtractionDetailsService
					.delete(dataTableExportImportDTO.getTableExtractDetails());

		} catch (ServiceException e) {

			e.printStackTrace();
		}
		return "redirect:"+AppConstant.TABLE_EXTR_DASH_BOARD;
	}

	@RequestMapping(value = AppConstant.TABLE_EXTR_DASH_BOARD, method = RequestMethod.GET)
	public String getDashBoard(
			@ModelAttribute("dataTableExportImportDTO") DataTableExportImportDTO dataTableExportImportDTO,
			HttpServletRequest request) {

		try {
			tableExtractionDetailsService.selectAll(
					dataTableExportImportDTO,
					(String) request.getSession().getAttribute(
							AppConstant.SESSION_UID));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return AppConstant.TABLE_EXTR_DASH_BOARD;
	}

	@RequestMapping(value = "downloadExtraction", method = RequestMethod.GET)
	public void downloadExtraction(
			@ModelAttribute("dbConnectionsDto") DataTableExportImportDTO dbConnectionsDto,
			HttpServletRequest reques,HttpServletResponse response) {

		try {
			DataTableExportImportDTO dataTableExportImport = tableExtractionDetailsService
					.selectTableExtractionDetailById(dbConnectionsDto);
			String filePath = profilerCreateService
					.exportDatabaseTables(dataTableExportImport);
			if (filePath != null) {
				//File file = new File(filePath);
				fileDownload(response, filePath);
				File file = new File(filePath);
				file.delete();
			}
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// String filePath =
		// profilerCreateService.exportDatabaseTables(dbConnectionsDto);
		//return "redirect:dashBoard";
	}

	private void fileDownload(HttpServletResponse response, String filename) {

		response.setContentType("text/html");
		PrintWriter out;
		try {
			out = response.getWriter();
			response.setContentType("APPLICATION/OCTET-STREAM");
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ filename + "\"");

			FileInputStream fileInputStream = new FileInputStream(filename);

			int i;
			while ((i = fileInputStream.read()) != -1) {
				out.write(i);
			}
			fileInputStream.close();
			out.close();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		}

	@RequestMapping(value = "exportTables", method = RequestMethod.POST)
	public String exportDataTable(
			@ModelAttribute("dbConnectionsDto") DataTableExportImportDTO dbConnectionsDto,
			HttpServletRequest request) {

		try {

			dbConnectionsDto.getTableExtractDetails().setUserId(
					(String) request.getSession().getAttribute(
							AppConstant.SESSION_UID));
			try {
				tableExtractionDetailsService.save(dbConnectionsDto
						.getTableExtractDetails());
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return "redirect:"+AppConstant.TABLE_EXTR_DASH_BOARD;
		// return "redirect:dataTableExtract";
	}

	@RequestMapping(value = "dataTableExtract", method = RequestMethod.GET)
	public String getDataTableExport(
			@RequestParam(value = AppConstant.PAGE, required = false) String page,
			ModelMap model,
			HttpServletRequest request,
			HttpServletResponse response,
			@ModelAttribute("dbConnectionsDto") DataTableExportImportDTO dbConnectionsDtoList)
			throws BaseException {
		logger.info(MessageConstant.DB_MANIPULAT_CTRL
				+ MessageConstant.DB_CON_DASH
				+ MessageConstant.LOG_INFO_PARAMS_SEPC + page);

		PaginationUtil pagenation = new PaginationUtil();
		int recordsperpage = 10;
		try {
			int offSet = pagenation.getOffset(request, recordsperpage);
			List<DbConnectionsDTO> dbConnectionsDTOs = dbConnectionService
					.connectionsDashboard(
							offSet,
							recordsperpage,
							true,
							(String) request.getSession().getAttribute(
									AppConstant.SESSION_UID));
	
			logger.info(MessageConstant.DB_MANIPULAT_CTRL
					+ MessageConstant.DB_CON_DASH
					+ MessageConstant.LOG_INFO_RETURN);
			dbConnectionsDtoList.setDbConnectionsDtoList(dbConnectionsDTOs);
			return "dataTableExport";
		} catch (BaseException baseEx) {
			logger.error(MessageConstant.DB_MANIPULAT_CTRL
					+ MessageConstant.DB_CON_DASH
					+ MessageConstant.LOG_ERROR_EXCEPTION + baseEx);
			if (null != baseEx.getErrorCode()
					|| baseEx.getErrorCode().equalsIgnoreCase("null")) {
				if (baseEx.getErrorCode().startsWith("")) {
					return "dataTableExport";
				}
			}

		}
		return "dataTableExport";
	}
}
