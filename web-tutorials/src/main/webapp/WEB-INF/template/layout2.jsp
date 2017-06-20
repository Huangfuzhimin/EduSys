<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<html>
<head>
    <title>Title</title>

    <link href="${pageContext.request.contextPath}/assets/themes/insdep/easyui.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/assets/themes/insdep/easyui_animation.css" rel="stylesheet"
          type="text/css">
    <link href="${pageContext.request.contextPath}/assets/themes/insdep/easyui_plus.css" rel="stylesheet"
          type="text/css">
    <link href="${pageContext.request.contextPath}/assets/themes/insdep/insdep_theme_default.css" rel="stylesheet"
          type="text/css">

    <link href="${pageContext.request.contextPath}/assets/themes/insdep/icon.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/assets/css/font-awesome.min.css" rel="stylesheet" type="text/css">

    <script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/jquery.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/jquery.easyui.min.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/assets/themes/insdep/jquery.insdep-extend.min.js"></script>

    <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/itheima.js"></script>


</head>
<body>

<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'north',border:false,bodyCls:'theme-header-layout'" style="height:50px">
        <div class="theme-navigate">
            <div class="left">
                <a href="#" class="easyui-linkbutton">首页</a>
            </div>
            <div class="right">
                <a href="#" class="easyui-menubutton theme-navigate-user-button button-blue"
                   data-options="menu:'#user',hasDownArrow:true">
                    <i class="fa fa-user"></i> 用户
                </a>
                <div id="user" class="theme-navigate-more-panel">
                    <div>联系我们</div>
                    <div>参与改进计划</div>
                    <div>关于</div>
                </div>
            </div>
        </div>

    </div>
    <div data-options="region:'center'">

        <tiles:insertAttribute name="body"></tiles:insertAttribute>

    </div>
</div>


</body>
</html>
