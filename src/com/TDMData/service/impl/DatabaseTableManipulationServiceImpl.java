package com.TDMData.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.TDMData.constant.MessageConstant;
import com.TDMData.databaseUtils.DatabaseOperationsUtil;
import com.TDMData.model.DO.DataConConnectionsDO;
import com.TDMData.model.DTO.DataTableExportImportDTO;
import com.TDMData.model.DTO.DatabaseLoadingInfoModel;
import com.TDMData.model.DTO.DbConnectionsDTO;
import com.TDMData.model.mapper.DbConnectionMapper;
import com.TDMData.model.mapper.UploadMapper;
import com.TDMData.service.DatabaseTableManipulationService;
import com.TDMData.service.DbConnectionService;

/**
 * 
 * @author Sushil Birajdar (subirajd)
 *
 */
@Service
@Component("databaseTableManipulationService")
public class DatabaseTableManipulationServiceImpl implements
		DatabaseTableManipulationService {

	@Autowired
	private DbConnectionMapper dbConnectionMapper;

	@Resource(name = MessageConstant.DB_CON_SERVICE)
	private DbConnectionService dbConnectionService;

	@Autowired
	private UploadMapper uploadMapper;

	@Override
	public String loadDatabaseLatable(
			DataTableExportImportDTO dataTableExportImportDTO) {
		DatabaseOperationsUtil databaseOperation = null;
		try {
			DbConnectionsDTO dbDto = dbConnectionService
					.getConnectionDetails(Long
							.parseLong(dataTableExportImportDTO
									.getDbConnectionDto().getConId()));

			DataConConnectionsDO databaseConnectionDo = dbConnectionMapper
					.convertDbConnectionsDTOtoDataConConnectionsDO(dbDto);
			StringBuffer url = new StringBuffer();

			uploadMapper.getUrl(databaseConnectionDo, url);// + "",
			String userName = databaseConnectionDo.getUserName();
			String password = databaseConnectionDo.getPassWord();
			dataTableExportImportDTO.setLoadingDataFailPass("success");

			Connection con =
			 DriverManager.getConnection(url.toString(),userName,password);
			/*Connection con = DriverManager.getConnection(
					"jdbc:oracle:thin:@IN-PNQ-COE07:1521:xe", "ssn_src",
					"ssn_src");
*/
			// System.out.println("\n"+con+"\n\n");
			// Class.forName("oracle.jdbc.driver.OracleDriver");

			InputStreamReader inputStream = new InputStreamReader(
					dataTableExportImportDTO.getDatabaseFile().getInputStream());
			StringBuffer dbList = new StringBuffer();
			BufferedReader bufferReader = new BufferedReader(inputStream);
			String temp = bufferReader.readLine();
			while (temp != null) {
				dbList.append(temp);
				temp = bufferReader.readLine();
			}
			String dbQueriesList[] = dbList.toString().split(";");
			ArrayList<String> alterConstraintsList = new ArrayList<String>();
		
			// update code
			boolean flag = false;
			
			ArrayList<DatabaseLoadingInfoModel> databaseLoadingList = new ArrayList<DatabaseLoadingInfoModel>();
			databaseOperation = getDatabaseClass("OracleDatabase");

			DatabaseLoadingInfoModel dbInfoModel = null;
			
			for (String str : dbQueriesList) {
				
				if(str.startsWith("ALTER TABLE")) {
					alterConstraintsList.add(str);
				}
				if (str.startsWith("CREATE table")) {
					dbInfoModel = new DatabaseLoadingInfoModel();
					dbInfoModel.setTableCreate(str);
					dbInfoModel.setTableName(str.substring(13, str.indexOf("(")));
					databaseOperation.initTableConstraints(dbInfoModel, con);					
					databaseLoadingList.add(dbInfoModel);

				}
				if(dbInfoModel == null
						&& str.startsWith("DataStart<")) {
					dbInfoModel = new DatabaseLoadingInfoModel();
					dbInfoModel.setTableCreate(null);
					dbInfoModel.setTableName(str.substring(10,str.indexOf(">")));
					databaseOperation.initTableConstraints(dbInfoModel, con);					
					databaseLoadingList.add(dbInfoModel);
				}
				if (dbInfoModel != null
						&& str.startsWith("DataStart<"
								+ dbInfoModel.getTableName() + ">")) {
					flag = true;
					continue;
				} else if (dbInfoModel != null
						&& str.startsWith("DataEnd<"
								+ dbInfoModel.getTableName() + ">")) {
					flag = false;
					dbInfoModel = null;
				}

				if (flag) {
					dbInfoModel.getListOfData().add(str);
					//dbInfoModel = null;
				}
			}

			if (dataTableExportImportDTO.getCreateSchema() != null
					&& dataTableExportImportDTO.getCreateSchema()
							.equalsIgnoreCase("createTableSchema")) {
					databaseOperation.deleteAndCreateTable(databaseLoadingList, con,alterConstraintsList);
			} else {
				if (dataTableExportImportDTO.getTableExtractDetails()
						.getTableOperation().equalsIgnoreCase("insertOrUpdate")) {
					databaseOperation.setConnection(con);
					databaseOperation.insertUpdate(databaseLoadingList, con);

				} else if (dataTableExportImportDTO.getTableExtractDetails()
						.getTableOperation()
						.equalsIgnoreCase("truncateAndLoad")) {
					databaseOperation.setConnection(con);
					databaseOperation.truncateInsert(databaseLoadingList, con);
				}
			}
			dataTableExportImportDTO
					.setDatabaseLoadingInfoModel(databaseLoadingList);
			databaseOperation.closeConnection();
			
			/*for(String str : alterConstraintsList) {
				System.out.println(" --- >> "+str);
			}*/
		} catch (Exception e) {
			try {
				//e.printStackTrace();
				databaseOperation.closeConnection();
			} catch (SQLException e1) {
			}
			
		}
		return null;
	}
	
	public static void main(String[] args) {
		String str = "DataStart<abcd>";
		//System.out.println(str.substring());
	}
	private DatabaseOperationsUtil getDatabaseClass(String databaseConnection)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		String className = "com.TDMData.databaseUtils.impl."
				+ databaseConnection;
		try {
			Class cls = Class.forName(className);
			Object object = cls.newInstance();
			return (DatabaseOperationsUtil) object;
		} catch (ClassNotFoundException classNotFoundException) {
			throw classNotFoundException;
		} catch (InstantiationException e) {
			throw e;
		} catch (IllegalAccessException e) {
			throw e;
		}
	}
}
