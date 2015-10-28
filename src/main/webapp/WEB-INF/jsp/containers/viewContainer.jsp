<%@include file="../templates/header.jsp" %>

<body>
    <%@include file="../templates/navbar.jsp" %>
    <div class="wrapper wrapper-fluid">
        <h2>${container.containerName}</h2>
        <h4>Description</h4>
        <p>${container.description}</p>
        <h4>Chemicals</h4>
        <c:forEach items="${chemicals}" var="chemical">
            <div class="container container-blue clickableContainer">
                <header>${chemical.name}</header>
                <main>
                <section class="row">
                    <div class="col-2">
                        MSDS LABEL
                    </div>
                    <div class="col-10">
                        <c:set var="fireDiamond" value="${chemical.fireDiamond}" />
                        <p>
                            <b>Flammability: </b>${fireDiamond.flammability} 
                            <b>Health: </b> ${fireDiamond.health} 
                            <b>Instability: </b>${fireDiamond.instability} 
                            <br/>
                            <b>Notice: </b> ${fireDiamond.notice}
                        </p>
                    </div>
                </section>
                </main>
                <a href="/chemicals/view/${chemical.name}"></a>
            </div>
        </c:forEach>
        <div class="">
            <button class="button button-red button-large" type="submit">Delete Container</button>
        </div>
        <br/>
    </div>
</body>

<%@include file="../templates/footer.jsp" %>
