package hr.fer.oprpp2.jmbag0036524183;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet that opens pages for changing age background.
 * @author Antonio
 *
 */
@WebServlet(name="sc", value="/setcolor")
public class ColorsServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	
	public ColorsServlet() {
		
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String str = req.getParameter("color");
		req.getSession().setAttribute("Color", str);
		
		
		resp.setContentType("text/html; charset=utf-8");
		resp.setStatus(HttpServletResponse.SC_OK);
		
		req
		.getRequestDispatcher("WEB-INF/pages/colors.jsp")
		.forward(req, resp);
	}
	
}
