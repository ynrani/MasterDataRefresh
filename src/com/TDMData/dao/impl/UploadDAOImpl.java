package com.TDMData.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.stereotype.Component;

import com.TDMData.constant.AppConstant;
import com.TDMData.constant.MessageConstant;
import com.TDMData.dao.UploadDAO;
import com.TDMData.exception.DAOException;
import com.TDMData.model.DO.DataConConnectionsDO;
//import com.TDMData.util.SepcQueryUtil;
import com.TDMData.util.SepcQueryUtil;

@Component(MessageConstant.UPLOAD_DAO)
public class UploadDAOImpl implements UploadDAO
{
	private static Logger logger = Logger.getLogger(UploadDAOImpl.class);

	private String dbType;
	private String schemaName;
	private SingleConnectionDataSource dataSource = null;

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	

	/**
	 * This method is used to fetch all the tables are in sequence which are exist in respective
	 * schema of oracle
	 */
	
	/**
	 * This method is used to fetch all the tables which are exist in respective schema of oracle
	 */
	@Override
	public List<String> getAllTables(String url, String username, String password)
			throws DAOException {
		logger.info(MessageConstant.UPLOAD_DAO_IMPL + MessageConstant.UPLOAD_GET_ALL_TABLES
				+ MessageConstant.LOG_INFO_PARAMS_NO);
		JdbcTemplate template = getTemplate(url, username, password);
		List<String> listTableNames = new ArrayList<String>();
		try {
			List<Map<String, Object>> listString = template.queryForList(SepcQueryUtil
					.getSpecificDBQuery(getDbType(), AppConstant.GET_ALL_TABLES, getVectorVals()));
			for (Map<String, Object> mapValues : listString) {
				listTableNames.add(mapValues.get("TABLE_NAME").toString().toUpperCase());
			}
			logger.info(MessageConstant.UPLOAD_DAO_IMPL + MessageConstant.UPLOAD_GET_ALL_TABLES
					+ MessageConstant.LOG_INFO_RETURN);
			return listTableNames;
		} catch (IllegalStateException illegalStateEx) {
			logger.error(MessageConstant.UPLOAD_DAO_IMPL + MessageConstant.UPLOAD_GET_ALL_TABLES
					+ MessageConstant.LOG_ERROR_EXCEPTION);
			throw new DAOException(MessageConstant.NRE_ENTITY_MGR_FACTORY_CLOSED_EXCEPTION,
					illegalStateEx);
		} catch (IllegalArgumentException illegalArgEx) {
			logger.error(MessageConstant.UPLOAD_DAO_IMPL + MessageConstant.UPLOAD_GET_ALL_TABLES
					+ MessageConstant.LOG_ERROR_EXCEPTION);
			throw new DAOException(MessageConstant.INVALID_QUERY_EXCEPTION, illegalArgEx);
		} catch (NullPointerException nullPointerEx) {
			logger.error(MessageConstant.UPLOAD_DAO_IMPL + MessageConstant.UPLOAD_GET_ALL_TABLES
					+ MessageConstant.LOG_ERROR_EXCEPTION);
			throw new DAOException(MessageConstant.NULL_POINTER_EXCEPTION, nullPointerEx);
		} catch (Exception otherEx) {
			logger.error(MessageConstant.UPLOAD_DAO_IMPL + MessageConstant.UPLOAD_GET_ALL_TABLES
					+ MessageConstant.LOG_ERROR_EXCEPTION);
			throw new DAOException(MessageConstant.DATABASE_EXCEPTION, otherEx);
		}

	}

	@Override
	public JdbcTemplate getTemplate(String strUrl, String strUsername, String strPassword)
			throws DAOException {
		logger.info(MessageConstant.UPLOAD_DAO_IMPL + MessageConstant.UPLOAD_GET_TEMP
				+ MessageConstant.LOG_INFO_PARAMS_NO);
		try {
			dataSource = new SingleConnectionDataSource(strUrl, strUsername, strPassword, true);
			dataSource.setAutoCommit(false);
			DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(
					dataSource);
			dataSourceTransactionManager.setRollbackOnCommitFailure(true);
			JdbcTemplate jdbcTemplate = new JdbcTemplate(
					dataSourceTransactionManager.getDataSource());
			if (strUrl != null) {
				if (strUrl.toLowerCase().contains("oracle")) {
					setDbType(AppConstant.DB_TYPE_ORACLE);
					setSchemaName("");
				} else if (strUrl.toLowerCase().contains("mysql")) {
					setDbType(AppConstant.DB_TYPE_MYSQL);
					String strArrays[] = strUrl.split("\\/");
					if (strArrays != null && strArrays.length > 0) {
						setSchemaName(strArrays[strArrays.length - 1].toUpperCase());
					} else {
						setSchemaName("TEST");// set for default now
					}
				} else if (strUrl.toLowerCase().contains("db2")) {
					setDbType(AppConstant.DB_TYPE_DB2);
					setSchemaName("");
				} else if (strUrl.toLowerCase().contains("sqlserver")) {
					setDbType(AppConstant.DB_TYPE_SQLSERVER);
					setSchemaName("");
				}
			}
			logger.info(MessageConstant.UPLOAD_DAO_IMPL + MessageConstant.UPLOAD_GET_TEMP
					+ MessageConstant.LOG_INFO_RETURN);
			return jdbcTemplate;
		} catch (IllegalStateException illegalStateEx) {
			logger.error(MessageConstant.UPLOAD_DAO_IMPL + MessageConstant.UPLOAD_GET_TEMP
					+ MessageConstant.LOG_ERROR_EXCEPTION);
			throw new DAOException(MessageConstant.NRE_ENTITY_MGR_FACTORY_CLOSED_EXCEPTION,
					illegalStateEx);
		} catch (IllegalArgumentException illegalArgEx) {
			logger.error(MessageConstant.UPLOAD_DAO_IMPL + MessageConstant.UPLOAD_GET_TEMP
					+ MessageConstant.LOG_ERROR_EXCEPTION);
			throw new DAOException(MessageConstant.INVALID_QUERY_EXCEPTION, illegalArgEx);
		} catch (NullPointerException nullPointerEx) {
			logger.error(MessageConstant.UPLOAD_DAO_IMPL + MessageConstant.UPLOAD_GET_TABLES
					+ MessageConstant.LOG_ERROR_EXCEPTION);
			throw new DAOException(MessageConstant.NULL_POINTER_EXCEPTION, nullPointerEx);
		} catch (Exception otherEx) {
			logger.error(MessageConstant.UPLOAD_DAO_IMPL + MessageConstant.UPLOAD_GET_TEMP
					+ MessageConstant.LOG_ERROR_EXCEPTION);
			throw new DAOException(MessageConstant.DATABASE_EXCEPTION, otherEx);
		}
	}
	private Vector<String> getVectorVals() {
		Vector<String> vct = new Vector<String>();
		if (getSchemaName() != null && !"".equals(getSchemaName())) {
			vct.add(getSchemaName());
		}
		return vct;
	}

}
