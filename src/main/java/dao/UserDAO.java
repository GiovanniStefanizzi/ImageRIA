package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import beans.User;

public class UserDAO {

	private  Connection connection;
	public UserDAO(Connection connection) {
		this.connection = connection;
	}
	

	public User checkCredentials(String username, String password) throws SQLException {
		String query = "SELECT * FROM imagegallery.user WHERE username = ? AND password = ?";
		try (PreparedStatement pStatement = connection.prepareStatement(query);) {
			pStatement.setString(1, username);
			pStatement.setString(2, password);
			try (ResultSet res = pStatement.executeQuery();) {
				if(!res.isBeforeFirst()) // no result
					return null;
				else {
					if(res.next()) {
						User user = new User();
						user.setId(res.getInt("idUser"));
						user.setUserName(res.getString("userName"));
						user.setEmail(res.getString("email"));
						user.setPassword(res.getString("password"));
					return user;
					}
				}
				
			}
			return null;
		}
	}
	
	public boolean alreadyTakenUserName(String username) throws SQLException{
	
		String query = "SELECT * FROM imagegallery.user WHERE username = ?";
		try (PreparedStatement pStatement = connection.prepareStatement(query);) {
			pStatement.setString(1, username);
			try (ResultSet res = pStatement.executeQuery();) {
				if(!res.isBeforeFirst()) // no result
					return false;
				else if(res.next())return true;
			}
		}
		return true;
	}		
	
	public boolean alreadyTakenEmail(String email) throws SQLException{
		
		String query = "SELECT * FROM imagegallery.user WHERE email = ?";
		try (PreparedStatement pStatement = connection.prepareStatement(query);) {
			pStatement.setString(1, email);
			try (ResultSet res = pStatement.executeQuery();) {
				if(!res.isBeforeFirst()) // no result
					return false;
				else if(res.next())return true;
			}
		}
		return true;
	}		
	
	public User registerUser(String username, String email, String password) throws SQLException{
		
		User user = new User();
		user.setUserName(username);
		user.setEmail(email);
		user.setPassword(password);
		
		String query ="INSERT INTO imagegallery.user (userName, email, password) VALUES (?, ?, ?)";
		
		try (PreparedStatement pStatement = connection.prepareStatement(query);) {
			
			pStatement.setString(1, user.getUserName());
			pStatement.setString(2, user.getEmail());
			pStatement.setString(3, user.getPassword());


			try {
				int result = pStatement.executeUpdate();
				if(result > 0)
					return user;
				else return null;
			}
			catch (SQLException e){
				connection.rollback();
				// statement.rollback
				return null;
			}
		}
	}
}
