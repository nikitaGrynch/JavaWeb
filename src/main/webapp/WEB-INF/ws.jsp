<%@ page contentType="text/html;charset=UTF-8" %>
<h1>Websocket</h1>
<div class="row">
    <div class="col s6">
        <input id="chat-input" type="text" value="Hi All" />
        <button type="button" onclick="sendMessageClick()">Send</button>
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
        const host = window.location.host + getAppContext() ;
        const ws = new WebSocket(`ws://${host}/chat`) ;
        ws.onopen = onWsOpen;
        ws.onclose = onWsClose;
        ws.onmessage = onWsMessage;
        ws.onerror = onWsError;
        window.websocket = ws ;
    }
    function onWsOpen(e) {
        window.websocket.send(
            JSON.stringify({
                command: "auth",
                data: window.localStorage.getItem("token")
            })
        )
    }
    function onWsClose(e) {
        console.log("onWsClose", e);
    }
    function onWsMessage(e) {
        // console.log("onWsMessage", e);
        const msgObj = JSON.parse(e.data);
        const li = document.createElement("li");
        li.className="collection-item";
        const div = document.createElement("div");
        const spanText = document.createElement("span");
        div.appendChild(spanText);
        spanText.innerText = msgObj.data;
        if(msgObj.hasOwnProperty("date")){
            const spanDate = document.createElement("span");
            spanDate.className = "secondary-content";
            const msgDate = new Date(msgObj.date);
            const now = new Date();
            spanDate.innerText = getMsgDateStr(msgDate, now);
            div.appendChild(spanDate);
        }
        li.appendChild(div);
        document.getElementById("chat-container").appendChild(li);
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