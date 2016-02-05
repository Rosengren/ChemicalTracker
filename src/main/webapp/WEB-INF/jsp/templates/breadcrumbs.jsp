<div class="ui breadcrumb">
    <c:set var="sectionLink" value="" />
    <c:forEach items="${breadcrumbs}" var="crumb">
    <c:set var="sectionLink" value="${sectionLink}/${crumb}" />
        <a class="section" href="${sectionLink}">${crumb}</a>
        <i class="right angle icon divider"></i>
    </c:forEach>
</div>