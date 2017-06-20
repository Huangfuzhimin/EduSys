<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/plugin/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/plugin/split/css/split.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/plugin/materialize/css/icon.css">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/static/plugin/materialize/css/materialdesignicons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/plugin/materialize/css/materialize.min.css">

    <!-- jquery -->
    <script src="${pageContext.request.contextPath}/static/plugin/jquery/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/static/plugin/jquery/jquery.params.js"></script>

    <!-- material -->
    <script src="${pageContext.request.contextPath}/static/plugin/materialize/js/materialize.js"></script>

    <!-- 分隔js -->
    <script src="${pageContext.request.contextPath}/static/plugin/split/split.js"></script>
    <!-- 树js -->
    <script src="${pageContext.request.contextPath}/static/plugin/treeview/js/bootstrap-treeview.min.js"></script>
    <!-- ace -->
    <script src="${pageContext.request.contextPath}/static/plugin/ace/src-min-noconflict/ace.js" type="text/javascript"
            charset="utf-8"></script>
    <script src="${pageContext.request.contextPath}/static/plugin/ace/src-min-noconflict/ext-language_tools.js"></script>


    <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/component-code.js"></script>

    <style>
        html, body {
            height: 100%;
        }

        .code {
            padding: 10px;
        }

        .code h3 {
            font-size: 20px;
            font-weight: bold;
        }

        .code p {
            padding: 0px;
        }

        .code table, .code tr, .code td {
            border: 1px;
            border-color: black;
        }
    </style>
</head>
<body>

<div style="height: 90%;">
    <!-- 左侧 -->
    <div id="containerLeft" class="split split-horizontal">
        <!-- 上部分 -->
        <div id="containerLeftUp" class="split split-vertical code"></div>
        <!-- 下部分 -->
        <div id="containerLeftDown" class="split split-vertical" style="background-color: #536dfe"></div>
    </div>
    <!-- 右侧 -->
    <div id="containerRight" class="split split-horizontal">
        <!-- 代码部分 -->
        <div id="containerRightCode" class="split split-horizontal">
            <!-- panel -->
            <div id="codePanel" style="width: 100%;height: 100%"></div>
        </div>
        <!-- 树部分 -->
        <div id="containerRightTree" class="split split-horizontal">
            <div id="tree"></div>
        </div>
    </div>

</div>

<script type="text/javascript">
    // 初始化布局
    Split(['#containerLeft', '#containerRight'], {
        sizes: [30, 70]
    });

    Split(['#containerLeftUp', '#containerLeftDown'], {
        direction: 'vertical'
    });

    Split(['#containerRightCode', '#containerRightTree'], {
        sizes: [70, 30],
        minSize: 0
    });

    function loadTree() {
        var url = "${pageContext.request.contextPath}/question/tree";
        var data = {
            "chapter": $.query.get("chapter"),
            "questionid": $.query.get("name")
        };
        var callback = function (result) {

            var title = result.title;
            var src = result.src;

            var tree = [
                {
                    text: title,
                    expandIcon: "glyphicon glyphicon-file",
                    state: {
                        expanded: true
                    },
                    nodes: [
                        {
                            text: "src"
                        }
                    ]
                }
            ];

            tree[0].nodes[0].nodes = [];
            for (var i = 0; i < src.length; i++) {
                var item = src[i];
                tree[0].nodes[0].nodes[i] = {
                    text: item.name,
                    attributes: {
                        type: 1,
                        readOnly: item.readonly
                    }
                };
            }

            var codePanel = new CodePanel();
            codePanel.layout($("#codePanel"));

            $('#tree').treeview({
                data: tree,
                onNodeSelected: function (event, node) {
                    var open = codePanel.openTab(node);
                    if (!open) {
                        if (node.state.expanded) {
                            $('#tree').treeview('collapseNode', [node.nodeId, {silent: true, ignoreChildren: false}]);
                        } else {
                            $('#tree').treeview('expandNode', [node.nodeId, {silent: true, ignoreChildren: false}]);
                        }
                        $('#tree').treeview('toggleNodeSelected', [node.nodeId, {silent: true}]);
                    }
                }
            });

        }

        $.post(url, data, callback, "json");
    }

    function loadDesc() {
        var url = "${pageContext.request.contextPath}/question/desc";
        var data = {
            "chapter": $.query.get("chapter"),
            "questionid": $.query.get("name")
        };
        var callback = function (result) {
            $("#containerLeftUp").html(result);
        };

        $.post(url, data, callback);
    }

    $(function () {
        loadTree();
        loadDesc();
    })
</script>


</body>
</html>
