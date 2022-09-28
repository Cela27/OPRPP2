package hr.fer.zemris.java.tecaj_13.web.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;

@WebServlet("/servleti/login")
public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String nick = "";
		String password = "";

		response.setContentType("text/html");
		boolean logged = false;
		BlogUser user=null;
		List<BlogUser> blogUsers=null;
		try {

			nick = request.getParameter("nick");
			password = request.getParameter("userPass");
			
			user = DAOProvider.getDAO().getBlogUser(nick, password);
			if(user!=null)
				logged=true;
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {

			blogUsers= DAOProvider.getDAO().getBlogUsers();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (logged) {
			request.getSession().setAttribute("current.user.id", user.getId());
			request.getSession().setAttribute("current.user.fn", user.getFirstName());
			request.getSession().setAttribute("current.user.ln", user.getLastName());
			request.getSession().setAttribute("current.user.nick", user.getNick());
			request.getSession().setAttribute("current.user.email", user.getEmail());
			request.setAttribute("data", false);
			request.setAttribute("users", blogUsers);
			request.getRequestDispatcher("/WEB-INF/pages/main.jsp").forward(request, response);
			
		} else {
			request.getSession().setAttribute("current.user.id", null);
			request.setAttribute("data", true);
			request.setAttribute("users", blogUsers);
			request.getRequestDispatcher("/WEB-INF/pages/main.jsp").forward(request, response);
		}

	}
}
