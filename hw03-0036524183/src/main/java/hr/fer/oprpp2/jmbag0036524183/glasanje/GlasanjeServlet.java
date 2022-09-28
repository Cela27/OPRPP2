package hr.fer.oprpp2.jmbag0036524183.glasanje;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet for voting.
 * @author Antonio
 *
 */
@WebServlet(name = "glasanje", urlPatterns = { "/glasanje" })
public class GlasanjeServlet extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Učitaj raspoložive bendove
		String fileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt");
		List<Trio> lista= new ArrayList<>();
		
		try (BufferedReader reader = new BufferedReader(new FileReader(fileName, StandardCharsets.UTF_8))) {
			String line = reader.readLine();
			while(line!=null) {
				
				int broj=Integer.parseInt(line.substring(0, line.indexOf(" ")));
				line=line.substring(line.indexOf(" "));
				System.out.println(line);
				
				line=line.trim();
				String ime= line.substring(0, line.indexOf("http"));
				line=line.trim();
				String link=line.substring(line.indexOf("http"));
				line=line.trim();
				lista.add(new Trio(broj, ime, link));
				line=reader.readLine();
			}
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		req.setAttribute("sudionici", lista);
		
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeIndex.jsp").forward(req, resp);
	}
	
	public static class Trio{
		int broj;
		String ime;
		String link;
		
		public int getBroj() {
			return broj;
		}
		public void setBroj(int broj) {
			this.broj = broj;
		}
		public String getIme() {
			return ime;
		}
		public void setIme(String ime) {
			this.ime = ime;
		}
		public String getLink() {
			return link;
		}
		public void setLink(String link) {
			this.link = link;
		}
		public Trio(int broj, String ime, String link) {
			super();
			this.broj = broj;
			this.ime = ime;
			this.link = link;
		}
		
		
	}
}	
