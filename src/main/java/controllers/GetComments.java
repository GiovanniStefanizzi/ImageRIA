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

import beans.Comment;
import dao.CommentDAO;
import utility.ConnectionHandler;



@WebServlet("/Album")
@MultipartConfig
public class GetComments extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Connection connection;

	public GetComments() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void init() throws ServletException {
		ServletContext context = getServletContext();
		connection = ConnectionHandler.instance.ConnectDb(context);
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		Integer imageId = null;
		try {
			imageId = Integer.parseInt(request.getParameter("img"));
		} catch (NumberFormatException | NullPointerException e){
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Missing parameters");
			return;
		}
		
		CommentDAO commentDao = new CommentDAO(connection);
		//handle comments
		List<Comment> comments = null;
		try {
			comments = commentDao.getComments(imageId);
			
		}
		catch(SQLException e){
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Error, resources not found");
		}
		String json = new Gson().toJson(comments);
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json);
	}
	
	
}	
	

