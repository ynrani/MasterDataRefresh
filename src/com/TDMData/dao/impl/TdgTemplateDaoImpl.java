/*
 * Object Name : TdgTemplateDaoImpl.java
 * Modification Block
 * ------------------------------------------------------------------
 * S.No.	Name 			Date			Bug_Fix_No			Desc
 * ------------------------------------------------------------------
 * 	1.	  vkrish14		9:42:26 AM				Created
 * ------------------------------------------------------------------
 * Copyrights: 2015 Capgemini.com
 */
package com.TDMData.dao.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.security.Timestamp;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.stereotype.Component;

import com.TDMData.dao.TdgTemplateDao;
import com.TDMData.exception.ServiceException;
import com.TDMData.model.DO.DataConConnectionsDO;
import com.TDMData.model.mapper.UploadMapper;
import com.TDMData.util.DatabaseSchemaGeneratorUtil;
import com.TDMData.util.TdgCentralConstant;
import com.TDMData.util.TdgCoreConverter;

/**
 * @author vkrish14
 *
 */
@Component("tdgTemplateDao")
public class TdgTemplateDaoImpl implements TdgTemplateDao {
	private static Logger logger = Logger.getLogger(TdgTemplateDaoImpl.class);
	private static String strClassName = " [ TdgTemplateDaoImpl ] ";
	private String dbType = "";
	private String schemaName = "";
	SingleConnectionDataSource dataSource = null;
	private Set<String> setDuplicateCycleCheck = null;
	@Autowired
	UploadMapper uploadMapper;

	/**
	 * This method is used to fetch tables in sequence order to dump the data
	 * into respective tables
	 */
	@Override
	public List<Object> generateSequenceOfTables(JdbcTemplate jdbcTemplate,
			String tableName, boolean isDuplicateCall) {
		List<Object> listObject = new ArrayList<Object>();
		if (!isDuplicateCall) {
			setDuplicateCycleCheck = new HashSet<String>();
		}
		if (!setDuplicateCycleCheck.contains(tableName)) {

			tableName += "#";
			listObject.add(tableName.toUpperCase());
			String temptableName = tableName.substring(0,
					tableName.indexOf("#"));
			Vector<String> vct = new Vector<String>(getVectorVals());
			vct.add(temptableName);
			logger.info(" Going for table " + temptableName);
			List<Map<String, Object>> listValues = jdbcTemplate
					.queryForList(TdgCentralConstant.getSpecificDBQuery(
							getDbType(),
							TdgCentralConstant.GET_SEQUENCE_TABLES, vct));
			for (Map<String, Object> mapValues : listValues) {
				listObject
						.add(generateSequenceOfTables(
								jdbcTemplate,
								((String) mapValues.entrySet().iterator()
										.next().getValue())
										+ tableName.substring(
												tableName.indexOf("#"),
												tableName.length())
												.toUpperCase(), true));
			}
			setDuplicateCycleCheck.add(tableName);
		}
		return listObject;
	}

