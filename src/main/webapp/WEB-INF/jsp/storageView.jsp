<%@include file="templates/header.jsp" %>

<body id="ctrackr" class="index popup pusher storage">

    <div class="full height">
        <div class="masthead segment bg1">
            <div class="ui container">
                <div class="introduction">
                    <h1 class="ui inverted header">
                        <span class="library">${title}</span>
                        <span class="tagline">${subtitle}</span>
                        <%--<span class="tagline">List of all the cabinets in room [add room here]</span>--%>
                    </h1>
                    <div class="ui hidden divider"></div>
                </div>
            </div>
        </div>
        <div class="ui container bottom-padding">
            <div class="ui icon button" title="Add new cabinet">
                <i class="add icon"></i>
            </div>
        </div>
        <div class="ui four column stackable grid container">

            <c:forEach begin="0" end="10" varStatus="loop">
                <div class="column">
                    <div class="ui link card" onclick="window.location='${linkToGoTo}'">
                        <div class="blurring dimmable image">
                            <div class="ui dimmer">
                                <div class="content">
                                    <div class="center">
                                        <div class="ui inverted button">View Details</div>
                                    </div>
                                </div>
                            </div>
                            <img src="img/placeholder.jpg">
                        </div>
                        <div class="content">
                            <div class="header">Title</div>
                            <div class="description">One or two sentence description that may go to several lines</div>
                        </div>
                        <div class="extra content">
                          <a class="right floated created">Arbitrary</a>
                          <a class="friends">
                            Arbitrary</a>
                        </div>
                    </div>
                </div>
            </c:forEach>

        </div>
    </div>
</body>

<%@include file="templates/footer.jsp" %>
