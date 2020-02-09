package com.TDMData.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class DatabaseSchemaGeneratorUtil {

	private StringBuffer alterTablePk = null;

	private StringBuffer fkReferences = null;
	private java.sql.ResultSetMetaData rsMetaData = null;
	private java.sql.DatabaseMetaData metaDataInfo = null;
	private Connection connection = null;

	public DatabaseSchemaGeneratorUtil(Connection con) {
		fkReferences = new StringBuffer();
		alterTablePk = new StringBuffer();
		this.connection = con;

		try {
			metaDataInfo = this.connection.getMetaData();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public StringBuffer getCreateTable(ResultSet rs, String table,
			StringBuffer tableSchema, char specialSymbol) {
		try {
			tableSchema
					.append("\n--------------------------------------------------------");
			tableSchema.append("\n--  DDL for Table " + table);
			tableSchema
					.append("\n--------------------------------------------------------;");
			tableSchema.append("\n\nCREATE table " + table + "(");

			java.sql.ResultSetMetaData rsMetaData = rs.getMetaData();
			/* HashMap<String, String> colsMap = new HashMap<String, String>(); */
			for (int i = 1; i <= rsMetaData.getColumnCount(); i++) {
				/* ----------------------------------------
				 * -------------check nullable-------------
				 * ----------------------------------------*/

				if (rsMetaData.isNullable(i) == 0) {
					alterTablePk.append("\nALTER TABLE " + specialSymbol
							+ table + specialSymbol + " MODIFY ("
							+ specialSymbol + rsMetaData.getColumnName(i)
							+ specialSymbol + " NOT NULL ENABLE);");
				}

				if (rsMetaData.getColumnTypeName(i).equalsIgnoreCase("DATE")
						|| rsMetaData.getColumnTypeName(i).equalsIgnoreCase(
								"TIMESTAMP")
						|| rsMetaData.getColumnTypeName(i).equalsIgnoreCase(
								"CLOB")
						|| rsMetaData.getColumnTypeName(i).equalsIgnoreCase(
								"BLOB")) {

					tableSchema.append("\n " + specialSymbol
							+ rsMetaData.getColumnName(i) + specialSymbol + " "
							+ rsMetaData.getColumnTypeName(i) + ",");

				} else {
					tableSchema.append("\n " + specialSymbol
							+ rsMetaData.getColumnName(i) + specialSymbol + " "
							+ rsMetaData.getColumnTypeName(i) + "("
							+ rsMetaData.getColumnDisplaySize(i) + "),");
				}
			}

			tableSchema.replace(tableSchema.lastIndexOf(","),
					tableSchema.lastIndexOf(",") + 1, "");
			tableSchema.append(");\n\n");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return tableSchema;
	}

	public StringBuffer getTableDataInsert(ResultSet rs, String table,
			StringBuffer tableSchema) {

		String tableRowsDetails = "";
		try {
			rsMetaData = rs.getMetaData();
			Map<String, String> colsMap = new LinkedHashMap<String, String>();
			for (int i = 1; i <= rsMetaData.getColumnCount(); i++) {
				colsMap.put(rsMetaData.getColumnName(i),
						rsMetaData.getColumnTypeName(i));
				tableRowsDetails =tableRowsDetails+ rsMetaData.getColumnName(i)+",";
			}
			/*System.out.println("tableRowsDetails    ---------- >>>>>  "+tableRowsDetails);*/
			tableSchema.append("DataStart<"+table+">;\n");
			tableRowsDetails = tableRowsDetails.substring(0,tableRowsDetails.lastIndexOf(","))+";";
			tableSchema.append(tableRowsDetails+"\n");
			while (rs.next()) {
				


				String values = "";
				Set<String> set = colsMap.keySet();
				for (String col : set) {
					//tableSchema.append(col + ",");

					if (colsMap.get(col).equalsIgnoreCase("DATE")) {
						values = values
								
								+ DateUtil.getOracleInsertDate(rs
										.getString(col))
								+",";
					} else {
						if (rs.getString(col) != null) {
							
							values = values +  rs.getString(col) + ",";
						} else {
							values = values + rs.getString(col) + ",";
						}
					}

				}
				values = values.substring(0, values.lastIndexOf(","))+";";

				//tableSchema.replace(tableSchema.lastIndexOf(","),
				//		tableSchema.lastIndexOf(",") + 1, ";");
				tableSchema.append( values + "\n");
				
				
				/*
				 * IMP Data INSERT QUERY PLEASE DO NOT DELETE 
				 * NOTE:- IMP DND
				 */
				/*

				tableSchema.append("Insert into " + table + " (");
				String values = "(";
				Set<String> set = colsMap.keySet();
				for (String col : set) {
					tableSchema.append(col + ",");

					if (colsMap.get(col).equalsIgnoreCase("DATE")) {
						values = values
								+ "to_date('"
								+ DateUtil.getOracleInsertDate(rs
										.getString(col))
								+ "','dd-mon-yyyy hh24:mi:ss'),";
					} else {
						if (rs.getString(col) != null) {
							values = values + "'" + rs.getString(col) + "',";
						} else {
							values = values + "" + rs.getString(col) + ",";
						}
					}

				}
				values = values.substring(0, values.lastIndexOf(","));

				tableSchema.replace(tableSchema.lastIndexOf(","),
						tableSchema.lastIndexOf(",") + 1, "");
				tableSchema.append(") values" + values + ");\n");*/
			}
			tableSchema.append("DataEnd<"+table+">;\n");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tableSchema;
	}

	private static int i = 1;

	public StringBuffer updateConstraints(String table, char specialSymbol) {
		try {
			alterTablePk
					.append("\n--------------------------------------------------------");
			alterTablePk.append("\n------------------Constraints " + table
					+ "---------------------");
			alterTablePk
					.append("\n--------------------------------------------------------;");

			// java.sql.DatabaseMetaData metaDataInfo = con.getMetaData();
			ResultSet rsPk;
			rsPk = metaDataInfo.getPrimaryKeys(null, null, table);

			Set<String> pkSet = new HashSet<String>();
			while (rsPk.next()) {
				pkSet.add(rsPk.getString(4));
			}
			
			
			// Addiing primary keys

			if (pkSet.size() > 0) {
				
				alterTablePk
						.append("\nALTER TABLE " + specialSymbol + table
								+ specialSymbol + " ADD CONSTRAINT "
								+ specialSymbol + "" + table + "_PK"
								+ specialSymbol + " PRIMARY KEY (");
				String tempPk = "";
				for (String pk : pkSet) {
					tempPk = tempPk + specialSymbol + pk + specialSymbol + ",";
					// tempAlter =
					// tempAlter+"\nALTER TABLE \""+table+"\" MODIFY (\""+pk+"\" NOT NULL ENABLE);";
				}

				System.out.println("Table name :: " + table + " Pk :: " + tempPk);
				alterTablePk
						.append(tempPk.substring(0, tempPk.lastIndexOf(",")));
				alterTablePk.append(");");

			}

			ResultSet exportPk = metaDataInfo
					.getExportedKeys(null, null, table);

			Map<String, String> checkFk = new HashMap<String, String>();

			while (exportPk.next()) {

				if (checkFk.get(exportPk.getString(8)) == null) {
					fkReferences = fkReferences.append("\n\nALTER TABLE "
							+ exportPk.getString(7) + " ADD CONSTRAINT "
							+ specialSymbol + "FK_" + exportPk.getString(7)
							+ "_CD" + i + specialSymbol + " FOREIGN KEY ("
							+ specialSymbol + "" + exportPk.getString(8)
							+ specialSymbol + ")" + " REFERENCES "
							+ specialSymbol + table + specialSymbol + " ("
							+ specialSymbol + exportPk.getString(8)
							+ specialSymbol + ") ENABLE;");
					checkFk.put(exportPk.getString(8), "success");
					i++;
				}

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public StringBuffer updateSchema(StringBuffer tableSchema) {
		tableSchema.append(alterTablePk);
		tableSchema.append(fkReferences);
		return tableSchema;
	}
}
