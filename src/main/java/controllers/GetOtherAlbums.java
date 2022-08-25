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
//import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import beans.Album;
import beans.User;
import dao.AlbumDAO;
import utility.ConnectionHandler;



@WebServlet("/GetOtherAlbums")
@MultipartConfig
public class GetOtherAlbums extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection;

	public GetOtherAlbums() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void init() throws ServletException {
		ServletContext context = getServletContext();
		connection = ConnectionHandler.instance.ConnectDb(context);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		AlbumDAO albumDao = new AlbumDAO(connection);
		//List <Album> userAlbums = null;
		List <Album> otherAlbums = null;

		
		User user = (User)request.getSession().getAttribute("user");		
		try {
			otherAlbums = albumDao.getOtherAlbums(user.getId());
		}catch (SQLException e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("not possibile to recover albums");
			return;
		}
		
		Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
		String json =  gson.toJson(otherAlbums);
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json);
	}


}
