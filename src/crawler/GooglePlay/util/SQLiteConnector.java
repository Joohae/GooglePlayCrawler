package crawler.GooglePlay.util;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteConnector extends SQLConnectorBase {
	@Override
	public void openDatabase(String dbName) throws ClassNotFoundException, SQLException 
	{
		Class.forName("org.sqlite.JDBC");	//	initialize SQLite JDBC
		this._conn = DriverManager.getConnection("jdbc:sqlite:"+dbName);
		this._conn.setAutoCommit(true);
	}

	@Override
	public boolean tableExists(String tableName) throws SQLException {
		Statement stmt = this._conn.createStatement();
		String query = "SELECT COUNT(*) as rowcount FROM sqlite_master WHERE type='table' AND name='" + tableName + "';";
		ResultSet rs = stmt.executeQuery(query);
		rs.next();
		return (rs.getInt("rowcount") > 0);
	}

	@Override
	public void createTable(String tableName) throws SQLException {
		Statement stmt = this._conn.createStatement();
		String query = "CREATE TABLE " + tableName + " "
					+ "(id 			INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
					+ " title		TEXT			NOT NULL, "
					+ " author		TEXT			NOT NULL, "
					+ " genre		TEXT			NOT NULL, "
					+ " comments 	INT 			NOT NULL, "
					+ " rel_date	TEXT			NOT NULL, "
					+ " rate		REAL			NOT NULL)";

		stmt.executeUpdate(query);
		stmt.close();
	}

	@Override
	public void insert(String query) throws SQLException {
		Statement stmt = this._conn.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	}

	@Override
	public void update(String query)  throws SQLException {
		
	}

	private Statement queryStmt;
	@Override
	public ResultSet query(String query) throws SQLException {
		queryStmt = this._conn.createStatement();
		ResultSet rs = queryStmt.executeQuery(query);
		return rs;
	}

	@Override
	public void closeResult() throws SQLException {
		if (queryStmt == null || queryStmt.isClosed()) return;
		queryStmt.getResultSet().close();
		queryStmt.close();
		queryStmt = null;
	}

	@Override
	public void delete(String query) throws SQLException {
		
	}
}
