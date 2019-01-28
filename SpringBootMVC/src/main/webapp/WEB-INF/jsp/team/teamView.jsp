
<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>

<html lang="en">

<head>
    <title>TeamView</title>
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/teamView.css"/>"/>
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

    </ul>


    <h2>${teamName}</h2>

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

                        <td>${player.name}</td>

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

    <button  type="submit" onclick="location.href='/user/team/${teamId}/player'">Add Players</button>





</body>
</html>
