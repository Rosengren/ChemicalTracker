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
        More Details will be added here
        <br/>
    </div>
</body>

<%@include file="../templates/footer.jsp" %>
