package com.TDMData.model.DTO;

public class TableExtractionDetailsDTO {

	private String id;
	private String userId;
	private String jobName;
	private DbConnectionsDTO dbConnectionDto;
	private String tableOperation;
	private String extractionType;
	private String tableList;
	private String extractOnly;

	
	public String getTableOperation() {
		return tableOperation;
	}



	public void setTableOperation(String tableOperation) {
		this.tableOperation = tableOperation;
	}



	public String getId() {
		return id;
	}

	

	public DbConnectionsDTO getDbConnectionDto() {
		return dbConnectionDto;
	}



	public void setDbConnectionDto(DbConnectionsDTO dbConnectionDto) {
		this.dbConnectionDto = dbConnectionDto;
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

	@Override
	public String toString() {
		return "TableExtractionDetailsDTO [id=" + id + ", userId=" + userId
				+ ", jobName=" + jobName + ", dbConnectionDto="
				+ dbConnectionDto + ", extractionType=" + extractionType
				+ ", tableList=" + tableList + ", extractOnly=" + extractOnly
				+ "]";
	}

}
