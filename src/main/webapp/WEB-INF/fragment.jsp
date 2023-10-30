<%@ page contentType="text/html;charset=UTF-8"%>
<%
    String str = request.getParameter("str");
%>
<h2>
    Hello from fragment
</h2>
<p>
    str = <%= str %>
</p>
