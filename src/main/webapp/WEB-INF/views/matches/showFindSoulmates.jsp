<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<script type="text/javascript">
	$(document)
			.ready(
					function() {
						$('button.send-soulmates-request')
								.live(
										'click',
										function() {
											var button = $(this);
											var buttons = $(this).parents(
													'div.buttons');

											button
													.addClass('loading-animation');
											$
													.get(
															baseUrl
																	+ '/soulmates/createSoulmatesRequest.html?requestedId='
																	+ button
																			.data('personid'),
															function(data) {
																if (data == "") {
																	button
																			.hide();
																	buttons
																			.find(
																					'.sent-soulmates-request')
																			.show();
																}
															})
													.complete(
															function() {
																button
																		.removeClass('loading-animation');
															});
											;
										});

						$('button.cancel-soulmates-request')
								.live(
										'click',
										function() {
											var button = $(this);
											var buttons = $(this).parents(
													'div.buttons');

											button
													.addClass('loading-animation');
											$
													.get(
															baseUrl
																	+ '/soulmates/deleteSoulmates.html?personBId='
																	+ button
																			.data('personid'),
															function(data) {
																if (data == "") {
																	buttons
																			.find(
																					'.sent-soulmates-request')
																			.hide();
																	buttons
																			.find(
																					'.send-soulmates-request')
																			.show();
																}
															})
													.complete(
															function() {
																button
																		.removeClass('loading-animation');
															});
											;
										});

						$('#show-more-matches')
								.click(
										function() {
											var thisButton = $(this);
											thisButton
													.addClass('loading-animation');

											var offset = $('#matches').data(
													'offset');
											var resultSize = $('#matches')
													.data('resultsize');

											$
													.get(
															baseUrl
																	+ '/matches/showMatchesList.html?&offset='
																	+ offset
																	+ '&resultSize='
																	+ resultSize,
															function(data) {
																if (($
																		.trim(data) !== "")) {
																	$(data)
																			.appendTo(
																					'#matches');
																	$(
																			'#matches')
																			.data(
																					'offset',
																					offset
																							+ resultSize);

																	if ($(data)
																			.filter(
																					'.match').length < resultSize) {
																		$(
																				'#no-more-matches')
																				.show();
																		thisButton
																				.remove();
																	}
																} else {
																	$(
																			'#no-more-matches')
																			.show();
																	thisButton
																			.remove();
																}
															})
													.complete(
															function() {
																thisButton
																		.removeClass('loading-animation')
															});
										});

						$('#search-person')
								.autoSuggest(
										baseUrl
												+ '/autocomplete/searchPeople.html',
										{
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

					});
</script>

<h2 class="content-heading">
	<spring:message code="matches.showFindSoulmates.title.page" />
</h2>

<br>
<div id="search_box2">
	<input type="text" id="search-person">
</div>

<h3 class="content-heading">
	<spring:message code="matches.showFindSoulmates.title.recommendations" />
</h3>


<div id="matches" data-offset="${matchesOffset}"
	data-resultsize="${matchesResultSize}">
	<jsp:include page="showMatchesList.jsp" />
</div>


<p></p>
	
<div id="no-more-matches" class="alert alert-danger" style="display: none;" role="alert">
<spring:message code="matches.showFindSoulmates.noMoreMatches" />
</div>

	<button class="btn btn-primary" id="show-more-matches">
		<spring:message
			code="matches.showFindSoulmates.button.showMoreMatches" />
	</button>