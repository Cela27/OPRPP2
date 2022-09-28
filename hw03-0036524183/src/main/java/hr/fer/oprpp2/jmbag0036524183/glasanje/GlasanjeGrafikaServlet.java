package hr.fer.oprpp2.jmbag0036524183.glasanje;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.image.RenderedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

/**
 * Servlet for displaying votes in pieChart.
 * @author Antonio
 *
 */
@WebServlet(name = "glasanje-grafika", urlPatterns = { "/glasanje-grafika" })
public class GlasanjeGrafikaServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("image/png");

		// Učitaj raspoložive bendove
		String fileNameRezultati = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
		File file = new File(fileNameRezultati);
		if (!file.exists()) {
			file.createNewFile();
		}

		// lista parova ime glasovi
		List<Par> list = new ArrayList<>();
		// mapa id brGlasova
		Map<Integer, Integer> mapaGlasovi = new TreeMap<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(fileNameRezultati, StandardCharsets.UTF_8))) {
			String line = reader.readLine();

			while (line != null) {
				String[] splits = line.split(" ");
				int broj = Integer.parseInt(splits[0]);

				int brojGlasova = Integer.parseInt(splits[1]);
				mapaGlasovi.put(broj, brojGlasova);
				line = reader.readLine();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		// mapa id ime
		Map<Integer, String> mapaIme = new TreeMap<>();

		String fileNameDefinicija = req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt");
		try (BufferedReader reader = new BufferedReader(new FileReader(fileNameDefinicija, StandardCharsets.UTF_8))) {
			String line = reader.readLine();
			while (line != null) {

				int broj = Integer.parseInt(line.substring(0, line.indexOf(" ")));
				line = line.substring(line.indexOf(" "));
				System.out.println(line);

				line = line.trim();
				String ime = line.substring(0, line.indexOf("http"));
				line = line.trim();
				String link = line.substring(line.indexOf("http"));
				line = line.trim();
				mapaIme.put(broj, ime);
				line = reader.readLine();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		for (int i = 1; i <= mapaGlasovi.size(); i++) {
			list.add(new Par(mapaIme.get(i), mapaGlasovi.get(i)));
		}

		JFreeChart chart = getChart(list);

		ServletOutputStream os = resp.getOutputStream();
		int width = 500;
		int heigt = 350;

		RenderedImage chartImage = chart.createBufferedImage(width, heigt);
		ImageIO.write(chartImage, "png", os);
		os.flush();
		os.close();
	}

	public JFreeChart getChart(List<Par> list) {

		DefaultPieDataset<String> dataset = new DefaultPieDataset<>();

		for (Par par : list) {
			dataset.setValue(par.getBend(), par.getBrojGlasova());
		}

		boolean legend = true;
		boolean tooltips = false;
		boolean urls = false;

		JFreeChart chart = ChartFactory.createPieChart("Bendovi", dataset, legend, tooltips, urls);

		chart.setBorderPaint(Color.BLUE);
		chart.setBorderStroke(new BasicStroke(5.0f));
		chart.setBorderVisible(true);

		return chart;
	}

	public static class Par {
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
}
