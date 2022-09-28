package hr.fer.oprpp2.p08;

import java.beans.PropertyVetoException;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

/**
 * Web listener for initializing database.
 * @author Antonio
 *
 */
@WebListener
public class Inicijalizacija implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		
		//za db.properties moras koristit Propertise klasu ne resourceBundle
		
		/**String path = "C:\\Eclipse Radne Povrsine\\OPRPP2_DZ4\\hw04-0036524183\\src\\main\\webapp\\WEB-INF\\dbsettings.properties";*/
		String path=sce.getServletContext().getRealPath("WEB-INF\\dbsettings.properties");
		Properties konfiguracija = new Properties();
		try {
			konfiguracija.load(new FileInputStream(path));
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		
		
//		
//		ResourceBundle konfiguracija=ResourceBundle.getBundle("dbsettings");
//		try(FileInputStream fis = new FileInputStream(path)) {
//			
//			konfiguracija= new PropertyResourceBundle(fis);
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
		
		//url = jdbc:derby://localhost:1527/baza1DB
//		String connectionURL = "jdbc:derby://" + konfiguracija.getString("host") + ":" + konfiguracija.getString("port")
//				+ "/" + konfiguracija.getString("name");
		
		String connectionURL = "jdbc:derby://" + konfiguracija.getProperty("host") + ":" + konfiguracija.getProperty("port")
		+ "/" + konfiguracija.getProperty("name");
		
		System.out.println(connectionURL);

		ComboPooledDataSource cpds = new ComboPooledDataSource();
		try {
			
			cpds.setJdbcUrl(connectionURL);
			cpds.setDriverClass("org.apache.derby.jdbc.ClientDriver");
			cpds.setUser(konfiguracija.getProperty("user"));
			cpds.setPassword(konfiguracija.getProperty("password"));
			cpds.setInitialPoolSize(5);
			cpds.setMinPoolSize(5);
			cpds.setAcquireIncrement(5);
			cpds.setMaxPoolSize(20);
		} catch (PropertyVetoException e1) {
			throw new RuntimeException("Pogreška prilikom inicijalizacije poola.", e1);
		}

		// tu mozda provjera jel baza kreirana
		Connection con = null;
		try {
			con = cpds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}

		PreparedStatement statement = null;
		String query1 = "CREATE TABLE Polls\r\n" + " (id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,\r\n"
				+ " title VARCHAR(150) NOT NULL,\r\n" + " message CLOB(2048) NOT NULL\r\n" + ")";

		String query2 = "CREATE TABLE PollOptions\r\n" + " (id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,\r\n"
				+ " optionTitle VARCHAR(100) NOT NULL,\r\n" + " optionLink VARCHAR(150) NOT NULL,\r\n"
				+ " pollID BIGINT,\r\n" + " votesCount BIGINT,\r\n" + " FOREIGN KEY (pollID) REFERENCES Polls(id)\r\n"
				+ ")\r\n";
		boolean unesiPolls=true;
		boolean unesiPollOptions=true;
		//provjeri jel Poll postoji ako ne kreiraj ga
		try {
			statement = con.prepareStatement(query1);
			statement.execute();

		} catch (SQLException e) {
			unesiPolls=false;
			//e.printStackTrace();
		}
		
		//Provjeri jel PollOPtions postoji ako ne kreiraj ga
		try {
			statement = con.prepareStatement(query2);
			statement.execute();
			
		} catch (SQLException e) {
			unesiPollOptions=false;
			//e.printStackTrace();
		}

		//sad znamo da tablice postoje
		try {
			statement=con.prepareStatement("SELECT * FROM Polls");
			ResultSet rs= statement.executeQuery();
			if(rs.next()==false) {
				unesiPolls=true;
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		try {
			statement=con.prepareStatement("SELECT * FROM PollOptions");
			ResultSet rs= statement.executeQuery();
			if(rs.next()==false) {
				unesiPollOptions=true;
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		long pollID1=0;
		long pollID2=0;
		
		if(unesiPolls) {
			try {
				//prvi poll
				statement = con.prepareStatement("INSERT INTO Polls (title, message) values (?,?)",Statement.RETURN_GENERATED_KEYS);
				statement.setString(1, "Odaberi omiljeni bend");
				statement.setString(2, "Od sljedećih bendova, koji Vam je bend najdraži? Kliknite na link kako biste glasali!");
				statement.executeUpdate();
				ResultSet rs= statement.getGeneratedKeys();
				if (rs.next()) {
				    pollID1 = rs.getInt(1);
				};
				
				
				//drugi poll
				statement = con.prepareStatement("INSERT INTO Polls (title, message) values (?,?)",Statement.RETURN_GENERATED_KEYS);
				statement.setString(1, "Odaberi omiljeno jelo");
				statement.setString(2, "Od sljedećih jela, koje Vam je jelo najdraže? Kliknite na link kako biste glasali!");
				statement.executeUpdate();
				rs= statement.getGeneratedKeys();
				if (rs.next()) {
				    pollID2 = rs.getInt(1);
				};
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		
		if(unesiPollOptions) {
				
			//puni pollOptions
			try {
				//prvi poll
				statement = con.prepareStatement("INSERT INTO PollOptions (optionTitle, optionLink, pollID, votesCount) values (?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
				statement.setString(1, "The Beatles");
				statement.setString(2, "https://www.youtube.com/watch?v=z9ypq6_5bsg");
				statement.setLong(3, pollID1);
				statement.setInt(4, 150);
				statement.executeUpdate();
				
				statement.setString(1, "The Platters");
				statement.setString(2, "https://www.youtube.com/watch?v=H2di83WAOhU");
				statement.setInt(4, 60);
				statement.executeUpdate();
				
				
				statement.setString(1, "The Beach Boys");
				statement.setString(2, "https://www.youtube.com/watch?v=2s4slliAtQU");
				statement.setInt(4, 150);
				statement.executeUpdate();
				
				statement.setString(1, "The Four Seasons");
				statement.setString(2, "https://www.youtube.com/watch?v=y8yvnqHmFds");
				statement.setInt(4, 20);
				statement.executeUpdate();
				
				statement.setString(1, "The Marcels");
				statement.setString(2, "https://www.youtube.com/watch?v=qoi3TH59ZE");
				statement.setInt(4, 33);
				statement.executeUpdate();
				
				statement.setString(1, "The Everly Brothers");
				statement.setString(2, "https://www.youtube.com/watch?v=tbU3zdAgiX8");
				statement.setInt(4, 25);
				statement.executeUpdate();
				
				statement.setString(1, "The Mamas And The Papas");
				statement.setString(2, "https://www.youtube.com/watch?v=N-aK6JnyFmk");
				statement.setInt(4, 20);
				statement.executeUpdate();
				
				//drugi pollOption
				statement = con.prepareStatement("INSERT INTO PollOptions (optionTitle, optionLink, pollID, votesCount) values (?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
				statement.setString(1, "Mađarica");
				statement.setString(2, "https://www.coolinarika.com/recept/madarica-e00e28b2-6430-11eb-9ec9-0242ac12007b");
				statement.setLong(3, pollID2);
				statement.setInt(4, 0);
				statement.executeUpdate();
				
				statement.setString(1, "Gulaš");
				statement.setString(2, "https://www.coolinarika.com/inspiracija/gulas-d3e5d3e4-610d-11eb-a0c9-0242ac120026");
				statement.executeUpdate();
				
				
				statement.setString(1, "Lambada");
				statement.setString(2, "https://www.coolinarika.com/recept/lambada-4c6ab3f0-6480-11eb-b316-0242ac120014");
				statement.executeUpdate();
				
				statement.setString(1, "Juha");
				statement.setString(2, "https://www.coolinarika.com/inspiracija/juha-cd047c4c-610d-11eb-943a-0242ac12001c");
				statement.executeUpdate();
				
				statement.setString(1, "Salata");
				statement.setString(2, "https://www.coolinarika.com/inspiracija/salata-c6e50c5a-610d-11eb-8d16-0242ac12003c");
				statement.executeUpdate();
				
				
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}		
		
		sce.getServletContext().setAttribute("hr.fer.zemris.dbpool", cpds);
		sce.getServletContext().setAttribute("path", path);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ComboPooledDataSource cpds = (ComboPooledDataSource) sce.getServletContext()
				.getAttribute("hr.fer.zemris.dbpool");
		if (cpds != null) {
			try {
				DataSources.destroy(cpds);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
