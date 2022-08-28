package controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.lang.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import beans.Album;
import beans.Image;
import dao.AlbumDAO;
import dao.ImageDAO;
import utility.ConnectionHandler;


@WebServlet("/Upload")
@MultipartConfig(
		  fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
		  maxFileSize = 1024 * 1024 * 40,      // 40 MB
		  maxRequestSize = 1024 * 1024 * 100   // 100 MB 
		)
public class ImageUpload extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	
	public ImageUpload() {
		super();
	}
	
	Connection connection;
	
	public void init() throws ServletException{
		ServletContext context = getServletContext();
		connection = ConnectionHandler.instance.ConnectDb(context);
	}	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Integer albumId = null;
		try {
			albumId = Integer.parseInt(request.getParameter("albumId"));
		}
		catch(NumberFormatException e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Missing parameters");
			
		}
		Part filePart = request.getPart("image");
		String title = StringEscapeUtils.escapeJava(request.getParameter("title"));
		String description = StringEscapeUtils.escapeJava(request.getParameter("description"));
		String path = System.getProperty("resources.imagesRIA");
		String imagePath = path + "/resources/";
		String fileSystemPath = "/resources/";
		
		
		
		if (filePart == null || title == null || title.isEmpty() || description == null || description.isEmpty() || albumId == null){
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Missing parameters");
			return;
		}
		
		 //User user = (User) request.getSession().getAttribute("user");
		 String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
		 fileSystemPath = fileSystemPath + fileName;
		 File f = new File(imagePath + fileName);
		 try (OutputStream output = new FileOutputStream(f);  InputStream file = filePart.getInputStream()){
			 file.transferTo(output);
		 }
		 
		 AlbumDAO albumDAO = new AlbumDAO(connection);
		 ImageDAO imageDAO = new ImageDAO(connection);
		 List<Image> images = null;
		 
		 try {
			 Album album = albumDAO.getById(albumId);
			 if(album == null) {
				 response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				 response.getWriter().println("Missing parameters");
				return;
				
				//TODO: handle error
			 }

			 imageDAO.insertImage(title, description, albumId, fileSystemPath);
			 images = albumDAO.getAllImagesFromAlbum(albumId);
			
			 
		} catch (SQLException e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("error in image creation");
			return;
		}
		 
		Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
     		String json = gson.toJson(images);
	    	response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(json);
		 
		 
		
	}	
}
