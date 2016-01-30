<%@include file="templates/header.jsp" %>

<body id="ctrackr" class="index pusher welcome">

    <div class="full height">
        <div class="masthead segment bg1">
            <div class="ui container">
                <div class="introduction">
                    <h1 class="ui inverted header">
                        <span class="library">Chemical Tracker</span>
                        <span class="tagline">Sign Up!</span>
                    </h1>
                    <div class="ui hidden divider"></div>
                    <form class="ui large form" action="/signup" method="post" commandName="userForm">
                        <div class="ui stacked">
                            <div class="field">
                                <div class="ui left icon input">
                                    <i class="user icon"></i>
                                    <input type="text" name="username" placeholder="Username"/>
                                </div>
                            </div>
                            <div class="field">
                                <div class="ui left icon input">
                                    <i class="lock icon"></i>
                                    <input type="password" name="password" placeholder="Password"/>
                                </div>
                            </div>
                            <input type="submit" class="ui fluid large green button" value="Sign Up"/>
                             <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                        </div>
                        <div class="ui error message"></div>
                    </form>
                    <div class="ui hidden divider"></div>
                </div>
            </div>
        </div>
    </div>
</body>

<%@include file="templates/footer.jsp" %>

