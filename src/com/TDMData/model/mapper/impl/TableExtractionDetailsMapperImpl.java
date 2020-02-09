package com.TDMData.model.mapper.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.TDMData.constant.MessageConstant;
import com.TDMData.model.DO.DataConConnectionsDO;
import com.TDMData.model.DO.TableExtractionDetailsDO;
import com.TDMData.model.DTO.DbConnectionsDTO;
import com.TDMData.model.DTO.TableExtractionDetailsDTO;
import com.TDMData.model.mapper.DbConnectionMapper;
import com.TDMData.model.mapper.TableExtractionDetailsMapper;

/**
 * 
 * @author sushil birajdar
 *
 */
@Component("tableExtractionDetailsMapper")
public class TableExtractionDetailsMapperImpl implements
		TableExtractionDetailsMapper {

	@Autowired
	@Qualifier(MessageConstant.DB_CONN_MAPPER)
	private DbConnectionMapper dbConnectionMapper;
	
	@Override
	public TableExtractionDetailsDTO convertTableExtractionDetailsDOToTableExtractionDetailsDTO(
			TableExtractionDetailsDO tableExtractionDo,
			TableExtractionDetailsDTO tableExtractionDto) {
		
		if(tableExtractionDo != null && tableExtractionDto != null){
			DbConnectionsDTO dbConnectionsDto = new DbConnectionsDTO();
			DataConConnectionsDO dbcon = tableExtractionDo.getDbConnection();
			dbConnectionMapper.convertDataConConnectionsDOtoDbConnectionsDTO(dbcon, dbConnectionsDto);
			
			tableExtractionDto.setDbConnectionDto(dbConnectionsDto);
			tableExtractionDto.setExtractionType(tableExtractionDo.getExtractionType());
			tableExtractionDto.setExtractOnly(tableExtractionDo.getExtractOnly());
			tableExtractionDto.setId(tableExtractionDo.getId());
			tableExtractionDto.setJobName(tableExtractionDo.getJobName());
			tableExtractionDto.setTableList(tableExtractionDo.getTableList());
			tableExtractionDto.setUserId(tableExtractionDo.getUserId());
		}
		
		return tableExtractionDto;
	}

	@Override
	public TableExtractionDetailsDO convertTableExtractionDetailsDTOToTableExtractionDetailsDO(
			TableExtractionDetailsDTO tableExtractionDto,
			TableExtractionDetailsDO tableExtractionDo) {
		if(tableExtractionDto != null && tableExtractionDo != null){
			
			DbConnectionsDTO dbConnectionsDto = tableExtractionDto.getDbConnectionDto();
			
			DataConConnectionsDO dbcon = dbConnectionMapper.convertDbConnectionsDTOtoDataConConnectionsDO(dbConnectionsDto);
			
			tableExtractionDo.setDbConnection(dbcon);
			tableExtractionDo.setExtractionType(tableExtractionDto.getExtractionType());
			tableExtractionDo.setExtractOnly(tableExtractionDto.getExtractOnly());
			tableExtractionDo.setId(tableExtractionDto.getId());
			tableExtractionDo.setJobName(tableExtractionDto.getJobName());
			tableExtractionDo.setTableList(tableExtractionDto.getTableList());
			tableExtractionDo.setUserId(tableExtractionDto.getUserId());
		}
		return tableExtractionDo;
	}

}
