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

@WebServlet("/servleti/logout")
public class LogoutServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<BlogUser> blogUsers = null;

		try {

			blogUsers = DAOProvider.getDAO().getBlogUsers();
		} catch (Exception e) {
			e.printStackTrace();
		}
		req.setAttribute("data", false);
		req.setAttribute("users", blogUsers);
		req.getSession().setAttribute("current.user.id", null);
		req.getSession().setAttribute("current.user.fn", null);
		req.getSession().setAttribute("current.user.ln", null);
		req.getSession().setAttribute("current.user.nick", null);
		req.getSession().setAttribute("current.user.email", null);
		req.getRequestDispatcher("/WEB-INF/pages/main.jsp").forward(req, resp);
	}

}
