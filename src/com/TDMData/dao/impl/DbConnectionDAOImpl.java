package com.TDMData.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.TDMData.constant.AppConstant;
import com.TDMData.constant.MessageConstant;
import com.TDMData.dao.DbConnectionDAO;
import com.TDMData.exception.DAOException;
import com.TDMData.model.DO.DataConConnectionsDO;

@Component(MessageConstant.DB_CONN_DAO)
public class DbConnectionDAOImpl implements DbConnectionDAO
{
	private static Logger logger = Logger.getLogger(DbConnectionDAOImpl.class);

	@Override
	public DataConConnectionsDO saveConnection(DataConConnectionsDO dataConConnectionsDO,
			EntityManager managerUser) throws DAOException {
		logger.info(MessageConstant.DB_CON_DAO + MessageConstant.DB_SAVE_CON
				+ MessageConstant.LOG_INFO_PARAMS_NO);
		try {
			if (null != managerUser) {
				if(dataConConnectionsDO.getDataConConnId() == 0){
					long no = (long) managerUser.createQuery(
							"SELECT NVL(MAX(p.dataConConnId),0)  from DataConConnectionsDO p").getSingleResult();
					dataConConnectionsDO.setDataConConnId(no+1);
				}
				managerUser.getTransaction().begin();
				dataConConnectionsDO = managerUser.merge(dataConConnectionsDO);
				managerUser.getTransaction().commit();
			}
			logger.info(MessageConstant.DB_CON_DAO + MessageConstant.DB_SAVE_CON
					+ MessageConstant.LOG_INFO_RETURN);
			return dataConConnectionsDO;
		} catch (IllegalStateException illegalStateEx) {
			logger.error(MessageConstant.DB_CON_DAO + MessageConstant.DB_SAVE_CON
					+ MessageConstant.LOG_ERROR_EXCEPTION);
			throw new DAOException(MessageConstant.NRE_ENTITY_MGR_FACTORY_CLOSED_EXCEPTION,
					illegalStateEx);
		} catch (IllegalArgumentException illegalArgEx) {
			logger.error(MessageConstant.DB_CON_DAO + MessageConstant.DB_SAVE_CON
					+ MessageConstant.LOG_ERROR_EXCEPTION);
			throw new DAOException(MessageConstant.INVALID_QUERY_EXCEPTION, illegalArgEx);
		} catch (NullPointerException nullPointerEx) {
			logger.error(MessageConstant.DB_CON_DAO + MessageConstant.DB_SAVE_CON
					+ MessageConstant.LOG_ERROR_EXCEPTION);
			throw new DAOException(MessageConstant.NULL_POINTER_EXCEPTION, nullPointerEx);
		} catch (Exception otherEx) {
			logger.error(MessageConstant.DB_CON_DAO + MessageConstant.DB_SAVE_CON
					+ MessageConstant.LOG_ERROR_EXCEPTION);
			throw new DAOException(MessageConstant.DATABASE_EXCEPTION, otherEx);
		}
	}

	@Override
	public List<DataConConnectionsDO> connectionsDashboard(int offSet, int recordsperpage,
			boolean b, String userId, EntityManager managerUser) throws DAOException {
		logger.info(MessageConstant.DB_CON_DAO + MessageConstant.DB_CON_DASH
				+ MessageConstant.LOG_INFO_PARAMS_NO);

		try {
			
			String query = "SELECT r FROM DataConConnectionsDO r where r.actionBy='" + userId
					+ "' AND r.active='Y' Order By r.actionDt desc";

			@SuppressWarnings(AppConstant.UNCHECKED)
			List<DataConConnectionsDO> list = managerUser.createQuery(query).setFirstResult(offSet)
					.setMaxResults(recordsperpage).getResultList();

			logger.info(MessageConstant.DB_CON_DAO + MessageConstant.DB_CON_DASH
					+ MessageConstant.LOG_INFO_RETURN);
			return list;
		} catch (IllegalStateException illegalStateEx) {
			logger.error(MessageConstant.DB_CON_DAO + MessageConstant.DB_CON_DASH
					+ MessageConstant.LOG_ERROR_EXCEPTION);
			throw new DAOException(MessageConstant.NRE_ENTITY_MGR_FACTORY_CLOSED_EXCEPTION,
					illegalStateEx);
		} catch (IllegalArgumentException illegalArgEx) {
			logger.error(MessageConstant.DB_CON_DAO + MessageConstant.DB_CON_DASH
					+ MessageConstant.LOG_ERROR_EXCEPTION);
			throw new DAOException(MessageConstant.INVALID_QUERY_EXCEPTION, illegalArgEx);
		} catch (NullPointerException nullPointerEx) {
			logger.error(MessageConstant.DB_CON_DAO + MessageConstant.DB_CON_DASH
					+ MessageConstant.LOG_ERROR_EXCEPTION);
			throw new DAOException(MessageConstant.NULL_POINTER_EXCEPTION, nullPointerEx);
		} catch (Exception otherEx) {
			logger.error(MessageConstant.DB_CON_DAO + MessageConstant.DB_CON_DASH
					+ MessageConstant.LOG_ERROR_EXCEPTION);
			throw new DAOException(MessageConstant.DATABASE_EXCEPTION, otherEx);
		}
	}

