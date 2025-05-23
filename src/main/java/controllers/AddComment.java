package controllers;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringEscapeUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import beans.Comment;
import beans.User;
import dao.CommentDAO;
import utility.ConnectionHandler;


@WebServlet("/Comments")
@MultipartConfig
public class AddComment extends HttpServlet{

	private static final long serialVersionUID = 1L;
	private Connection connection;
	
	public AddComment() {
		// TODO Auto-generated constructor stub
	}
	
	public void init() throws ServletException {
		ServletContext context = getServletContext();
		connection = ConnectionHandler.instance.ConnectDb(context);
	}
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Integer imageId = null;
		String text = null;
		User user = null;
		try {
			user = (User)request.getSession().getAttribute("user");
			imageId = Integer.parseInt(request.getParameter("selectedImg"));
			text = StringEscapeUtils.escapeJava(request.getParameter("text"));
		} catch (NumberFormatException |  NullPointerException e){
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Missing parameters");
			return;
		}
		if(imageId == null || text == null || user == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Missing parameters");
		}
		
		CommentDAO commentDAO = new CommentDAO(connection);
		List<Comment> comments = null;
		try {
			commentDAO.addComment(imageId, user.getUserName(), text);
			comments = commentDAO.getComments(imageId);
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Error adding comment");
			e.printStackTrace();
		};
		
		Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
		String json = gson.toJson(comments);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json);
		
		
	}
	

}
