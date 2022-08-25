package utility;

import java.sql.*;
import javax.servlet.*;


public class ConnectionHandler {
	
	public static ConnectionHandler instance = new ConnectionHandler();
	
	public Connection ConnectDb(ServletContext context) throws UnavailableException{
		Connection connection = null;
		try {
			String driver = context.getInitParameter("dbDriver");
			String url = context.getInitParameter("dbUrl");
			String user = context.getInitParameter("dbUser");
			String password = context.getInitParameter("dbPassword");
			Class.forName(driver); //istanzio classe corrispondente al drivermanager per MySQL
			connection = DriverManager.getConnection(url, user, password);
		}
		catch (ClassNotFoundException e) {
			throw new UnavailableException("Can't load db driver");
		}
		catch(SQLException e){
			throw new UnavailableException("Can't connect to db");
		}
		
		return connection;
	}
	
	/*
	public TemplateEngine startTemplate(ServletContext context) {
		TemplateEngine templateEngine = new TemplateEngine();
		ServletContextTemplateResolver resolver = new ServletContextTemplateResolver(context);
		resolver.setTemplateMode(TemplateMode.HTML);
		templateEngine.setTemplateResolver(resolver);
		resolver.setSuffix(".html");
		return templateEngine;
	} */
	
	public void closeConnection(Connection connection) throws SQLException{
		try {
			if (connection != null) 
				connection.close();
		} 
		catch(SQLException sqle) {}
	}
	
	
}
