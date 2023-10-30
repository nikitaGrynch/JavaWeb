document.addEventListener('DOMContentLoaded', function() {
    M.Modal.init(document.querySelectorAll('.modal'), {
        opacity: 0.6,
        inDuration: 200,
        outDuration: 200,
        onOpenStart: onModalOpens

    });

    const authModalSignInButton = document.getElementById("auth-modal-sign-in-button")
    if(authModalSignInButton) authModalSignInButton.addEventListener("click", signInButtonClick)

    const spaContainer = document.getElementById("spa-container")
    if(spaContainer) {
        const token = window.localStorage.getItem("token");
        if(token){
            const tokenObj = JSON.parse(atob(token));
            if(Date.parse(tokenObj.exp) < Date.now()) {
                window.localStorage.removeItem("token");
                alert("Token is expired");
                window.location.reload();
                return;
            }
            spaContainer.innerText = "Token expiration date: " + tokenObj.exp.toString();
            console.log(tokenObj);
        }
        else{
            spaContainer.innerText = "No token";
        }
    }

    const spaLogoutButton = document.getElementById("spa-btn-logout");
    if(spaLogoutButton) spaLogoutButton.addEventListener("click", spaLogoutClick);

    const spaGetInfoButton = document.getElementById("spa-btn-get-info");
    if(spaGetInfoButton) spaGetInfoButton.addEventListener("click", spaGetInfoClick);
});

function spaLogoutClick() {
    window.localStorage.removeItem("token");
    window.location.reload();
}
function spaGetInfoClick() {
    const spaContainer = document.getElementById("spa-container");
    if(!spaContainer) throw "#spa-container not found";
    fetch(`${getAppContext()}/uk/tpl/template1.html`, {
        method: "GET",
        headers: {
            'Authorization': `Bearer ${window.localStorage.getItem('token')}`
        }

    }).then(r=>r.text()).then(t => {
        spaContainer.innerHTML += t;
        console.log(window.localStorage.getItem('token'));
    });

    // fetch(`${getAppContext()}/uk/tpl/N*P.png`, {
    //     method: "GET",
    //     headers: {
    //         'Authorization': `Bearer ${window.localStorage.getItem('token')}`
    //     }
    //
    // }).then(r=>r.blob())
    //     .then(blob => {
    //         const blobUrl = URL.createObjectURL(blob);
    //         console.log(blobUrl);
    //         spaContainer.innerHTML += `<img src="${blobUrl}" alt="img" />`
    //     }).catch((err) => console.log(err));

    fetch(`${getAppContext()}/uk/tpl/NP.png`, {
        method: "GET",
        headers: {
            'Authorization': `Bearer ${window.localStorage.getItem('token')}`
        }

    }).then(r=> {
        if (r.status == 200) {
            r.blob()
                .then(blob => {
                    const blobUrl = URL.createObjectURL(blob);
                    console.log(blobUrl);
                    spaContainer.innerHTML += `<img src="${blobUrl}" alt="img" />`
                }).catch((err) => console.log(err));
        } else {
            r.text().then(t => console.log("Error: " + t));
        }
    });
}

function onModalOpens() {
    const [authLogin, authPassword, authMessage] = getModalElements();
    authLogin.value = '';
    authPassword.value = '';
    authMessage.innerText = '';
}

function getModalElements() {
    const authLogin = document.getElementById('auth-login');
    if(!authLogin) throw "#auth-login not found";
    const authPassword = document.getElementById('auth-password');
    if(!authPassword) throw "#auth-password not found";
    const authMessage = document.getElementById('auth-message-container');
    if(!authMessage) throw "#auth-message-container not found";
    return [authLogin, authPassword, authMessage];
}

function signInButtonClick() {
   const [authLogin, authPassword, authMessage] = getModalElements();

    const login = authLogin.value;
    if(login.length === 0){
        authMessage.innerText = 'Login cannot be empty';
        return;
    }
    const password = authPassword.value;
    if(password.length === 0){
        authMessage.innerText = 'Password cannot be empty';
        return;
    }

    authMessage.innerText = '';

    const url = `${getAppContext()}/auth?login=${login}&password=${password}`;
    fetch(url).then(r=> {
        if(r.status === 202){  // get token
            r.json().then(encodedToken => {
                // check the integrity of the token by decoding and the presence of jti
                try{
                    const token = JSON.parse(atob(encodedToken));
                    if(typeof token === 'undefined'){
                        authMessage.innerText = "Token integrity is violated";
                    }
                    else{
                        window.localStorage.setItem("token", encodedToken);
                    }
                    window.location.reload();
                }
                catch (e){
                    authMessage.innerText = "Incorrect data received"
                }
                console.log(encodedToken);
            })
        }
        else{
            authMessage.innerText = "Authentication denied";
        }
    })
}

function getAppContext() {
    var isContextPreset = false;
    return (isContextPreset) ? "" : '/' + window.location.pathname.split('/')[1];
}