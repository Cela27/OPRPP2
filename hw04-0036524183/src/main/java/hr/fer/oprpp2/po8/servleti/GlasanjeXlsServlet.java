package hr.fer.oprpp2.po8.servleti;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import hr.fer.oprpp2.p08.dao.DAOProvider;
import hr.fer.oprpp2.p08.model.UnosPollOptions;

/*
 * Servlet for creating .xls file of results.
 */
@WebServlet(name = "glasanje-xls", urlPatterns = { "/servleti/glasanje-xls" })
public class GlasanjeXlsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		resp.setContentType("application/vnd.ms-excel");
		int id = 0;

		try {
			id = Integer.parseInt(req.getParameter("pollID"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<UnosPollOptions> list = DAOProvider.getDao().dohvatiUnoseIzPollOptionsZaOdređenPoll(id);

		@SuppressWarnings("resource")
		HSSFWorkbook workbook = new HSSFWorkbook();

		// Creating a blank Excel sheet

		HSSFSheet sheet = workbook.createSheet();
		int i = 1;
		for (UnosPollOptions unos: list) {
			Row row = sheet.createRow(i);

			Cell cell1 = row.createCell(0);
			cell1.setCellValue(unos.getOptionTitle());

			Cell cell2 = row.createCell(1);
			cell2.setCellValue(unos.getGlasovi());
			i++;
		}

		try {
			OutputStream os = resp.getOutputStream();
			workbook.write(os);

			os.close();
			os.flush();

		} catch (Exception e) {
			e.printStackTrace();
		}
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
