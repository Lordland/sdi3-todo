package uo.sdi.persistence.util;


import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import uo.sdi.persistence.PersistenceException;

public class Jdbc {
	private static final String QUERIES_PROPERTIES_FILE = "/sql_queries.properties";
	
	private static JdbcHelper jdbc = new JdbcHelper(QUERIES_PROPERTIES_FILE);
	
	private static Properties sqlQueries;
	
	static {
		sqlQueries = loadProperties( QUERIES_PROPERTIES_FILE );
	}

	private static ThreadLocal<Connection> threadLocal = new ThreadLocal<>();

	public static Connection createConnection() {
			Connection con = jdbc.createConnection();
			threadLocal.set(con);
			return con;
	}

	public static Connection getCurrentConnection() throws SQLException {
		Connection con = threadLocal.get();
		if (con == null){
			con = createConnection();
		}
		if (con.isClosed()){
			con = createConnection();
		}
		return con;
	}

	public static String getSqlQuery(String queryKey) {
		return sqlQueries.getProperty(queryKey).trim();
	}

	public static void close(ResultSet rs, PreparedStatement ps, Connection con) {
		jdbc.close(ps, rs,con);
	}

	public static void close(PreparedStatement ps, Connection con) {
		jdbc.close(ps,con);
	}

	static void close(ResultSet rs) {
		if (rs != null) { try{ rs.close(); } catch (Exception ex){}};
	}

	public static void close(PreparedStatement ps) {
		if (ps != null) { try{ ps.close(); } catch (Exception ex){}};
	}

	/**
	 * If not using a Transaction object a call to this method physically closes 
	 * the connection (each call to a Dao method is in its own transaction).
	 * 
	 * If there is a Transaction open then this method don't do anything as the 
	 * Transaction itself will close the connection by calling commit or rollback
	 *    
	 * @param con
	 */
	public static void close(Connection con) {
		if ( ! isInAutoCommitMode(con) ) return; // Transaction is in charge
		
		threadLocal.set(null);
		if (con != null) { try{ con.close(); } catch (Exception ex){}};
	}

	private static boolean isInAutoCommitMode(Connection con) {
		try {
			return con.getAutoCommit();
		} catch (SQLException e) {
			throw new PersistenceException("Unexpected exception", e);
		}
	}

	private static Properties loadProperties(String fileName) {
		Properties prop = new Properties();
		InputStream stream = Jdbc.class.getClassLoader().getResourceAsStream(fileName);
		try {
			prop.load( stream );
		} catch (IOException e) {
			throw new PersistenceException("Wrong configutation file " + fileName );
		}
		return prop;
	}

}
