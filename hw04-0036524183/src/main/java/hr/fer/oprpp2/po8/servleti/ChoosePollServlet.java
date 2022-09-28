package hr.fer.oprpp2.po8.servleti;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.oprpp2.p08.dao.DAOProvider;
import hr.fer.oprpp2.p08.model.UnosPolls;

/**
 * Servlet for fetching polls and showing them to user.
 * @author Antonio
 *
 */
@WebServlet(name="cp", value="/servleti/index.html")
public class ChoosePollServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		List<UnosPolls> unosi = DAOProvider.getDao().dohvatiUnoseIzPolls();

		req.setAttribute("unosi", unosi);
		req
		.getRequestDispatcher("/WEB-INF/pages/choosePoll.jsp")
		.forward(req, resp);
	}
}
