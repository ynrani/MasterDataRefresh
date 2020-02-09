package com.TDMData.service.impl;

import java.util.List;

import javassist.bytecode.analysis.ControlFlow.Catcher;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.TDMData.constant.MessageConstant;
import com.TDMData.dao.TableExtractionDetailsDAO;
import com.TDMData.exception.DAOException;
import com.TDMData.exception.ServiceException;
import com.TDMData.model.DO.TableExtractionDetailsDO;
import com.TDMData.model.DTO.DataTableExportImportDTO;
import com.TDMData.model.DTO.TableExtractionDetailsDTO;
import com.TDMData.model.mapper.TableExtractionDetailsMapper;
import com.TDMData.service.TableExtractionDetailsService;

/**
 * subirajd
 */
@Component("tableExtractionDetailsService")
public class TableExtractionDetailsServiceImpl extends DataConBaseServiceImpl
		implements TableExtractionDetailsService {

	private static final Logger logger = Logger
			.getLogger(TableExtractionDetailsServiceImpl.class);

	@Autowired
	private TableExtractionDetailsMapper tableExtractionDetailsMapper;

	@Autowired
	private TableExtractionDetailsDAO tableExtractionDetailsDao;

	@Override
	public boolean save(TableExtractionDetailsDTO tableExtractDetails)
			throws ServiceException {
		boolean flag = false;
		EntityManager entityManager = openUserEntityManager();

		logger.info(MessageConstant.TABLE_EXTRACTION_SERVICE
				+ MessageConstant.TABLE_EXTRACTION_SERVICE_SAVING
				+ MessageConstant.LOG_INFO_PARAMS_SEPC);
		try {

			flag = tableExtractionDetailsDao
					.save(tableExtractionDetailsMapper
							.convertTableExtractionDetailsDTOToTableExtractionDetailsDO(
									tableExtractDetails,
									new TableExtractionDetailsDO()),
							entityManager);
			closeUserEntityManager(entityManager);
			logger.info(MessageConstant.TABLE_EXTRACTION_SERVICE
					+ MessageConstant.TABLE_EXTRACTION_SERVICE_SUCCESS);
		} catch (NullPointerException e) {
			logger.error(MessageConstant.TABLE_EXTRACTION_SERVICE
					+ MessageConstant.TABLE_EXTRACTION_SERVICE_FAIL
					+ MessageConstant.NULL_POINTER_EXCEPTION);

			closeUserEntityManager(entityManager);
			throw new ServiceException(MessageConstant.NULL_POINTER_EXCEPTION,
					e);
		} catch (DAOException daoEx) {
			logger.error(MessageConstant.TABLE_EXTRACTION_SERVICE
					+ MessageConstant.LOG_ERROR_EXCEPTION);
			closeUserEntityManager(entityManager);
			throw new ServiceException(daoEx.getErrorCode(), daoEx);

		} catch (Exception e) {
			closeUserEntityManager(entityManager);
			logger.error(MessageConstant.TABLE_EXTRACTION_SERVICE
					+ MessageConstant.TABLE_EXTRACTION_SERVICE_FAIL
					);
			throw new ServiceException(MessageConstant.SERVICE_EXCEPTION, e);
		}

		return flag;
	}

	@Override
	public boolean selectAll(DataTableExportImportDTO dataTableExportImportDTO,
			String userId) throws ServiceException {
		logger.info(MessageConstant.TABLE_EXTRACTION_SERVICE
				+ MessageConstant.TABLE_EXTRACTION_SERVICE_SELECT
				+ MessageConstant.TABLE_EXTRACTION_SERVICE_SELECTING_METHOD);
		EntityManager entityManager = openUserEntityManager();
		try {

			List<TableExtractionDetailsDO> tableExtractDoList = tableExtractionDetailsDao
					.select(userId, entityManager);
			for (TableExtractionDetailsDO tableDetails : tableExtractDoList) {
				dataTableExportImportDTO
						.getTableExtractionDetailsDTOList()
						.add(tableExtractionDetailsMapper
								.convertTableExtractionDetailsDOToTableExtractionDetailsDTO(
										tableDetails,
										new TableExtractionDetailsDTO()));
			}
			closeUserEntityManager(entityManager);
			logger.info(MessageConstant.TABLE_EXTRACTION_SERVICE
					+ MessageConstant.TABLE_EXTRACTION_SERVICE_SELECT_SUCCESS);

		} catch (NullPointerException nullExceptione) {
			logger.error(MessageConstant.TABLE_EXTRACTION_SERVICE
					+ MessageConstant.TABLE_EXTRACTION_SERVICE_SELECTING_FAIL
					+ MessageConstant.NULL_POINTER_EXCEPTION);
			closeUserEntityManager(entityManager);
			throw new ServiceException(
					MessageConstant.TABLE_EXTRACTION_SERVICE, nullExceptione);
		} catch (DAOException e) {
			logger.error(MessageConstant.TABLE_EXTRACTION_SERVICE
					+ MessageConstant.TABLE_EXTRACTION_SERVICE_SELECTING_FAIL
					+ e.getMessage());
			closeUserEntityManager(entityManager);
			throw new ServiceException(
					MessageConstant.TABLE_EXTRACTION_SERVICE, e);
			
		}

		return false;
	}

	@Override
	public DataTableExportImportDTO selectTableExtractionDetailById(
			DataTableExportImportDTO dbConnectionsDto) throws ServiceException {
		EntityManager entityManager = openUserEntityManager();
		try {
			logger.info(MessageConstant.TABLE_EXTRACTION_SERVICE + MessageConstant.TABLE_EXTRACTION_SERVICE_SELECT + MessageConstant.TABLE_EXTRACTION_SERVICE_SELECTING_METHOD);
			
			TableExtractionDetailsDO tableDetailsDo = tableExtractionDetailsDao.selectTableExtractionDetailsById(dbConnectionsDto.getId(), entityManager);
			dbConnectionsDto.setTableExtractDetails(tableExtractionDetailsMapper.convertTableExtractionDetailsDOToTableExtractionDetailsDTO(tableDetailsDo, new TableExtractionDetailsDTO()));
			logger.info(MessageConstant.TABLE_EXTRACTION_SERVICE + MessageConstant.TABLE_EXTRACTION_SERVICE_SELECT_SUCCESS);
			closeUserEntityManager(entityManager);
			return dbConnectionsDto;
		}
		catch(NullPointerException nullPointer) {
			
			logger.error(MessageConstant.TABLE_EXTRACTION_SERVICE
					+ MessageConstant.TABLE_EXTRACTION_SERVICE_SELECTING_FAIL
					+ MessageConstant.NULL_POINTER_EXCEPTION);
			closeUserEntityManager(entityManager);
			throw new ServiceException(MessageConstant.NULL_POINTER_EXCEPTION,nullPointer);
			
		}
		catch(DAOException e) {
			logger.error(MessageConstant.TABLE_EXTRACTION_SERVICE
					+ MessageConstant.TABLE_EXTRACTION_SERVICE_SELECTING_FAIL
					+ e.getMessage());
			closeUserEntityManager(entityManager);
			throw new ServiceException(
					MessageConstant.TABLE_EXTRACTION_SERVICE, e);
		}
		catch (Exception e) {
			logger.error(MessageConstant.TABLE_EXTRACTION_SERVICE
					+ MessageConstant.TABLE_EXTRACTION_SERVICE_SELECTING_FAIL
					+ e.toString());
			throw new ServiceException(MessageConstant.SERVICE_EXCEPTION,e);
		}
	}

	@Override
	public boolean delete(TableExtractionDetailsDTO tableExtractDetails)
			throws ServiceException {
		logger.info(MessageConstant.TABLE_EXTRACTION_SERVICE + MessageConstant.TABLE_EXTRACTION_SERVICE_DELETEING);
		EntityManager entityManager = openUserEntityManager();
		try {
			boolean flag = tableExtractionDetailsDao.deleteTableExtractionById(tableExtractDetails.getId(), entityManager);
			logger.info(MessageConstant.TABLE_EXTRACTION_SERVICE + MessageConstant.TABLE_EXTRACTION_SERVICE_DELETE + MessageConstant.TABLE_EXTRACTION_SERVICE_DELETE_SUCCESS);
			closeUserEntityManager(entityManager);
			return flag;
		}
		catch(NullPointerException e) {
			logger.error(MessageConstant.TABLE_EXTRACTION_SERVICE
					+ MessageConstant.TABLE_EXTRACTION_SERVICE_DELETE + MessageConstant.NULL_POINTER_EXCEPTION
					+ e.toString());
			closeUserEntityManager(entityManager);
			throw new ServiceException(MessageConstant.TABLE_EXTRACTION_SERVICE + MessageConstant.TABLE_EXTRACTION_SERVICE_DELETE );
		}
		catch(Exception e) {
			logger.error(MessageConstant.TABLE_EXTRACTION_SERVICE
					+ MessageConstant.TABLE_EXTRACTION_SERVICE_DELETE + MessageConstant.SERVICE_EXCEPTION
					+ e.toString());
			closeUserEntityManager(entityManager);
			throw new ServiceException(MessageConstant.TABLE_EXTRACTION_SERVICE + MessageConstant.TABLE_EXTRACTION_SERVICE_DELETE,e.toString() );
		}
	}
}
