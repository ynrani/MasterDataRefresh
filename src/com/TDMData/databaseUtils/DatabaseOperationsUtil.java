package com.TDMData.databaseUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.TDMData.model.DTO.DatabaseLoadingInfoModel;

public interface DatabaseOperationsUtil {

	public void insertUpdate(List<DatabaseLoadingInfoModel> databaseLoadingInfoModelList,Connection con);
	public void truncateInsert(List<DatabaseLoadingInfoModel> databaseLoadingInfoModelList,Connection con);
	public void deleteAndCreateTable(List<DatabaseLoadingInfoModel> databaseLoadingInfoModelList,Connection con,List<String> alterStatemetsList);
	
	public void initTableConstraints(DatabaseLoadingInfoModel databaseTableModel,Connection con);
	
	public void setConnection(Connection con);
	public void closeConnection() throws SQLException;
}
