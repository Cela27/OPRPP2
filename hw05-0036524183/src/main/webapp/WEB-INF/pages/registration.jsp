<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%

String fn =(String) request.getSession().getAttribute("current.user.fn");
String ln = (String)request.getSession().getAttribute("current.user.ln");

    boolean created = (boolean)request.getAttribute("created");
%>
<html>
  <body>
	<h1> test</h1>
	<p>Uspjesna registracija</p>
	
  </body>
</html>