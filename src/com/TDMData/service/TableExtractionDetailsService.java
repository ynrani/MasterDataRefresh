package com.TDMData.service;

import com.TDMData.exception.ServiceException;
import com.TDMData.model.DTO.DataTableExportImportDTO;
import com.TDMData.model.DTO.TableExtractionDetailsDTO;

public interface TableExtractionDetailsService {

	public boolean save(TableExtractionDetailsDTO tableExtractDetails) throws ServiceException;
	public boolean selectAll(DataTableExportImportDTO tableExtractDetails,String userId) throws ServiceException;
	public DataTableExportImportDTO selectTableExtractionDetailById(DataTableExportImportDTO dbConnectionsDto)throws ServiceException;
	public boolean delete(TableExtractionDetailsDTO tableExtractDetails) throws ServiceException;
}
