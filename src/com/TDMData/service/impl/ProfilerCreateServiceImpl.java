package com.TDMData.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Resource;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.TDMData.constant.MessageConstant;
import com.TDMData.dao.TdgTemplateDao;
import com.TDMData.model.DO.DataConConnectionsDO;
import com.TDMData.model.DTO.DataTableExportImportDTO;
import com.TDMData.model.DTO.DbConnectionsDTO;
import com.TDMData.model.DTO.TableExtractionDetailsDTO;

import com.TDMData.model.mapper.DbConnectionMapper;
import com.TDMData.model.mapper.UploadMapper;
import com.TDMData.service.DbConnectionService;
import com.TDMData.service.ProfilerCreateService;


@Component
@Service("profilerCreateService")
public class ProfilerCreateServiceImpl extends DataConBaseServiceImpl  implements ProfilerCreateService{
	
	@Autowired
	private TdgTemplateDao tdgTemplateDao;
	/*@Autowired
	TdmSubProfileDao tdmSubProfileDao;	
*/
	@Resource(name = MessageConstant.DB_CON_SERVICE)
	DbConnectionService dbConnectionService;
	
	
	/*@Autowired
	TdmSubProfileMapper tdmSubProfileMapper;*/
	
	@Autowired
	DbConnectionMapper dbConnectionMapper;

	@Autowired
	UploadMapper uploadMapper;

	/*@Override
	public List<String> getAllTabs(String url, String username, String password)
			throws ServiceException{
		List<String> lstTables = new ArrayList<String>();
		try {
			lstTables.addAll(tdgTemplateDao.getAllTables(url, username, password));
		} catch(Exception e) {
			e.printStackTrace();
		}
		return lstTables;
	}

	@Override
	public List<String> getColsByTabs(String url, String username, String password,
			List<String> listTabs) throws ServiceException{
		List<String> lstTables = new ArrayList<String>();
		try {
			for(String tablename: listTabs){
				List<String> lstCols = tdgTemplateDao.getColsByTabs(url, username, password, listTabs);
				for(String colunm : lstCols){
			lstTables.add(tablename+"."+colunm);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return lstTables;
	}

	@Override
	public String saveProfiler(TdmProfilerDTO tdmProfilerDTO, String requestValues)
			throws ServiceException{
		String response = "FAILED";
		EntityManager managerUser = openUserEntityManager();
		try {
			
			response = tdmSubProfileDao.saveProfile(tdmSubProfileMapper.convertTdmProfileDTOTOProjectDO(tdmProfilerDTO,requestValues),managerUser);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			closeUserEntityManager(managerUser);
		}
		return response;
	}
*/
/*	@Override
	public String checkProfilerName(String profilerName) throws ServiceException{
		String response = "FAILED";
		EntityManager managerUser = openUserEntityManager();
		try {
			
			response = tdmSubProfileDao.checkProfileName(profilerName,managerUser);
			return response;
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			closeUserEntityManager(managerUser);
		}
		return AppConstant.FAILED;
	}*/

	/*@Override
	public long getExistingProfilersCount(String username) throws ServiceException{
		long lResult =0;
		EntityManager managerUser = null;
		try {
			managerUser = openUserEntityManager();
			lResult = tdmSubProfileDao.getExistingProfilersCount(username,managerUser);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(managerUser != null)
			closeUserEntityManager(managerUser);
		}
		return lResult;
	}*/

