<%@include file="templates/header.jsp" %>

<body id="ctrackr" class="index popup pusher storage" ontouchstart>

    <div class="ui modal">
        <div class="header">
            ${addTooltip}
        </div>
        <div class="content">
            <div class="ui form">
                <div class="field">
                    <label>Name</label>
                    <input type="text" id="newStorageName" placeholder="Name" />
                </div>
                <div class="field">
                    <label>Description</label>
                    <textarea rows="4" id="newStorageDesc"></textarea>
                </div>
            </div>
        </div> 
        <div class="actions">
            <div class="ui black deny button">
                Cancel
            </div>
            <div class="ui positive button" id="submitCreateStorage">
                Submit
            </div>
        </div>
    </div>

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
            <div class="ui four column stackable grid container">

                <c:forEach items="${storages}" var="storage">
                    <div class="column">
                        <div class="ui link card" >
                            <div class="blurring dimmable image"onclick="window.location+='/${storage.name}'">
                                <div class="ui dimmer">
                                    <div class="content">
                                        <div class="center">
                                            <div class="ui inverted button">View Details</div>
                                        </div>
                                    </div>
                                </div>
                                <img src="${storage.imageURL}">
                            </div>
                            <div class="content">
                                <div class="header">${storage.name}</div>
                                <div class="description">${storage.description}</div>
                            </div>
                            <div class="extra content">
                                <a class="right floated created" href="/report/generate/${storage.name}"">
                                  Generate Report
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
<%@include file="templates/footer.jsp" %>
