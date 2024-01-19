package com.example.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.*;

public class SJDBCAgent {
	private static final Log log= LogFactory.getLog(SJDBCAgent.class);

	public static Connection getConnection(String type,String ip,String port,String databasename,String userName,String password)
	{
		Connection conn = null;
		try {
			if("sqlServer".equalsIgnoreCase(type)){
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				String url ="jdbc:sqlserver://" + ip + ":" + port + ";databaseName="+databasename;
				conn= DriverManager.getConnection(url, userName, password);
			}else if("Oracle".equalsIgnoreCase(type)){
				String url="jdbc:oracle:thin:@"+ip+":"+port+":"+databasename;
				Class.forName("oracle.jdbc.driver.OracleDriver");
				conn = DriverManager.getConnection(url, userName, password);
			}else if("mysql".equalsIgnoreCase(type)){//mysql
				Class.forName("com.mysql.jdbc.Driver");
				String url="jdbc:mysql://" + ip+":"+port+"/"+databasename+"?characterEncoding=UTF-8" ;
				conn = DriverManager.getConnection(url, userName, password);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("SJDBCAgent数据连接获取异常：" + e.getMessage());
			log.info("SJDBCAgent数据连接获取异常：" + e);
		}
		return conn;
	}

	public static void close(Connection conn, Statement st, ResultSet rs)
	{
		try{rs.close();}catch(Exception e){}
		try{st.close();}catch(Exception e){}
		try{conn.close();}catch(Exception e){}
	}

	public static String checkDB(String driverClassName,String url,String userName,String passWord)
	{
        Connection conn=null;
		try {
			Class.forName(driverClassName);
			try {
                conn=DriverManager.getConnection(url,userName,passWord);
			} catch (SQLException e) {
				e.printStackTrace();
				return "数据库连接失败";
			} finally {
			    close(conn,null,null);
            }
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return "数据库驱动不存在!";
		}
		return "";
	}
}
