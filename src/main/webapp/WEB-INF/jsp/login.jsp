<%@include file="templates/header.jsp" %>

<body>
    <%@include file="templates/navbar.jsp" %>
    <div class="wrapper wrapper-fluid">
        <c:if test="${param.error}">
            Invalid username and password
        </c:if>
        <c:if test="${param.logout}">
            You have been logged out.
        </c:if>
        <form class="form" action="/login" method="post">
            <label>Username</label>
            <input type="text" name="username"/>
            <br/>
            <label>Password</label>
            <input type="password" name="password">
            <br/>
            <input type="submit" value="Sign In" />
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
        </form>
    </div>
</body>

<%@include file="templates/footer.jsp" %>