	/*@Override
	public ProfilerDashboardListDTO fetchAllProfiles(long lRequestId, int offSet,
			int recordsperpage, boolean b, String username) throws ServiceException{
		//ProfilerDashboardListDTO profilerDashboardListDTO = null;
		EntityManager managerUser = openUserEntityManager();
		ProfilerDashboardListDTO listDto = null;
		try{
			List<ProjectDO> listProjectDO = tdmSubProfileDao.fetchAllProfiles(0, offSet, recordsperpage, b, username, managerUser);
			listDto = tdmSubProfileMapper.convertProjectDOToProfilerDashboardListDTO(listProjectDO);
		}catch(DAOException e){
			e.printStackTrace();
		}finally{
			if(managerUser != null)
			closeUserEntityManager(managerUser);
		}
		return listDto;
	}
*/
	/*@Override
	public void deleteProfiler(String reqVals) throws ServiceException{
		//ProfilerDashboardListDTO profilerDashboardListDTO = null;
		EntityManager managerUser = openUserEntityManager();
		try{
			tdmSubProfileDao.deleteProfiler(reqVals,  managerUser);
			
		}catch(DAOException e){
			e.printStackTrace();
		}finally{
			if(managerUser != null)
			closeUserEntityManager(managerUser);
		}
	}*/
/*
	@Override
	public TdmProfilerDTO fetchProfileDetails(String reqVals) throws ServiceException{
		// TODO Auto-generated method stub
		EntityManager managerUser = openUserEntityManager();
		TdmProfilerDTO tdmProfilerDTO = null;
		try{
			ProjectDO projectDo = tdmSubProfileDao.fetchProfile(reqVals,  managerUser);
			if(projectDo != null)
			tdmProfilerDTO = tdmSubProfileMapper.covertProjectDOToTDMProfilerDTO(projectDo);
		}catch(DAOException e){
			e.printStackTrace();
		}finally{
			if(managerUser != null)
			closeUserEntityManager(managerUser);
		}
		return tdmProfilerDTO;
	}*/

/*	@Override
	public String updateProfiler(TdmProfilerDTO tempTdmProfilerDTO, String replaceAll)
			throws ServiceException{
		String response = "FAILED";
		EntityManager managerUser = openUserEntityManager();
		try {
			
			response = tdmSubProfileDao.editProfile(tdmSubProfileMapper.convertTdmProfileDTOTOProjectDO(tempTdmProfilerDTO,replaceAll),managerUser);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			closeUserEntityManager(managerUser);
		}
		return response;
	}
*/
	//By Sushil Birajdar
	@SuppressWarnings("unchecked")
	@Override
	public String fetchConditionRelationalTabs(DbConnectionsDTO dbDto1,String tableName,String conditionParentChild){
		
		//StringBuffer bufferOutput = new StringBuffer();
		StringBuffer  url = new StringBuffer();
		String urlTemp = "";
		DataConConnectionsDO databaseConnectionDo = dbConnectionMapper.convertDbConnectionsDTOtoDataConConnectionsDO(dbDto1);
		JSONObject jsonTableResponse = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		try {
				urlTemp = uploadMapper.getUrl(databaseConnectionDo, url)+"";
				List<String> lstpassedTabs = new ArrayList<String>();
				if (tableName.contains(",")) {
					String[] splits = tableName.split(",");
					lstpassedTabs.addAll(Arrays.asList(splits));
				} else
					lstpassedTabs.add(tableName);
				
				List<String> lstCols = tdgTemplateDao.getRelationTabs(
						urlTemp, databaseConnectionDo.getUserName(),
						databaseConnectionDo.getPassWord(), lstpassedTabs,conditionParentChild);
				if (lstCols != null && !lstCols.isEmpty()){
					lstCols.removeAll(lstpassedTabs);
					for (int i = 0; i < lstCols.size(); i++) {
						jsonArray.put(lstCols.get(i));
						/*bufferOutput.append(lstCols.get(i));
						if (i != iSeperatorCheck)
							bufferOutput.append(",");*/
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		jsonTableResponse.put("responseData", jsonArray);
			return jsonTableResponse.toString();
	}
	
	@Override
	public String exportDatabaseTables(DataTableExportImportDTO dbConnectionsDto) {
		try {
			TableExtractionDetailsDTO tableDetailsDto = dbConnectionsDto.getTableExtractDetails();
			DbConnectionsDTO dbDto = tableDetailsDto.getDbConnectionDto();
					
			/*System.out.println("Connection name is : "+dbConnectionsDto.getTableExtractDetails().getDbConnectionDto().getConnectionName()+" Connection Id "+dbConnectionsDto.getTableExtractDetails().getDbConnectionDto().getConnectionName());
			System.out.println("Connection name is : "+dbConnectionsDto.getTableExtractDetails().getJobName()+" Job Id "+dbConnectionsDto.getTableExtractDetails().getId());
*/
			//TableExtractionDetailsDTO tableDetails =  dbConnectionsDto.getId();
			//DbConnectionsDTO dbDto = dbConnectionService.getConnectionDetails(Long.parseLong(dbConnectionsDto.getDbConnectionDto().getConId()));
			DataConConnectionsDO dbDo = dbConnectionMapper.convertDbConnectionsDTOtoDataConConnectionsDO(dbDto);
			
			//System.out.println(url);
			String tableNameList = tableDetailsDto.getTableList();
					
			/*for(String table : dbConnectionsDto.getDataTables()) {
				tableNameList = tableNameList +table+",";
			}*/
			tableNameList = tableNameList.substring(0,tableNameList.lastIndexOf(","));
			String parentTableDetails = null;
			//System.out.println(dbConnectionsDto.getFetchConditionTable() + "  : "+(dbConnectionsDto.getFetchConditionTable().equals("Child")));
			if(tableDetailsDto.getExtractionType().equals("Child") ||tableDetailsDto.getExtractionType().equals("All")) {
				parentTableDetails = fetchConditionRelationalTabs(dbDto, tableNameList, "all");
			}
			else if(tableDetailsDto.getExtractionType().equalsIgnoreCase("Parent")){
				parentTableDetails = fetchConditionRelationalTabs(dbDto, tableNameList, "parent");
			}
			
			try {
				if(parentTableDetails != null) {
					org.json.JSONObject jsonObject = new org.json.JSONObject(parentTableDetails);
					JSONArray jsonArray = new JSONArray(jsonObject.get("responseData").toString());
					for(int i = 0; i<jsonArray.length(); i++) {
						tableNameList = tableNameList+","+jsonArray.get(i);
					}
				}
				
				String filePath = tdgTemplateDao.exportingDatabaseTable(dbDo,tableNameList,tableDetailsDto.getExtractOnly());
				return filePath;
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//System.out.println(tableName+"   :   "+parentTableDetails);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	/*
	@Override
	public String fetchAllRelationalTabs(TdmProfilerDTO tempTdmProfilerDTO, String reqVals)
			throws ServiceException{
		List<String> lstTables = new ArrayList<String>();
		StringBuffer bufferOutput = new StringBuffer();
		try {
			List<String> lstpassedTabs = new ArrayList<String>();
			if (reqVals.contains(",")) {
				String[] splits = reqVals.split(",");
				lstpassedTabs.addAll(Arrays.asList(splits));
			} else
				lstpassedTabs.add(reqVals);
			System.out.println("tempTdmProfilerDTO.getStrUrl() "+ tempTdmProfilerDTO.getStrUrl());
			List<String> lstCols = tdgTemplateDao.getAllRelationTabs(
					tempTdmProfilerDTO.getStrUrl(), tempTdmProfilerDTO.getUserName(),
					tempTdmProfilerDTO.getPassword(), lstpassedTabs);
			if (lstCols != null && !lstCols.isEmpty()){
				lstCols.removeAll(lstpassedTabs);
				int iSeperatorCheck = lstCols.size()-1;
				for (int i = 0; i < lstCols.size(); i++) {
					bufferOutput.append(lstCols.get(i));
					if (i != iSeperatorCheck)
						bufferOutput.append(",");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bufferOutput.toString();
	}

	@Override
	public List<String> generateQueries(String reqVals) throws ServiceException{
		// TODO Auto-generated method stub
		EntityManager managerUser = openUserEntityManager();
		List<String> listResult = new ArrayList<String>();
		try{
			ProjectDO projectDo = tdmSubProfileDao.fetchProfile(reqVals,  managerUser);
			DataConConnectionsDO dataCon = projectDo.getSubsetCriterias().get(0).getDataConConnection();
			if(projectDo != null){
				SubsetCriteriaDO criteriaDo = projectDo.getSubsetCriterias().get(0);
				Set<String> setTables = new HashSet<String>();
				Set<String> setConditions = new HashSet<String>();
				if(StringUtils.isNotEmpty(criteriaDo.getSubsetCondTab())){
					if(criteriaDo.getSubsetCondTab().contains(",")){
					setTables.addAll(Arrays.asList(criteriaDo.getSubsetCondTab().split(",")));
					setTables.removeAll(Arrays.asList(null,""));
					}else{
						setTables.add(criteriaDo.getSubsetCondTab());
					}
				}
				if(StringUtils.isNotEmpty(criteriaDo.getSubsetCondCol())){
					String strReplacedVal = criteriaDo.getSubsetCondCol().replaceAll("#", " ");
					if(strReplacedVal.contains("$")){
						setConditions.addAll(Arrays.asList(strReplacedVal.split("\\$")));
						setConditions.removeAll(Arrays.asList(null,""));
					}else{
						setConditions.add(strReplacedVal);
					}
					setConditions.remove("CREATEPROFILER");
				}
				listResult.addAll(tdgTemplateDao.generateQueries(getUrl(dataCon).toString(),dataCon.getUserName(),dataCon.getPassWord(),setTables,setConditions));
			}
		}catch(DAOException e){
			e.printStackTrace();
		}finally{
			if(managerUser != null)
			closeUserEntityManager(managerUser);
		}
		return listResult;
	}*/
}
