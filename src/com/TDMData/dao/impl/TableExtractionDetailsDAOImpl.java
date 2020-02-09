package com.TDMData.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.TDMData.constant.MessageConstant;
import com.TDMData.dao.TableExtractionDetailsDAO;
import com.TDMData.exception.DAOException;
import com.TDMData.model.DO.TableExtractionDetailsDO;

@Component("tableExtractionDetailsDao")
public class TableExtractionDetailsDAOImpl implements TableExtractionDetailsDAO {
	private static Logger logger = Logger
			.getLogger(TableExtractionDetailsDAOImpl.class);

	@Override
	public boolean save(TableExtractionDetailsDO tableExtractionDo,
			EntityManager entityManager) throws DAOException {

		boolean flag = false;
		logger.info(MessageConstant.TABLE_EXTRACTION_DAO_IMPL
				+ MessageConstant.TABLE_SAVE_DETAILS
				+ MessageConstant.LOG_INFO_PARAMS_NO);
		/*
		 * System.out.println("*************************");
		 * System.out.println("In Table Extraction Dao :  job name is " +
		 * tableExtractionDo.getJobName()); System.out
		 * .println("In Table Extraction Dao :  Database connection id is " +
		 * tableExtractionDo.getDbConnection() .getDataConConnId());
		 * System.out.println("*************************");
		 */
		long id = 0;
		TableExtractionDetailsDO tempTblExtr = null;
		try {
			if (tableExtractionDo.getId() != null && !tableExtractionDo.getId().equalsIgnoreCase("")) {
				logger.info(MessageConstant.TABLE_EXTRACTION_DAO_IMPL
						+ MessageConstant.TABLE_UPDATE_DETAILS
						+ MessageConstant.LOG_INFO_PARAMS_NO );
				tempTblExtr = (TableExtractionDetailsDO) entityManager
						.createQuery(
								"SELECT p FROM TableExtractionDetailsDO p where p.id="
										+ tableExtractionDo.getId() + "")
						.getSingleResult();

				tempTblExtr
						.setDbConnection(tableExtractionDo.getDbConnection());
				tempTblExtr.setExtractionType(tableExtractionDo
						.getExtractionType());
				tempTblExtr.setExtractOnly(tableExtractionDo.getExtractOnly());
				tempTblExtr.setJobName(tableExtractionDo.getJobName());
				tempTblExtr.setTableList(tableExtractionDo.getTableList());
				tempTblExtr.setUserId(tableExtractionDo.getUserId());
				entityManager.getTransaction().begin();
				entityManager.persist(tempTblExtr);
				entityManager.getTransaction().commit();
				logger.info(MessageConstant.TABLE_EXTRACTION_DAO_IMPL
						+ MessageConstant.TABLE_UPDATE_DETAILS_SUCCESS
						 );

			} else {
				id = Long
						.parseLong((String) entityManager
								.createQuery(
										"SELECT COALESCE(MAX(p.id),0) FROM TableExtractionDetailsDO p")
								.getSingleResult());
				tableExtractionDo.setId((id + 1) + "");
				entityManager.getTransaction().begin();
				entityManager.persist(tableExtractionDo);
				entityManager.getTransaction().commit();
				
			}
			flag = true;
			logger.info(MessageConstant.TABLE_EXTRACTION_DAO_IMPL
					+ MessageConstant.TABLE_SAVE_DETAILS
					+ MessageConstant.TABLE_SAVE_SUCCESS
					+ MessageConstant.LOG_INFO_RETURN);
		} catch (IllegalStateException illegalStateEx) {
			logger.error(MessageConstant.TABLE_EXTRACTION_DAO_IMPL
					+ MessageConstant.TABLE_SAVE_DETAILS
					+ MessageConstant.TABLE_SAVE_FAIL
					+ MessageConstant.LOG_ERROR_EXCEPTION);
			throw new DAOException(
					MessageConstant.NRE_ENTITY_MGR_FACTORY_CLOSED_EXCEPTION,
					illegalStateEx);
		} catch (IllegalArgumentException illegalArgEx) {
			logger.error(MessageConstant.TABLE_EXTRACTION_DAO_IMPL
					+ MessageConstant.TABLE_SAVE_DETAILS
					+ MessageConstant.TABLE_SAVE_FAIL
					+ MessageConstant.LOG_ERROR_EXCEPTION);
			illegalArgEx.printStackTrace();
			throw new DAOException(MessageConstant.INVALID_QUERY_EXCEPTION,
					illegalArgEx);
		} catch (NullPointerException nullPointerException) {
			logger.error(MessageConstant.TABLE_EXTRACTION_DAO_IMPL
					+ MessageConstant.TABLE_SAVE_DETAILS
					+ MessageConstant.TABLE_SAVE_FAIL
					+ MessageConstant.LOG_ERROR_EXCEPTION);
			throw new DAOException(MessageConstant.NULL_POINTER_EXCEPTION,
					nullPointerException);
		} catch (Exception daoException) {
			logger.error(MessageConstant.TABLE_EXTRACTION_DAO_IMPL
					+ MessageConstant.TABLE_SAVE_DETAILS
					+ MessageConstant.TABLE_SAVE_FAIL
					+ MessageConstant.LOG_ERROR_EXCEPTION);

			throw new DAOException(MessageConstant.DATABASE_EXCEPTION,
					daoException);
		}

		return flag;
	}

