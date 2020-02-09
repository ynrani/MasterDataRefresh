package com.TDMData.databaseUtils.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Set;

import com.TDMData.model.DTO.DatabaseLoadingInfoModel;

/*
 * subirajd
 */
public class TDMConstraintsHandlerUtil {

	public void setImportedConstraints(DatabaseLoadingInfoModel databaseLoding,
			DatabaseMetaData dbMeta) {
		try {
			ResultSet rs = dbMeta.getImportedKeys(null, null,
					databaseLoding.getTableName());
			while (rs.next()) {
				databaseLoding.getListOfForeignKeys().add(rs.getString(12));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void setPrimaryConstraints(DatabaseLoadingInfoModel databaseLoding,
			DatabaseMetaData dbMeta) {
		try {
			ResultSet rs = dbMeta.getPrimaryKeys(null, null,
					databaseLoding.getTableName());

			while (rs.next()) {
				databaseLoding.getListOfPrimaryKeys().add(rs.getString(6));

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void setExportedConstraints(DatabaseLoadingInfoModel databaseLoding,
			DatabaseMetaData dbMeta) {

		try {
			ResultSet rs = dbMeta.getExportedKeys(null, null,
					databaseLoding.getTableName());
			while (rs.next()) {
				String tableKeyValue = databaseLoding.getListOfExportKeys()
						.get(rs.getString(7));
				if (tableKeyValue != null) {
					tableKeyValue = tableKeyValue + "," + rs.getString(12);
				} else {
					tableKeyValue = rs.getString(12);
				}
				// set the key as a child table name and value is list of
				// foreign key separated by (,) comma
				databaseLoding.getListOfExportKeys().put(rs.getString(7),
						tableKeyValue);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Disable all primary key and related foreign keys for this model or table
	/*public boolean disablePrimaryKeyConstraint(
			DatabaseLoadingInfoModel dbLoadingInfoModel, Connection conn) throws SQLException{

		try {
			List<String> pkList = dbLoadingInfoModel.getListOfPrimaryKeys();
			Statement state = null;

			state = conn.createStatement();
			Set<String> keySet = dbLoadingInfoModel.getListOfExportKeys()
					.keySet();
			
			System.out.println("Table name primary key constraints are "+dbLoadingInfoModel.getTableName());
			// Disable all foreign keys related to this table primary key
			for (String tableName : keySet) {
				
				String exportKeys[] = dbLoadingInfoModel.getListOfExportKeys()
						.get(tableName).split(",");
				for (String val : exportKeys) {
					System.out.println("Table name set values are :::: "+tableName +" keys : "+val);
					state = conn.createStatement();
					state.executeUpdate("ALTER TABLE " + tableName
							+ " disable CONSTRAINT " + val);
					state.close();
				}
			}
			
			// Disable all primary keys
			
			for (String pk : pkList) {
				try {
					state = conn.createStatement();
					
					state.executeUpdate("ALTER TABLE "
							+ dbLoadingInfoModel.getTableName()
							+ " disable CONSTRAINT " + pk);
					
					state.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				// state.addBatch("ALTER TABLE "+dbLoadingInfoModel+" disable CONSTRAINT "+pk);
			}
		//	state.executeBatch();
			state.close();
			return true;
		} catch (SQLException sqlException) {
			throw sqlException;
		}

	}
*/
	public void disableTableConstraints(
			DatabaseLoadingInfoModel dbLoadingInfoModel, Connection conn) {

		for (String constraint : dbLoadingInfoModel.getListOfForeignKeys()) {
			try {
				String disableQuery = "ALTER TABLE "
						+ dbLoadingInfoModel.getTableName()
						+ " DISABLE constraint " + constraint;
				Statement state = conn.prepareStatement(disableQuery);
				state.executeUpdate(disableQuery);
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}

	}

	public void enableTableConstraints(
			DatabaseLoadingInfoModel dbLoadingInfoModel, Connection conn) {

		for (String constraint : dbLoadingInfoModel.getListOfForeignKeys()) {

			String disableQuery = "ALTER TABLE "
					+ dbLoadingInfoModel.getTableName() + " ENABLE constraint "
					+ constraint;
			Statement state;
			try {
				state = conn.prepareStatement(disableQuery);
				state.executeUpdate(disableQuery);
			} catch (SQLException e) {

				e.printStackTrace();
			}

		}

	}

}
