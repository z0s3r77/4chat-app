const stompClient = new StompJs.Client({
	brokerURL: 'ws://localhost:8080/gs-guide-websocket'
});

stompClient.onConnect = (frame) => {
	setConnected(true);
	console.log('Connected: ' + frame);

	// Suscribirse al chat
	stompClient.subscribe('/topic/chat/1', (messageOutput) => {
		console.log('Mensaje recibido: ', messageOutput.body);
		showGreeting(JSON.parse(messageOutput.body).name);
	});
};

stompClient.onWebSocketError = (error) => {
	console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
	console.error('Broker reported error: ' + frame.headers['message']);
	console.error('Additional details: ' + frame.body);
};

function setConnected(connected) {
	console.log("Conectanod");
	$("#connect").prop("disabled", connected);
	$("#disconnect").prop("disabled", !connected);
	if (connected) {
		$("#conversation").show();
	}
	else {
		$("#conversation").hide();
	}
	$("#greetings").html("");
}

function connect() {
	try {
		stompClient.activate();
	} catch (error) {
		console.error('Error al activar el cliente Stomp: ', error);
	}
}

function disconnect() {
	stompClient.deactivate();
	setConnected(false);
	console.log("Disconnected");
}

function sendMessage() {
	stompClient.publish({
		destination: "/app/hello",
		body: JSON.stringify({'name': $("#sender").val() +": " + $("#message").val()})
	});
}

function showGreeting(message) {
	console.log("Mensaje recibido: ", message);
	$("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
	$("form").on('submit', (e) => {
		e.preventDefault();
	});

	$("#connect").click(() => connect());
	$("#disconnect").click(() => disconnect());
	$("#send").click(() => sendMessage());
});