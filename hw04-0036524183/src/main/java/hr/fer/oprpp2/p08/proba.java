package hr.fer.oprpp2.p08;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class proba {
	public static void main(String[] args) {
		String path = "C:\\Eclipse Radne Povrsine\\OPRPP2_DZ4\\hw04-0036524183\\src\\main\\webapp\\WEB-INF\\dbsettings.properties";

		Properties konfiguracija = new Properties();
		try {
			konfiguracija.load(new FileInputStream(path));
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		String connectionURL = "jdbc:derby://" + konfiguracija.getProperty("host") + ":" + konfiguracija.getProperty("port")
		+ "/" + konfiguracija.getProperty("name");
		
		System.out.println(connectionURL);
		System.out.println(konfiguracija.getProperty("host"));
	}
}
