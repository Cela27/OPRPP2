<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
Object id = request.getSession().getAttribute("current.user.id");
boolean logged = true;
if (id == null)
	logged = false;

String fn =(String) request.getSession().getAttribute("current.user.fn");
String ln = (String)request.getSession().getAttribute("current.user.ln");
%>
<html>
<body>
	<c:if test="<%=!logged%>">
		<a href="/blog/servleti/register">Registriraj se</a>
	</c:if>
	<c:if test="<%=logged%>">
	<p><%=fn %> <%=ln %></p>
		<a href="/blog/servleti/logout">Logout</a>
	</c:if>
	<br>

	<p>Popis svih djela</p>
	<c:forEach var="entry" items="${entries}">
		<h2><a href="/blog/servleti/author/${nick}/${entry.id}">${entry.title}</a></h2>
	</c:forEach>
	
	<br>
	
	<c:if test="${isti}">
		<a href="/blog/servleti/author/${nick}/new">Dodaj novi entry!</a>
	</c:if>
</body>
</html>
