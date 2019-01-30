<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>

<html lang="en">

<head>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/main.css"/>"/>
    <title>Main page</title>

</head>

<body>
<a class="header__menu__top">${msg}</a>
<input type="button" onclick="location.href = '/logout'" value="Logout" />
    <header class="header">
        <div class="header__contents">
            <h1 class="header__title">BBall StatTracker</h1>
            <div class="header__menu">
                <ul class="header__menu__list">
                    <li>
                        <a class="header__menu__item" href="<c:url value="/user/team"/>">Team</a>
                    </li>
                    <li>
                        <a class="header__menu__item" href="<c:url value="/user/stats"/>">Stats</a>
                    </li>
                </ul>
        </div>
        </div>
    </header>

    <div class="main">
        <a href="/user/pregame"><button>Start</button></a>
    </div>

</body>

</html>

