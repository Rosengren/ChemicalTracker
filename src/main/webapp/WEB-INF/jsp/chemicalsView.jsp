<%@include file="templates/header.jsp" %>
<%@include file="templates/menu.jsp" %>

<body id="ctrackr" class="index popup pusher storage" ontouchstart>

    <div class="ui modal addChemicalModal">
        <div class="header">
            Select chemicals to add to this Cabinet
        </div>
        <div class="content">
            <div class="ui form">
                <c:forEach items="${chemicalNames}" var="chemicalName">
                    <div class="ui checkbox">
                        <input type="checkbox" name="${chemicalName}">
                        <label>${chemicalName}</label>
                    </div>
                    <div class="ui hidden divider"></div>
                </c:forEach>
            </div>
        </div> 
        <div class="actions">
            <div class="ui black deny button">
                Cancel
            </div>
            <div class="ui positive button" id="submitAddChemicalsToCabinet">
                Submit
            </div>
        </div>
    </div>

    <div class="ui basic modal confirm">
        <i class="close icon"></i>
        <div class="header">
            Are you sure?
        </div>
        <div class="description">
            <p>Are you sure you would like to remove this chemical?</p>
        </div>
        <div class="actions">
            <div class="ui red basic inverted button">
                <i class="remove icon"></i>
                    No
            </div>
            <div class="ui green ok inverted button" id="confirmRemove">
                <i class="checkmark icon"></i>
                Yes
            </div>
        </div>
    </div>

    <div class="full height">
        <div class="masthead segment bg1">
            <div class="ui container">
                <div class="introduction">
                    <h1 class="ui inverted header">
                        <span class="library">${title}</span>
                        <%--<span class="tagline">${subtitle}</span>--%>
                    </h1>
                    <div class="ui hidden divider"></div>
                </div>
            </div>
        </div>

        <div class="ui container bottom-padding">
            <div class="ui breadcrumb">
                <c:set var="sectionLink" value="" />
                <c:forEach items="${breadcrumbs}" var="crumb">
                <c:set var="sectionLink" value="${sectionLink}/${crumb}" />
                    <a class="section" href="${sectionLink}">${crumb}</a>
                    <div class="divider"> / </div>
                </c:forEach>
            </div>
            <div class="ui icon button addModal right floated" title="${addTooltip}">
                <i class="add icon"></i>
            </div>
        </div>
        <div class="ui container">
            <div class="ui four column stackable grid container">

                <c:forEach items="${chemicals}" var="chemical">
                    <div class="column">
                        <div class="ui link card" >
                            <div class="blurring dimmable image"onclick="window.location+='/${chemical.name}'">
                                <div class="ui dimmer">
                                    <div class="content">
                                        <div class="center">
                                            <div class="ui inverted button">View Details</div>
                                        </div>
                                    </div>
                                </div>
                                <%--<img src="/img/placeholder.jpg">--%>
                                <img src="${chemical.imageURL}">
                            </div>
                            <div class="content">
                                <div class="header">${chemical.name}</div>
                                <div class="description">description...</div>
                            </div>
                            <div class="extra content">
                              <a class="right floated created remove" data="${chemical.name}">
                                  Remove
                              </a>
                              <a class="" onclick="alert('todo: add ability to edit')">
                                  <i class="edit icon"></i>Edit
                                </a>
                            </div>
                        </div>
                    </div>
                </c:forEach>

            </div>
        </div>
    </div>
</body>

<div type="hidden" id="csrf" value="${_csrf.token}"></div>
<div type="hidden" id="username" value="${username}"></div>
<div type="hidden" id="addURL" value="${addURL}"></div>
<div type="hidden" id="removeURL" value="${removeURL}"></div>
<%@include file="templates/footer.jsp" %>
