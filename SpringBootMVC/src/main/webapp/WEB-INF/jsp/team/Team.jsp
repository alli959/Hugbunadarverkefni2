
<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>

<html lang="en">

    <head>
        <title>Team</title>
        <link rel="stylesheet" type="text/css" href="<c:url value="/css/team.css"/>"/>
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

        </ul>


        <sf:form method="POST" modelAttribute="createTeam" action="team">

            <table>

                <tr>
                    <td>Team Name:</td>
                    <td><sf:input path="name" type="text" placeholder="Enter the team name"/></td>
                </tr>



            </table>

            <input type="submit" VALUE="Add team"/>

        </sf:form>


        <c:choose>
            <c:when test="${not empty teams}">

                <table class="allTeams">
                    <tr>
                        <th>Name</th>
                    </tr>


                    <c:forEach var="team" items="${teams}">
                        <tr>

                            <td><a href="/user/team/${team.id}">${team.name}</a></td>

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
