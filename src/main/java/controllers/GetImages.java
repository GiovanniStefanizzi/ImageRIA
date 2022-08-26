package controllers;

import java.io.IOException;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import beans.Album;
import beans.Image;
import dao.AlbumDAO;
import utility.ConnectionHandler;



@WebServlet("/GetImages")
@MultipartConfig
public class GetImages extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Connection connection;

	public GetImages() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void init() throws ServletException {
		ServletContext context = getServletContext();
		connection = ConnectionHandler.instance.ConnectDb(context);
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		Integer albumId = null;
		
		try {
			albumId = Integer.parseInt(request.getParameter("album"));
		} catch (NumberFormatException | NullPointerException e){
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Missing parameters");
			return;
		}
		
		
		AlbumDAO albumDao = new AlbumDAO(connection);
		List<Image> images = null;
		
		try {
			Album album = albumDao.getById(albumId);
			if(album == null) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().println("Selected album does not exist");
				return;
			}
			images = albumDao.getAllImagesFromAlbum(album.getId());
			
		}
		catch(SQLException e){
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Error, resources not found");
			return;
		}
		
		Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
		String json = gson.toJson(images);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json);
	}
	
	
}	
	

