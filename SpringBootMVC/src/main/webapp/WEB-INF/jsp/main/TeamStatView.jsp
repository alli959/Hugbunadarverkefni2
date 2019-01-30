<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<html lang="en">
<head>
    <title>TeamSelect</title>
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/teamStatView.css"/>"/>
</head>
<body>

<header class="header">
    <div class="header__contents">
        <input type="button" onclick="location.href = '/user'" value="<== Back To Main Menu"/>
        <div class="header__menu">
            <div class="header__menu__user">
                <a class="header__menu__item">${msg}</a>
                <input type="button" onclick="location.href = '/logout'" value="Logout" />
            </div>
        </div>
    </div>
</header>

<c:choose>
    <c:when test="${not empty teams}">

        <table class="allPlayers">
            <tr>
                <th>TeamName</th>

            </tr>


            <c:forEach var="team" items="${teams}">
                <tr>
                    <td><a href ="/user/stats/${team.id}">${team.name}</a></td>


                </tr>
            </c:forEach>
        </table>
    </c:when>


    <c:otherwise>
        <h3>No teams</h3>
    </c:otherwise>
</c:choose>

</body>
</html>
