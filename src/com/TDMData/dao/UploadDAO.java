package com.TDMData.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import com.TDMData.exception.DAOException;

public interface UploadDAO {
	public List<String> getAllTables(String url, String username, String password)
			throws DAOException;
	public JdbcTemplate getTemplate(String strUrl, String strUsername, String strPassword)
			throws DAOException;
//	public List<String> getUrl(DataConConnectionsDO databaseConnectionDo, String url) throws DAOException;
}
