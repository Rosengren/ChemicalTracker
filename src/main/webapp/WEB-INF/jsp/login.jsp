<%@include file="templates/header.jsp" %>

<body id="ctrackr" class="index pusher welcome">

    <div class="full height">
        <div class="masthead segment bg1">
            <div class="ui container">
                <div class="introduction">
                    <h1 class="ui inverted header">
                        <span class="library">Chemical Tracker</span>
                        <span class="tagline">Login to your account</span>
                    </h1>
                    <div class="ui hidden divider"></div>
                    <form class="ui large form" action="/login" method="post">
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
                            <input type="submit" class="ui fluid large green button" value="Login"/>
                             <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                        </div>
                        <div class="ui error message"></div>
                    </form>
                    <div class="ui hidden divider"></div>
                    <div class="ui">
                        Don't have an existing account?
                        <a href="/signup">Sign Up</a>
                    </div>
                </div>
            </div>
           
        </div>
    </div>
    <!-- <div class="wrapper wrapper-fluid">
        <c:if test="${param.error}">
            Invalid username and password
        </c:if>
        <c:if test="${param.logout}">
            You have been logged out.
        </c:if>
        <form class="form" action="/login" method="post">
            <label>Username</label>
            <input type="text" name="username"/>
            <br/>
            <label>Password</label>
            <input type="password" name="password">
            <br/>
            <input type="submit" class="button button-green button-large" value="Sign In" />
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
        </form>
    </div> -->
</body>

<%@include file="templates/footer.jsp" %>
