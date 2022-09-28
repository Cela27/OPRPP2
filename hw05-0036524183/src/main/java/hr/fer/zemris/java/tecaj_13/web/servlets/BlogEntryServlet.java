package hr.fer.zemris.java.tecaj_13.web.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.tecaj_13.dao.DAO;
import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.model.BlogComment;
import hr.fer.zemris.java.tecaj_13.model.BlogEntry;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;

@WebServlet(name = "blogEntryServlet", urlPatterns = { "/servleti/author/*" })
public class BlogEntryServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = req.getPathInfo();
		req.setAttribute("path", path);

		if (path.endsWith("new")) {

			req.getRequestDispatcher("/WEB-INF/pages/new.jsp").forward(req, resp);
		} else if (path.endsWith("edit")) {

			// tu stavi ako ti treba nes za edit

			req.getRequestDispatcher("/WEB-INF/pages/edit.jsp").forward(req, resp);
		}
		// eid
		else if (isNumeric(path.substring(path.lastIndexOf('/') + 1))) {
			Long entryId = Long.parseLong(path.substring(path.lastIndexOf('/') + 1));
			String nick = path.substring(0, path.lastIndexOf('/'));
			nick = nick.substring(nick.lastIndexOf('/') + 1);
			BlogUser blogUser = DAOProvider.getDAO().getBlogUser(nick);

			BlogEntry blogEntry = DAOProvider.getDAO().getBlogEntry(entryId, blogUser.getId());
			req.setAttribute("nick", nick);
			req.setAttribute("entry", blogEntry);
			req.getSession().setAttribute("current.entry.id", entryId);
			req.getSession().setAttribute("current.entry.title", blogEntry.getTitle());
			req.getSession().setAttribute("current.entry.text", blogEntry.getText());

			List<BlogComment> blogComments = blogEntry.getComments();
			req.setAttribute("comments", blogComments);

			String loggedNick = (String) req.getSession().getAttribute("current.user.nick");

			if (nick.equals(loggedNick)) {
				req.setAttribute("isti", true);
			}

			req.getRequestDispatcher("/WEB-INF/pages/eid.jsp").forward(req, resp);

		} else {
			String nick = path.substring(path.lastIndexOf('/') + 1);
			BlogUser blogUser = DAOProvider.getDAO().getBlogUser(nick);
			List<BlogEntry> blogEntries = blogUser.getEntries();
			String loggedNick = (String) req.getSession().getAttribute("current.user.nick");

			if (nick.equals(loggedNick)) {
				req.setAttribute("isti", true);
			}

			req.setAttribute("entries", blogEntries);
			req.setAttribute("nick", nick);
			req.getRequestDispatcher("/WEB-INF/pages/nick.jsp").forward(req, resp);
		}

	}

	public static boolean isNumeric(String strNum) {
		if (strNum == null) {
			return false;
		}
		try {
			double d = Double.parseDouble(strNum);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
}
