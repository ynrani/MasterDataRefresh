package com.TDMData.model.DO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="EXTRACT_TABLE")
public class TableExtractionDetailsDO {

	@Id
	@Column(name="ID")
	private String id;	
	
	@Column(name="USER_ID")
	private String userId;
	
	@Column(name="JOBNAME")
	private String jobName;
	
	
//	@Column(name="CONNECTIONID")
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="CONNECTIONID")
	private DataConConnectionsDO  dbConnection;
	
	@Column(name="EXTRACTIONTABLE")
	private String extractionType;
	
	@Column(name="TABLE_LIST")
	
	private String tableList;
	
	@Column(name="EXTRACTONLY")
	private String extractOnly;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	
	
	public DataConConnectionsDO getDbConnection() {
		return dbConnection;
	}
	public void setDbConnection(DataConConnectionsDO dbConnection) {
		this.dbConnection = dbConnection;
	}
	public String getExtractionType() {
		return extractionType;
	}
	public void setExtractionType(String extractionType) {
		this.extractionType = extractionType;
	}
	public String getTableList() {
		return tableList;
	}
	public void setTableList(String tableList) {
		this.tableList = tableList;
	}
	public String getExtractOnly() {
		return extractOnly;
	}
	public void setExtractOnly(String extractOnly) {
		this.extractOnly = extractOnly;
	}
	
	
}
