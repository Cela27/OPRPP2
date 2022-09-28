package hr.fer.oprpp2.po8.servleti;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.oprpp2.p08.dao.DAOProvider;
import hr.fer.oprpp2.p08.model.UnosPollOptions;
import hr.fer.oprpp2.p08.model.UnosPolls;

/**
 * Servlet for sending vote to database.
 * @author Antonio
 *
 */
@WebServlet(name = "glas", urlPatterns = { "/servleti/glas" })
public class GlasServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		//ovo nije dobro slanje glasova je sigurnosno krivo salji id i onda za id izvuci glas
		
		int id = 0;
		int pollID=0;
		int glasovi=0;
		try {
			id = Integer.parseInt(req.getParameter("id"));
			pollID = Integer.parseInt(req.getParameter("pollID"));
			UnosPollOptions unos= DAOProvider.getDao().dohvatiUnosIzPollOptions(id);
			glasovi= unos.getGlasovi();
		} catch (Exception e) {
			e.printStackTrace();
		}
		glasovi++;
		DAOProvider.getDao().izmjeniGlas(id, glasovi);
		
		List<UnosPollOptions> list= DAOProvider.getDao().dohvatiUnoseIzPollOptionsZaOdređenPoll(pollID);
		int maxGlasova=0;
		for(UnosPollOptions unos: list) {
			if(unos.getGlasovi()>maxGlasova) {
				maxGlasova=unos.getGlasovi();
			}
		}
		List<UnosPollOptions> pobjednici= new ArrayList<>();
		for(UnosPollOptions unos: list) {
			if(unos.getGlasovi()==maxGlasova) {
				pobjednici.add(unos);
			}
		}
		
		req.setAttribute("unosi", list);
		req.setAttribute("pobjednici", pobjednici);
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeRezultati.jsp").forward(req, resp);
	}
}
