<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<jsp:useBean id="profileUtility" type="youapp.utility.ProfileUtility"
	scope="application" />
<jsp:useBean id="timeUtility" class="youapp.utility.TimeUtility"
	scope="application" />


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


</div>

<div class="jumbotron row">
	<div class="col-4 col-sm-4 col-lg-4">
		YouMeIBD connects you with people who have similar health condition,
		interests and motivation <br></br> <i>Sign up to learn from shared
			knowledge</i>
	</div>
	<div class="col-3 col-sm-3 col-lg-3"></div>
	<div class="col-3 col-sm-3 col-lg-3">
		<p>
			<a
				href="<c:out value='${pageContext.request.contextPath}/login/show.html'/>"><button
					type="submit" class="btn btn-success">Sign Up</button></a>
		</p>
		<p></p>
		<p>
			<a
				href="<c:out value='${pageContext.request.contextPath}/login/register.html'/>"><button
					type="submit" class="btn btn-primary">Login via Facebook</button></a>
		</p>
	</div>
</div>

<div class="container">
	<div class="row">
		<!-- Boxes de Acoes -->
		<div class="col-xs-12 col-sm-6 col-lg-4">
			<div class="box">
				<div class="icon">
					<div class="image">
						
					</div>
					<div class="info">
						<h3 class="title">Connect</h3>
						<p>The YouAPP is about creating a movement for improved health. Together, we 
						can expand the conversation and enhance communication between patients and physicians, thereby creating community and knowledge. </p>
						<div class="more">
							<a title="Title Link" href="http://improvecarenowblog.org" target="_blank">
								Read More
								<i class="fa fa-angle-double-right"></i>
							</a>
						</div>
					</div>
					</div>
			
					<div class="space"></div>
			</div>
		</div>	
				
				
		<div class="col-xs-12 col-sm-6 col-lg-4">
			<div class="box">
				<div class="icon">
					<div class="image">
						
					</div>
					<div class="info">
						<h3 class="title">Discuss</h3>
						<p>See the latest posts, activities, events, and group discussions with you soulmates.Create and answer matchmaking questions.Get new insides 
						the medications of your friends.</p>
						<div class="more">
							<a title="Title Link" href="http://improvecarenowblog.org" target="_blank">
								Read More
								<i class="fa fa-angle-double-right"></i>
							</a>
						</div>
					</div>
				
		</div>
					<div class="space"></div>
					
					</div>
					</div>
					
		<div class="col-xs-12 col-sm-6 col-lg-4">
			<div class="box">
				<div class="icon">
					<div class="image">
					
					</div>
					<div class="info">
						<h3 class="title">Meet People</h3>
						<p>Bring IBD patients with similar interets through their Facebook Networks, by 
						proposing them "people like themselves" based on their self-defined attributes and a game-like quiz with user-definable questions.</p>
						<div class="more">
							<a title="Title Link" href="http://improvecarenowblog.org" target="_blank">
								Read More
								<i class="fa fa-angle-double-right"></i>
							</a>
						</div>
					</div>
			</div>
			
					<div class="space"></div>				
		</div>		

</div>
</div>
</div>

<!-- /.panel-body -->

