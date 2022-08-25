package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import beans.Image;

public class ImageDAO {
	
	private Connection connection;
	
	public ImageDAO(Connection connection) {
		this.connection = connection;
	}
	
	public Image selectImage(int idImage)throws SQLException {
		
		Image img = new Image();
		String queryString = "SELECT * FROM imagegallery.image WHERE idImage = ?";	
		
		try(PreparedStatement preparedStatement = connection.prepareStatement(queryString)){
			preparedStatement.setInt(1, idImage);
			try(ResultSet result = preparedStatement.executeQuery()){
				while (result.next()){
					 img.setAlbumId(result.getInt("idAlbum"));
					 img.setId(result.getInt("idImage"));
					 img.setTitle(result.getString("title"));
					 img.setDescription(result.getString("description"));
					 img.setDate(result.getDate("date"));
					 img.setSource(result.getString("source"));
				}
			}
			
		}			
		return img;
	}
	
	public void insertImage(String title, String description, int idAlbum, String source) throws SQLException{
        String queryString = "INSERT INTO image(title, description, idAlbum, source, date) VALUES (?, ?, ?, ?, ?)";
        long millis=System.currentTimeMillis();  
		java.sql.Date date = new java.sql.Date(millis);  
        connection.setAutoCommit(false);
        
        try(PreparedStatement preparedStatement = connection.prepareStatement(queryString)){
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, description);
            preparedStatement.setInt(3, idAlbum);
            preparedStatement.setString(4, source);
            preparedStatement.setDate(5, date);
            preparedStatement.executeUpdate();
            connection.commit();
        }
    }
	
}
