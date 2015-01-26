<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<jsp:useBean id="profileUtility" type="youapp.utility.ProfileUtility"
	scope="application" />
<jsp:useBean id="timeUtility" class="youapp.utility.TimeUtility"
	scope="application" />

<script>
	$(document)
			.ready(
					function() {
						$('#search-person')
								.autoSuggest(
										baseUrl
												+ '/autocomplete/searchPeople.html',
										{
											retrieveLimit : 5,
											queryParam : 'name',
											selectedItemProp : 'name',
											selectedValuesProp : 'id',
											searchObjProps : 'name',
											minChars : 2,
											startText : 'Search Person',
											emptyText : messages['messaging.sendMessage.conversationParticipantsAutocomplete.emptyText'],
											asHtmlID : 'conversationparticipants',
											formatList : function(data, elem) {
												return elem
														.html('<img src="' + data.thumbnailUrl + '" />'
																+ data.name);
											},
											selectionLimit : 1,
											resultClick : function(data) {
												window.location.href = baseUrl
														+ '/profile/show.html?personId='
														+ data.attributes.id;
											}
										});
						$('#messaging-send-this')
								.click(
										function() {
											var thisButton = $(this);
											thisButton
													.addClass('loading-animation');

											$
													.post(
															baseUrl
																	+ '/messaging/createMessage.html',
															{
																conversationParticipants : $(
																		'#messaging-send #as-values-conversationparticipants')
																		.val(),
																text : $(
																		'#messaging-send #text')
																		.val()
															},
															function(data) {
																$('#text').val(
																		'');
																thisButton
																		.attr(
																				'disabled',
																				'disabled');
															})
													.complete(
															function() {
																thisButton
																		.removeClass('loading-animation');
																location
																		.reload();
															});

										});

					});
</script>


<h2>
	<spring:message code="messaging.showConversation.title.page" />
</h2>

<div id="messaging-send">
	<div class="form-group">
		<input class="form-control" type="text" id="conversationParticipants"
			name="conversationParticipants" />
	</div>
	<div class="form-group">
		<spring:message code="messaging.sendMessage.text"
			var="placeholderText" />
		<textarea id="text" name="text" class="form-control"
			placeholder="${placeholderText}"><c:out value="${text}" /></textarea>
	</div>
	<button id="messaging-send-this" class="btn btn-primary">

		<spring:message code="messaging.sendMessage.button.send" />
	</button>
</div>

<br>
<br>
<h3 class="content-heading">
	<spring:message code="messaging.showMyConversations.title.page" />
</h3>
<div id="conversations">

	<c:forEach items="${conversations}" var="conversation">
		<div class="panel panel-danger color-grey show-conversation">
			<div class="panel-heading">
				<a
					href="${pageContext.request.contextPath}/messaging/showConversation.html?conversationGroupId=${conversation.conversationGroupId}"
					class="conversation ${conversation.newMessages ? 'new-messages' : ''}">
		
				<span class="conversation-participants"> <c:forEach
						items="${conversation.conversationMembers}"
						var="conversationMember" varStatus="conversationMembersStatus">
						<c:choose>
							<c:when test="${conversationMember ne ownPersonId}">
								<c:out
									value="${profileUtility.getProfileName(conversationParticipantsMap[conversationMember], isConversationParticipantPublicMap[conversationMember])}" />
							</c:when>
							<c:otherwise>
								<span class="sender"><spring:message
										code="messaging.showMyConversations.sender.me" /></span>
							</c:otherwise>
						</c:choose>
						<c:if test="${!conversationMembersStatus.last}">
							<c:out value=", " />
						</c:if>
					</c:forEach>
				</span>
				</a>
			</div>
			<div class="panel-body">

				<div class="row">
					<div class="col-xs-12 col-sm-6 col-md-8">
						<i><c:out
								value="${timeUtility.getElapsedTime(conversation.conversationMessages[0].timestamp, pageContext.response.locale)}" />:</i>
						<c:choose>
							<c:when
								test="${fn:length(conversation.conversationMessages[0].text)>300}">
								<c:out
									value="${fn:substring(conversation.conversationMessages[0].text, 0, 300)}" />...
										</c:when>
							<c:otherwise>
								<c:out value="${conversation.conversationMessages[0].text}" />
							</c:otherwise>
						</c:choose>
					</div>
					<!-- Pictures -->

					<div class="col-xs-12 col-md-2"
						style="float: right; margin-top: -40px; text-align: right;">
						<c:choose>

							<c:when test="${fn:length(conversation.conversationMembers)>4}">

								<c:forEach items="${conversation.conversationMembers}"
									var="conversationMember" varStatus="conversationMembersStatus"
									end="2">

									<img class="img-thumbnail"
										src="${profileUtility.getProfileThumbnailUrl(conversationMember)}">

								</c:forEach>
								<c:out value="..."></c:out>
							</c:when>
							<c:otherwise>
								<c:forEach items="${conversation.conversationMembers}"
									var="conversationMember" varStatus="conversationMembersStatus">

									<img class="img-thumbnail"
										src="${profileUtility.getProfileThumbnailUrl(conversationMember)}">

								</c:forEach>

							</c:otherwise>

						</c:choose>
					</div>
				</div>

			</div>



			<div class="panel-footer">
				<a
					href="${pageContext.request.contextPath}/messaging/showConversation.html?conversationGroupId=${conversation.conversationGroupId}"
					class="conversation ${conversation.newMessages ? 'new-messages' : ''}">
		
					<spring:message code="messaging.showMyConversations.read" />
				
			</a>
			</div>
		</div>

	</c:forEach>
</div>