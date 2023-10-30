<%@ page contentType="text/html;charset=UTF-8" %>
<h1>Websocket</h1>
<div class="row">
    <div class="col s3">
        <input id="chat-input" type="text" value="Hi All" />
        <button type="button" onclick="sendMessageClick()">Send</button>
        <ul class="collection" id="chat-container"></ul>
    </div>
    <div class="col s9"></div>
</div>
<script>
    document.addEventListener('DOMContentLoaded', initWebsocket);
    function sendMessageClick() {
        window.websocket.send(
            document.getElementById("chat-input").value
        );
    }
    function initWebsocket() {
        const host = window.location.host + getAppContext() ;
        const ws = new WebSocket(`ws://${host}/chat`) ;
        ws.onopen = onWsOpen;
        ws.onclose = onWsClose;
        ws.onmessage = onWsMessage;
        ws.onerror = onWsError;
        window.websocket = ws ;
    }
    function onWsOpen(e) {
        console.log("onWsOpen", e);
    }
    function onWsClose(e) {
        console.log("onWsClose", e);
    }
    function onWsMessage(e) {
        // console.log("onWsMessage", e);
        const li = document.createElement("li");
        li.className="collection-item";
        li.appendChild(document.createTextNode(e.data));
        document.getElementById("chat-container").appendChild(li);
    }
    function onWsError(e) {
        console.log("onWsError", e);
    }
    function getAppContext() {
        var isContextPreset = false ;
        return isContextPreset ? "" : '/' + window.location.pathname.split('/')[1] ;
    }
</script>