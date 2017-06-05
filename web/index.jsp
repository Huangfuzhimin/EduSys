<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/12/9
  Time: 14:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <h2>Hi CMS Hi</h2>

    <%
        out.println("Hello World！哈哈");
        out.println("你的 IP 地址 " + request.getRemoteAddr());
    %>
</body>
</html>