	@Override
	public DataConConnectionsDO savedConnection(String dataConConnId, EntityManager managerUser)
			throws DAOException {
		logger.info(MessageConstant.DB_CON_DAO + MessageConstant.DB_SAVED_CON
				+ MessageConstant.LOG_INFO_PARAMS_NO);
		try {

			String query = "SELECT r FROM DataConConnectionsDO r where r.dataConConnId="
					+ Long.parseLong(dataConConnId);

			DataConConnectionsDO dataConConnectionsDO = (DataConConnectionsDO) managerUser
					.createQuery(query).getSingleResult();

			logger.info(MessageConstant.DB_CON_DAO + MessageConstant.DB_SAVED_CON
					+ MessageConstant.LOG_INFO_RETURN);
			return dataConConnectionsDO;
		} catch (IllegalStateException illegalStateEx) {
			logger.error(MessageConstant.DB_CON_DAO + MessageConstant.DB_SAVED_CON
					+ MessageConstant.LOG_ERROR_EXCEPTION);
			throw new DAOException(MessageConstant.NRE_ENTITY_MGR_FACTORY_CLOSED_EXCEPTION,
					illegalStateEx);
		} catch (IllegalArgumentException illegalArgEx) {
			logger.error(MessageConstant.DB_CON_DAO + MessageConstant.DB_SAVED_CON
					+ MessageConstant.LOG_ERROR_EXCEPTION);
			throw new DAOException(MessageConstant.INVALID_QUERY_EXCEPTION, illegalArgEx);
		} catch (NullPointerException nullPointerEx) {
			logger.error(MessageConstant.DB_CON_DAO + MessageConstant.DB_SAVED_CON
					+ MessageConstant.LOG_ERROR_EXCEPTION);
			throw new DAOException(MessageConstant.NULL_POINTER_EXCEPTION, nullPointerEx);
		} catch (Exception otherEx) {
			logger.error(MessageConstant.DB_CON_DAO + MessageConstant.DB_SAVED_CON
					+ MessageConstant.LOG_ERROR_EXCEPTION);
			throw new DAOException(MessageConstant.DATABASE_EXCEPTION, otherEx);
		}
	}

