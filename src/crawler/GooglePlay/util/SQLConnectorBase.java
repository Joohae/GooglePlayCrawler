package crawler.GooglePlay.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class SQLConnectorBase {
	public Connection _conn;

	public abstract void openDatabase(String dbName) throws ClassNotFoundException, SQLException;
	
	public abstract boolean tableExists(String tableName) throws SQLException;
	public abstract void createTable(String tableName) throws SQLException;

	public abstract void insert(String query) throws SQLException;
	public abstract ResultSet query(String query) throws SQLException;
	public abstract void closeResult() throws SQLException;
	public abstract void update(String query) throws SQLException;
	public abstract void delete(String query) throws SQLException;
}
