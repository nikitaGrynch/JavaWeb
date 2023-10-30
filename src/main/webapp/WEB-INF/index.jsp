<%@ page contentType="text/html;charset=UTF-8" %>
<%

%>
<html>
<head>
    <title>JSP</title>
</head>
<body>
    <h1>Java Web. Intro</h1>
    <%
        String str = "Hello";
        str += "World";
        int x = 10;
    %>
<p>
    str = <%= str%>, x + 10 = <%= x + 10 %>
</p>
    <ul>
        <% for (int i = 1; i <= 10; i++) { %>
        <li>
            item No <%= i %>
        </li>
        <% } %>
    </ul>
    <jsp:include page="fragment.jsp">
        <jsp:param name="str" value="<%= str %>"/>
        <jsp:param name="x" value="<%= x %>"/>
    </jsp:include>
</body>
</html>
