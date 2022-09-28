<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
Object id = request.getSession().getAttribute("current.user.id");
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
	<a href="/blog/servleti/logout">Logout</a>
	<br>
	<h1>Unesite novi blogEntry</h1>
	<form action="/blog/servleti/new" method="post">

		Title:<input type="text" name="title" /><br /> 
		Text:<input type="text" name="text" /><br /> 
		<input type="submit" value="Unesi" />

	</form>
	
	</c:if>
	
	
	
	
</body>

</html>
