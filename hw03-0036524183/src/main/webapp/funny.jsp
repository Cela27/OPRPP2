<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.awt.Color"%>
<%@page import="java.util.Random"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<%
String color = "blue";
Object boja = request.getSession().getAttribute("Color");
List<String> list = new ArrayList<>();
list.add("red"); list.add("pink"); list.add("cyan"); list.add("yellow");list.add("orange"); list.add("black"); list.add("white"); list.add("green");list.add("blue"); 

Random rand = new Random();
String bojaFonta = list.get(rand.nextInt(list.size()));
if (boja == null)
	boja = "white";
%>
<body style='background-color: <%=boja%>;'>
	<p style="color:<%=bojaFonta %>;">In my junior year of high school, this guy asked me on a date. He rented a Redbox movie and made a pizza. 
	We were watching the movie and the oven beeped so the pizza was done. He looked me dead in the eye and said, “This is the worst part.” 
	I then watched this boy open the oven and pull the pizza out with his bare hands, rack and all, screaming at the top of his lungs. 
	We never had a second date.</p>

</body>
</html>