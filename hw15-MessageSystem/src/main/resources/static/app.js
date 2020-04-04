let stompClient = null;

const setConnected = (connected) => {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#chatLine").show();
        stompClient.send("/app/list", {}, {})
    }
    else {
        $("#chatLine").hide();
    }
    $("#message").html("");
}

const connect = () => {
    stompClient = Stomp.over(new SockJS('/gs-guide-websocket'));
    stompClient.connect({}, (frame) => {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/response', (message) => {
            const userList = JSON.parse(message.body)
            $("#userList").innerHTML = ""
            let newUserListContent = "";
            userList.forEach(function (createdUser) {
                const name = createdUser.name
                const age = createdUser.age
                const address = createdUser.addressDataSet.street
                newUserListContent = newUserListContent + "<tr><td>" + name + " " + age + " " + address + "</td></tr>"
            })
            document.getElementById("userList").innerHTML = newUserListContent
        })
    })
}

const disconnect = () => {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

const sendName = () => {
    const json = JSON.stringify(
        {
            'name': $("#name").val(),
            'age': $("#age").val(),
            'addressDataSet': {
                'street': $("#address").val()
            }
        })
    console.log(stompClient)
    stompClient.send("/app/create", {}, json)
}

$(function () {
    $("form").on('submit', (event) => {
        event.preventDefault();
});
    $("#connect").click(connect);
    $("#disconnect").click(disconnect);
    $("#send").click(sendName);
});
