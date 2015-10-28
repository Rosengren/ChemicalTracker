<%@include file="templates/header.jsp" %>

<body>
    <%@include file="templates/navbar.jsp" %>
    <div class="wrapper wrapper-fluid">
        <h3>Welcome, ${username}!</h3>
        <div>
            <h4>Containers</h4>
            <c:if test="${empty containers}">
                <p>You currently have no containers. Click on the <em>Create Container</em> tab to make
                a new container.</p>
            </c:if>
            <c:forEach items="${containers}" var="container">
                <div class="container container-blue clickableContainer">
                    <header>${container.containerName}</header>
                    <main>
                        <p>
                            <b>Description:</b>
                            <br/>
                            ${container.description}</p>
                        <p>
                            <b>Contains:</b>
                            <br/>
                            <c:forEach items="${container.chemicalNames}" var="chemicalName">
                                ${chemicalName},
                            </c:forEach>
                        </p>
                    </main>
                    <a href="/containers/view/${container.containerName}"></a>
                </div>
            </c:forEach>
        </div>
    </div>
</body>

<%@include file="templates/footer.jsp" %>

