package hr.fer.oprpp2.jmbag0036524183;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet for computing trigonometric functions of parameter a-b.
 * @author Antonio
 *
 */
@WebServlet(name="trig", urlPatterns={"/trigonometric"})
public class TrigonometricServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public static class TrigBroja {
		int broj;
		double sin;
		double cos;
		
		public TrigBroja(int broj, double sin, double cos) {
			super();
			this.broj=broj;
			this.sin=sin;
			this.cos=cos;
		}

		public int getBroj() {
			return broj;
		}

		public double getSin() {
			return sin;
		}

		public double getCos() {
			return cos;
		}
		
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Integer a = 0, b = 360;
		
		try {
			a = Integer.valueOf(req.getParameter("a"));
		} catch(Exception ignorable) {
		}
		
		try {
			b = Integer.valueOf(req.getParameter("b"));
		} catch(Exception ignorable) {
		}
		
		if(a > b) {
			Integer tmp = a;
			a = b;
			b = tmp;
		}
		
		if(b > a+720) {
			b=a+720;
		}
		
		List<TrigBroja> rezultati = new ArrayList<>();
		
		for(double i = a; i <= b; i=i+1) {
			double cos= Math.cos(i);
			double sin= Math.sin(i);
			rezultati.add(new TrigBroja((int)i, sin, cos));
		}
		
		req.setAttribute("rezultati", rezultati);
		
		req
			.getRequestDispatcher("/WEB-INF/pages/trigonometric.jsp")
			.forward(req, resp);
	}
	
	
	
	
	
	
	
	
	
}