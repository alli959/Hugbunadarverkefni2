<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>

<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Spring Login Form</title>
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/login.css"/>"/>
</head>
<body>

<sf:form method="POST" modelAttribute="user" action="/login">

    <div align="center">
        <div style="color: blue" >${msg}</div>
        <table>
            <tr>
                <td>User Name</td>
                <td><input type="text" name="userName" /></td>
            </tr>
            <tr>
                <td>Password</td>
                <td><input type="password" name="password" /></td>
            </tr>
            <tr>
            </tr>
        </table>
        <div style="color: red">${error}</div>
        <p>Don't have an account?</p>
        <a href="/register">Click here to register</a>
    </div>
    <input type="submit" value="Submit" />
</sf:form>
</body>
</html>
