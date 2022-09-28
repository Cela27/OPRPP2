package hr.fer.zemris.java.tecaj_13.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;

//to sve mozda /register sam moze obavit
@WebServlet("/servleti/registration")
public class RegistrationServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String firstName = "";
		String lastName = "";
		String email = "";
		String nick="";
		String password;

		response.setContentType("text/html");
		boolean created = false;
		boolean data = false;
		try {
			firstName = request.getParameter("firstName");
			lastName = request.getParameter("lastName");
			email = request.getParameter("email");
			nick = request.getParameter("nick");
			password = request.getParameter("password");

			if (firstName == "" || lastName == "" || email == "" || nick == "" || password == "") {
				data = true;
				created = false;
			} else {
				created = DAOProvider.getDAO().createBlogUser(firstName, lastName, email, nick, password);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (created) {
			request.getSession().setAttribute("logged", true);
			request.setAttribute("created", created);
			
			BlogUser blogUser=DAOProvider.getDAO().getBlogUser(nick);
			request.getSession().setAttribute("current.user.id", blogUser.getId());
			request.getSession().setAttribute("current.user.fn", blogUser.getFirstName());
			request.getSession().setAttribute("current.user.ln", blogUser.getLastName());
			request.getSession().setAttribute("current.user.nick", blogUser.getNick());
			request.getSession().setAttribute("current.user.email", blogUser.getEmail());
			request.getRequestDispatcher("/WEB-INF/pages/registration.jsp").forward(request, response);
		} else {
			request.setAttribute("firstName", firstName);
			request.setAttribute("lastName", lastName);
			request.setAttribute("email", email);
			if (data) {
				request.setAttribute("data", data);
			} else {
				request.setAttribute("created", created);
			}

			request.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(request, response);
		}

	}

}
