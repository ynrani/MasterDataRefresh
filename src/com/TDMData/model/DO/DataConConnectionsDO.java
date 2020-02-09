package com.TDMData.model.DO;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * The persistent class for the DATA_CON_CONNECTIONS database table.
 * 
 */
@Entity
@Table(name="DATA_CON_CONNECTIONS")
@NamedQueries({@NamedQuery(name="DataConConnectionDO.findAll", query="SELECT d FROM DataConConnectionsDO d"),
	@NamedQuery(name="DataConConnectionDO.findByconnectionName", query="SELECT d FROM DataConConnectionsDO d WHERE d.connectionName =:connectionName"),
	@NamedQuery(name="DataConConnectionDO.findByDataConConnId", query="SELECT d FROM DataConConnectionsDO d WHERE d.dataConConnId =:dataConConnId")
})
public class DataConConnectionsDO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="DATA_CON_CONN_ID")
	private long dataConConnId;

	@Column(name="ACTION_BY")
	private String actionBy;

	@Column(name="ACTION_DT")
	private Timestamp actionDt;

	private String active;

	@Column(name="CONNECTION_NAME")
	private String connectionName;

	@Column(name="DB_TYPE")
	private String dbType;

	@Column(name="HOST_NAME")
	private String hostName;

	@Column(name="PASS_WORD")
	private String passWord;

	@Column(name="PORT_NO")
	private Long portNo;

	private String sid;

	@Column(name="USER_NAME")
	private String userName;

	//bi-directional many-to-one association to SubsetCriteriaDO
/*	@OneToMany(mappedBy="dataConConnection")
	private List<SubsetCriteriaDO> subsetCriterias;*/

	public DataConConnectionsDO() {
	}

	public long getDataConConnId() {
		return this.dataConConnId;
	}

	public void setDataConConnId(long dataConConnId) {
		this.dataConConnId = dataConConnId;
	}

	public String getActionBy() {
		return this.actionBy;
	}

	public void setActionBy(String actionBy) {
		this.actionBy = actionBy;
	}

	public Timestamp getActionDt() {
		return this.actionDt;
	}

	public void setActionDt(Timestamp actionDt) {
		this.actionDt = actionDt;
	}

	public String getActive() {
		return this.active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getConnectionName() {
		return this.connectionName;
	}

	public void setConnectionName(String connectionName) {
		this.connectionName = connectionName;
	}

	public String getDbType() {
		return this.dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public String getHostName() {
		return this.hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getPassWord() {
		return this.passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public Long getPortNo() {
		return this.portNo;
	}

	public void setPortNo(long l) {
		this.portNo = l;
	}

	public String getSid() {
		return this.sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	/*public List<SubsetCriteriaDO> getSubsetCriterias() {
		return this.subsetCriterias;
	}

	public void setSubsetCriterias(List<SubsetCriteriaDO> subsetCriterias) {
		this.subsetCriterias = subsetCriterias;
	}

	public SubsetCriteriaDO addSubsetCriteria(SubsetCriteriaDO subsetCriteria) {
		getSubsetCriterias().add(subsetCriteria);
		subsetCriteria.setDataConConnection(this);

		return subsetCriteria;
	}

	public SubsetCriteriaDO removeSubsetCriteria(SubsetCriteriaDO subsetCriteria) {
		getSubsetCriterias().remove(subsetCriteria);
		subsetCriteria.setDataConConnection(null);

		return subsetCriteria;
	}*/

}