	@Override
	public List<TableExtractionDetailsDO> select(String userId,
			EntityManager entityManager) throws DAOException {

		logger.info(MessageConstant.TABLE_EXTRACTION_DAO_IMPL
				+ MessageConstant.TABLE_EXTRACTION_DAO_SELECT
				+ MessageConstant.LOG_INFO_PARAMS_NO);
		try {

			String qry = "SELECT p FROM TableExtractionDetailsDO p where userId='"
					+ userId + "'";
			List<TableExtractionDetailsDO> list = entityManager
					.createQuery(qry).getResultList();
			logger.info(MessageConstant.TABLE_EXTRACTION_DAO_IMPL
					+ MessageConstant.TABLE_EXTRACTION_DAO_SELECT_SUCCESS);
			return list;
		} catch (IllegalStateException illegalStateEx) {
			logger.error(MessageConstant.TABLE_EXTRACTION_DAO_IMPL
					+ MessageConstant.TABLE_EXTRACTION_DAO_SELECT_DETAILS
					+ MessageConstant.TABLE_EXTRACTION_DAO_SELECT_FAIL
					+ MessageConstant.LOG_ERROR_EXCEPTION);
			throw new DAOException(
					MessageConstant.NRE_ENTITY_MGR_FACTORY_CLOSED_EXCEPTION,
					illegalStateEx);
		} catch (IllegalArgumentException illegalArgEx) {
			logger.error(MessageConstant.TABLE_EXTRACTION_DAO_IMPL
					+ MessageConstant.TABLE_EXTRACTION_DAO_SELECT_DETAILS
					+ MessageConstant.TABLE_EXTRACTION_DAO_SELECT_FAIL
					+ MessageConstant.LOG_ERROR_EXCEPTION);
			throw new DAOException(MessageConstant.INVALID_QUERY_EXCEPTION,
					illegalArgEx);
		} catch (NullPointerException nullPointerException) {
			logger.error(MessageConstant.TABLE_EXTRACTION_DAO_IMPL
					+ MessageConstant.TABLE_EXTRACTION_DAO_SELECT_DETAILS
					+ MessageConstant.TABLE_EXTRACTION_DAO_SELECT_FAIL
					+ MessageConstant.LOG_ERROR_EXCEPTION);
			throw new DAOException(MessageConstant.NULL_POINTER_EXCEPTION,
					nullPointerException);
		} catch (Exception daoException) {
			logger.error(MessageConstant.TABLE_EXTRACTION_DAO_IMPL
					+ MessageConstant.TABLE_SAVE_DETAILS
					+ MessageConstant.TABLE_SAVE_FAIL
					+ MessageConstant.LOG_ERROR_EXCEPTION);
			throw new DAOException(MessageConstant.DATABASE_EXCEPTION,
					daoException);
		}
	}

