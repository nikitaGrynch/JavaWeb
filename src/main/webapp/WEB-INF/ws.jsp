<%@ page contentType="text/html;charset=UTF-8" %>
<h1>Websocket</h1>
<div class="row">
    <div class="col s6" id="chat-block">
        <b id="chat-nik">Log in...</b><br />
        <span style="font-size: x-small; color:rgb(128,128,128)" id="chat-token"></span>
        <input id="chat-input" disabled type="text" value="Hi All" />
        <button id="chat-send" disabled type="button" onclick="sendMessageClick()">Send</button>
        <ul class="collection" id="chat-container"></ul>
    </div>
    <div class="col s6"></div>
</div>
<script>
    document.addEventListener('DOMContentLoaded', initWebsocket);
    function sendMessageClick() {
        window.websocket.send(
            JSON.stringify({
                command: "chat",
                data: document.getElementById("chat-input").value
            })
        );
    }
    function initWebsocket() {
        const token = JSON.parse(atob(window.localStorage.getItem("token")));
        const exp = new Date(token.exp /* + ' UTC' */);
        document.getElementById("chat-token").innerText = "Token expires at " + dateString(exp);
        const host = window.location.host + getAppContext() ;
        const ws = new WebSocket(`ws://${host}/chat`) ;
        ws.onopen = onWsOpen;
        ws.onclose = onWsClose;
        ws.onmessage = onWsMessage;
        ws.onerror = onWsError;
        window.websocket = ws ;
    }

    function dateString(date){
        if(date.getDate() == new Date().getDate()){
            return `${('0' + date.getHours()).slice(-2)}:${('0' + date.getMinutes()).slice(-2)}`;
        }
        return `${('0' + date.getDate()).slice(-2)}.${('0' + (date.getMonth() + 1)).slice(-2)}.${date.getFullYear()} ${('0' + date.getHours()).slice(-2)}:${('0' + date.getMinutes()).slice(-2)}`;

    }

    function wsSend(command, data) {
        window.websocket.send(JSON.stringify({
            command: command,
            data: data
        }));
    }

    function onWsOpen(e) {
        wsSend("auth", window.localStorage.getItem("token"));
        // window.websocket.send(
        //     JSON.stringify({
        //         command: "auth",
        //         data: window.localStorage.getItem("token")
        //     })
        // )
    }
    function onWsClose(e) {
        console.log("onWsClose", e);
    }
    function onWsMessage(e) {

        // console.log("onWsMessage", e);
        const msgObj = JSON.parse(e.data);

        switch (msgObj.status){
            case 200: // load - last messages array
                loadChatMessages(msgObj.data)
                break;
            case 201: // broadcast
                appendChatMessage(msgObj.data, msgObj.date)
                break;
            case 202: // token accepted, .data == nik
                wsSend("load", "")
                enableChat(msgObj.data);
                break;
            case 403: // token rejected
                disableChat();
                break;
            case 405: // command unrecognized
                break;
        }


    }

    function loadChatMessages(arr){
        // console.log(arr);
        arr.forEach(appendChatObject);
    }

    function appendChatObject(obj){
        appendChatMessage(`${obj.senderNik}: ${obj.message}`, obj.moment)
    }

    function appendChatMessage(msg, date) {
        const li = document.createElement("li");
        li.className = "collection-item";
        const div = document.createElement("div");
        const spanText = document.createElement("span");
        div.appendChild(spanText);
        spanText.innerText = msg;
        const spanDate = document.createElement("span");
        spanDate.className = "secondary-content";
        const msgDate = new Date(date);
        const now = new Date();
        spanDate.innerText = getMsgDateStr(msgDate, now);
        div.appendChild(spanDate);

        li.appendChild(div);
        document.getElementById("chat-container").appendChild(li);
        const token = JSON.parse(atob(window.localStorage.getItem("token")));
        const exp = new Date(token.exp);
        document.getElementById("chat-token").innerText = "Token expires at " + dateString(exp);
    }

    function enableChat(nik) {
        const li = document.createElement("li");
        for(let child of document.getElementById("chat-block").children){
            child.disabled = false;
        }
        // li.className = "collection-item";
        // li.innerText = nik;
        document.getElementById("chat-nik").innerText = nik;
        wsSend("join", "");
        // appendChatMessage(nik + " joined", new Date());
        // document.getElementById("chat-input").disabled = false;
        // document.getElementById("chat-send").disabled = false;
        //appendChatMessage(nik + " joined", new Date())
    }

    function disableChat() {
        document.getElementById("chat-nik").innerText = "OFF";
        for(let child of document.getElementById("chat-block").children){
            child.disabled = true;
        }
        // document.getElementById("chat-input").disabled = true;
        // document.getElementById("chat-send").disabled = true;
    }

    function onWsError(e) {
        console.log("onWsError", e);
    }
    function getAppContext() {
        var isContextPreset = false ;
        return isContextPreset ? "" : '/' + window.location.pathname.split('/')[1] ;
    }

    function getMsgDateStr(msgDate, nowDate){
        let msgDateStr;
        if(msgDate.getDate() === nowDate.getDate()){
            msgDateStr = "Today, "
            msgDateStr += `${('0' + msgDate.getHours()).slice(-2)}:${('0' + msgDate.getMinutes()).slice(-2)}`;
        }
        else{
            const diffTime = Math.abs(nowDate - msgDate);
            const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
            if(diffDays === 1){
                msgDateStr = `Yesterday, ${('0' + msgDate.getHours()).slice(-2)}:${('0' + msgDate.getMinutes()).slice(-2)}`;

            }
            else if (diffDays >= 2 && diffDays <= 3){
                msgDateStr = diffDays + " days ago";
            }
            else{
                msgDateStr = `${('0' + msgDate.getDate()).slice(-2)}.${('0' + (msgDate.getMonth() + 1)).slice(-2)}.${msgDate.getFullYear()} ${('0' + msgDate.getHours()).slice(-2)}:${('0' + msgDate.getMinutes()).slice(-2)}`;
            }
        }
        return msgDateStr;
    }
</script>