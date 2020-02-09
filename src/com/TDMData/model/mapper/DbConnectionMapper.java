package com.TDMData.model.mapper;

import java.util.List;

import com.TDMData.exception.ServiceException;
import com.TDMData.model.DO.DataConConnectionsDO;
import com.TDMData.model.DTO.DbConnectionsDTO;

public interface DbConnectionMapper
{

	public DataConConnectionsDO convertDbConnectionsDTOtoDataConConnectionsDO(
			DbConnectionsDTO dbConnectionsDTO);

	public DbConnectionsDTO convertDataConConnectionsDOtoDbConnectionsDTO(
			DataConConnectionsDO dataConConnectionsDO, DbConnectionsDTO dbConnectionsDTO);

	public StringBuffer getUrl(DbConnectionsDTO dbConnectionsDTO, StringBuffer url)
			throws ServiceException;

	public List<DbConnectionsDTO> convertDataConConnectionsDOstoDbConnectionsDTOs(
			List<DataConConnectionsDO> dataConConnectionsDO,
			List<DbConnectionsDTO> dbConnectionsDTOs);
}
