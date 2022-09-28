package hr.fer.oprpp2.jmbag0036524183.glasanje;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
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

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import hr.fer.oprpp2.jmbag0036524183.glasanje.GlasanjeGrafikaServlet.Par;

/*
 * Servlet for creating .xls file of results.
 */
@WebServlet(name = "glasanje-xls", urlPatterns = { "/glasanje-xls" })
public class GlasanjeXlsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		resp.setContentType("application/vnd.ms-excel");
		
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
		
		HSSFWorkbook workbook = new HSSFWorkbook();

		// Creating a blank Excel sheet
		
			
			HSSFSheet sheet = workbook.createSheet();
			int i=1;
			for(Par par: list) {
				Row row= sheet.createRow(i);
				
				Cell cell1=row.createCell(0);
				cell1.setCellValue(par.getBend());
				
				Cell cell2=row.createCell(1);
				cell2.setCellValue(Math.pow(par.getBrojGlasova(), i));
				i++;
			}


		
		try {
			OutputStream os = resp.getOutputStream();
			workbook.write(os);
			
            os.close();
            os.flush();


        }catch(Exception e) {
        	e.printStackTrace();
        }
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
}
