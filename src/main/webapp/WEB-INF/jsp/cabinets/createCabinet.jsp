<%@include file="../templates/header.jsp" %>

<body>
    <%@include file="../templates/navbar.jsp" %>
    <div class="wrapper wrapper-fluid">
    <c:if test="${not empty success}">
        <div class="alert">
        <c:choose>
            <c:when test="${success}">
                <p>Successfully added cabinet!</p>
            </c:when>
            <c:otherwise>
                <p>Error: did not successfully add cabinet!</p>
            </c:otherwise>
        </c:choose>
        </div>
    </c:if>
        <h2>Create New Cabinet</h2>
        <form:form method="post" action="/cabinets/new" modelAttribute="storage">
            <input type="hidden" name="username" value="${username}">

            <div class="form_group">
                <label>Cabinet Name</label>
                <input type="text" name="name"/>
            </div>

            <div class="form_group">
                <label>Description</label>
                <textarea name="description"></textarea>
            </div>

            <div class="form_group">
                <label>Chemicals</label>
                <c:forEach items="${chemicals}" var="chemical">
                    <div><input type="checkbox" name="chemicalNames" value="${chemical.name}">${chemical.name}</input></div>
                </c:forEach>
            </div>

            <div class="form_group row">
                <button class="button button-green button-large col-left-12" type="submit">Submit</button>
            </div>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
        </form:form>
    </div>
</body>

<%@include file="../templates/footer.jsp" %>