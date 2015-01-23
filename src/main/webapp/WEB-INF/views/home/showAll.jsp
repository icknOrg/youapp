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
					type="submit" class="btn btn-success">Login via Facebook</button></a>
		</p>
	</div>
</div>


<div class="panel-heading">
	 Timeline
</div>
<!-- /.panel-heading -->
<div class="panel-body">
	<ul class="timeline">
		<li>
			<div class="timeline-badge">
				
			</div>
			<div class="timeline-panel">
				<div class="timeline-heading">
					<h4 class="timeline-title">Lorem ipsum dolor</h4>
					<p>
						<small class="text-muted"> 11
							hours ago via Twitter</small>
					</p>
				</div>
				<div class="timeline-body">
					<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit.
						Libero laboriosam dolor perspiciatis omnis exercitationem. Beatae,
						officia pariatur? Est cum veniam excepturi. Maiores praesentium,
						porro voluptas suscipit facere rem dicta, debitis.</p>
				</div>
			</div>
		</li>
		<li class="timeline-inverted">
			<div class="timeline-badge warning">
				
			</div>
			<div class="timeline-panel">
				<div class="timeline-heading">
					<h4 class="timeline-title">Lorem ipsum dolor</h4>
				</div>
				<div class="timeline-body">
					<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit.
						Autem dolorem quibusdam, tenetur commodi provident cumque magni
						voluptatem libero, quis rerum. Fugiat esse debitis optio, tempore.
						Animi officiis alias, officia repellendus.</p>
					<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit.
						Laudantium maiores odit qui est tempora eos, nostrum provident
						explicabo dignissimos debitis vel! Adipisci eius voluptates, ad
						aut recusandae minus eaque facere.</p>
				</div>
			</div>
		</li>
		<li>
			<div class="timeline-badge danger">
				<i class="fa fa-bomb"></i>
			</div>
			<div class="timeline-panel">
				<div class="timeline-heading">
					<h4 class="timeline-title">Lorem ipsum dolor</h4>
				</div>
				<div class="timeline-body">
					<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit.
						Repellendus numquam facilis enim eaque, tenetur nam id qui vel
						velit similique nihil iure molestias aliquam, voluptatem totam
						quaerat, magni commodi quisquam.</p>
				</div>
			</div>
		</li>
		<li class="timeline-inverted">
			<div class="timeline-panel">
				<div class="timeline-heading">
					<h4 class="timeline-title">Lorem ipsum dolor</h4>
				</div>
				<div class="timeline-body">
					<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit.
						Voluptates est quaerat asperiores sapiente, eligendi, nihil.
						Itaque quos, alias sapiente rerum quas odit! Aperiam officiis
						quidem delectus libero, omnis ut debitis!</p>
				</div>
			</div>
		</li>
		<li>
			<div class="timeline-badge info">
				<i class="fa fa-save"></i>
			</div>
			<div class="timeline-panel">
				<div class="timeline-heading">
					<h4 class="timeline-title">Lorem ipsum dolor</h4>
				</div>
				<div class="timeline-body">
					<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit.
						Nobis minus modi quam ipsum alias at est molestiae excepturi
						delectus nesciunt, quibusdam debitis amet, beatae consequuntur
						impedit nulla qui! Laborum, atque.</p>
					<hr>
					<div class="btn-group">
						<button type="button"
							class="btn btn-primary btn-sm dropdown-toggle"
							data-toggle="dropdown">
							<i class="fa fa-gear"></i> <span class="caret"></span>
						</button>
						<ul class="dropdown-menu" role="menu">
							<li><a href="#">Action</a></li>
							<li><a href="#">Another action</a></li>
							<li><a href="#">Something else here</a></li>
							<li class="divider"></li>
							<li><a href="#">Separated link</a></li>
						</ul>
					</div>
				</div>
			</div>
		</li>
		<li>
			<div class="timeline-panel">
				<div class="timeline-heading">
					<h4 class="timeline-title">Lorem ipsum dolor</h4>
				</div>
				<div class="timeline-body">
					<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit.
						Sequi fuga odio quibusdam. Iure expedita, incidunt unde quis nam!
						Quod, quisquam. Officia quam qui adipisci quas consequuntur
						nostrum sequi. Consequuntur, commodi.</p>
				</div>
			</div>
		</li>
		<li class="timeline-inverted">
			<div class="timeline-badge success">
				<i class="fa fa-graduation-cap"></i>
			</div>
			<div class="timeline-panel">
				<div class="timeline-heading">
					<h4 class="timeline-title">Lorem ipsum dolor</h4>
				</div>
				<div class="timeline-body">
					<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit.
						Deserunt obcaecati, quaerat tempore officia voluptas debitis
						consectetur culpa amet, accusamus dolorum fugiat, animi dicta
						aperiam, enim incidunt quisquam maxime neque eaque.</p>
				</div>
			</div>
		</li>
	</ul>
</div>
<!-- /.panel-body -->

