package hr.fer.zemris.java.tecaj_13.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.model.BlogComment;
import hr.fer.zemris.java.tecaj_13.model.BlogEntry;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;

@WebServlet(urlPatterns = { "/servleti/comment/*" })
public class CommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String path = request.getPathInfo();
		String message = "";
		try {
			String nick = path.substring(path.lastIndexOf('/')+1);
			message = request.getParameter("message");
			Long entryId=(long)request.getSession().getAttribute("current.entry.id");
			BlogUser blogUser= DAOProvider.getDAO().getBlogUser(nick);
			Long userIdVlasnikaEntrya=blogUser.getId();
			
			String email= (String) request.getSession().getAttribute("current.user.email");
			
			BlogComment blogComment= DAOProvider.getDAO().createBlogComment(email, message, entryId, userIdVlasnikaEntrya);
			
			if(blogComment!=null) {
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
