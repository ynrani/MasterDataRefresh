package com.TDMData.databaseUtils.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.TDMData.databaseUtils.DatabaseOperationsUtil;
import com.TDMData.model.DTO.DatabaseLoadingInfoModel;

public class OracleDatabase implements DatabaseOperationsUtil {

	private Connection connection = null;
	private DatabaseMetaData dbMeta = null;

	private TDMConstraintsHandlerUtil tdmConstraint = new TDMConstraintsHandlerUtil();

	@Override
	public void initTableConstraints(
			DatabaseLoadingInfoModel databaseTableModel, Connection con) {
		try {
			this.dbMeta = con.getMetaData();

			tdmConstraint.setImportedConstraints(databaseTableModel, dbMeta);
			tdmConstraint.setPrimaryConstraints(databaseTableModel, dbMeta);
			tdmConstraint.setExportedConstraints(databaseTableModel, dbMeta);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void insertUpdate(List<DatabaseLoadingInfoModel> dataModellist,
			Connection con) {

		this.connection = con;
		TDMConstraintsHandlerUtil tdmConstraint = new TDMConstraintsHandlerUtil();

		java.sql.DatabaseMetaData metaDataInfo = null;
		try {
			metaDataInfo = this.connection.getMetaData();
			this.dbMeta = connection.getMetaData();

			// Setting and disable all Constraint.....
			for (DatabaseLoadingInfoModel model : dataModellist) {
				// tdmConstraint.setImportedConstraints(model, dbMeta);
				tdmConstraint.disableTableConstraints(model, connection);
			}

			for (DatabaseLoadingInfoModel model : dataModellist) {
				/*try{*/
				ResultSet primaryKeys = null;
				String primaryKey = null;
				primaryKeys = metaDataInfo.getPrimaryKeys(null, null,
						model.getTableName());
				while (primaryKeys.next()) {
					primaryKey = primaryKeys.getString(4);
					break;
				}

				String metaData[] = null;

				metaData = model.getListOfData().get(0).split(",");
				long totalInsert = 0;
				long totalUpdate = 0;
				long failToLoad = 0;
				for (int i = 1; i < model.getListOfData().size(); i++) {
					boolean flag = selectRow(metaData, primaryKey,
							model.getTableName(), model.getListOfData().get(i));
					if (flag) {
						try{
						updateRow(metaData, primaryKey, model.getTableName(),
								model.getListOfData().get(i));
						totalUpdate++;
						}
						catch(SQLException e) {
							failToLoad++;
						}
					} else {
						try{
						insertRow(metaData, model.getTableName(), model
								.getListOfData().get(i));
						totalInsert++;
						}
						catch(Exception e) {
							failToLoad++;
							
						}
					}
				}
				model.setTotalInsert(totalInsert);
				model.setTotalUpdate(totalUpdate);
				model.setFailToLoad(failToLoad);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		for (DatabaseLoadingInfoModel model : dataModellist) {
			tdmConstraint.enableTableConstraints(model, connection);
		}
	}

	private boolean insertRow(String metaData[], String tableName,
			String colValues) throws Exception {

		String dataValues[] = colValues.split(",");

		String insertQuery = "INSERT INTO " + tableName + " (";
		for (int i = 0; i < dataValues.length; i++) {
			insertQuery = insertQuery + metaData[i] + ", ";
		}
		insertQuery = insertQuery.substring(0, insertQuery.lastIndexOf(","))
				+ ")";

		String values = " VALUES(";

		for (int i = 0; i < dataValues.length; i++) {
			if (dataValues[i].equalsIgnoreCase("null")) {
				values = values + "" + dataValues[i] + ",";

			} else {
				values = values + "'" + dataValues[i] + "',";
			}
		}
		values = values.substring(0, values.lastIndexOf(",")) + ")";
		insertQuery = insertQuery + values;
		try {
			Statement state = connection.prepareStatement(insertQuery);
			state.executeUpdate(insertQuery);
			state.close();
		} catch (Exception e) {
			throw e;
		}
		return true;
	}

	private boolean selectRow(String metaData[], String primaryKey,
			String tableName, String colValues) throws SQLException {
		String dataValues[] = colValues.split(",");

		String selectQuery = "SELECT * FROM " + tableName + " WHERE "
				+ primaryKey + " = '";
		for (int i = 0; i < dataValues.length; i++) {
			if (metaData[i].equalsIgnoreCase(primaryKey)) {
				selectQuery = selectQuery + "" + dataValues[i] + "'";
				break;
			}
		}
		Statement state = this.connection.prepareStatement(selectQuery);
		ResultSet rs = state.executeQuery(selectQuery);
		return rs.next();
	}

	private int updateRow(String metaData[], String primaryKey,
			String tableName, String colValues) throws SQLException {

		String updateTableSQL = "UPDATE " + tableName + " SET";
		String whereClaus = " where ";
		String dataValues[] = colValues.split(",");

		for (int j = 0; j < metaData.length; j++) {
			if (metaData[j].equalsIgnoreCase(primaryKey)) {
				whereClaus = whereClaus + "" + metaData[j] + " = '"
						+ dataValues[j] + "'";
			} else {
				if (!dataValues[j].equalsIgnoreCase("null")) {
					updateTableSQL = updateTableSQL + " " + metaData[j] + "='"
							+ dataValues[j] + "', ";
				} else {
					updateTableSQL = updateTableSQL + " " + metaData[j] + "="
							+ dataValues[j] + ", ";
				}
			}
		}
		updateTableSQL = updateTableSQL.substring(0,
				updateTableSQL.lastIndexOf(','));
		updateTableSQL = updateTableSQL + whereClaus;
		Statement ps = null;
		ps = this.connection.prepareStatement(updateTableSQL);
		int result = ps.executeUpdate(updateTableSQL);
		ps.close();
		return result;
	}

	@Override
	public void truncateInsert(List<DatabaseLoadingInfoModel> dataModellist,
			Connection con) {
		//java.sql.DatabaseMetaData metaDataInfo = null;

		try {
		///	metaDataInfo = this.connection.getMetaData();
			this.dbMeta = connection.getMetaData();
			TDMConstraintsHandlerUtil tdmConstraint = new TDMConstraintsHandlerUtil();

			for (DatabaseLoadingInfoModel model : dataModellist) {
				// tdmConstraint.setImportedConstraints(model, dbMeta);
				tdmConstraint.disableTableConstraints(model, connection);

			}
			for (DatabaseLoadingInfoModel model : dataModellist) {
				Statement state = con.createStatement();
				state.executeUpdate("TRUNCATE TABLE " + model.getTableName());
				state.close();
			}

			for (DatabaseLoadingInfoModel model : dataModellist) {
				String metaData[] = model.getListOfData().get(0).split(",");
				long totalInsert = 0;
				long failToLoad = 0;
				for (int i = 1; i < model.getListOfData().size(); i++) {
					try {
						insertRow(metaData, model.getTableName(), model
								.getListOfData().get(i));
						totalInsert++;
					} catch (Exception e) {
						failToLoad++;
					}
					
				}
				model.setTotalInsert(totalInsert);
				model.setFailToLoad(failToLoad);
			}
			for (DatabaseLoadingInfoModel model : dataModellist) {
				tdmConstraint.enableTableConstraints(model, connection);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setConnection(Connection con) {
		this.connection = con;
		try {
			this.dbMeta = connection.getMetaData();
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	@Override
	public void closeConnection() throws SQLException {
		this.connection.close();
	}

	@Override
	public void deleteAndCreateTable(
			List<DatabaseLoadingInfoModel> databaseLoadingInfoModelList,
			Connection con, List<String> alterStateList) {
		this.connection = con;
		Statement state = null;
		try {

			// Drop all tables
			state = this.connection.createStatement();
			for (DatabaseLoadingInfoModel dbModel : databaseLoadingInfoModelList) {
				state.addBatch("DROP TABLE " + dbModel.getTableName()
						+ " cascade constraints");
			}
			try{
			state.executeBatch();
			state.close();
			}
			catch(Exception e) {
				System.out.println("Table not found :: "+e.getMessage());
			}
			state = this.connection.createStatement();
			for (DatabaseLoadingInfoModel dbModel : databaseLoadingInfoModelList) {

				// this is to create query to create new table schema
				state.addBatch(dbModel.getTableCreate());
			}
			state.executeBatch();
			state.close();

			// Insert data in created tables
			for (DatabaseLoadingInfoModel dbModel : databaseLoadingInfoModelList) {
				
				String metaData[] = dbModel.getListOfData().get(0).split(",");
				long totalInsert = 0;
				long totalFail = 0;
				
				for (int i = 1; i < dbModel.getListOfData().size(); i++) {
					try{
					insertRow(metaData, dbModel.getTableName(), dbModel
							.getListOfData().get(i));
					totalInsert++;
					}
					catch(Exception e) {
						
						totalFail ++;
						
					}
				}
				dbModel.setTotalInsert(totalInsert);
				dbModel.setFailToLoad(totalFail);
				
			}
			// alter all constraints related to this source table schema
			state = this.connection.createStatement();
			for (String alterList : alterStateList) {
				state.addBatch(alterList);
			}
			state.executeBatch();
			state.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
