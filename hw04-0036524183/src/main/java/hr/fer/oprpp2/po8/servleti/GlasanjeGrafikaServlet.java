package hr.fer.oprpp2.po8.servleti;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.util.List;

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

import hr.fer.oprpp2.p08.dao.DAOProvider;
import hr.fer.oprpp2.p08.model.UnosPollOptions;

/**
 * Servlet for displaying votes in pieChart.
 * @author Antonio
 *
 */
@WebServlet(name = "glasanje-grafika", urlPatterns = { "/servleti/glasanje-grafika" })
public class GlasanjeGrafikaServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("image/png");

		// Učitaj raspoložive bendove
		int id = 0;

		try {
			id = Integer.parseInt(req.getParameter("pollID"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<UnosPollOptions> list = DAOProvider.getDao().dohvatiUnoseIzPollOptionsZaOdređenPoll(id);
		JFreeChart chart = getChart(list);

		ServletOutputStream os = resp.getOutputStream();
		int width = 500;
		int heigt = 350;

		RenderedImage chartImage = chart.createBufferedImage(width, heigt);
		ImageIO.write(chartImage, "png", os);
		os.flush();
		os.close();
	}

	public JFreeChart getChart(List<UnosPollOptions> list) {

		DefaultPieDataset<String> dataset = new DefaultPieDataset<>();

		for (UnosPollOptions unos : list) {
			dataset.setValue(unos.getOptionTitle(), unos.getGlasovi());
		}

		boolean legend = true;
		boolean tooltips = false;
		boolean urls = false;

		JFreeChart chart = ChartFactory.createPieChart("Voting results", dataset, legend, tooltips, urls);

		chart.setBorderPaint(Color.BLUE);
		chart.setBorderStroke(new BasicStroke(5.0f));
		chart.setBorderVisible(true);

		return chart;
	}

}
