<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<h1>Databases</h1>
<h2>JDBC</h2>
<p>
  <strong>JDBC</strong> -
</p>
<h2>Testing</h2>
<div class="row">
  Create Db Table
  <a id="db-create-button" class="waves-effect waves-light btn">
    <i class="material-icons left">create_new_folder</i>
    Request
  </a>
  <span id="success-icon" class="left" hidden><i class="material-icons">select_check_box</i></span>
</div>
  <div class="row" id="error-block" hidden>
    <div class="col s12 m6">
      <div class="card blue-grey darken-1">
        <div class="card-content white-text">
          <span class="card-title">Error</span>
          <p id="error-text"></p>
        </div>
      </div>
    </div>
  </div>
<div class="row">
  <form class="col s12">
    <div class="row">
      <div class="input-field col s6">
        <i class="material-icons prefix">badge</i>
        <input placeholder="Input your full name" id="db-call-me-name" type="text" class="validate">
        <label for="db-call-me-name">Name</label>
      </div>
    </div>
    <div class="row">
      <div class="input-field col s4">
        <i class="material-icons prefix">call</i>
        <input placeholder="+380 XX XXX XX XX" id="db-call-me-phone" type="tel" class="validate">
        <label for="db-call-me-phone">Phone number</label>
      </div>
      <div class="input-field col s3">
        <button type="button" id="db-call-me-button" class="waves-effect waves-light btn">
          <i class="material-icons right">call</i>
          Order
        </button>
      </div>
    </div>
  </form>
</div>

<h2>Logging</h2>

<div class="row">
  <button type="button" id="db-get-all-button" class="waves-effect waves-light btn">
    <i class="material-icons right">table_chart</i>
    View calls
  </button>
  <button type="button" id="db-get-all-with-deleted-button" class="waves-effect waves-light btn">
    <i class="material-icons right">table_chart</i>
    Show all cals
  </button>
</div>
<div id="db-get-all-container"></div>