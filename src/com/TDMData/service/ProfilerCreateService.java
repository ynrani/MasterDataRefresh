package com.TDMData.service;

import java.util.List;

import com.TDMData.exception.ServiceException;
import com.TDMData.model.DTO.DataTableExportImportDTO;
import com.TDMData.model.DTO.DbConnectionsDTO;/*
import com.TDMData.model.DTO.ProfilerDashboardListDTO;
import com.TDMData.model.DTO.TdmProfilerDTO;*/

public interface ProfilerCreateService{

//	List<String> getAllTabs(String string, String string2, String string3) throws ServiceException;
//	List<String> getColsByTabs(String string, String string2, String string3, List<String> listTabs) throws ServiceException;
//	String saveProfiler(TdmProfilerDTO tdmProfilerDTO, String requestValues) throws ServiceException;
///	String checkProfilerName(String profilerName) throws ServiceException;
	//long getExistingProfilersCount(String username) throws ServiceException;
	//ProfilerDashboardListDTO fetchAllProfiles(long lRequestId, int offSet, int recordsperpage,
		//	boolean b, String username)throws ServiceException;
//	void deleteProfiler(String reqVals)throws ServiceException;
//	TdmProfilerDTO fetchProfileDetails(String reqVals)throws ServiceException;
//	String updateProfiler(TdmProfilerDTO tempTdmProfilerDTO, String replaceAll)throws ServiceException;
//	String fetchAllRelationalTabs(TdmProfilerDTO tempTdmProfilerDTO, String reqVals)throws ServiceException;
//	List<String> generateQueries(String reqVals)throws ServiceException;
	
	String fetchConditionRelationalTabs(DbConnectionsDTO dbDto,String tableName,String conditionParentChild)throws ServiceException;
	String exportDatabaseTables(DataTableExportImportDTO dbConnectionsDto)throws ServiceException;
}
