<%@include file="templates/header.jsp" %>

<body>
    <%@include file="templates/navbar.jsp" %>
    <div class="wrapper wrapper-fluid">
        <h3>Welcome, ${username}!</h3>
        <div>
            <h4>Cabinets</h4>
            <c:if test="${empty cabinets}">
                <p>You currently have no cabinets. Click on the <em>Create Cabinet</em> tab to make
                a new cabinet.</p>
            </c:if>
            <c:forEach items="${cabinets}" var="cabinet">
                <div class="container container-blue clickableContainer">
                    <header>${cabinet.name}</header>
                    <main>
                        <p>
                            <b>Description:</b>
                            <br/>
                            ${cabinet.description}</p>
                        <p>
                            <b>Contains:</b>
                            <br/>
                            <c:forEach items="${cabinet.storedItemNames}" var="chemicalName">
                                ${chemicalName},
                            </c:forEach>
                        </p>
                    </main>
                    <a href="/cabinets/view/${cabinet.name}"></a>
                </div>
            </c:forEach>
        </div>
    </div>
</body>

<%@include file="templates/footer.jsp" %>

