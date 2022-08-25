package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import beans.Comment;


public class CommentDAO {
private Connection connection;
	
	public CommentDAO(Connection connection) {
		this.connection = connection;
	}
	
	
	//TODO
	public List<Comment> getComments(int idImage) throws SQLException {
		List<Comment> comments = new ArrayList<>();
		
		String queryString = "SELECT * from imagegallery.comment WHERE imageId = ? ORDER BY Date ASC";
		
		try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
			preparedStatement.setInt(1,idImage);
			
			try(ResultSet result = preparedStatement.executeQuery()){
				while(result.next()) {
					Comment comment = new Comment();
					comment.setText(result.getString("text"));
					comment.setUserName(result.getString("userName"));
					comments.add(comment);
				}
			}
		}
		return comments;
	}
	
	public void addComment(int imageId, String userName, String text) throws SQLException {
		    
		 String query = "INSERT INTO imagegallery.comment(imageId,userName,text,date) VALUES (?,?,?,?)";
		 
		 long millis=System.currentTimeMillis();  
		 java.sql.Date date = new java.sql.Date(millis);  
		 
		
		 connection.setAutoCommit(false);
		 
		 try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
				preparedStatement.setInt(1, imageId);
				preparedStatement.setString(2, userName);
				preparedStatement.setString(3, text);
				preparedStatement.setDate(4, date);
				preparedStatement.executeUpdate();
				connection.commit();
		} 	
		
	}
	
	
	
}
