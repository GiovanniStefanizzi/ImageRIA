package controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.Album;
import dao.AlbumDAO;
import utility.ConnectionHandler;

@WebServlet("/SaveOrder")
@MultipartConfig
public class SaveOrder extends HttpServlet{
	
	private static final long serialVersionUID = 1L;

	public SaveOrder() {
		super();
	}
	
	Connection connection;
	
	public void init() throws ServletException{
		ServletContext context = getServletContext();
		connection = ConnectionHandler.instance.ConnectDb(context);
	}	

	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String jsonString = request.getParameter("order-arr");
		System.out.println(jsonString);
		
		
		List<String> orderStr = Arrays.asList(jsonString.split(","));
		List<Integer> order = orderStr.stream().map(x -> Integer.parseInt(x)).collect(Collectors.toList());
		System.out.println(order);
		
		AlbumDAO albumDao = new AlbumDAO(connection);
		Album album = null;
		int i = 0;
		
		
		
		try {
		for(int albumId:order) {
			album = albumDao.getById(albumId);
			if(album == null) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().println("Missing parameters");
				return;
			}
			albumDao.saveOrder(albumId, i);
			i++;
		}
		}
		catch(SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Error setting album order");
			return;
		}
	}
}
