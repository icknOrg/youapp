<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:useBean id="date" class="java.util.Date" />

	<ul >
		<li><a href="<c:out value='${pageContext.request.contextPath}'/>"> Home</a></li>
		<li><a href="<c:out value='${pageContext.request.contextPath}/home/about.html'/>">About</a></li>
		<li><a href="http://improvecarenowblog.org" target="_blank">Blog</a></li>
		<li><a href="http://improvecarenowblog.org/2012/12/07/youmeibd/" target="_blank">FAQ</a></li>
 		<li><a href="<c:out value='${pageContext.request.contextPath}/home/termsofuse.html'/>">Terms of Use</a></li>
	</ul>
	<span class="copyright" >© <fmt:formatDate value="${date}" pattern="yyyy" /> &nbsp;<a href="http://c3nproject.org/innovations/patient-engagement-community/youmeibd">Collaborative Chronic Care Network</a></span><br><br>
    <p class="graytext">YouMeIBD is a social health network for people to meet soulmates, share feelings and channel relevant information. <br>
     Information on our platform is voluntarily submitted by individual members and does not include medical advice.</p>
<a href="#top" id="top-link">Top ↑</a>
<!-- End Footer-->