	/*@Override
	public Long connectionsDashboardCount(String userId, EntityManager managerUser)
			throws DAOException {
		try {

			String query = "SELECT COUNT(*) FROM DataConConnectionsDO r where r.actionBy='"
					+ userId + '\'';

			Long cnt = (Long) managerUser.createQuery(query).getSingleResult();

			logger.info(MessageConstant.DB_CON_DAO + MessageConstant.DB_CON_DASH_CNT
					+ MessageConstant.LOG_INFO_RETURN);
			return cnt;
		} catch (IllegalStateException illegalStateEx) {
			logger.error(MessageConstant.DB_CON_DAO + MessageConstant.DB_CON_DASH_CNT
					+ MessageConstant.LOG_ERROR_EXCEPTION);
			throw new DAOException(MessageConstant.NRE_ENTITY_MGR_FACTORY_CLOSED_EXCEPTION,
					illegalStateEx);
		} catch (IllegalArgumentException illegalArgEx) {
			logger.error(MessageConstant.DB_CON_DAO + MessageConstant.DB_CON_DASH_CNT
					+ MessageConstant.LOG_ERROR_EXCEPTION);
			throw new DAOException(MessageConstant.INVALID_QUERY_EXCEPTION, illegalArgEx);
		} catch (NullPointerException nullPointerEx) {
			logger.error(MessageConstant.DB_CON_DAO + MessageConstant.DB_CON_DASH_CNT
					+ MessageConstant.LOG_ERROR_EXCEPTION);
			throw new DAOException(MessageConstant.NULL_POINTER_EXCEPTION, nullPointerEx);
		} catch (Exception otherEx) {
			logger.error(MessageConstant.DB_CON_DAO + MessageConstant.DB_CON_DASH_CNT
					+ MessageConstant.LOG_ERROR_EXCEPTION);
			throw new DAOException(MessageConstant.DATABASE_EXCEPTION, otherEx);
		}
	}
*/
	@Override
	public boolean deleteConnection(String conId, EntityManager managerUser) throws DAOException {
		logger.info(MessageConstant.DB_CON_DAO + MessageConstant.DB_CON_DELETE
				+ MessageConstant.LOG_INFO_PARAMS_NO);
		int count = 0;
		boolean sts = false;
		try {
			if (null != managerUser) {
				managerUser.getTransaction().begin();
				Query q1 = managerUser
						.createQuery("DELETE FROM DataConConnectionsDO r where r.dataConConnId =:dataConConnId");
				q1.setParameter("dataConConnId", Long.parseLong(conId));
				count = q1.executeUpdate();
				if (0 != count) {
					sts = true;
				}
				managerUser.getTransaction().commit();
			}
			logger.info(MessageConstant.DB_CON_DAO + MessageConstant.DB_CON_DELETE
					+ MessageConstant.LOG_INFO_RETURN + count);
			return sts;
		} catch (IllegalStateException illegalStateEx) {
			logger.error(MessageConstant.DB_CON_DAO + MessageConstant.DB_CON_DELETE
					+ MessageConstant.LOG_ERROR_EXCEPTION);
			throw new DAOException(MessageConstant.NRE_ENTITY_MGR_FACTORY_CLOSED_EXCEPTION,
					illegalStateEx);
		} catch (IllegalArgumentException illegalArgEx) {
			logger.error(MessageConstant.DB_CON_DAO + MessageConstant.DB_CON_DELETE
					+ MessageConstant.LOG_ERROR_EXCEPTION);
			throw new DAOException(MessageConstant.INVALID_QUERY_EXCEPTION, illegalArgEx);
		} catch (NullPointerException nullPointerEx) {
			logger.error(MessageConstant.DB_CON_DAO + MessageConstant.DB_CON_DELETE
					+ MessageConstant.LOG_ERROR_EXCEPTION);
			throw new DAOException(MessageConstant.NULL_POINTER_EXCEPTION, nullPointerEx);
		} catch (Exception otherEx) {
			logger.error(MessageConstant.DB_CON_DAO + MessageConstant.DB_CON_DELETE
					+ MessageConstant.LOG_ERROR_EXCEPTION);
			throw new DAOException(MessageConstant.DATABASE_EXCEPTION, otherEx);
		}
	}

