package crawler.GooglePlay.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import crawler.GooglePlay.util.SQLConnectorBase;

public class GooglePlayDAO extends DAOBase {
	public static final int ORDER_BY_COMMENTS = 1;
	public static final int ORDER_BY_RELEASE_DATE = 2;
	public static final int ORDER_BY_STAR_RATING = 3;

	private final String _tableName = "crawler";

	private int _idx;
	private ArrayList<GooglePlayData> _gplist = new ArrayList<GooglePlayData>();
	private GooglePlayData _data = null;

	public GooglePlayDAO(SQLConnectorBase connector, String dbName) 
			throws ClassNotFoundException, SQLException 
	{
		setSQLConnector(connector);

		this._conn.openDatabase(dbName);
		if (!this._conn.tableExists(this._tableName)) {
			this._conn.createTable(this._tableName);
		}
	}

	@Override
	public void clearData() {
		this._data = null;
	}

	@Override
	public <T> void setData(T data) {
		this._data = (GooglePlayData) data;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getData() {
		return (T) this._data;
	}

	@Override
	public void insertData() throws SQLException {
		if (this._data == null || this._data.getId() > 0) {
			throw new SQLException("No data has assigned to insert");
		}
		
		/* making query string should be a part of Connector with validation,
		 * but I make the query string here to save time. */
		String query = "INSERT INTO " + this._tableName 
				+ "(title, author, genre, comments, rel_date, rate) "
				+ String.format(" VALUES (\"%s\", \"%s\", \"%s\", %d, \"%s\", %.2f);",
						this._data.getTitle(),
						this._data.getAuthor(),
						this._data.getGenre(),
						this._data.getComments(),
						this._data.getReleaseDate(),
						this._data.getRate()
					);
		this._conn.insert(query);
	}

	@Override
	public int queryData(int orderBy) throws SQLException {
		this._idx = -1;
		StringBuilder query = new StringBuilder();
		query.append("SELECT id, title, author, genre, comments, rel_date, rate "
					+ "FROM " + this._tableName);
		switch (orderBy) {
			case ORDER_BY_COMMENTS:
				query.append(" ORDER BY comments");
				break;
			case ORDER_BY_RELEASE_DATE:
				query.append(" ORDER BY rel_date");
				break;
			case ORDER_BY_STAR_RATING:
				query.append(" ORDER BY rate");
				break;
		}
		query.append(";");

		ResultSet rs = this._conn.query(query.toString());
		
		this._gplist.clear();
		while(rs.next()) {
			GooglePlayData data = new GooglePlayData();

			data.setId(rs.getInt("id"));
			data.setTitle(rs.getString("title"));
			data.setAuthor(rs.getString("author"));
			data.setGenre(rs.getString("genre"));
			data.setComments(rs.getInt("comments"));
			data.setReleaseDate(rs.getString("rel_date"));
			data.setRate(rs.getFloat("rate"));

			this._gplist.add(data);
		}
		this._conn.closeResult();

		return this._gplist.size();
	}

	@Override
	public void updateData()  throws SQLException {
		if (this._data == null || this._data.getId() > 0) {
			throw new SQLException("No data has assigned to update");
		}
		throw new SQLException("Not implemented yet");
	}

	@Override
	public void deleteData()  throws SQLException {
		if (this._data == null || this._data.getId() > 0) {
			throw new SQLException("No id has specified to delete");
		}
		throw new SQLException("Not implemented yet");
	}

	@Override
	public boolean hasNext() {
		if (this._gplist.size() < 1 || this._idx+1 >= this._gplist.size()) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void next() {
		this._idx++;
		if (this._gplist.size() < 1 || this._idx >= this._gplist.size()) {
			this._data = null;
		} else {
			this._data = this._gplist.get(this._idx);
		}
	}
}
