$(document).ready(function() {
	var apiKey = '3106852';
	var sessionId = '2d0800d33d4d1f7cd70c71dacdb828bef5c8ee4a';
	var token = 'devtoken';	
	var publisher;
	var session = TB.initSession(sessionId);
	
	// Set up event listeners
	session.addEventListener('sessionConnected', sessionConnectedHandler);
	session.addEventListener('streamCreated', streamCreatedHandler);
	session.connect(apiKey, token);
	
	// On session connect handler
	function sessionConnectedHandler(event) {
		var div = document.createElement('div');
		div.setAttribute('id', 'publisher');
		
		document.getElementById('publisher').appendChild(div);
		
		publisher = session.publish(div.id);
	}
	
	// On stream created handler
	function streamCreatedHandler(event) {
		for (var i = 0; i < event.streams.length; i++) {
			// Enable the identify button as soon as the publisher hits 'Allow'
			if (event.streams[i].connection.connectionId == session.connection.connectionId) {
				$("#identifyButton").css("display", "block");
			}
		}
	}
});