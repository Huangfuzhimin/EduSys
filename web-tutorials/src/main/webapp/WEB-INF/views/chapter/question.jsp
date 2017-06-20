<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/plugin/materialize/css/icon.css">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/static/plugin/materialize/css/materialdesignicons.min.css">
    <!-- materialize CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/plugin/materialize/css/materialize.min.css">

    <!-- easy ui 部分 -->
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/static/plugin/easyui/themes/material/easyui.css">
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/static/plugin/easyui/themes/icon.css">

    <script type="text/javascript" src="${pageContext.request.contextPath}/static/plugin/jquery/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/static/plugin/jquery/jquery.params.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/static/plugin/easyui/jquery.easyui.min.js"></script>

    <!-- ace 部分 -->
    <script src="${pageContext.request.contextPath}/static/plugin/ace/src-min/ace.js" type="text/javascript"
            charset="utf-8"></script>
    <script src="${pageContext.request.contextPath}/static/plugin/ace/src-min/ext-language_tools.js"></script>

    <script src="${pageContext.request.contextPath}/static/js/component-project.js" type="text/javascript"></script>

</head>
<body>
<!-- Dropdown Structure -->
<ul id="dropdown1" class="dropdown-content">
    <li><a href="#!">one</a></li>
    <li><a href="#!">two</a></li>
    <li class="divider"></li>
    <li><a href="#!">three</a></li>
</ul>
<nav>
    <div class="nav-wrapper teal">
        <a href="#!" class="brand-logo">Logo</a>
        <ul class="right hide-on-med-and-down">
            <li><a href="sass.html">Sass</a></li>
            <li><a href="badges.html">Components</a></li>
            <!-- Dropdown Trigger -->
            <li><a class="dropdown-button" href="#!" data-activates="dropdown1">Dropdown<i class="material-icons right">arrow_drop_down</i></a>
            </li>
        </ul>
    </div>
</nav>

<div id="layout" class="easyui-layout" data-options="fit:true" style="padding-bottom: 60px">
    <div data-options="region:'west',split:true" style="width:35%">
        <div class="easyui-accordion" data-options="fit:true">
            <div title="题目描述" data-options="iconCls:'icon-j-info'">
                <div id="projectDesc" style="padding: 8px;"></div>
            </div>
            <div title="控制台" data-options="iconCls:'icon-j-console'">
            </div>
        </div>
    </div>
    <div data-options="region:'east',split:true,iconCls:'icon-j-project'" title="目录结构" style="width:20%">

        <!-- 目录树 -->
        <ul id="tree" class="easyui-tree"></ul>

    </div>
    <div data-options="region:'center'">

        <div id="code" class="easyui-panel" title="编码区"
             data-options="fit:true,iconCls:'icon-j-code',tools:[{iconCls:'icon-j-run',handler:function(){clickRun()}}]">
            <!-- 代码tab区 -->
            <div id="tabs" class="easyui-tabs" data-options="fit:true"></div>
        </div>

    </div>
</div>

<script type="text/javascript">
    var structure = null;

    function loadTree() {
        var url = "${pageContext.request.contextPath}/question/tree";
        var data = {
            "chapter": $.query.get("chapter"),
            "questionid": $.query.get("name")
        };
        var callback = function (result) {
            structure = new ProjectStructure("tree", result, "tabs");
        };
        $.post(url, data, callback, 'json');
    }

    function loadDesc() {
        var url = "${pageContext.request.contextPath}/question/desc";
        var data = {
            "chapter": $.query.get("chapter"),
            "questionid": $.query.get("name")
        };
        var callback = function (result) {
            $("#projectDesc").html(result);
        };

        $.post(url, data, callback);
    }

    function clickRun() {
        if (structure == null) {
            return;
        }

        var codes = structure.getCodes();
        var code = codes[0].content;//临时使用

        var username = "aaa";

        var data = {
            "chapter": $.query.get("chapter"),
            "username": username,
            "questionid": $.query.get("name"),
            "code": code
        };
        console.log(data);
        var url = "${pageContext.request.contextPath}/run";
        var callback = function (result) {
            console.log(result);
        }
        $.post("/run", data, callback,"json");
    }

    function clickReset() {
        alert('reset');
    }


    $(function () {
        loadTree();
        loadDesc();
    })
</script>


</body>
</html>