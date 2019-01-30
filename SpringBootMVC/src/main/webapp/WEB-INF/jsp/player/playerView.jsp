
<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>

<html lang="en">

<head>
    <title>Player</title>
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/playerView.css"/>"/>
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

    <ul class = "navBar">
        <li><h2><a href="/user/team">Create Team</a></h2></li>
        <li><h2><a href="/user/team/${teamId}">Team</a></h2></li>
        <li><h2><a href="/user/team/${teamId}/player">Add Players</a></h2></li>
        <li><h2><a href="/user/team/${teamId}/player/${name}">Players</a></h2></li>
    </ul>

    <h1> ${name} </h1>


<h1>Statistics</h1>

<c:choose>
    <c:when test="${not empty players}">


        <table class="allPlayers">
            <tr>
                <th>Player Name</th>
                <th>Score</th>
                <th>3 Points</th>
                <th>3 Point perc</th>
                <th>2 Points</th>
                <th>2 point perc</th>
                <th>Free Throws</th>
                <th>Free Throw perc</th>
                <th>Assists</th>
                <th>Rebounds</th>
                <th>Blocks</th>
                <th>Steals</th>
                <th>Turnovers</th>
                <th>Fouls</th>

            </tr>


            <c:forEach var="player" items="${players}">
                <tr>
                    <td>${player.playerName}</td>

                    <td>${player.points}</td>

                    <td>${player.threePointHit}</td>

                    <td>${player.threePointer}</td>

                    <td>${player.twoPointHit}</td>

                    <td>${player.twoPointer}</td>

                    <td>${player.freeThrowHit}</td>

                    <td>${player.freeThrow}</td>

                    <td>${player.assists}</td>

                    <td>${player.rebounds}</td>

                    <td>${player.blocks}</td>

                    <td>${player.steals}</td>

                    <td>${player.turnovers}</td>

                    <td>${player.fouls}</td>
                </tr>
            </c:forEach>
        </table>
    </c:when>


    <c:otherwise>
        <h3>This player hasn't done anything right...</h3>
    </c:otherwise>
</c:choose>








</body>
</html>
