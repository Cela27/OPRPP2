package hr.fer.oprpp2.jmbag0036524183;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

/**
 * Servlet for displalying pieDiagram.
 * @author Antonio
 *
 */
@WebServlet(name = "report", urlPatterns = { "/reportImage" })
public class ReportServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		resp.setContentType("image/png");
		JFreeChart chart = getChart();
		ServletOutputStream os = resp.getOutputStream();
		int width=500;
		int heigt=350;

        RenderedImage chartImage = chart.createBufferedImage(width, heigt);
        ImageIO.write(chartImage, "png", os);
        os.flush();
        os.close();

	}

	public JFreeChart getChart() {
		DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
		dataset.setValue("1", 23.3);
		dataset.setValue("2", 32.4);
		dataset.setValue("3", 44.2);

		boolean legend = true;
		boolean tooltips = false;
		boolean urls = false;

		JFreeChart chart = ChartFactory.createPieChart("Brojke", dataset, legend, tooltips, urls);

		chart.setBorderPaint(Color.GREEN);
		chart.setBorderStroke(new BasicStroke(5.0f));
		chart.setBorderVisible(true);

		return chart;
	}

}
