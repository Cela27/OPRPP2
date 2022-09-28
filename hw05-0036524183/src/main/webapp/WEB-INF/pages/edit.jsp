<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
Object id = request.getSession().getAttribute("current.user.id");
String title =(String) request.getSession().getAttribute("current.entry.title");
String text = (String)request.getSession().getAttribute("current.entry.text");

String fn =(String) request.getSession().getAttribute("current.user.fn");
String ln = (String)request.getSession().getAttribute("current.user.ln");

boolean logged = true;
if (id == null)
	logged = false;
%>

<html>

<body>
	<c:if test="<%=!logged%>">

		<a href="/blog/servleti/register">Registriraj se</a>

	</c:if>
	<c:if test="<%=logged%>">
		<p><%=fn %> <%=ln %></p>
		<a href="/blog/servleti/logout">Logout</a>
		<br>
		<h1>Izmjenite ovaj entry</h1>
		<form action="/blog/servleti/edit/?id=<%=id%>" method="post">

			Title:<input type="text" name="title" value='<%=title %>'><br /> Text:<input
				type="text" name="text" value="<%=text %>"><br /> <input type="submit"
				value="Izmjeni" />

		</form>

	</c:if>

</body>

</html>
