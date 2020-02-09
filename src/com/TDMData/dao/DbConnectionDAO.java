package com.TDMData.dao;

import java.util.List;

import javax.persistence.EntityManager;

import com.TDMData.exception.DAOException;
import com.TDMData.model.DO.DataConConnectionsDO;

public interface DbConnectionDAO
{

	public DataConConnectionsDO saveConnection(DataConConnectionsDO dataConConnectionsDO,
			EntityManager managerUser) throws DAOException;

	public List<DataConConnectionsDO> connectionsDashboard(int offSet, int recordsperpage,
			boolean b, String userId, EntityManager managerUser) throws DAOException;

	public DataConConnectionsDO savedConnection(String dataConConnId, EntityManager managerUser)
			throws DAOException;

/*	public Long connectionsDashboardCount(String userId, EntityManager managerUser)
			throws DAOException;*/

	public boolean deleteConnection(String conId, EntityManager managerUser) throws DAOException;

	public List<DataConConnectionsDO> connectionsDetails(String userId, EntityManager managerUser) throws DAOException;

	public String connectionNameCheck(String connectionName, String connectionId, EntityManager managerUser) throws DAOException;

	public DataConConnectionsDO getConnectionDetails(Long connectionId, EntityManager managerUser)
			throws DAOException;

}
