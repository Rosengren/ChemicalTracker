<%@include file="../templates/header.jsp" %>

<body>
    <%@include file="../templates/navbar.jsp" %>
    <div class="wrapper wrapper-fluid">
    <c:if test="${not empty success}">
        <div class="alert">
        <c:choose>
            <c:when test="${success}">
                <p>Successfully added chemical!</p>
            </c:when>
            <c:otherwise>
                <p>Error: did not successfully add chemical!</p>
            </c:otherwise>
        </c:choose>
        </div>
    </c:if>
        <h2>Add New Chemical</h2>
        <form:form method="post" action="/chemicals/new" modelAttribute="chemical">

            <div class="form_group">
                <label>Chemical Name</label>
                <input type="text" name="name"/>
            </div>

            <h5>Fire Diamond</h5>

            <div class="form_group row">
                <label class="col-2">Flammability</label>
                <div class="col-1">
                    <input type="number" min="0" max="5" name="fireDiamond.flammability"/>
                </div>
            </div>

            <div class="form_group row">
                <label class="col-2">Health</label>
                <div class="col-1">
                    <input type="number" min="0" max="5" name="fireDiamond.health"/>
                </div>
            </div>

            <div class="form_group row">
                <label class="col-2">Instability</label>
                <div class="col-1">
                    <input type="number" min="0" max="5" name="fireDiamond.instability"/>
                </div>
            </div>

            <div class="form_group">
                <label>Notice</label>
                <textarea name="fireDiamond.notice"></textarea>
            </div>

            <c:forEach items="${chemical.properties}" var="chemicalProperties">
                <h5>${chemicalProperties.key}</h5>
                <c:forEach items="${chemicalProperties.value}" var="subProperties">
                    <div class="form_group">
                        <label>${subProperties.key}</label>
                        <textarea name="${subProperties.value}"></textarea>
                    </div>
                </c:forEach>
            </c:forEach>
            <div class="form_group row">
                <button class="button button-green button-large col-left-9" type="submit">Submit</button>
            </div>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
        </form:form>
    </div>
</body>

<%@include file="../templates/footer.jsp" %>
