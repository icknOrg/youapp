<%@page contentType="text/javascript" pageEncoding="UTF-8"
%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
var messages = new Array();
<c:forEach var="key" items="${keys}">messages["<spring:message text='${key}' javaScriptEscape='true'/>"] = "<spring:message code='${key}' javaScriptEscape='true' />";
</c:forEach>

var baseUrl = "${pageContext.request.contextPath}";