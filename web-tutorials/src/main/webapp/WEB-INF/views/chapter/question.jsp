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

    <script>
        String.prototype.format = function () {
            var args = arguments;
            return this.replace(/\{(\d+)\}/g,
                function (m, i) {
                    return args[i];
                });
        }
    </script>
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
        <div id="accordion" class="easyui-accordion" data-options="fit:true">
            <div title="题目描述" data-options="iconCls:'icon-j-info'">
                <div id="projectDesc" style="padding: 8px;"></div>
            </div>
            <div title="控制台" data-options="iconCls:'icon-j-console'">
                <div id="projectConsole" style="padding: 8px;"></div>
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
    var redisURL = "http://119.23.137.220:7379/";

    function loadTree() {
        var url = "${pageContext.request.contextPath}/question/tree";
        var data = {
            "chapter": $.query.get("chapter"),
            "questionid": $.query.get("name"),
            "type": $.query.get("type")
        };
        var callback = function (result) {
            structure = new ProjectStructure("tree", result, "tabs",$.query.get("type"));
        };
        $.post(url, data, callback, 'json');
    }

    function loadDesc() {
        var url = "${pageContext.request.contextPath}/desc";
        var data = {
            "chapter": $.query.get("chapter"),
            "questionid": $.query.get("name"),
            "type": $.query.get("type")
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
        $("#accordion").accordion('select', '题目描述');

        var codes = structure.getCodes();
        var code = codes[0].content;//临时使用

        var username = "aaa";

        var data = {
            "chapter": $.query.get("chapter"),
            "username": username,
            "questionid": $.query.get("name"),
            "type": $.query.get("type"),
            "code": code
        };
        console.log(data);
        var url = "${pageContext.request.contextPath}/run";
        var callback = function (result) {
            console.log(result);
        }
        $.post("/run", data, callback, "json");
    }

    function clickReset() {
        alert('reset');
    }

    function subscribeChannel(channelKey) {
        function checkData() {
            if (xhr.readyState === 3) {
                var response = xhr.responseText;
                var chunk = response.slice(previous_response_length);
                previous_response_length = response.length;
                var sub = $.parseJSON(chunk);
                var sub_arr = sub.SUBSCRIBE;

                var bean = sub_arr[2];
                if (sub_arr[1] === channelKey && (typeof bean) === "string") {
                    bean = $.parseJSON(bean);
                    if (bean.code === 0) {
                        loadReport(bean.data);
                    } else {
                        var show = "错误码: " + bean.code + "<br/>";
                        show += "错误信息: <br/>" + bean.data;

                        $("#projectConsole").html(show);
                        $("#accordion").accordion('select', '控制台');
                    }

                }
            }
        }

        var previous_response_length = 0;
        var xhr = new XMLHttpRequest();
        xhr.open("GET", redisURL + "SUBSCRIBE/" + channelKey, true);
        xhr.onreadystatechange = checkData;
        xhr.send(null);
//                console.log("xhr.send: " + key);
    }

    function loadReport(data) {
        $("#projectConsole").load(
            "${pageContext.request.contextPath}/report1",
            {"path": data},
            function (res) {
                $("#accordion").accordion('select', '控制台');
            }
        );

    }


    $(function () {
        loadTree();
        loadDesc();

        var username = "aaa";
        var chapter = $.query.get("chapter");
        var questionid = $.query.get("name");
        var type = $.query.get("type");

        // 订阅指定用户的指定题目
        var channelKey = "{0}_{1}_{2}_{3}".format(type, username, chapter, questionid);
        subscribeChannel(channelKey);
    })
</script>


</body>
</html>