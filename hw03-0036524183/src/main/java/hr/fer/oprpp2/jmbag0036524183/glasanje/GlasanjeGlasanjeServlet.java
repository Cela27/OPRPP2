package hr.fer.oprpp2.jmbag0036524183.glasanje;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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
 * Servlet for saving votes.
 * @author Antonio
 *
 */
@WebServlet(name = "glasanje-glasaj", urlPatterns = { "/glasanje-glasaj" })
public class GlasanjeGlasanjeServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int id = 0;
		try {
			id = Integer.valueOf(req.getParameter("id"));
		} catch (Exception ignorable) {
		}

		// Učitaj raspoložive bendove
		String fileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
		File file = new File(fileName);
		if (!file.exists()) {
			file.createNewFile();
		}

		Map<Integer, Integer> mapa = new TreeMap<>();
		// ucitaj rezultate
		try (BufferedReader reader = new BufferedReader(new FileReader(fileName, StandardCharsets.UTF_8))) {
			String line = reader.readLine();

			while (line != null) {
				String[] splits = line.split(" ");
				int broj = Integer.parseInt(splits[0]);

				int brojGlasova = Integer.parseInt(splits[1]);
				mapa.put(broj, brojGlasova);
				line = reader.readLine();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		mapa.put(id, mapa.get(id) + 1);

		// upisi rezultate

		// prebrisi sve
		boolean isFileCreated = file.createNewFile();
		if (!isFileCreated) {
			file.delete();
			isFileCreated = file.createNewFile();
		}
		// upisuj
		for (Map.Entry<Integer, Integer> ent : mapa.entrySet()) {
			// klasicno formatiranje zapisa {adresa#sifra}
			String zapis = ent.getKey().toString()+ " "+ent.getValue().toString();
			try (BufferedWriter writer = new BufferedWriter(
					new FileWriter(fileName, StandardCharsets.UTF_8, true))) {

				writer.append(zapis + "\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		resp.sendRedirect(req.getContextPath() + "/glasanje-rezultati");

	}

}