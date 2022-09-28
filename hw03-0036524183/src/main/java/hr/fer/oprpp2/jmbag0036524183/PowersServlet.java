package hr.fer.oprpp2.jmbag0036524183;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 * Servelet that creates .xls of powers for numbers a-b.
 * @author Antonio
 *
 */
@WebServlet(name = "power", urlPatterns = { "/powers" })
public class PowersServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Integer a = 1, b = 1, n = 1;

		try {
			a = Integer.valueOf(req.getParameter("a"));
		} catch (Exception ignorable) {
		}

		try {
			b = Integer.valueOf(req.getParameter("b"));
		} catch (Exception ignorable) {
		}
		try {
			n = Integer.valueOf(req.getParameter("n"));
		} catch (Exception ignorable) {
		}

		boolean kriviParametri = false;
		if (a > 100 || a < -100 || b > 100 || b < -100 || n > 5 || n < 1) {
			kriviParametri = true;
		}

		if (kriviParametri) {
			req.getRequestDispatcher("/WEB-INF/pages/errorPowers.jsp").forward(req, resp);
			return;
		}

		HSSFWorkbook workbook = new HSSFWorkbook();

		
		for (int i = 1; i <= n; i++) {

			HSSFSheet sheet = workbook.createSheet(String.valueOf(i));
			int j = 1;
			int aTmp = 1;
			while (aTmp <= b) {
				Row row = sheet.createRow(j);

				Cell cell1 = row.createCell(1);
				cell1.setCellValue(aTmp);

				Cell cell2 = row.createCell(2);
				cell2.setCellValue(Math.pow(aTmp, i));

				aTmp++;
				j++;
			}
		}

		try {

			// Writing the workbook
			// apache/bin sprema
			File file = new File("powers.xls");
			file.createNewFile();
			FileOutputStream out = new FileOutputStream(file);
			workbook.write(out);

			// Closing file output connections
			out.close();

			req.getRequestDispatcher("/WEB-INF/pages/donePowers.jsp").forward(req, resp);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
