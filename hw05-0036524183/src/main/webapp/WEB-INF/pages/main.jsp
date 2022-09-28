<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
Object id = request.getSession().getAttribute("current.user.id");
boolean logged = true;
if (id == null)
	logged = false;

String fn = (String) request.getSession().getAttribute("current.user.fn");
String ln = (String) request.getSession().getAttribute("current.user.ln");
%>
<html>
<body>
	<c:if test="<%=!logged%>">
		<a href="/blog/servleti/register">Registriraj se</a>
		<form action="login" method="post">

			Nick:<input type="text" name="nick" /><br /> Password:<input
				type="password" name="userPass" /><br /> <input type="submit"
				value="login" />

		</form>
		<c:if test="${data}">
			<p>Incorrect password or nick.</p>
		</c:if>
	</c:if>
	<c:if test="<%=logged%>">
		<p><%=fn%>
			<%=ln%></p>
		<a href="/blog/servleti/logout">Logout</a>
	</c:if>
	<br />

	<p>Popis svih autora- Kliknite na link da dođete do njihovih djela.</p>
	<c:forEach var="user" items="${users}">
		<a href="/blog/servleti/author/${user.nick}">Autor: ${user.nick}</a>
	</c:forEach>

</body>
</html>
