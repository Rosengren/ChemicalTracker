<%@include file="templates/header.jsp" %>

<body id="ctrackr" class="index popup pusher chemical" ontouchstart>
    <%@include file="templates/sidebar.jsp" %>
    <div class="ui modal">
        <div class="header">
            ${addTooltip}
        </div>
        <div class="content">
            <div class="ui form">
                <div class="field">
                    <label>Name</label>
                    <input type="text" id="addChemicalName" placeholder="Name" />
                </div>
                <div class="field">
                    <label>Description</label>
                    <textarea rows="4" id="addChemicalDesc"></textarea>
                </div>
            </div>
        </div>
        <div class="actions">
            <div class="ui black deny button">
                Cancel
            </div>
            <div class="ui positive button" id="submitAddChemical">
                Submit
            </div>
        </div>
    </div>

    <div class="pusher">
        <%@include file="templates/menu.jsp" %>
        <div class="full height">
            <div class="masthead segment bg1">
                <div class="ui container">
                    <div class="introduction">
                        <h1 class="ui inverted header">
                            <span class="library">${title}</span>
                            <span class="tagline">${subtitle}</span>
                        </h1>
                        <div class="ui hidden divider"></div>
                    </div>
                </div>
            </div>

            <div class="ui container bottom-padding">
                <%@include file="templates/breadcrumbs.jsp" %>

                <div class="ui icon button addModal right floated" title="${addTooltip}">
                    <i class="add icon"></i>
                </div>

                <div class="ui hidden divider"></div>

                <div class="ui container">
                    <div class="ui relaxed divided items">
                        <div class="item">
                            <div class="ui small image">

                                <ul class="diamond-grid">
                                    <li class="tooltip" title="Flammability">
                                        <a class="diamond red">
                                            <div class="text">${fireDiamond.flammability}</div>
                                        </a>
                                    </li>
                                    <li class="tooltip" title="Health">
                                        <a class="diamond blue">
                                            <div class="text">${fireDiamond.health}</div>
                                        </a>
                                    </li>
                                    <li class="tooltip" title="Instability/Reactivity">
                                        <a class="diamond yellow">
                                            <div class="text">${fireDiamond.instability}</div>
                                        </a>
                                    </li>
                                    <li class="tooltip" title="Personal Protection">
                                        <a class="diamond white">
                                            <div class="text">${fireDiamond.notice}</div>
                                        </a>
                                    </li>
                                </ul>
                            </div>
                            <div class="content">
                                <h2>Material Safety Diamond</h2>
                                <div class="description noBullets">
                                    <ul>
                                        <li>Flammability: ${fireDiamond.flammability}</li>
                                        <li>Health: ${fireDiamond.health}</li>
                                        <li>Instability: ${fireDiamond.instability}</li>
                                        <li>Personal Protection: ${fireDiamond.notice}</li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <c:forEach items="${chemical.properties}" var="property">
                    <div class="topPadding"></div>
                    <h2 class="ui dividing header">${property.key}</h2>
                    <div class="ui two column stackable grid container">

                        <c:forEach items="${property.value}" var="subproperty">

                            <div class="column">
                                <h3>${subproperty.key}</h3>
                                <p>${subproperty.value}</p>
                            </div>
                        </c:forEach>

                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
</body>

<div type="hidden" id="csrf" value="${_csrf.token}"></div>
<div type="hidden" id="username" value="${username}"></div>
<div type="hidden" id="addURL" value="${addURL}"></div>
<%@include file="templates/footer.jsp" %>