	@Override
	public List<DataConConnectionsDO> connectionsDetails(String userId, EntityManager managerUser)
			throws DAOException{
		logger.info(MessageConstant.DB_CON_DAO + MessageConstant.DB_CON_DASH
				+ MessageConstant.LOG_INFO_PARAMS_NO);

		try {
			
			String query = "SELECT r FROM DataConConnectionsDO r where r.actionBy='" + userId
					+ "' AND r.active='Y' Order By r.actionDt desc";

			@SuppressWarnings(AppConstant.UNCHECKED)
			List<DataConConnectionsDO> list = managerUser.createQuery(query).getResultList();

			logger.info(MessageConstant.DB_CON_DAO + MessageConstant.DB_CON_DASH
					+ MessageConstant.LOG_INFO_RETURN);
			return list;
		} catch (IllegalStateException illegalStateEx) {
			logger.error(MessageConstant.DB_CON_DAO + MessageConstant.DB_CON_DASH
					+ MessageConstant.LOG_ERROR_EXCEPTION);
			throw new DAOException(MessageConstant.NRE_ENTITY_MGR_FACTORY_CLOSED_EXCEPTION,
					illegalStateEx);
		} catch (IllegalArgumentException illegalArgEx) {
			logger.error(MessageConstant.DB_CON_DAO + MessageConstant.DB_CON_DASH
					+ MessageConstant.LOG_ERROR_EXCEPTION);
			throw new DAOException(MessageConstant.INVALID_QUERY_EXCEPTION, illegalArgEx);
		} catch (NullPointerException nullPointerEx) {
			logger.error(MessageConstant.DB_CON_DAO + MessageConstant.DB_CON_DASH
					+ MessageConstant.LOG_ERROR_EXCEPTION);
			throw new DAOException(MessageConstant.NULL_POINTER_EXCEPTION, nullPointerEx);
		} catch (Exception otherEx) {
			logger.error(MessageConstant.DB_CON_DAO + MessageConstant.DB_CON_DASH
					+ MessageConstant.LOG_ERROR_EXCEPTION);
			throw new DAOException(MessageConstant.DATABASE_EXCEPTION, otherEx);
		}
	}
	
	
	@Override
	public DataConConnectionsDO getConnectionDetails(Long connectionId, EntityManager managerUser)
			throws DAOException{
		logger.info(MessageConstant.DB_CON_DAO + MessageConstant.DB_CON_DASH
				+ MessageConstant.LOG_INFO_PARAMS_NO);

		try {
			
			DataConConnectionsDO connectionDo  = (DataConConnectionsDO)managerUser.createNamedQuery("DataConConnectionDO.findByDataConConnId",DataConConnectionsDO.class).setParameter("dataConConnId", connectionId). getSingleResult();
			
			//DataConConnectionsDO connectionDo  =(DataConConnectionsDO) managerUser.createQuery("SELECT d FROM DataConConnectionsDO d WHERE d.dataConConnId = "+connectionId).getSingleResult();
			
			logger.info(MessageConstant.DB_CON_DAO + MessageConstant.DB_CON_DASH
					+ MessageConstant.LOG_INFO_RETURN);
			return connectionDo;
		} catch (IllegalStateException illegalStateEx) {
			logger.error(MessageConstant.DB_CON_DAO + MessageConstant.DB_CON_DASH
					+ MessageConstant.LOG_ERROR_EXCEPTION);
			throw new DAOException(MessageConstant.NRE_ENTITY_MGR_FACTORY_CLOSED_EXCEPTION,
					illegalStateEx);
		} catch (IllegalArgumentException illegalArgEx) {
			logger.error(MessageConstant.DB_CON_DAO + MessageConstant.DB_CON_DASH
					+ MessageConstant.LOG_ERROR_EXCEPTION);
			throw new DAOException(MessageConstant.INVALID_QUERY_EXCEPTION, illegalArgEx);
		} catch (NullPointerException nullPointerEx) {
			logger.error(MessageConstant.DB_CON_DAO + MessageConstant.DB_CON_DASH
					+ MessageConstant.LOG_ERROR_EXCEPTION);
			throw new DAOException(MessageConstant.NULL_POINTER_EXCEPTION, nullPointerEx);
		} catch (Exception otherEx) {
			logger.error(MessageConstant.DB_CON_DAO + MessageConstant.DB_CON_DASH
					+ MessageConstant.LOG_ERROR_EXCEPTION);
			throw new DAOException(MessageConstant.DATABASE_EXCEPTION, otherEx);
		}
	}

	@Override
	public String connectionNameCheck(String connectionName, String connectionId, EntityManager managerUser)
			throws DAOException{
		logger.info(MessageConstant.DB_CON_DAO + MessageConstant.DB_CON_DASH
				+ MessageConstant.LOG_INFO_PARAMS_NO);

		try {

			String query = "SELECT r FROM DataConConnectionsDO r where r.connectionName='" + connectionName
					+ "' ";
			if(StringUtils.isNotEmpty(connectionId)){
				query += " AND r.dataConConnId <>'"+connectionId+"'";
			}

			@SuppressWarnings(AppConstant.UNCHECKED)
			List<DataConConnectionsDO> list = managerUser.createQuery(query).getResultList();

			logger.info(MessageConstant.DB_CON_DAO + MessageConstant.DB_CON_DASH
					+ MessageConstant.LOG_INFO_RETURN);
			return list != null && !list.isEmpty() ? "NAME_EXIST" : "";
		} catch (IllegalStateException illegalStateEx) {
			logger.error(MessageConstant.DB_CON_DAO + MessageConstant.DB_CON_DASH
					+ MessageConstant.LOG_ERROR_EXCEPTION);
			throw new DAOException(MessageConstant.NRE_ENTITY_MGR_FACTORY_CLOSED_EXCEPTION,
					illegalStateEx);
		} catch (IllegalArgumentException illegalArgEx) {
			logger.error(MessageConstant.DB_CON_DAO + MessageConstant.DB_CON_DASH
					+ MessageConstant.LOG_ERROR_EXCEPTION);
			throw new DAOException(MessageConstant.INVALID_QUERY_EXCEPTION, illegalArgEx);
		} catch (NullPointerException nullPointerEx) {
			logger.error(MessageConstant.DB_CON_DAO + MessageConstant.DB_CON_DASH
					+ MessageConstant.LOG_ERROR_EXCEPTION);
			throw new DAOException(MessageConstant.NULL_POINTER_EXCEPTION, nullPointerEx);
		} catch (Exception otherEx) {
			logger.error(MessageConstant.DB_CON_DAO + MessageConstant.DB_CON_DASH
					+ MessageConstant.LOG_ERROR_EXCEPTION);
			throw new DAOException(MessageConstant.DATABASE_EXCEPTION, otherEx);
		}
	}
}
