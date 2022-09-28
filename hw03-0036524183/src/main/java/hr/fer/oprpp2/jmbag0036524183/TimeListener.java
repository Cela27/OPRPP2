package hr.fer.oprpp2.jmbag0036524183;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Time listener for writing time of application start.
 * @author Antonio
 *
 */
@WebListener
public class TimeListener implements ServletContextListener{

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		sce.getServletContext().setAttribute("vrijeme", System.currentTimeMillis());
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		sce.getServletContext().removeAttribute("vrijeme");
		
	}

}