	@Override
	public TableExtractionDetailsDO selectTableExtractionDetailsById(
			String extractionId, EntityManager entityManager) throws DAOException {
		logger.info(MessageConstant.TABLE_EXTRACTION_DAO_IMPL
				+ MessageConstant.TABLE_EXTRACTION_DAO_SELECT
				+ MessageConstant.LOG_INFO_PARAMS_NO);
		try {
			
			
			TableExtractionDetailsDO tableDo = (TableExtractionDetailsDO) entityManager.createQuery("SELECT p FROM TableExtractionDetailsDO p where p.id='"+extractionId+"'").getSingleResult();
			logger.info(MessageConstant.TABLE_EXTRACTION_DAO_IMPL
					+ MessageConstant.TABLE_EXTRACTION_DAO_SELECT
					+ MessageConstant.TABLE_EXTRACTION_DAO_SELECT_SUCCESS);
			return tableDo;
		}
		catch(IllegalStateException illegalStateException) {
			logger.info(MessageConstant.TABLE_EXTRACTION_DAO_IMPL + MessageConstant.TABLE_EXTRACTION_DAO_SELECT_DETAILS + MessageConstant.TABLE_EXTRACTION_DAO_SELECT_FAIL + MessageConstant.LOG_ERROR_EXCEPTION);
			throw new DAOException(illegalStateException.toString(),illegalStateException);
		}
		catch (IllegalArgumentException illegalArgEx) {
			logger.error(MessageConstant.TABLE_EXTRACTION_DAO_IMPL
					+ MessageConstant.TABLE_EXTRACTION_DAO_SELECT_DETAILS
					+ MessageConstant.TABLE_EXTRACTION_DAO_SELECT_FAIL
					+ MessageConstant.LOG_ERROR_EXCEPTION);
			throw new DAOException(MessageConstant.INVALID_QUERY_EXCEPTION,
					illegalArgEx);
		} catch (NullPointerException nullPointerException) {
			logger.error(MessageConstant.TABLE_EXTRACTION_DAO_IMPL
					+ MessageConstant.TABLE_EXTRACTION_DAO_SELECT_DETAILS
					+ MessageConstant.TABLE_EXTRACTION_DAO_SELECT_FAIL
					+ MessageConstant.LOG_ERROR_EXCEPTION);
			throw new DAOException(MessageConstant.NULL_POINTER_EXCEPTION,
					nullPointerException);
		} catch (Exception daoException) {
			logger.error(MessageConstant.TABLE_EXTRACTION_DAO_IMPL
					+ MessageConstant.TABLE_SAVE_DETAILS
					+ MessageConstant.TABLE_SAVE_FAIL
					+ MessageConstant.LOG_ERROR_EXCEPTION);
			throw new DAOException(MessageConstant.DATABASE_EXCEPTION,
					daoException);
		}

	}

	@Override
	public boolean deleteTableExtractionById(String extractionId,
			EntityManager entityManager) throws DAOException {
		logger.info(MessageConstant.TABLE_EXTRACTION_DAO_IMPL + MessageConstant.TABLE_EXTRACTION_DAO_DELETING);
		try {
			
			System.out.println("delete success :: "+extractionId);
			entityManager.getTransaction().begin();
			entityManager.createQuery("DELETE FROM TableExtractionDetailsDO p where p.id='"+extractionId+"'").executeUpdate();
			entityManager.getTransaction().commit();
			logger.info(MessageConstant.TABLE_EXTRACTION_DAO_IMPL + MessageConstant.TABLE_EXTRACTION_DAO_DELETING);
			
		}
		catch(NullPointerException nullExp) {
			logger.error(MessageConstant.TABLE_EXTRACTION_DAO_IMPL + MessageConstant.TABLE_EXTRACTION_DAO_DELETE + MessageConstant.TABLE_EXTRACTION_DAO_DELETE_FAIL + MessageConstant.NULL_POINTER_EXCEPTION);
			throw new DAOException(MessageConstant.TABLE_EXTRACTION_DAO_IMPL + MessageConstant.NULL_POINTER_EXCEPTION, nullExp);
		}
		catch (IllegalArgumentException e) {
			logger.error(MessageConstant.TABLE_EXTRACTION_DAO_IMPL + MessageConstant.TABLE_EXTRACTION_DAO_DELETE + MessageConstant.TABLE_EXTRACTION_DAO_DELETE_FAIL + MessageConstant.INVALID_ARGUMENTS);
		}
		catch (IllegalStateException e) {
			logger.error(MessageConstant.TABLE_EXTRACTION_DAO_IMPL + MessageConstant.TABLE_EXTRACTION_DAO_DELETE + MessageConstant.TABLE_EXTRACTION_DAO_DELETE_FAIL + MessageConstant.INVALID_QUERY_EXCEPTION);
		}
		catch (Exception e) {
			logger.error(MessageConstant.TABLE_EXTRACTION_DAO_IMPL + MessageConstant.TABLE_EXTRACTION_DAO_DELETE + MessageConstant.TABLE_EXTRACTION_DAO_DELETE_FAIL + MessageConstant.LOG_ERROR_EXCEPTION);
		}
		return false;
	}
}
