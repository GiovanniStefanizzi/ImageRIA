package controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;


import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;


import beans.User;
import dao.AlbumDAO;
import utility.ConnectionHandler;



@WebServlet("/CreateAlbum")
@MultipartConfig
public class CreateAlbum extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection;

	public CreateAlbum() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void init() throws ServletException {
		ServletContext context = getServletContext();
		connection = ConnectionHandler.instance.ConnectDb(context);
	}



	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		User user = (User)request.getSession().getAttribute("user");		
		int userId = user.getId();
		
		String title;
		// Get and escape request parameters
		title = StringEscapeUtils.escapeJava(request.getParameter("title"));
		
		if(title.isEmpty()||title == null) {
			
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Missing parameters");
			return;
		}	
		
		// create the album in the database
		AlbumDAO albumDao = new AlbumDAO(connection);
		try {
			albumDao.createAlbum(userId, title);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("failure in album creation process");
			return;
		}
		
	}

}
