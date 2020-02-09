package com.TDMData.model.DTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DatabaseLoadingInfoModel {

	private String tableName;
	private String tableCreate;
	private ArrayList<String> listOfData = new ArrayList<String>();
	private ArrayList<String> listOfForeignKeys = new ArrayList<String>();
	private ArrayList<String> listOfPrimaryKeys = new ArrayList<String>();
	private Map<String,String> listOfExportKeys = new HashMap<String,String>();
	
	private long totalUpdate = 0;
	private long totalInsert = 0;
	private long failToLoad = 0;
	
	
	public long getFailToLoad() {
		return failToLoad;
	}
	public void setFailToLoad(long failToLoad) {
		this.failToLoad = failToLoad;
	}
	public Map<String, String> getListOfExportKeys() {
		return listOfExportKeys;
	}
	public void setListOfExportKeys(Map<String, String> listOfExportKeys) {
		this.listOfExportKeys = listOfExportKeys;
	}
	public long getTotalUpdate() {
		return totalUpdate;
	}
	public void setTotalUpdate(long totalUpdate) {
		this.totalUpdate = totalUpdate;
	}
	public long getTotalInsert() {
		return totalInsert;
	}
	public void setTotalInsert(long totalInsert) {
		this.totalInsert = totalInsert;
	}
	public ArrayList<String> getListOfPrimaryKeys() {
		return listOfPrimaryKeys;
	}
	public void setListOfPrimaryKeys(ArrayList<String> listOfPrimaryKeys) {
		this.listOfPrimaryKeys = listOfPrimaryKeys;
	}
	public ArrayList<String> getListOfForeignKeys() {
		return listOfForeignKeys;
	}
	public void setListOfForeignKeys(ArrayList<String> listOfForeignKeys) {
		this.listOfForeignKeys = listOfForeignKeys;
	}
	public String getTableCreate() {
		return tableCreate;
	}
	public void setTableCreate(String tableCreate) {
		this.tableCreate = tableCreate;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public ArrayList<String> getListOfData() {
		return listOfData;
	}
	public void setListOfData(ArrayList<String> listOfData) {
		this.listOfData = listOfData;
	}
	
	
	
}
