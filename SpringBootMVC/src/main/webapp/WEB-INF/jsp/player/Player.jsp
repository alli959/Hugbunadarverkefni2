
<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>

<html lang="en">


    <%--private String name;
    private Long playerNo;
    private String playerPos;--%>
    <head>
        <title>Create Players</title>
        <link rel="stylesheet" type="text/css" href="<c:url value="/css/players.css"/>"/>
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

    </ul>

    <sf:form method="POST" modelAttribute="playerAdd" action="player">



        <table>


            <tr>
                <td>Player nr. ${playerNo + 1}  of 12</td>
            </tr>



            <tr>
                <td>Name:</td>
                <td><sf:input path="name" type="text" placeholder="Enter player name"/></td>
            </tr>



            <tr>
                <td>Position:</td>


                <td><sf:input path="playerPos" type="text" placeholder="C,P,S,SG or PG"/></td>

            </tr>

            <tr>
                <td>Jersey number:</td>

                <td><sf:input path="playerNr" type="number"/></td>

            </tr>

        </table>

        <input type="submit" VALUE="Add to team"/>

    </sf:form>

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

                        <td><a href ="player/${player.id}">${player.name}</a></td>

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
    </body>

</html>
