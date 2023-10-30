<%@ page import="java.util.Date" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html >
<%
  String pageBody = (String) request.getAttribute("page-body");
  String context = request.getContextPath();
  String contextCulture = context + "/" + request.getAttribute("culture");
  long time = new Date().getTime();
%>
<html>
<head>
  <title>Java web</title>
  <!--Import Google Icon Font-->
  <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
  <!-- Compiled and minified CSS -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css">
  <!-- Site CSS -->
  <link rel="stylesheet" href="<%=context%>/css/site.css?<%=time%>" />
</head>
<body>
<nav>
  <div class="nav-wrapper deep-orange">
    <!-- Modal Trigger -->
    <a class="auth-trigger modal-trigger right" href="#auth-modal"><i class="material-icons">door_front</i></a>

    <a href="<%= context %>/" class="site-logo right">Java</a>
    <ul id="nav-mobile">
      <li><a href="<%=contextCulture%>/jsp">JSP About</a></li>
      <li <%= "filters.jsp".equals(pageBody) ? "class='active'" : ""%>
        ><a href="<%=contextCulture%>/filters">Filters</a></li>
      <li <%= "ioc.jsp".equals(pageBody) ? "class='active'" : ""%>
      ><a href="<%=contextCulture%>/ioc">IoC</a></li>
      <li <%= "db.jsp".equals(pageBody) ? "class='active'" : ""%>
      ><a href="<%=contextCulture%>/db">DB</a></li>
      <li <%= "spa.jsp".equals(pageBody) ? "class='active'" : ""%>
      ><a href="<%=contextCulture%>/spa">SPA</a></li>
      <li <%= "ws.jsp".equals(pageBody) ? "class='active'" : ""%>
      ><a href="<%=contextCulture%>/ws">WS</a></li>
    </ul>
  </div>
</nav>
<main style="margin: 20px">
  <jsp:include page="<%= pageBody%>" />
</main>
<footer class="page-footer">
  <div class="container">
    <div class="row">
      <div class="col l6 s12">
        <h5 class="white-text">Footer Content</h5>
        <p class="grey-text text-lighten-4">You can use rows and columns here to organize your footer content.</p>
      </div>
      <div class="col l4 offset-l2 s12">
        <h5 class="white-text">Links</h5>
        <ul>
          <li><a class="grey-text text-lighten-3" href="#!">Link 1</a></li>
          <li><a class="grey-text text-lighten-3" href="#!">Link 2</a></li>
          <li><a class="grey-text text-lighten-3" href="#!">Link 3</a></li>
          <li><a class="grey-text text-lighten-3" href="#!">Link 4</a></li>
        </ul>
      </div>
    </div>
  </div>
  <div class="footer-copyright">
    <div class="container">
      © 2014 Copyright Text
      <a class="grey-text text-lighten-4 right" href="#!">More Links</a>
    </div>
  </div>
</footer>


<!-- Modal Structure -->
<div id="auth-modal" class="modal">
  <div class="modal-content">
    <h4>Auth</h4>
    <div class="row">
      <div class="input-field col s6">
        <i class="material-icons prefix">person</i>
        <input id="auth-login" type="text" >
        <label for="auth-login">Enter a login</label>
      </div>
      <div class="input-field col s6">
        <i class="material-icons prefix">lock</i>
        <input id="auth-password" type="password">
        <label for="auth-password">Enter a password</label>
      </div>
    </div>
  </div>
  <div class="modal-footer">
    <b id="auth-message-container"></b>
    <a href="<%=context%>/signup" class="modal-close btn-flat deep-orange lighten-2">Sign Up</a>
    <button id="auth-modal-sign-in-button" class="waves-effect waves-green btn-flat deep-orange lighten-3">Sign In</button>
  </div>
</div>

<!-- Compiled and minified JavaScript -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>
<!-- Site script -->
<script src="<%= context %>/js/site.js"></script>
<script src="<%= context %>/js/auth.js?<%=time%>"></script>
</body>
</html>
