<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<h2 style="border-bottom: 0px dotted #CCC; padding-left:190px;" class="content-heading"><spring:message code="statusupdate.createLoginStatusUpdate.title.howAreYou"/></h2>
<form>
  <div id="smileys-buttons">
      <input type="radio" name="radio" id="radio1" value="5"><label for="radio1"><img src="${pageContext.request.contextPath}/img/great.gif" class="emoticon"></label>
      <input type="radio" name="radio" id="radio2" value="0"><label for="radio2"><img src="${pageContext.request.contextPath}/img/ok.gif" class="emoticon"></label>
      <input type="radio" name="radio" id="radio3" value="-5"><label for="radio3"><img src="${pageContext.request.contextPath}/img/sad.gif" class="emoticon"></label>
  </div>
  
  <spring:message code="statusupdate.createLoginStatusUpdate.message" var="message"/>
  <textarea placeholder="${message}"></textarea>
</form>