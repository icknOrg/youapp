<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- Start Footer--> 
<div class="footer-container">
	<div class="footer">
        <div class="footer-top">
        	<div class="footer-top-column">
        		<h3>About YouMeIBD</h3>
        		<p>Our vision is to improve the life of people living with IBD by enabling a collective intelligent community.</p><br>
      		</div>   
			<div class="footer-top-column">
                <h3>Connect With Us</h3>
				<div class="social-nav">
					<div class="social-links">
						<ul >
				            <li style="padding-top:0px!important;"><a href="http://improvecarenowblog.org" target="_blank">Blog</a></li>
  				            <li><a href="http://improvecarenowblog.org/2012/12/07/youmeibd/" target="_blank">FAQ Blogspot</a></li>
						</ul>
					</div>
				</div>
            </div>
            <div class="footer-top-column last">
            	  <c:choose>
              		<c:when test='${(not empty sessionScope.isRegistered) && (sessionScope.isRegistered == true)}'>
		                <h3>Report Bugs</h3>
		                <p>We would be happy to receive bug reports (including a description how to reproduce the behaviour) as well as suggestions for improvement.</p>
		                <!-- Begin MailChimp Signup Form -->
						<div id="mc_embed_signup">
							<form id="footerForm" action="" method="post" name="mc-embedded-subscribe-form" class="validate" target="_blank">
								<fieldset>
								<div class="clear">
									<button id="mailtoBugReport" type="button" class="btn-green btn-newsletter" name="bugreportButton"> 
										Report Bug
									</button>
							    </div>
							    </fieldset>
							</form>
						</div>
              		</c:when>
        		</c:choose>
           
            </div>
            <!--End mc_embed_signup-->
        </div>
	</div>
</div>
<div class="footer-bottom">
	<ul style="float:left; padding:0px!important; margin:0px!important!;">
		<li style="padding: 0 10px 0 0px!important;"><a href="<c:out value='${pageContext.request.contextPath}'/>"> Home</a></li>
		<li><a href="<c:out value='${pageContext.request.contextPath}/home/about.html'/>">About</a></li>
		<li><a href="http://improvecarenowblog.org" target="_blank">Blog</a></li>
		<li><a href="http://improvecarenowblog.org/2012/12/07/youmeibd/" target="_blank">FAQ</a></li>
 		<li style="border-right:0px!important;"><a href="<c:out value='${pageContext.request.contextPath}/home/termsofuse.html'/>">Terms of Use</a></li>
	</ul>
	<span class="copyright" style="position:relative; top:-2px;">© 2011-2013 YouMeIBD.com. All rights reserved.</span><br><br>
    <p class="graytext">YouMeIBD is a social health network for people to meet soulmates, share feelings and channel relevant information. <br>
     Information on our platform is voluntarily submitted by individual members and does not include medical advice.</p>
</div>
<a href="#top" id="top-link">Top ↑</a>
<!-- End Footer-->