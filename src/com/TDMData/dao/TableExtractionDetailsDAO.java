package com.TDMData.dao;

import java.util.List;

import javax.persistence.EntityManager;

import com.TDMData.exception.DAOException;
import com.TDMData.model.DO.TableExtractionDetailsDO;

public interface TableExtractionDetailsDAO {

	public boolean save(TableExtractionDetailsDO tableExtractionDo,EntityManager entityManager) throws DAOException;
	public List<TableExtractionDetailsDO> select(String userId, EntityManager entityManager) throws DAOException;
	public TableExtractionDetailsDO selectTableExtractionDetailsById(String extractionId, EntityManager entityManager) throws DAOException;
	public boolean deleteTableExtractionById(String extractionId, EntityManager entityManager) throws DAOException;
}
