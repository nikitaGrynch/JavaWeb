<%@ page contentType="text/html;charset=UTF-8"%>
<h1>Servlet Filter</h1>
<p>
    Для передачи данных с фильтра можно использовать атрибуты запроса
    <code>req.setAttribute("charset", charset) ;</code> доступ
    к этим параметрам - в любом месте, в частности в jsp:
    charset = '<%=request.getAttribute("charset")%>'
    <br />
    browser: '<%=request.getAttribute("browser")%>'
    <br />
    browserType: '<%=request.getAttribute("browserType")%>'
</p>

