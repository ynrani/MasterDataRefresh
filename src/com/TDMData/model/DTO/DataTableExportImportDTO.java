package com.TDMData.model.DTO;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class DataTableExportImportDTO {

	private DbConnectionsDTO dbConnectionDto;
	
	private List<String> dataTables = new ArrayList<String>();
	private List<String> selectedTableList = new ArrayList<String>();
	private List<String> unSelectedTableList = new ArrayList<String>();
	private String fetchConditionTable;
	private String exportType;
	private List<DbConnectionsDTO> dbConnectionsDtoList = new ArrayList<DbConnectionsDTO>();
	private MultipartFile databaseFile;
	private String loadingDataFailPass;	
	private TableExtractionDetailsDTO tableExtractDetails = new TableExtractionDetailsDTO();	
	private List<TableExtractionDetailsDTO> tableExtractionDetailsDTOList = new ArrayList<TableExtractionDetailsDTO>();	
	//private List<TableDataLoadDTO> tableDataLoadList = new ArrayList<TableDataLoadDTO>();
	private String id;
	private List<DatabaseLoadingInfoModel> databaseLoadingInfoModel = new ArrayList<DatabaseLoadingInfoModel>();
	
	private String createSchema;
	
	
	
	public String getCreateSchema() {
		return createSchema;
	}

	public void setCreateSchema(String createSchema) {
		this.createSchema = createSchema;
	}

	public List<DatabaseLoadingInfoModel> getDatabaseLoadingInfoModel() {
		return databaseLoadingInfoModel;
	}

	public void setDatabaseLoadingInfoModel(
			List<DatabaseLoadingInfoModel> databaseLoadingInfoModel) {
		this.databaseLoadingInfoModel = databaseLoadingInfoModel;
	}

	public List<String> getUnSelectedTableList() {
		return unSelectedTableList;
	}

	public void setUnSelectedTableList(List<String> unSelectedTableList) {
		this.unSelectedTableList = unSelectedTableList;
	}

	public List<String> getSelectedTableList() {
		return selectedTableList;
	}

	public void setSelectedTableList(List<String> selectedTableList) {
		this.selectedTableList = selectedTableList;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<TableExtractionDetailsDTO> getTableExtractionDetailsDTOList() {
		return tableExtractionDetailsDTOList;
	}

	public void setTableExtractionDetailsDTOList(
			List<TableExtractionDetailsDTO> tableExtractionDetailsDTOList) {
		this.tableExtractionDetailsDTOList = tableExtractionDetailsDTOList;
	}

	public TableExtractionDetailsDTO getTableExtractDetails() {
		return tableExtractDetails;
	}

	public void setTableExtractDetails(TableExtractionDetailsDTO tableExtractDetails) {
		this.tableExtractDetails = tableExtractDetails;
	}

	/*public List<TableDataLoadDTO> getTableDataLoadList() {
		return tableDataLoadList;
	}

	public void setTableDataLoadList(List<TableDataLoadDTO> tableDataLoadList) {
		this.tableDataLoadList = tableDataLoadList;
	}
*/
	

	public String getLoadingDataFailPass() {
		return loadingDataFailPass;
	}

	public void setLoadingDataFailPass(String loadingDataFailPass) {
		this.loadingDataFailPass = loadingDataFailPass;
	}

	public MultipartFile getDatabaseFile() {
		return databaseFile;
	}

	public void setDatabaseFile(MultipartFile databaseFile) {
		this.databaseFile = databaseFile;
	}

	public String getExportType() {
		return exportType;
	}

	public void setExportType(String exportType) {
		this.exportType = exportType;
	}

	public String getFetchConditionTable() {
		return fetchConditionTable;
	}

	public void setFetchConditionTable(String fetchConditionTable) {
		this.fetchConditionTable = fetchConditionTable;
	}

	public List<String> getDataTables() {
		return dataTables;
	}

	public void setDataTables(List<String> dataTables) {
		this.dataTables = dataTables;
	}

	public DbConnectionsDTO getDbConnectionDto() {
		return dbConnectionDto;
	}

	public void setDbConnectionDto(DbConnectionsDTO dbConnectionDto) {
		this.dbConnectionDto = dbConnectionDto;
	}

	
	public List<DbConnectionsDTO> getDbConnectionsDtoList() {
		return dbConnectionsDtoList;
	}

	public void setDbConnectionsDtoList(List<DbConnectionsDTO> dbConnectionsDtoList) {
		this.dbConnectionsDtoList = dbConnectionsDtoList;
	}
	
	
}
