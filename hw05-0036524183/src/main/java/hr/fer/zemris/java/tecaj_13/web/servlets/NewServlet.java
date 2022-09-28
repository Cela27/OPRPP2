package hr.fer.zemris.java.tecaj_13.web.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.model.BlogEntry;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;

@WebServlet(urlPatterns = { "/servleti/new" })
public class NewServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String title = "";
		String text = "";
		long userId=0;
		try {

			title = request.getParameter("title");
			text = request.getParameter("text");
			userId=(long) request.getSession().getAttribute("current.user.id");
			
			BlogEntry blogEntry= DAOProvider.getDAO().createBlogEntry(title, text, userId);
			
			if(blogEntry!=null) {
				request.getRequestDispatcher("/WEB-INF/pages/uspjeh.jsp").forward(request, response);
			}
			else {
				request.getRequestDispatcher("/WEB-INF/pages/neuspjeh.jsp").forward(request, response);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
