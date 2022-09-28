package hr.fer.zemris.java.tecaj_13.console;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Proba {
	public static void main(String[] args) {
		String path="/servleti/author/perica/78";
		System.out.println(path.substring(path.lastIndexOf('/')+1)); 
		
		String nick=path.substring(0, path.lastIndexOf('/'));
		nick=nick.substring(nick.lastIndexOf('/')+1);
		System.out.println(nick);
	}
}
