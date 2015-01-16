<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!--
<h1><spring:message code="home.showall.title"/></h1>
<h2><spring:message code="home.showall.subtitle"/></h2>
<p>
<spring:message code="home.showall.description"/>
</p>
-->

<div class="start">
<h1 style="font-size:3em!important;">This is a place for the IBD community to connect.</h1>
<h2 style="font-size:1.5em!important;">The ultimate goal of the youapp is to enrich and improve the life of people living with IBD. We connect users based on their interest, location and expertise. By doing so, we want to build up a vivid and intelligent swarm of patients to collectively find the right answers to questions that people share.</h2>

<a href="<c:out value='${pageContext.request.contextPath}/login/show.html'/>"><input type="submit" value="Connect Me to People Like Me ♥" class="btn-green btn-start"></a>
<a href="<c:out value='${pageContext.request.contextPath}/login/register.html'/>"><input type="submit" value="Login via Facebook ✓" class="btn-fb btn-start"></a>

</div>
<div class="start2"></div>

<div class="clear"></div>
<br>
<div class="gridstart">
<div class="title-yellow">Connect</div>
<div class="text">Lorem ipsum dolor sit amet, orci eu, pretium ornare, augue in. Congue eget fusce ultricies donec ut elit, ab habitant commodo cupiditate libero tristique sed, nulla urna congue purus massa metus. <a href="connect.html">Connect »</a></div>
</div>
<div class="gridstart">
<div class="title-blue">Discuss</div>
<div class="text">Lorem ipsum dolor sit amet, orci eu, pretium ornare, augue in. Congue eget fusce ultricies donec ut elit, ab habitant commodo cupiditate libero tristique sed, nulla urna congue purus massa metus.  <a href="discuss.html">Discuss »</a></div>
</div>
<div class="gridstartlast">
<div class="title-orange">Meet Up</div>
<div class="text">Meet other people in your own area or whereever you go. Bring your discussions to real life.<br> <a href="#">Meet People »</a></div>
</div>

<div class="clear"></div>
