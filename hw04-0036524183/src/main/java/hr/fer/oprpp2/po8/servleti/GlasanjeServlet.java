package hr.fer.oprpp2.po8.servleti;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.oprpp2.p08.dao.DAOProvider;
import hr.fer.oprpp2.p08.model.UnosPollOptions;

/**
 * Servlet for voting.
 * @author Antonio
 *
 */
@WebServlet(name = "glasanje", urlPatterns = { "/servleti/glasanje" })
public class GlasanjeServlet extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Učitaj raspoložive bendove
		
		int id=0;
		
		try {
			id= Integer.parseInt(req.getParameter("pollID"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		List<UnosPollOptions> list=DAOProvider.getDao().dohvatiUnoseIzPollOptionsZaOdređenPoll(id);
		req.setAttribute("unosi", list);
		
		req.getRequestDispatcher("/WEB-INF/pages/glasanje.jsp").forward(req, resp);
		
		
	}

}	
