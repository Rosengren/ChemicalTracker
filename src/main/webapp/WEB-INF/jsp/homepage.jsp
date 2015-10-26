<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html lang="en">

<meta name="viewport" content="width=device-width">
<head>
    <link rel="stylesheet" type="text/css" href="http://cdn.everything.io/kickstart/3.x/css/kickstart.min.css"> <%--Kickstart--%>
    <link rel="stylesheet" type="text/css" href="css/main.css">

    <script src="http://code.jquery.com/jquery-2.1.4.js"></script>
    <script src="http://cdn.everything.io/kickstart/3.x/js/kickstart.min.js"></script> <%--Kickstart--%>

    <script src="js/main.js"></script>
</head>

<body>
    <div class="navbar navbar-fluid">
        <nav>
            <ul>
                <li><a href="/">ChemicalTracker</a></li>
                <li><a href="#"></a></li>
            </ul>
            <ul>
                <li><a href="#">Create Container</a></li>
                <li><a href="#">Add Chemical</a></li>
                <li><a href="#">About</a></li>
            </ul>
        </nav>
    </div>
    <div class="wrapper wrapper-fluid">
        <c:url value="/resources/text.txt" var="url"/>
        <spring:url value="/resources/text.txt" htmlEscape="true" var="springUrl" />
        Spring URL: ${springUrl} at ${time}
        <br>
        JSTL URL: ${url}
        <br>
        Message: ${message}
        <br>
        <a href="/hello">Click Hello</a>
    </div>
</body>

</html>
