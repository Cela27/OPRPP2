<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
Object id = request.getSession().getAttribute("current.user.id");

String fn = (String) request.getSession().getAttribute("current.user.fn");
String ln = (String) request.getSession().getAttribute("current.user.ln");

boolean logged = true;
if (id == null)
	logged = false;
%>
<html>
<c:if test="<%=!logged%>">
	<a href="/blog/servleti/register">Registriraj se</a>
</c:if>
<c:if test="<%=logged%>">
	<p><%=fn%>
		<%=ln%></p>
	<a href="/blog/servleti/logout">Logout</a>
</c:if>
<body>
	<h1>Odabrani entry:</h1>
	<h3>${entry.title}</h3>
	<p>${entry.text}</p>
	<br>

	<c:if test="${isti}">

		<a href="/blog/servleti/author/${nick}/edit">Uredi ovaj entry!</a>
	</c:if>

	<h2>Komentari:</h2>
	<c:forEach var="comment" items="${comments}">
		<h6>${comment.usersEMail}: ${comment.message}; Objavljeno:
			${comment.postedOn}</h6>
	</c:forEach>

	<form action="/blog/servleti/comment/${nick}" method="post">

		Message:<input type="text" name="message"><br /> <input
			type="submit" value="Objavi" />

	</form>

</body>
</html>