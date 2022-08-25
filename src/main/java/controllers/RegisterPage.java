package controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.commons.lang.*;
import beans.User;
import dao.UserDAO;
import utility.ConnectionHandler;

@WebServlet("/Register")
@MultipartConfig

public class RegisterPage extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	public RegisterPage() {
		super();
	}
	
	Connection connection;
	
	public void init() throws ServletException{
		ServletContext context = getServletContext();
		connection = ConnectionHandler.instance.ConnectDb(context);
	}
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//String path = "/index.html";
		//ServletContext servletContext = getServletContext();
		/*
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		templateEngine.process(path, ctx, response.getWriter());
		*/
	}
	
	

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = null;
		String email = null;
		String password = null;
		String repeatedPassword = null;
		System.out.print("se verimmo a roma");
	
		
		//ServletContext servletContext = getServletContext();
		
		
		// Get and escape request parameters
		username = StringEscapeUtils.escapeJava(request.getParameter("username"));
		email = StringEscapeUtils.escapeJava(request.getParameter("email"));
		password = StringEscapeUtils.escapeJava(request.getParameter("password"));
		repeatedPassword = StringEscapeUtils.escapeJava(request.getParameter("repeatedpassword"));
		
		if (username == null || password == null || username.isEmpty() || password.isEmpty()
		|| email == null || repeatedPassword == null || email.isEmpty() || repeatedPassword.isEmpty()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Parameters cannot be empty");
			return;
		}
		
		
		//final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		
		//check if password and repeatedPassword match
		if(!password.equals(repeatedPassword)) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Password dont't match");
			return;
		}
		
		//email format validation
	    String regex = "^(.+)@(.+)$";  
	    Pattern pattern = Pattern.compile(regex);
	    Matcher matcher = pattern.matcher(email); 
	    if(!matcher.matches()) {
	    	response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Wrong email format");
			return;
	    	//ctx.setVariable("emailErrorMsg", "wrong email format");
	    }
	    
	    //check if username is already taken
		UserDAO userDao = new UserDAO(connection);
		boolean alreadyTakenUsername = false;
		try {
			alreadyTakenUsername = userDao.alreadyTakenUserName(username);
		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Failure in database credential checking");
			return;
		}
		if(alreadyTakenUsername) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Username is already taken");
			return;
		} //ctx.setVariable("usernameErrorMsg", "username is already taken");
		
		
		
		//check if there's an existing account with the email
		boolean alreadyTakenEmail = false;
		try {
			alreadyTakenEmail = userDao.alreadyTakenEmail(email);
		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Failure in database credential checking");
			return;
		}
		if(alreadyTakenEmail) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("An account with this email is already existing");
			return;
		}//ctx.setVariable("existingAccountErrorMsg", "there is an existing account with this email ");
		
		//insert the user in the database
		User user = null;
		try {
			user = userDao.registerUser(username, email, password);
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failure in database registration process");
			return;
		}

		// If the user exists, add info to the session and go to home page, otherwise
		// return an error message
		if (user == null) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Failure in database registration processg");
			return;
		}
		
		//redirect to login
		request.getRequestDispatcher("Login").forward(request, response);

	}
	
	
	
}

