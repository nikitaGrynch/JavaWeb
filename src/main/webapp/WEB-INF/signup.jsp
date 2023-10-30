<%@ page import="step.learning.dto.models.SignupFormModel" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String regData = (String) request.getAttribute("reg-data");
    if( regData == null ) { regData = ""; };
    SignupFormModel formModel = (SignupFormModel) request.getAttribute("reg-model");
    Map<String, String> validationErrors =
            request.getAttribute("validationErrors") == null
            ? new HashMap<String, String>()
            : (Map<String, String>) request.getAttribute("validationErrors");
    String loginClass = regData == null ? "validate" : (
            validationErrors.containsKey("login") ? "invalid" : "valid"
            );
%>

<h2>Registration</h2>
<ul>
    <%for (String key:validationErrors.keySet()){ %>
        <li><b><%=validationErrors.get(key)%></b></li>
    <%}%>
</>

<p>
    <%=request.getAttribute("culture")%>
</p>

<div class="row">
    <form class="col s12" action="" method="POST" enctype="multipart/form-data">
        <div class="row">
            <div class="input-field col s6">
                <i class="material-icons prefix">person</i>
                <input id="reg-login" name="reg-login" type="text" class="<%=loginClass%>"
                value="<%=formModel == null ? "" : formModel.getLogin()%>" >
                <label for="reg-login">Login</label>
                <%if(validationErrors.containsKey("login")) {%>
                    <span class="helper-text" data-error="<%=validationErrors.get("login")%>" data-success="right"></span>
                <%}%>
            </div>
            <div class="input-field col s6">
                <i class="material-icons prefix">badge</i>
                <input id="reg-name" name="reg-name" type="text" class="validate"
                       value="User name">
                <label for="reg-name">Real Name</label>
            </div>
        </div>
        <div class="row">
            <div class="input-field col s6">
                <i class="material-icons prefix">alternate_email</i>
                <input id="reg-email" name="reg-email" type="email" class="validate"
                value="user@gmail.com">
                <label for="reg-email">Email</label>
            </div>
            <div class="input-field col s6">
                <i class="material-icons prefix">child_friendly</i>
                <input id="reg-birthdate" name="reg-birthdate" type="date" class="validate"
                value="2001-09-11">
                <label for="reg-birthdate">Birth Date</label>
            </div>
        </div>
        <div class="row">
            <div class="input-field col s6">
                <i class="material-icons prefix">lock</i>
                <input id="reg-password" name="reg-password" type="password" class="validate"
                value="Qwerty123">
                <label for="reg-password">Enter a password</label>
            </div>
            <div class="input-field col s6">
                <i class="material-icons prefix">lock_open</i>
                <input id="reg-repeat" name="reg-repeat" type="password" class="validate"
                value="Qwerty123">
                <label for="reg-repeat">Repeat a password</label>
            </div>
        </div>
        <div class="row">
            <div class="input-field col s6">
                <label for="agree">
                    <input type="checkbox" id="agree" name="agree" class="filled-in" />
                    <span>I won't break anything</span>
                </label>
            </div>
            <div class="file-field input-field col s6">
                <div class="btn deep-purple">
                    <span><i class="material-icons">portrait</i></span>
                    <input type="file" name="reg-avatar" />
                </div>
                <div class="file-path-wrapper">
                    <label>
                        <input class="file-path validate" type="text"
                        placeholder="Avatar-image" />
                    </label>
                </div>
            </div>
        </div>
        <div class="input-field row right-align">
            <button class="waves-effect waves-light btn" type="submit"><i class="material-icons right">how_to_reg</i>Register</button>
        </div>
    </form>
</div>
