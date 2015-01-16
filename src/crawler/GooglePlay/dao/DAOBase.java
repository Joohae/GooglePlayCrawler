package crawler.GooglePlay.dao;

import java.sql.SQLException;

import crawler.GooglePlay.util.SQLConnectorBase;

public abstract class DAOBase{
	protected SQLConnectorBase _conn = null;
	public void setSQLConnector(SQLConnectorBase conn) throws SQLException {
		if (_conn != null) throw new SQLException("SQL Connector alread assigned");
		_conn = conn;
	}
	
	public abstract void insertData() throws SQLException;
	public abstract int queryData(int orderBy) throws SQLException;
	public abstract void updateData() throws SQLException;
	public abstract void deleteData() throws SQLException;
	
	public abstract boolean hasNext();
	public abstract void next();

	public abstract void clearData();
	public abstract <T> void setData(T data);
	public abstract <T> T getData();

}