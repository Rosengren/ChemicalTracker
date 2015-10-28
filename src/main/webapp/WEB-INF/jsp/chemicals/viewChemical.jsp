<%@include file="../templates/header.jsp" %>

<body>
    <%@include file="../templates/navbar.jsp" %>
    <div class="wrapper wrapper-fluid">
        <h2>${chemical.name}</h2>
        <h4>FireDiamond</h4>
        <section class="row">
            <div class="col-1">
                MSDS LABEL
            </div>
            <div class="col-11">
                <ul>
                    <li class="row"><div class="col-2"><b>Flammability</b></div><div class="col=9">${fireDiamond.flammability}</div></li>
                    <li class="row"><div class="col-2"><b>Health</b></div> ${fireDiamond.health}</li>
                    <li class="row"><div class="col-2"><b>Instability</b></div> ${fireDiamond.instability}</li>
                    <li class="row"><div class="col-2"><b>Notice</b></div> ${fireDiamond.notice}</li>
                </ul>
            </div>
        </section>
            <c:forEach items="${chemical.properties}" var="chemicalProperties">
                <h4>${chemicalProperties.key}</h4>
                <c:forEach items="${chemicalProperties.value}" var="subProperties">
                    <div class="form_group row">
                        <h5 class="col-12">${subProperties.key}</h5>
                        <p class="col-12">${subProperties.value}</p>
                    </div>
                </c:forEach>
            </c:forEach>
        <br/>
    </div>
</body>

<%@include file="../templates/footer.jsp" %>
