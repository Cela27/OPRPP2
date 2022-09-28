package hr.fer.oprpp2.jmbag0036524183;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/*
 * Servlet for displaying short story.
 */
@WebServlet(name="prica", urlPatterns={"/stories/funny.jsp"})
public class PricaServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		req
		.getRequestDispatcher("/funny.jsp")
		.forward(req, resp);
		
	}	
}
