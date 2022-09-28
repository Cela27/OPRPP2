package hr.fer.oprpp2.jmbag0036524183.glasanje;


import java.io.BufferedReader;
import java.io.File;
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

import hr.fer.oprpp2.jmbag0036524183.glasanje.GlasanjeServlet.Trio;

/**
 * Servlet for redirecting to results-webpage.
 * @author Antonio
 *
 */
@WebServlet(name = "glasanje-rezultati", urlPatterns = { "/glasanje-rezultati" })
public class GlasanjeRezultatiServlet extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Učitaj raspoložive bendove
		String fileNameRezultati = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
		File file= new File(fileNameRezultati);
		if(!file.exists()) {
			file.createNewFile();
		}
		
		//lista parova ime glasovi
		List<Par> list= new ArrayList<>();
		//mapa id brGlasova
		Map<Integer, Integer> mapaGlasovi= new TreeMap<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(fileNameRezultati, StandardCharsets.UTF_8))) {
			String line = reader.readLine();

			while(line!=null) {
				String[] splits= line.split(" ");
				int broj=Integer.parseInt(splits[0]);
				
				int brojGlasova=Integer.parseInt(splits[1]);
				mapaGlasovi.put(broj, brojGlasova);
				line=reader.readLine();
			}
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		//mapa id ime
		Map<Integer, String> mapaIme= new TreeMap<>();
		Map<String, String> mapaLink= new TreeMap<>();
		String fileNameDefinicija=req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt");
		try (BufferedReader reader = new BufferedReader(new FileReader(fileNameDefinicija, StandardCharsets.UTF_8))) {
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
				mapaIme.put(broj, ime);
				mapaLink.put(ime,link);
				line=reader.readLine();
			}
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		for(int i=1 ;i<=mapaGlasovi.size();i++) {
			list.add(new Par(mapaIme.get(i), mapaGlasovi.get(i)));
		}
		
		req.setAttribute("rezultati", list);
		
		int maxGlasova=-1;
		
		for(Par par: list) {
			if(par.getBrojGlasova()>maxGlasova) {
				maxGlasova=par.getBrojGlasova();
			}
		}
		
		List<ParLink> pobjednici= new ArrayList<>();
		
		for(Par par: list) {
			if(par.getBrojGlasova()==maxGlasova) {
				pobjednici.add(new ParLink(par.getBend(), mapaLink.get(par.getBend())));
			}
		}
		req.setAttribute("pobjednici", pobjednici);
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeRezultati.jsp").forward(req, resp);
	}
	
	public static class Par{
		String bend;
		int brojGlasova;
		
		
		public Par(String bend, int brojGlasova) {
			super();
			this.bend = bend;
			this.brojGlasova = brojGlasova;
		}
		public String getBend() {
			return bend;
		}
		public void setBend(String ime) {
			this.bend = ime;
		}
		public int getBrojGlasova() {
			return brojGlasova;
		}
		public void setBrojGlasova(int brojGlasova) {
			this.brojGlasova = brojGlasova;
		}	
	}
	
	public static class ParLink{
		String bend;
		String link;
		public String getBend() {
			return bend;
		}
		public void setBend(String bend) {
			this.bend = bend;
		}
		public String getLink() {
			return link;
		}
		public void setLink(String link) {
			this.link = link;
		}
		public ParLink(String bend, String link) {
			super();
			this.bend = bend;
			this.link = link;
		}
		
		
		
	}
}