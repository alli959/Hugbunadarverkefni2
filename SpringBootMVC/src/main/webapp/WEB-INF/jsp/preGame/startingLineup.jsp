<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>


<html lang="en">
<head>
    <title>Line-up</title>
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/startingLineup.css"/>"/>
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

<h1>Bench</h1>

<c:choose>
    <c:when test="${not empty players}">

        <table class="allPlayers">
            <tr>
                <th>Name</th>
                <th>Player Position</th>
                <th>Jersey Nr.</th>
            </tr>


            <c:forEach var="player" items="${players}">
                <tr>

                    <td><a href ="${teamId}/${player.id}">${player.name}</a></td>

                    <td>${player.playerPos}</td>

                    <td>${player.playerNr}</td>
                </tr>
            </c:forEach>
        </table>
    </c:when>


    <c:otherwise>
        <h3>No players!</h3>
    </c:otherwise>
</c:choose>

<h1>Starters</h1>


<c:choose>
    <c:when test="${not empty starters}">

        <table class="starters">
            <tr>
                <th>Name</th>
                <th>Player Position</th>
                <th>Jersey Nr.</th>
            </tr>


            <c:forEach var="starter" items="${starters}">
                <tr>

                    <td><a href ="${teamId}/${starter.id}">${starter.name}</a></td>

                    <td>${starter.playerPos}</td>

                    <td>${starter.playerNr}</td>
                </tr>
            </c:forEach>
        </table>
    </c:when>


    <c:otherwise>
        <h3>No players!</h3>
    </c:otherwise>
</c:choose>


<div class="main">
<button type="button" onclick="location.href = '/game'">Start game</button>
</div>

<div style="color: red">${error}</div>

</body>
</html>
