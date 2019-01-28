<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html lang="en">
<head>
    <title>Select Team</title>
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/teamSelect.css"/>"/>
</head>
<body>

<header class="header">
    <div class="header__contents">
        <input type="button" onclick="location.href = '/user'" value="<== Back To Main Menu"/>
            <div>
                <a class="header__menu__item">${msg}</a>
                <input type="button" onclick="location.href = '/logout'" value="Logout" />
            </div>
    </div>
</header>

<h1>Select Team</h1>
<c:choose>
    <c:when test="${not empty teams}">

        <table class="allTeams">
            <tr>
                <th>Name</th>
            </tr>


            <c:forEach var="team" items="${teams}">
                <tr>

                    <td><a href="/user/pregame/${team.id}">${team.name}</a></td>

                </tr>
            </c:forEach>
        </table>
    </c:when>


    <c:otherwise>
        <h3>No teams!</h3>
    </c:otherwise>
</c:choose>

</body>
</html>