	/**
	 * 
	 * exporting database tables and return the file path
	 * 
	 */
@Override
	public String exportingDatabaseTable(
			DataConConnectionsDO dataConConnectionsDO, String listOfTables,String exportOnly) {
		String filePath = null;
		char specialSymbol = '"';
		
		String dbType = dataConConnectionsDO.getDbType();

		if(dbType.toLowerCase().contains("oracle")) {
			specialSymbol = '"';
		}
		else if(dbType.toLowerCase().contains("mysql")) {
			specialSymbol = '`';
		}
		if (dbType.toLowerCase().contains("oracle") || dbType.toLowerCase().contains("mysql") || dbType.toLowerCase().contains("SQLServer")) {
			
			StringBuffer url1 = new StringBuffer();
			try {
				String url = uploadMapper.getUrl(dataConConnectionsDO, url1)
						+ "";
				System.out.println("--------------->>>> URL ::: "+url+"  userName::: "+dataConConnectionsDO.getUserName()+"   password  :  "+ dataConConnectionsDO.getPassWord());
				Connection con = DriverManager.getConnection(url,
						dataConConnectionsDO.getUserName(),
						dataConConnectionsDO.getPassWord());

				StringBuffer tableSchema = new StringBuffer();

				tableSchema.append("--------------------------------------------------------\n");
				tableSchema.append("--- File created --- ");
				tableSchema.append(new Date());
				tableSchema
						.append("\n--------------------------------------------------------;");

				String tableArr[] = listOfTables.split(",");
				/*java.sql.DatabaseMetaData metaDataInfo = con.getMetaData();*/
				DatabaseSchemaGeneratorUtil dbUpdateUtil = new DatabaseSchemaGeneratorUtil(con);
				
				if(exportOnly.equalsIgnoreCase("onlySchema")) {
					for (String table : tableArr) {
						Statement state = con.prepareStatement("SELECT * FROM "
								+ table);
						ResultSet rs = state.executeQuery("SELECT * FROM "
								+ table);
						dbUpdateUtil.getCreateTable(rs, table, tableSchema,specialSymbol);
						dbUpdateUtil.updateConstraints(table,specialSymbol);
					}
				}
				
				else if(("onlyData").equalsIgnoreCase(exportOnly)) {
					for (String table : tableArr) {
						Statement state = con.prepareStatement("SELECT * FROM "
								+ table);
						ResultSet rs = state.executeQuery("SELECT * FROM "
								+ table);
						
						dbUpdateUtil.getTableDataInsert(rs, table, tableSchema);
						
					}
				}
				else {
					for (String table : tableArr) {						
						Statement state = con.prepareStatement("SELECT * FROM "
								+ table);
						ResultSet rs = state.executeQuery("SELECT * FROM "
								+ table);
						dbUpdateUtil.getCreateTable(rs, table, tableSchema,specialSymbol);
						dbUpdateUtil.getTableDataInsert(rs, table, tableSchema);
						dbUpdateUtil.updateConstraints(table,specialSymbol);
					}
				}
				tableSchema
						.append("\n--------------------------------------------------------------------\n");
				tableSchema
						.append("-------------------------adding constraints-------------------------\n");
				tableSchema
						.append("--------------------------------------------------------------------;\n");
				
				
				
				dbUpdateUtil.updateSchema(tableSchema);
				java.sql.Timestamp timestamp = new java.sql.Timestamp(System.currentTimeMillis());
				filePath = "d:/dump_"+timestamp.getTime()+".txt";
				File file = new File(filePath);
				BufferedWriter writer = new BufferedWriter(new FileWriter(file));
				writer.write(tableSchema.toString());
				writer.close();
				
				/* System.out.println("" + fkReferences); */
			//	System.out.println("" + tableSchema);
				con.close();
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return filePath;
	}

	// private StringBuffer get

	/**
	 * This method is used to fetch all the tables which are exist in respective
	 * schema of oracle
	 */
	@Override
	public List<String> getAllTables(String url, String username,
			String password) {
		String strMethodName = " [ getAllTables() ] ";
		logger.info(strClassName + strMethodName
				+ " inside getAllTables method");

		List<String> listTableNames = new ArrayList<String>();
		List<String> listURLs = new ArrayList<String>();
		List<String> listUserNames = new ArrayList<String>();
		List<String> listPasswords = new ArrayList<String>();
		// try {
		if (url != null && url.contains("#")) {
			if (url != null && url.contains("#")) {
				listURLs.addAll(Arrays.asList(url.split("#")));
			} else {
				listURLs.add(url);
			}
			if (username != null && username.contains("#")) {
				listUserNames.addAll(Arrays.asList(username.split("#")));
			} else {
				listUserNames.add(username);
			}
			if (password != null && password.contains("#")) {
				listPasswords.addAll(Arrays.asList(password.split("#")));
			} else {
				listPasswords.add(password);
			}
			if (!listURLs.isEmpty() && !listUserNames.isEmpty()
					&& !listPasswords.isEmpty()) {
				for (int i = 0; i < listURLs.size(); i++) {
					String urlTemp = listURLs.get(i);
					String userNameTemp = listUserNames.get(i);
					String passTemp = listPasswords.get(i);
					JdbcTemplate template = getTemplate(urlTemp, userNameTemp,
							passTemp);
					try {
						// JdbcTemplate template = getTemplate(urlTemp,
						// userNameTemp, passTemp);
						List<Map<String, Object>> listString = template
								.queryForList(TdgCentralConstant
										.getSpecificDBQuery(
												getDbType(),
												TdgCentralConstant.GET_ALL_TABLES,
												getVectorVals()));
						for (Map<String, Object> mapValues : listString) {
							listTableNames.add(mapValues.get("TABLE_NAME")
									.toString().toUpperCase());
						}
					} finally {
						cleanupDataSource(template);
					}
				}
			}
		} else {
			JdbcTemplate template = getTemplate(url, username, password);
			try {
				// JdbcTemplate template = getTemplate(urlTemp, userNameTemp,
				// passTemp);
				List<Map<String, Object>> listString = template
						.queryForList(TdgCentralConstant.getSpecificDBQuery(
								getDbType(), TdgCentralConstant.GET_ALL_TABLES,
								getVectorVals()));
				for (Map<String, Object> mapValues : listString) {
					listTableNames.add(mapValues.get("TABLE_NAME").toString()
							.toUpperCase());
				}
			} finally {
				cleanupDataSource(template);
			}
		}

		logger.info(strClassName + strMethodName
				+ " got all tables list from passed schema");
		logger.info(strClassName + strMethodName
				+ " return getAllTables method");
		// } /*finally {
		// cleanupDataSource(template);
		// }*/
		return listTableNames;
	}

	/**
	 * This method is used to fetch all the columns which are exist in
	 * respective schema of oracle
	 */
	@Override
	public List<String> getAllCols(String url, String username, String password) {
		String strMethodName = " [ getAllCols() ] ";
		logger.info(strClassName + strMethodName + " inside getAllCols method");
		List<String> listResult = getColsByTabs(url, username, password, null);
		return listResult;
	}

	@Override
	public List<String> getColsByTabs(String url, String username,
			String password, List<String> lstTabNames) {
		List<String> listResult = new ArrayList<String>();
		List<String> listURLs = new ArrayList<String>();
		List<String> listUserNames = new ArrayList<String>();
		List<String> listPasswords = new ArrayList<String>();
		int iCount = 1;
		StringBuffer strBuffer = new StringBuffer();
		boolean bTabsPassed = false;
		if (lstTabNames != null && !lstTabNames.isEmpty()) {
			bTabsPassed = true;
			for (String string : lstTabNames) {
				strBuffer.append('\'').append(string).append('\'');
				if (iCount != lstTabNames.size()) {
					strBuffer.append(',');
				}
				iCount++;

			}
		}
		if (url != null && url.contains("#")) {
			listURLs.addAll(Arrays.asList(url.split("#")));
		} else {
			listURLs.add(url);
		}
		if (username != null && username.contains("#")) {
			listUserNames.addAll(Arrays.asList(username.split("#")));
		} else {
			listUserNames.add(username);
		}
		if (password != null && password.contains("#")) {
			listPasswords.addAll(Arrays.asList(password.split("#")));
		} else {
			listPasswords.add(password);
		}
		if (!listURLs.isEmpty() && !listUserNames.isEmpty()
				&& !listPasswords.isEmpty()) {
			for (int i = 0; i < listURLs.size(); i++) {
				String urlTemp = listURLs.get(i);
				String userNameTemp = listUserNames.get(i);
				String passTemp = listPasswords.get(i);
				if (bTabsPassed) {
					listResult.addAll(getSpecificAllColsByTabs(urlTemp,
							userNameTemp, passTemp, strBuffer.toString()));
				} else {
					listResult.addAll(getSpecificAllCols(urlTemp, userNameTemp,
							passTemp));
				}
			}
		}
		return listResult;
	}

	private Set<String> getSpecificAllCols(String url, String username,
			String password) {
		String strMethodName = " [ getSpecificAllCols() ] ";
		JdbcTemplate template = getTemplate(url, username, password);
		Set<String> listResult = new HashSet<String>();
		try {
			List<Map<String, Object>> listString = template
					.queryForList(TdgCentralConstant.getSpecificDBQuery(
							getDbType(), TdgCentralConstant.GET_ALL_COLUMNS,
							getVectorVals()));
			for (Map<String, Object> map : listString) {
				listResult.add(String.valueOf(map.get("COLUMN_NAME"))
						.toUpperCase());
			}
			logger.info(strClassName + strMethodName
					+ " got all columns list from passed schema");
			logger.info(strClassName + strMethodName
					+ " return getAllCols method");
		} finally {
			cleanupDataSource(template);
		}
		return listResult;
	}

	private Set<String> getSpecificAllColsByTabs(String url, String username,
			String password, String tabs) {
		String strMethodName = " [ getSpecificAllCols() ] ";
		JdbcTemplate template = getTemplate(url, username, password);
		Set<String> listResult = new HashSet<String>();
		try {
			Vector<String> vct = new Vector<String>();

			if (getDbType().equals(TdgCentralConstant.DB_TYPE_POSTGRES)) {
				for (String str : getVectorVals()) {
					vct.add(str.toLowerCase());
				}
				vct.add(tabs.toLowerCase());
			} else {
				vct.addAll(getVectorVals());
				vct.add(tabs);
			}
			List<Map<String, Object>> listString = template
					.queryForList(TdgCentralConstant.getSpecificDBQuery(
							getDbType(),
							TdgCentralConstant.GET_ALL_COLUMNS_BY_TAB, vct));
			for (Map<String, Object> map : listString) {
				listResult.add(String.valueOf(map.get("COLUMN_NAME"))
						.toUpperCase());
			}
			logger.info(strClassName + strMethodName
					+ " got all columns list from passed schema");
			logger.info(strClassName + strMethodName
					+ " return getAllCols method");
		} finally {
			cleanupDataSource(template);
		}
		return listResult;
	}

	/**
	 * This method is used to fetch all the constraints columns which refers to
	 * Foreign key and unique constraints in oracle
	 */
	@Override
	public List<Map<String, Object>> getAllConstraintCols(String url,
			String username, String password) {
		String strMethodName = " [ getAllConstraintCols() ] ";
		logger.info(strClassName + strMethodName
				+ " inside getAllConstraintCols method");
		JdbcTemplate template = getTemplate(url, username, password);
		List<Map<String, Object>> listString = null;
		try {
			listString = TdgCoreConverter.getConvertedResult(template,
					TdgCentralConstant.getSpecificDBQuery(getDbType(),
							TdgCentralConstant.GET_ALL_CONSTRAINTS_COLUMNS,
							getVectorVals()), getDbType());
			logger.info(strClassName + strMethodName
					+ " return getAllConstraintCols method");
		} finally {
			cleanupDataSource(template);
		}
		return listString;
	}

	/**
	 * This method is used to fetch all the tables are in sequence which are
	 * exist in respective schema of oracle
	 */
	@Override
	public List<Object> getTables(JdbcTemplate jdbcTemplate, String tableName) {
		String strMethodName = " [ getTables() ] ";
		logger.info(strClassName + strMethodName + " inside getTables method");
		List<Object> listObject = new ArrayList<Object>();
		Vector<String> vct = new Vector<String>(getVectorVals());
		vct.add(tableName);
		List<Map<String, Object>> listValues = jdbcTemplate
				.queryForList(TdgCentralConstant.getSpecificDBQuery(
						getDbType(), TdgCentralConstant.GET_TABLES, vct));// generic
																			// solution
		logger.info(strClassName + strMethodName
				+ " got foriegn key constraints of the table : " + tableName);
		for (Map<String, Object> mapValues : listValues) {
			listObject
					.add(generateSequenceOfTables(
							jdbcTemplate,
							((String) mapValues.entrySet().iterator().next()
									.getValue())
									+ tableName.substring(
											tableName.indexOf("#"),
											tableName.length()), false));
		}
		logger.info(strClassName + strMethodName
				+ " return from getTables method");
		return listObject;
	}

	@Override
	public List<Map<String, Object>> getTableNamesByColsName(
			JdbcTemplate jdbcTemplate, List<String> listcolumnsName) {
		String strMethodName = " [ getTableNamesByColsName() ] ";
		logger.info(strClassName + strMethodName
				+ " inside getTableNamesByColsName method");
		List<Map<String, Object>> listString = null;
		boolean bPostgresCheck = false;
		if (TdgCentralConstant.DB_TYPE_POSTGRES.equals(getDbType())) {
			bPostgresCheck = true;
		}
		StringBuffer strBuffer = new StringBuffer(
				TdgCentralConstant.getSpecificDBQuery(getDbType(),
						TdgCentralConstant.GET_TABLES_BY_COLUMNS,
						getVectorVals())).append("( ");
		if (listcolumnsName != null && !listcolumnsName.isEmpty()) {
			int iCount = 0;
			for (String strColsName : listcolumnsName) {
				iCount++;
				/**
				 * checking for multiple column names check
				 */
				if (strColsName.contains("#")) {
					String[] strArray = strColsName.split("#");
					for (int i = 0; i < strArray.length; i++) {
						if (!bPostgresCheck) {
							strBuffer.append(" '")
									.append(strArray[i].toUpperCase())
									.append('\'');
						} else {
							strBuffer.append(" '")
									.append(strArray[i].toLowerCase())
									.append('\'');
						}
						if (i != strArray.length - 1) {
							strBuffer.append(',');
						}
					}
					if (strArray != null && strArray.length > 0) {
						strBuffer.append(',');
					}
				} else {
					if (!bPostgresCheck) {
						strBuffer.append(" '")
								.append(strColsName.toUpperCase()).append('\'');
					} else {
						strBuffer.append(" '")
								.append(strColsName.toLowerCase()).append('\'');
					}
					if (iCount != listcolumnsName.size()) {
						strBuffer.append(',');
					}
				}
				if (iCount == listcolumnsName.size()) {
					strBuffer.append(')');
				}
			}
			listString = TdgCoreConverter.getConvertedResult(jdbcTemplate,
					strBuffer.toString(), getDbType());
		}
		logger.info(strClassName + strMethodName
				+ " return from getTableNamesByColsName method");
		return listString;
	}

	@Override
	public JdbcTemplate getTemplate(String strUrl, String strUsername,
			String strPassword) {
		String strMethodName = " [ getTemplate() ] ";
		logger.info(strClassName + strMethodName + " inside getTemplate method");
		dataSource = new SingleConnectionDataSource(strUrl, strUsername,
				strPassword, true);
		dataSource.setAutoCommit(false);
		DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(
				dataSource);
		// dataSourceTransactionManager.commit(false);
		dataSourceTransactionManager.setRollbackOnCommitFailure(true);
		JdbcTemplate jdbcTemplate = new JdbcTemplate(
				dataSourceTransactionManager.getDataSource());
		if (strUrl != null) {
			if (strUrl.toLowerCase().contains("oracle")) {
				setDbType(TdgCentralConstant.DB_TYPE_ORACLE);
				setSchemaName("");
			} else if (strUrl.toLowerCase().contains("mysql")) {
				setDbType(TdgCentralConstant.DB_TYPE_MYSQL);
				String strArrays[] = strUrl.split("\\/");
				if (strArrays != null && strArrays.length > 0) {
					setSchemaName(strArrays[strArrays.length - 1].toUpperCase());
				} else {
					setSchemaName("TEST");// set for default now
				}
			} else if (strUrl.toLowerCase().contains("db2")) {
				setDbType(TdgCentralConstant.DB_TYPE_DB2);
				if (strUrl.contains("currentSchema=")) {
					String[] strVals = strUrl.split("currentSchema=");
					setSchemaName(strVals[1].substring(0,
							strVals[1].indexOf(";")).toUpperCase());
				} else {
					setSchemaName("");
				}
			} else if (strUrl.toLowerCase().contains("sqlserver")) {
				setDbType(TdgCentralConstant.DB_TYPE_SQLSERVER);
				setSchemaName("");
			} else if (strUrl.toLowerCase().contains("postgres")) {
				setDbType(TdgCentralConstant.DB_TYPE_POSTGRES);
				setSchemaName("");
			}
		}
		logger.info(strClassName + strMethodName
				+ " return from getTemplate method");
		return jdbcTemplate;
	}

	@Override
	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	private Vector<String> getVectorVals() {
		Vector<String> vct = new Vector<String>();
		if (getSchemaName() != null && !"".equals(getSchemaName())) {
			vct.add(getSchemaName());
		}
		return vct;
	}

	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	@Override
	public void cleanupDataSource(JdbcTemplate template) {
		try {
			if (template != null
					&& !template.getDataSource().getConnection().isClosed()) {
				// dataSource.destroy();
				template.getDataSource().getConnection().close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Set<String> getTableNamesFromCols(JdbcTemplate jdbcTemplate,
			Map<String, Object> mapCols) {
		String strMethodName = " [ getTableNamesFromCols() ] ";
		logger.info(strClassName + strMethodName
				+ " inside getTableNamesFromCols method");
		Set<String> stTabs = new HashSet<String>();
		if (mapCols != null && !mapCols.isEmpty()) {
			List<String> listCols = new ArrayList<String>();
			for (String strKey : mapCols.keySet()) {
				listCols.add(strKey);
			}
			List<Map<String, Object>> lstResult = getTableNamesByColsName(
					jdbcTemplate, listCols);
			for (Map<String, Object> map : lstResult) {
				stTabs.add(String.valueOf(map.get("TABLE_NAME")).toUpperCase());
			}
		}
		logger.info(strClassName + strMethodName
				+ " return getTableNamesFromCols method");
		return stTabs;
	}

	@Override
	public Set<String> getTableNamesFromCols(JdbcTemplate jdbcTemplate,
			List<String> listCols) {
		String strMethodName = " [ getTableNamesFromCols() ] ";
		logger.info(strClassName + strMethodName
				+ " inside getTableNamesFromCols method");
		Set<String> stTabs = new HashSet<String>();
		if (listCols != null && !listCols.isEmpty()) {
			List<Map<String, Object>> lstResult = getTableNamesByColsName(
					jdbcTemplate, listCols);
			for (Map<String, Object> map : lstResult) {
				stTabs.add(String.valueOf(map.get("TABLE_NAME")).toUpperCase());
			}
		}
		logger.info(strClassName + strMethodName
				+ " return getTableNamesFromCols method");
		return stTabs;
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public Vector generatePkValues(JdbcTemplate template, String query,
			Map<String, Map<String, String>> mapSequences, long iRecords,
			String strTableName) {
		Vector<Object> vct = new Vector<Object>();
		boolean isNumber = true;
		if (mapSequences == null || !mapSequences.containsKey(strTableName)) {
			List<Map<String, Object>> listPkVal = null;
			String strPrefix = "";
			long lPkValue = 0L;
			long lLength = 1L;
			try {
				/*
				 * listPkVal = getPrimaryKeyValue(template, query); lPkValue =
				 * Long.parseLong(listPkVal.get(0).get("IDGEN") + "");
				 */
				// one going fire with all prefixes set
				listPkVal = getPrimaryKeyValue(template,
						query.replaceAll("#", ""));
				lPkValue = Long.parseLong(listPkVal.get(0).get("IDGEN") + "");
			} catch (Exception se) {
				logger.error(se.getMessage());
				if (listPkVal != null && !listPkVal.isEmpty()) {
					String strPk = listPkVal.get(0).get("IDGEN") + "";
					Pattern pt = Pattern.compile("[A-Za-z]");
					if (pt.matcher(strPk).find()) {
						String str = strPk.replaceAll("[A-Za-z]", "#");
						strPrefix = strPk
								.substring(0, str.lastIndexOf("#") + 1);
						lLength = strPk.substring(str.lastIndexOf("#") + 1,
								str.length()).length();
						if (str.contains("#")) {
							listPkVal = getPrimaryKeyValue(template,
									query.replaceAll("#", "strPrefix"));
							lPkValue = Long.parseLong(listPkVal.get(0).get(
									"IDGEN")
									+ "");
							str = strPk.replaceAll("[A-Za-z]", "#");
							strPrefix = strPk.substring(0,
									str.lastIndexOf("#") + 1);
							lLength = strPk.substring(str.lastIndexOf("#") + 1,
									str.length()).length();
							lPkValue = Long.parseLong("".equalsIgnoreCase(strPk
									.substring(str.lastIndexOf("#") + 1,
											str.length())) ? "0" : strPk
									.substring(str.lastIndexOf("#") + 1,
											str.length())) + 1;
						} else {
							lPkValue = Long.parseLong(strPk) + 1;
						}
					} else {
					}
				}
			}
			// need to check for replace varchar or char types in db
			if (lLength == 1) {
				lPkValue++;
			}
			String strGeneratedValue = strPrefix
					+ String.format("%0" + lLength + "d", lPkValue);
			Pattern pt = Pattern.compile("[A-Za-z]");
			if (pt.matcher(strGeneratedValue).find()
					&& !strGeneratedValue.startsWith("0"))
				isNumber = false;
			if (isNumber) {
				vct.add(Integer.parseInt(strGeneratedValue));
			} else {
				vct.add(strGeneratedValue);
			}
			for (int i = 1; i < iRecords; i++) {
				if (isNumber) {
					vct.add(Integer.parseInt(strPrefix
							+ (String.format("%0" + lLength + "d", ++lPkValue))));
				} else {
					vct.add(strPrefix
							+ (String.format("%0" + lLength + "d", ++lPkValue)));
				}
			}
		} else {
			// need to implement for sequence
			for (Map.Entry<String, Map<String, String>> mapEntry : mapSequences
					.entrySet()) {
				if (strTableName.toUpperCase().equals(
						mapEntry.getKey().toUpperCase())) {
					Vector<String> vctTemp = new Vector<String>(getVectorVals());
					vctTemp.add(mapEntry.getValue().entrySet().iterator()
							.next().getValue());
					String strPrefix = mapEntry.getValue().entrySet()
							.iterator().next().getKey();
					for (int i = 1; i <= iRecords; i++) {
						vct.add(strPrefix
								+ template.queryForLong(TdgCentralConstant
										.getSpecificDBQuery(
												getDbType(),
												TdgCentralConstant.SEQUENCE_VALUE,
												vctTemp)));
					}
					break;
				}
			}
		}
		return vct;
	}

	private List<Map<String, Object>> getPrimaryKeyValue(
			JdbcTemplate jdbcTemplate, String strquery) {
		List<Map<String, Object>> listString = jdbcTemplate
				.queryForList(strquery);
		return listString;
	}

	@Override
	public List<Map<String, Object>> getSequenceColsByTableName(
			JdbcTemplate jdbcTemplate, String strTBName, String strPKColumnName) {
		List<Map<String, Object>> listString = null;
		Vector<String> vct = new Vector<String>(getVectorVals());
		vct.add(strTBName);
		if (strPKColumnName != null) {
			vct.add(strPKColumnName);
			listString = TdgCoreConverter.getConvertedResult(jdbcTemplate,
					TdgCentralConstant.getSpecificDBQuery(getDbType(),
							TdgCentralConstant.GET_SEQUENCE_OF_COLUMNS2, vct),
					getDbType());
		} else {
			listString = TdgCoreConverter.getConvertedResult(jdbcTemplate,
					TdgCentralConstant.getSpecificDBQuery(getDbType(),
							TdgCentralConstant.GET_SEQUENCE_OF_COLUMNS1, vct),
					getDbType());
		}
		return listString;
	}

	@Override
	public Map<String, String> getTableNameByFkName(JdbcTemplate jdbcTemplate,
			String strTBName, Map<String, String> mapFkColumns) {
		Map<String, String> mapValues = new HashMap<String, String>();
		StringBuffer strQuery = null;
		Vector<String> vct = null;
		for (Map.Entry<String, String> val : mapFkColumns.entrySet()) {
			vct = new Vector<String>(getVectorVals());
			vct.add(strTBName);
			vct.add(val.getKey());
			strQuery = new StringBuffer(TdgCentralConstant.getSpecificDBQuery(
					getDbType(), TdgCentralConstant.GET_TABLE_NAME_BY_FK, vct));
			List<Map<String, Object>> listString = jdbcTemplate
					.queryForList(strQuery.toString());
			mapValues.put(val.getKey(),
					String.valueOf(listString.get(0).get("TABLE_NAME"))
							.toUpperCase());
		}
		return mapValues;
	}

	@Override
	public List<Map<String, Object>> getConstraintsOfTable(
			JdbcTemplate jdbcTemplate, String tableName) {
		Vector<String> vct = new Vector<String>(getVectorVals());
		vct.add(tableName);
		List<Map<String, Object>> listValues = TdgCoreConverter
				.getConvertedResult(jdbcTemplate, TdgCentralConstant
						.getSpecificDBQuery(getDbType(),
								TdgCentralConstant.GET_CONSTRAINTS_OF_TABLES,
								vct), getDbType());
		return listValues;
	}

	@Override
	public List<Map<String, Object>> getNotNullConstraintsOfTable(
			JdbcTemplate jdbcTemplate, String tableName) {
		Vector<String> vct = new Vector<String>(getVectorVals());
		vct.add(tableName);
		String strQuery = TdgCentralConstant.getSpecificDBQuery(getDbType(),
				TdgCentralConstant.GET_NOT_NULL_CONSTRAINTS_OF_TABLES, vct);
		List<Map<String, Object>> listValues = new ArrayList<Map<String, Object>>();
		if (strQuery != null && !"".equals(strQuery)) {
			listValues.addAll(TdgCoreConverter.getConvertedResult(jdbcTemplate,
					strQuery, getDbType()));
		}
		return listValues;
	}

	@Override
	public List<Map<String, Object>> getPkColumnType(JdbcTemplate jdbcTemplate,
			String constraint_name, String tableName) {
		Vector<String> vct = new Vector<String>(getVectorVals());
		vct.add(constraint_name);
		vct.add(tableName);
		List<Map<String, Object>> listValues = TdgCoreConverter
				.getConvertedResult(jdbcTemplate, TdgCentralConstant
						.getSpecificDBQuery(getDbType(),
								TdgCentralConstant.GET_PK_COLUMN_TYPE, vct),
						getDbType());
		return listValues;
	}

	@Override
	public List<Map<String, Object>> getOntToOneTableInformation(
			JdbcTemplate jdbcTemplate, String tableName) {
		Vector<String> vct = new Vector<String>(getVectorVals());
		vct.add(tableName);
		List<Map<String, Object>> listValues = jdbcTemplate
				.queryForList(TdgCentralConstant.getSpecificDBQuery(
						getDbType(),
						TdgCentralConstant.ONE_TO_ONE_RELATIONS_FIND_TABLES,
						vct));
		return listValues;
	}

	@Override
	public List<Map<String, Object>> getConstraintRelationTable(
			JdbcTemplate jdbcTemplate, String strConstraintName,
			String strTableName) {
		Vector<String> vct = new Vector<String>(getVectorVals());
		vct.add(strConstraintName);
		vct.add(strTableName);
		List<Map<String, Object>> listValues = jdbcTemplate
				.queryForList(TdgCentralConstant.getSpecificDBQuery(
						getDbType(),
						TdgCentralConstant.CONSTRAINTS_RELATIONS_TABLES, vct));
		return listValues;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> toCheckrelations(List<Object> listObject,
			List<Object> listResult) {
		if (listResult == null) {
			listResult = new ArrayList<Object>();
		}
		for (Object obj : listObject) {
			if (obj instanceof List) {
				toCheckrelations((List<Object>) obj, listResult);
			} else {
				listResult.add(obj);
			}
		}
		return listResult;
	}

	@Override
	public List<List<Object>> toCheckrelations(List<Object> listObject) {
		List<List<Object>> listObjectFinal = new ArrayList<List<Object>>();
		List<Object> listObj = new ArrayList<Object>();
		for (Object obj : listObject) {
			List<Object> listTemp = new ArrayList<Object>(listObj);
			List<Object> duplicateCheck = containsCheck(listObj,
					String.valueOf(obj));
			if (duplicateCheck != null) {
				if (!containsAllCheck(listObjectFinal, listTemp)) {
					listObjectFinal.add(listTemp);
					listObjectFinal = chechSublist(listObjectFinal, listTemp);
				}
				listObj = duplicateCheck;
				List<Object> listTmp = new ArrayList<Object>(listObj);
				listTmp.add(obj);
				listObj.add(obj);
				if (!containsAllCheck(listObjectFinal, listTmp)) {
					listObjectFinal.add(listTmp);
					listObjectFinal = chechSublist(listObjectFinal, listTemp);
				}
			} else {
				listObj.add(obj);
			}
		}
		// if()
		listObjectFinal = chechSublist(listObjectFinal, listObj);
		return listObjectFinal;
	}

	@Override
	public List<List<Object>> chechSublist(List<List<Object>> listObjectFinal,
			List<Object> listTemp) {
		List<List<Object>> listResult = new ArrayList<List<Object>>();
		if (listObjectFinal != null) {
			listResult.addAll(listObjectFinal);
			for (List<Object> listObj : listObjectFinal) {
				if (listTemp.containsAll(listObj)
						&& listTemp.size() != listObj.size()) {
					listResult.remove(listObj);
					if (!containsAllCheck(listResult, listTemp))
						listResult.add(listTemp);
				}
			}
		}
		if (listObjectFinal == null || listObjectFinal.isEmpty()) {
			listResult.add(listTemp);
		}
		return listResult;
	}

	@Override
	public boolean containsAllCheck(List<List<Object>> listObjectFinal,
			List<Object> listTemp) {
		for (List<Object> listObj : listObjectFinal) {
			if (listObj.containsAll(listTemp)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<Object> containsCheck(List<Object> listObj, String strValue) {
		List<Object> listResult = null;
		for (Object obj : listObj) {
			String strValueList = String.valueOf(obj);
			if (strValueList.substring(strValueList.indexOf("#")).length() == strValue
					.substring(strValue.indexOf("#")).length()) {
				listResult = listObj.subList(0, listObj.indexOf(obj));
				return listResult;
			}
		}
		return listResult;
	}

	@Override
	public Set<String> getTableNameInsert(List<Object> listTableNamewithHash,
			int iHashCount) {
		Set<String> setTableNames = new HashSet<String>();
		for (Object objTbName : listTableNamewithHash) {
			String strTbName = String.valueOf(objTbName);
			if (strTbName.substring(strTbName.indexOf("#"), strTbName.length())
					.length() == iHashCount) {
				setTableNames
						.add(strTbName.substring(0, strTbName.indexOf("#")));
			}
		}
		return setTableNames;
	}

	@Override
	public int getHighestHashValue(List<Object> listTableNamewithHash) {
		int i = 0;
		for (Object objTbName : listTableNamewithHash) {
			String strTbName = String.valueOf(objTbName);
			int j = strTbName.substring(strTbName.indexOf("#"),
					strTbName.length()).length();
			if (j > i) {
				i = j;
			}
		}
		return i;
	}

	@Override
	public List<String> getAllTables(JdbcTemplate template) {
		String strMethodName = " [ getTables() ] ";
		List<String> listTableNames = new ArrayList<String>();
		try {
			List<Map<String, Object>> listString = template
					.queryForList(TdgCentralConstant.getSpecificDBQuery(
							getDbType(), TdgCentralConstant.GET_ALL_TABLES,
							getVectorVals()));
			for (Map<String, Object> mapValues : listString) {
				listTableNames.add(mapValues.get("TABLE_NAME").toString()
						.toUpperCase());
			}
			logger.info(strClassName + strMethodName
					+ " got all tables list from passed schema");
			logger.info(strClassName + strMethodName
					+ " return getAllTables method");
		} finally {
			// cleanupDataSource(template);
		}
		return listTableNames;
	}

	@Override
	public String getMaxValue(JdbcTemplate jdbcTemplate, String strDbSeq) {
		String strValue = String.valueOf(jdbcTemplate.queryForObject(strDbSeq,
				Object.class));
		return strValue;
	}

	@Override
	public Map<String, Integer> getTableColumnsInSequence(
			JdbcTemplate jdbcTemplate, String strTableName) {
		Map<String, Integer> mapString = new HashMap<String, Integer>();
		Vector<String> vct = new Vector<String>(getVectorVals());
		vct.add(strTableName);
		// needs to check null
		List<Map<String, Object>> listString = TdgCoreConverter
				.getConvertedResult(
						jdbcTemplate,
						TdgCentralConstant.getSpecificDBQuery(getDbType(),
								TdgCentralConstant.GET_COLS_TABS_SEQUENCE, vct),
						getDbType());
		for (Map<String, Object> mapValues : listString) {
			mapString
					.put(mapValues.get("COLUMN_NAME").toString().toUpperCase(),
							Integer.parseInt(String.valueOf(mapValues
									.get("COLUMN_ID"))));
		}
		return sortByComparator(mapString);
	}

	private static Map<String, Integer> sortByComparator(
			Map<String, Integer> unsortMap) {

		// Convert Map to List
		List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(
				unsortMap.entrySet());

		// Sort list with comparator, to compare the Map values
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1,
					Map.Entry<String, Integer> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		// Convert sorted map back to a Map
		Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
		for (Iterator<Map.Entry<String, Integer>> it = list.iterator(); it
				.hasNext();) {
			Map.Entry<String, Integer> entry = it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}

	@Override
	public List<String> getDateColumns(JdbcTemplate jdbcTemplate,
			String strTableName) {
		List<String> lstString = new ArrayList<String>();
		Vector<String> vct = new Vector<String>(getVectorVals());
		vct.add(strTableName);
		// needs to check null
		List<Map<String, Object>> listString = TdgCoreConverter
				.getConvertedResult(jdbcTemplate, TdgCentralConstant
						.getSpecificDBQuery(getDbType(),
								TdgCentralConstant.GET_DATE_COLUMNS_OF_TABLE,
								vct), getDbType());
		for (Map<String, Object> mapValues : listString) {
			lstString
					.add(mapValues.get("COLUMN_NAME").toString().toUpperCase());
		}
		return lstString;
	}

	@Override
	public List<String> fetchAllManualDictionaries(EntityManager managerentity) {
		final List<String> lstString = new ArrayList<String>();
		Session hibernateSession = managerentity.unwrap(Session.class);

		hibernateSession.doWork(new org.hibernate.jdbc.Work() {

			@Override
			public void execute(Connection connection) throws SQLException {
				// do whatever you need to do with the connection
				ResultSet rs = connection
						.createStatement()
						.executeQuery(
								"select TABLE_NAME from USER_ALL_TABLES WHERE TABLE_NAME LIKE 'MANUAL_%'");
				while (rs.next())
					lstString.add(rs.getString("TABLE_NAME"));
			}
		});

		return lstString;
	}

	@Override
	public List<String> getRelationTabs(String strUrl, String strUsername,
			String password, List<String> lstpassedTabs,
			String conditionParrentChield) {

		JdbcTemplate template = null;
		Set<String> finalTabs = new HashSet<String>();
		try {
			template = getTemplate(strUrl, strUsername, password);
			if (conditionParrentChield != null
					&& conditionParrentChield.equalsIgnoreCase("parent")) {
				setDuplicateCycleCheck = new HashSet<String>();
				// below code is going to be fetch parent tables list

				for (String passedTabs : lstpassedTabs) {

					getParentTables(template, passedTabs);
				}

				finalTabs.addAll(setDuplicateCycleCheck);
			} else if (conditionParrentChield != null
					&& conditionParrentChield.equalsIgnoreCase("child")) {
				// below code is going to be fetch child table list
				setDuplicateCycleCheck = new HashSet<String>();

				for (String passedTabs : lstpassedTabs) {

					getChildTables(template, passedTabs);
				}
				finalTabs.addAll(setDuplicateCycleCheck);

			}

			else if (conditionParrentChield != null
					&& conditionParrentChield.equalsIgnoreCase("all")) {

				setDuplicateCycleCheck = new HashSet<String>();
				// below code is going to be fetch parent tables list
				for (String passedTabs : lstpassedTabs) {
					getParentTables(template, passedTabs);
				}
				finalTabs.addAll(setDuplicateCycleCheck);
				setDuplicateCycleCheck = new HashSet<String>();
				for (String passedTabs : finalTabs) {
					getChildTables(template, passedTabs);
				}
				finalTabs.addAll(setDuplicateCycleCheck);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (template != null) {
				cleanupDataSource(template);
			}
		}
		finalTabs.addAll(setDuplicateCycleCheck);
		return new ArrayList<String>(finalTabs);
	}

	// by fetching relational tables
	@Override
	public List<String> getAllRelationTabs(String strUrl, String strUsername,
			String password, List<String> lstpassedTabs) {
		JdbcTemplate template = null;
		Set<String> finalTabs = new HashSet<String>();
		try {

			setDuplicateCycleCheck = new HashSet<String>();
			template = getTemplate(strUrl, strUsername, password);

			// below code is going to be fetch parent tables list
			for (String passedTabs : lstpassedTabs) {
				getParentTables(template, passedTabs);
			}
			finalTabs.addAll(setDuplicateCycleCheck);
			// below code is going to be fetch child table list
			setDuplicateCycleCheck = new HashSet<String>();
			for (String passedTabs : finalTabs) {
				getChildTables(template, passedTabs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (template != null) {
				cleanupDataSource(template);
			}
		}
		finalTabs.addAll(setDuplicateCycleCheck);
		return new ArrayList<String>(finalTabs);
	}

	private void getParentTables(JdbcTemplate jdbcTemplate, String tableName) {
		// List<Object> listObject = new ArrayList<Object>();
		if (!setDuplicateCycleCheck.contains(tableName)) {

			// tableName += "#";
			// listObject.add(tableName);
			String temptableName = tableName;// .substring(0,
												// tableName.indexOf("#"));
			Vector<String> vct = new Vector<String>(getVectorVals());
			vct.add(temptableName);
			logger.info(" Going for table " + temptableName);
			setDuplicateCycleCheck.add(tableName);
			List<Map<String, Object>> listValues = jdbcTemplate
					.queryForList(TdgCentralConstant.getSpecificDBQuery(
							getDbType(),
							TdgCentralConstant.GET_SEQUENCE_TABLES, vct));
			for (Map<String, Object> mapValues : listValues) {

				getParentTables(jdbcTemplate, ((String) mapValues.entrySet()
						.iterator().next().getValue()));
			}

		}
	}

	private void getChildTables(JdbcTemplate jdbcTemplate, String tableName) {
		// List<Object> listObject = new ArrayList<Object>();
		if (!setDuplicateCycleCheck.contains(tableName)) {

			// tableName += "#";
			// listObject.add(tableName);
			String temptableName = tableName;// .substring(0,
												// tableName.indexOf("#"));
			Vector<String> vct = new Vector<String>(getVectorVals());
			vct.add(temptableName);
			logger.info(" Going for table " + temptableName);
			setDuplicateCycleCheck.add(tableName);

			List<Map<String, Object>> listValues = jdbcTemplate
					.queryForList(TdgCentralConstant.getSpecificDBQuery(
							getDbType(), TdgCentralConstant.GET_CHILD_TABLES,
							vct));
			for (Map<String, Object> mapValues : listValues) {
				getChildTables(jdbcTemplate, ((String) mapValues.entrySet()
						.iterator().next().getValue()));
			}
			// setDuplicateCycleCheck.add(tableName);
		}
	}

	@Override
	public List<String> generateQueries(String url, String userName,
			String password, Set<String> setTables, Set<String> setConditions) {
		List<String> listQueires = new ArrayList<String>();

		Set<String> conditionTables = new HashSet<String>();

		Map<String, String> mapTabWithQuery = new HashMap<String, String>();

		Map<String, List<String>> mapTableConditions = new HashMap<String, List<String>>();
		for (String strTable : setConditions) {
			if (!conditionTables.contains(strTable.substring(0,
					strTable.indexOf(".")).trim())) {
				conditionTables.add(strTable
						.substring(0, strTable.indexOf(".")).trim());
				List<String> listConditions = new ArrayList<String>();
				listConditions.add(strTable);
				mapTableConditions.put(
						strTable.substring(0, strTable.indexOf(".")).trim(),
						listConditions);
			} else {
				List<String> listConditions = mapTableConditions.get(strTable
						.substring(0, strTable.indexOf(".")).trim());
				listConditions.add(strTable);
			}
		}

		// going to generate queries for conditional tables
		JdbcTemplate jdbcTemplate = getTemplate(url, userName, password);
		StringBuffer strBufferSelect = null;
		for (String conTable : conditionTables) {
			Map<String, Integer> mapTabColumnSequences = getTableColumnsInSequence(
					jdbcTemplate, conTable);
			strBufferSelect = new StringBuffer(" SELECT ");
			int iCount = 0;
			for (Map.Entry<String, Integer> mapFinalCols : mapTabColumnSequences
					.entrySet()) {
				strBufferSelect.append(mapFinalCols.getKey());
				if (iCount != mapTabColumnSequences.size() - 1)
					strBufferSelect.append(" , ");
				iCount++;
			}
			iCount = 0;
			strBufferSelect.append(" FROM ").append(conTable).append(" WHERE ");
			if (mapTableConditions.containsKey(conTable)) {
				List<String> conditions = mapTableConditions.get(conTable);
				for (String strCondition : conditions) {
					strBufferSelect.append(strCondition);
					if (iCount != conditions.size() - 1)
						strBufferSelect.append(" AND ");
					iCount++;
				}
			}

			mapTabWithQuery.put(conTable, strBufferSelect.toString());
		}

		// end of creating condition related tables

		// generating final queries
		for (Map.Entry<String, String> mapFinalTabs : mapTabWithQuery
				.entrySet()) {
			listQueires.add(mapFinalTabs.getValue());
		}

		return listQueires;
	}

	private void getParentTableQuery(JdbcTemplate jdbcTemplate,
			String tableName, Map<String, String> mapTabWithQuery) {
		// List<Object> listObject = new ArrayList<Object>();
		// if(!mapTabWithQuery.containsKey(tableName)){

		// tableName += "#";
		// listObject.add(tableName);
		String temptableName = tableName;// .substring(0,
											// tableName.indexOf("#"));
		Vector<String> vct = new Vector<String>(getVectorVals());
		vct.add(temptableName);
		if (!mapTabWithQuery.containsKey(tableName)) {
			Map<String, Integer> mapTabColumnSequences = getTableColumnsInSequence(
					jdbcTemplate, tableName);
			StringBuffer strBufferSelect = new StringBuffer(" SELECT ");
			int iCount = 0;
			for (Map.Entry<String, Integer> mapFinalCols : mapTabColumnSequences
					.entrySet()) {
				strBufferSelect.append(mapFinalCols.getKey());
				if (iCount != mapTabColumnSequences.size() - 1)
					strBufferSelect.append(" , ");
				iCount++;
			}
			iCount = 0;
			strBufferSelect.append(" FROM ").append(tableName)
					.append(" WHERE ");
			/*
			 * if (mapTableConditions.containsKey(conTable)) { List<String>
			 * conditions = mapTableConditions.get(conTable); for (String
			 * strCondition : conditions) {
			 * strBufferSelect.append(strCondition); if (iCount !=
			 * conditions.size() - 1) strBufferSelect.append(" AND "); iCount++;
			 * } }
			 */
			mapTabWithQuery.put(tableName, strBufferSelect.toString());
		}
		logger.info(" Going for table " + temptableName);
		// setDuplicateCycleCheck.add(tableName);
		List<Map<String, Object>> listValues = jdbcTemplate
				.queryForList(TdgCentralConstant.getSpecificDBQuery(
						getDbType(), TdgCentralConstant.GET_SEQUENCE_TABLES,
						vct));
		for (Map<String, Object> mapValues : listValues) {
			getParentTableQuery(jdbcTemplate, ((String) mapValues.entrySet()
					.iterator().next().getValue()), mapTabWithQuery);
		}

		// }
	}

}

/*
 * @Override public Vector<String> getVectorVals(){ Vector<String> vct = new
 * Vector<String>(); if (getSchemaName() != null && !"".equals(getSchemaName()))
 * { vct.add(getSchemaName()); } return vct; }
 */

// }
