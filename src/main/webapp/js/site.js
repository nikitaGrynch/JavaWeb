 // db.jsp
 const createButton = document.getElementById("db-create-button");
if(createButton) createButton.addEventListener("click", createButtonClick);
const callMeButton = document.getElementById("db-call-me-button");
    if(callMeButton) callMeButton.addEventListener("click", callMeButtonClick);
    const getAllButton = document.getElementById("db-get-all-button");
    if(getAllButton) getAllButton.addEventListener("click", getAllButtonClick);
const showAll = document.getElementById("db-get-all-with-deleted-button");
if(showAll){
    showAll.addEventListener("click", getAllWithDeletedButtonClick);
    showAll.disabled = true;
}
function getAllButtonClick() {
    fetch(window.location.href, {
        method: "LINK"
    }).then(r => r.json()).then(showCalls).then(() => showAll.disabled = false);
}

 function getAllWithDeletedButtonClick() {
     fetch(window.location.href, {
         method: "UNLINK"
     }).then(r => r.json()).then(showCalls);
 }
function showCalls( json ) {
    const container = document.getElementById("db-get-all-container");
    if( ! container ) throw "#db-get-all-container not found" ;
    const table = document.createElement('table');
    table.classList.add('striped');
    const thead = document.createElement('thead');
    const tr = document.createElement('tr');
    const th1 = document.createElement('th');
    th1.innerText = 'id';
    const th2 = document.createElement('th');
    th2.innerText = 'name';
    const th3 = document.createElement('th');
    th3.innerText = 'phone';
    const th4 = document.createElement('th');
    th4.innerText = 'call';
    const th5 = document.createElement('th');
    th5.innerText = 'Del';
    tr.appendChild(th1);
    tr.appendChild(th2);
    tr.appendChild(th3);
    tr.appendChild(th4);
    tr.appendChild(th5);
    thead.appendChild(tr);
    table.appendChild(thead);
    const tbody = document.createElement('tbody');
    json.forEach(call => {
        const tr = document.createElement('tr');
        const td1 = document.createElement('td');
        td1.innerText = call.id;
        const td2 = document.createElement('td');
        td2.innerText = call.name;
        const td3 = document.createElement('td');
        td3.innerText = call.phone;
        tr.appendChild(td1);
        tr.appendChild(td2);
        tr.appendChild(td3);
        // callMoment
        const td4 = document.createElement('td');
        if( typeof call.callMoment == 'undefined' || call.callMoment == null ) {
            // кнопка "подзвонити"
            const btn = document.createElement('button');
            btn.classList.add('btn');
            btn.classList.add('waves-effect');
            btn.classList.add('waves-light');
            btn.classList.add('deep-orange');
            if(typeof call.deleteMoment == "undefined" || call.deleteMoment == null){
                btn.addEventListener('click', makeCallClick);
                btn.appendChild(document.createTextNode("call"));
            }
            else{
                btn.addEventListener('click', restoreClick);
                btn.appendChild(document.createTextNode("restore"));
            }
            btn.setAttribute( 'data-call-id', call.id ) ;
            td4.appendChild(btn);
        }
        else {
            // show date
            td4.appendChild(document.createTextNode(call.callMoment));
        }
        tr.appendChild(td4);
        // Delete button
        const td5 = document.createElement('td');
        if(typeof call.deleteMoment == "undefined" || call.deleteMoment == null) {
            const btn5 = document.createElement('button');
            btn5.appendChild(document.createTextNode("X"));
            btn5.classList.add('btn');
            btn5.classList.add('white-text');
            btn5.classList.add('deep-purple');
            btn5.addEventListener('click', deleteClick);
            btn5.setAttribute('data-call-id', call.id);
            td5.appendChild(btn5);
        }
        else{
            td5.appendChild(document.createTextNode(call.deleteMoment))
        }
        tr.appendChild(td5);

        tbody.appendChild(tr);
    });
    table.appendChild(tbody);
    container.innerHTML = "";
    container.appendChild( table ) ;
}

function deleteClick(e){
    const  callId = e.target.getAttribute('data-call-id');
    if(confirm("Delete order " + callId)){
        fetch(window.location.href + "?call-id=" + callId, {
            method: "DELETE"
        }).then(r => {
            if(r.status === 204) {
                const tr = e.target  // button
                    .parentNode   // td
                    .parentNode;  // tr
                tr.parentNode.removeChild(tr);

                // e.target.parentNode.parentNode.remove();
            }
        })
    }
}

function makeCallClick(e){
    const  callId = e.target.getAttribute('data-call-id');
    if(confirm("Make call to order " + callId)){
        console.log(callId)
        fetch(window.location.href + "?call-id=" + callId, {
            method: "CALL"
        }).then(r => r.json()).then(j => {
            if(typeof j.callMoment == 'undefined') {
                alert(j);
            }
            else {
                e.target.parentNode.innerHTML = j.callMoment;
            }
            M.toast({html: `Your order #${j.id}`});
        })
    }
}

function restoreClick(e) {
    const  callId = e.target.getAttribute('data-call-id');
    fetch(window.location.href + "?call-id=" + callId, {
        method: "RESTORE"
    }).then(r => r.json()).then(j => {
        getAllWithDeletedButtonClick()
    })
}

function callMeButtonClick() {
    const nameInput = document.getElementById("db-call-me-name");
    if(!nameInput) throw "nameInput not found";
    if(!nameInput.value){
        M.toast({html: "Name is required"});
        return;
    }
    const phoneInput = document.getElementById("db-call-me-phone");
    if(!phoneInput) throw "phoneInput not found";
    if(!phoneInput.value){
        M.toast({html: "Phone is required"});
        return;
    }
    fetch(window.location.href, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(({
            name: nameInput.value,
            phone: phoneInput.value
        }))
    }).then(r => r.json()).then(j => {
        console.log(j);
    })
}

function createButtonClick() {
    fetch(window.location.href, {
        method: "POST"
    }).then(r => r.json()).then(j => {
        console.log(j);
        if(j.status === "error"){
            const errorBlock = document.getElementById("error-block");
            errorBlock.hidden = false;
            const  errorText = document.getElementById("error-text");
            errorText.innerText = j.message;
        }
        else if(j.status === "success"){
            const successIcon = document.getElementById("success-icon");
            successIcon.hidden = false;
        }
    })
}