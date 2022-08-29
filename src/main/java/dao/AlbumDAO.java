package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import beans.Album;
import beans.Image;

public class AlbumDAO {
	private Connection connection;
	
	public AlbumDAO(Connection connection) {
		this.connection = connection;
	}
	
	public List<Album> getUserAlbums(int ownerId) throws SQLException{
		
		List<Album> albums = new ArrayList<Album>();	
		String query = "SELECT * FROM imagegallery.album WHERE ownerId = ? ORDER BY `order` ASC, date DESC";
		
		try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
			preparedStatement.setInt(1, ownerId);
			try(ResultSet result = preparedStatement.executeQuery()){
				while (result.next()){
					Album album = new Album();
					album.setOwnerId(result.getInt("ownerId"));
					album.setDate(result.getDate("date"));
					album.setId(result.getInt("idAlbum"));
					album.setTitle(result.getString("title"));
					albums.add(album);
				}
			}	
		} 	
		return albums;
	}
	
	
	public List<Album> getOtherAlbums(int ownerId) throws SQLException{

		List<Album> albums = new ArrayList<Album>();	
		String query = "SELECT ownerId, date, idAlbum, title, userName FROM imagegallery.album "+
				"JOIN imagegallery.user ON (album.ownerId = user.idUser) "+
				"WHERE ownerId != ? ORDER BY date DESC";
		
		
		try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
			preparedStatement.setInt(1, ownerId);
			try(ResultSet result = preparedStatement.executeQuery()){
				while (result.next()){
					Album album = new Album();
					album.setOwnerId(result.getInt("ownerId"));
					album.setDate(result.getDate("date"));
					album.setId(result.getInt("idAlbum"));
					album.setTitle(result.getString("title"));
					album.setOwnerUserName(result.getString("userName"));	
					albums.add(album);
				}
			}	
		} 	
		return albums;
	}
	
	public Album getById(int albumId) throws SQLException {
		Album album = null;
		String query = "SELECT * FROM imagegallery.album WHERE idAlbum = ?";
		
		
		
		try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
			preparedStatement.setInt(1, albumId);
			try(ResultSet result = preparedStatement.executeQuery()){
				if (result.next()){
					album = new Album();
					album.setOwnerId(result.getInt("ownerId"));
					album.setDate(result.getDate("date"));
					album.setId(result.getInt("idAlbum"));
					album.setTitle(result.getString("title"));
				}
			}	
		} 	
		return album;
		
	}
	
	
	//create album
	 public void createAlbum(int ownerId, String title)throws SQLException {
		 
	     long millis=System.currentTimeMillis();  
		 java.sql.Date date = new java.sql.Date(millis);       
		    
		 String query = "INSERT INTO imagegallery.album(ownerId,title,date) VALUES (?,?,?)";
		 
		
		 connection.setAutoCommit(false);
		 
		 try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
				preparedStatement.setInt(1, ownerId);
				preparedStatement.setString(2, title);
				preparedStatement.setDate(3,  date);
				preparedStatement.executeUpdate();
				connection.commit();
		} 		
	 }
	 
	 //get the number of pages of the selected album
	 
	    public int getPageCount(int albumId) throws SQLException{
	        String query = "SELECT CEIL(COUNT(*)/5) FROM imagegallery.image WHERE idAlbum=?";
	        try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
	        	preparedStatement.setInt(1, albumId);
	            try(ResultSet res = preparedStatement.executeQuery();){
	                if (res.next()) return res.getInt(1);
	                return 1;
	            }
	        }
	    }
	   
	 
	 //get five photos for the album web page
	 
	 public List<Image> getFiveImages(int albumId, int currentPage) throws SQLException {
		 List<Image> images = new ArrayList<>();
		 
		 String queryString = "SELECT * FROM imagegallery.image " +
				 				"WHERE idAlbum = ? ORDER BY Date DESC LIMIT 5 OFFSET ?";
		 
		 
		 try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
			 preparedStatement.setInt(1, albumId);
			 preparedStatement.setInt(2, 5*(currentPage-1));
			 
			 try(ResultSet result = preparedStatement.executeQuery()){
				 while(result.next()) {
					 Image img = new Image();
					 img.setAlbumId(result.getInt("idAlbum"));
					 img.setId(result.getInt("idImage"));
					 img.setTitle(result.getString("title"));
					 img.setDescription(result.getString("description"));
					 img.setDate(result.getDate("date"));
					 img.setSource(result.getString("source"));
					 images.add(img);
				 }
			 } 
		 } 
		 return images;
	 }
	 
	 public List<Image> getAllImagesFromAlbum(int albumId) throws SQLException{
		 List<Image> images = new ArrayList<>();
		 
		 String queryString = "SELECT * FROM imagegallery.image " +
				 				"WHERE idAlbum = ? ORDER BY Date DESC";
		 
		 try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
			 preparedStatement.setInt(1, albumId);
			 
			 try(ResultSet result = preparedStatement.executeQuery()){
				 while(result.next()) {
					 Image img = new Image();
					 img.setAlbumId(result.getInt("idAlbum"));
					 img.setId(result.getInt("idImage"));
					 img.setTitle(result.getString("title"));
					 img.setDescription(result.getString("description"));
					 img.setDate(result.getDate("date"));
					 img.setSource(result.getString("source"));
					 images.add(img);
				 }
			 } 
		 } 
		 return images;
	 }
	 
	 
	 public void saveOrder(int albumId, int order) throws SQLException {
		 String query = "UPDATE `imagegallery`.`album` SET `order` = ? WHERE (`idAlbum` = ?)";
		 

		 connection.setAutoCommit(false);
		 
		 try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
				preparedStatement.setInt(1, order);
				preparedStatement.setInt(2, albumId);
				preparedStatement.executeUpdate();
				connection.commit();
		} 		
		 
		 
	 }
	 
}