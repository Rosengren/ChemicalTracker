<%@include file="templates/header.jsp" %>
<%@include file="templates/menu.jsp" %>

<body id="ctrackr" class="index popup storage" ontouchstart>
    <%@include file="templates/sidebar.jsp" %>
    <div class="ui modal addStorageModal">
        <div class="header">
            ${addTooltip}
        </div>
        <div class="content">

            <form id="storageForm" class="ui form" method="POST" action="${formLink}" enctype="multipart/form-data">
                <div class="field">
                    <label>Photo</label>
                    <input type="file" name="Image">
                </div>
                <div class="field">
                    <label>Name</label>
                    <input type="text" name="Name">
                </div>
                <div class="field">
                    <label>Description</label>
                    <input type="text" name="Description">
                </div>
                <input type="hidden" name="Location">
                <input type="hidden" name="ParentID" value="${parentID}">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
            </form>


            <div class="actions">
                <div class="ui black deny button">
                    Cancel
                </div>
                <div class="ui positive button" id="submitCreateStorage">
                    Submit
                </div>
            </div>
        </div>
    </div>

    <div class="ui modal editStorageModal">
        <div class="header">
            Edit the storage Item
        </div>
        <div class="content">

        <form id="editStorageForm" class="ui form" method="POST" action="" enctype="multipart/form-data">
            <div class="field">
                <label>Photo</label>
                <input type="file" name="Image">
            </div>
            <div class="field">
                <label>Name</label>
                <input type="text" name="Name">
            </div>
            <div class="field">
                <label>Description</label>
                <input type="text" name="Description">
            </div>
            <input type="hidden" name="Location">
            <%--<input type="hidden" name="ParentID" value="${parentID}">--%>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
        </form>
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

    <div class="pusher">
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
            <div class="ui container">
                <div id="formSubmissionMsg" class="ui hidden positive message bottom-padding">
                    <i class="close icon"></i>
                    <div class="header"></div>
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
                <div class="ui four column stackable centered grid">
                    <c:if test="${empty storages}">
                        <div class="ui center aligned grid container">

                            <div class="column">
                                <h2 class="ui header">
                                    No elements in this location
                              </h2>
                              <h2 class="storage-message">Click the 
                                <div class="ui icon button addModal center" title="${addTooltip}">
                                    <i class="add icon"></i>
                                </div>
                                above to add a new location
                              </h2>
                            </div>
                        </div>
                    </c:if>
                    <c:forEach items="${storages}" var="storage">
                        <div class="column">
                            <div class="ui link centered card" >
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
                                    <a class="right floated created" href="/report/generate/${storage.name}">
                                      Generate Report
                                    </a>

                                    <div class="ui dropdown">
                                      <div class="text">Options</div>
                                      <i class="dropdown icon"></i>
                                      <div class="menu">
                                        <div class="item remove">Remove</div>
                                        <div class="item" id="editStorage">Edit</div>
                                      </div>
                                    </div>
                                  <!-- <a id="editStorage">
                                      <i class="edit icon"></i>Edit
                                    </a> -->
                                </div>
                            </div>
                        </div>
                    </c:forEach>

                </div>
            </div>
        </div>
    </div>
</body>

<div type="hidden" id="csrf" value="${_csrf.token}"></div>
<div type="hidden" id="username" value="${username}"></div>
<div type="hidden" id="addURL" value="${addURL}"></div>
<%@include file="templates/footer.jsp" %>
