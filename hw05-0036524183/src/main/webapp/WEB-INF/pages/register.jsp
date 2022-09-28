<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
Object tmp = request.getAttribute("created");
boolean created = false;
if (tmp == null)
	created = true;

Object tmp1 = request.getAttribute("data");
boolean data = true;
if (tmp1 == null)
	data = false;

String fn =(String) request.getSession().getAttribute("current.user.fn");
String ln = (String)request.getSession().getAttribute("current.user.ln");
%>
<html>
<body>
	<form action="registration" method="post">
		First Name:<input type="text" name="firstName" value="${firstName}"/><br/>
		Last Name:<input type="text" name="lastName" value="${lastName}"/><br/>
		Email:<input type="text" name="email" value="${email}"/><br /> Nick:<input
			type="text" name="nick" /><br /> Password:<input type="password"
			name="password" /><br /> <input type="submit" value="register" />

		<c:if test="<%=!created%>">
			<p>Nick je već korišten probajte ponovo.</p>
		</c:if>
		
		<c:if test="<%=data%>">
			<p>Svi podatci moraju biti uneseni.</p>
		</c:if>

	</form>
</body>
</html>
