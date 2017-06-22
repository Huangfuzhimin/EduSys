<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>

<head>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/plugin/materialize/css/icon.css">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/static/plugin/materialize/css/materialdesignicons.min.css">
    <!-- materialize CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/plugin/materialize/css/materialize.min.css">

    <!-- jquery -->
    <script src="${pageContext.request.contextPath}/static/plugin/jquery/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/static/plugin/jquery/jquery.params.js"></script>

    <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/itheima.js"></script>

    <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/component-card.js"></script>

    <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/component-tab.js"></script>

    <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/component-chapter.js"></script>

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

<div id="container" class="container">

</div>

<script type="text/javascript">

    $(function () {
        loadChapter();
    });
    function loadChapter() {
        var chapter = $.query.get("name");

        var url = "${pageContext.request.contextPath}/chapter";
        var data = {chapter: chapter};
        var callback = function (result) {
            if (result.code == 0) {
                var datas = result.data;

                var list = [];
                for (var i = 0; i < datas.length; i++) {
                    var item = datas[i];

                    var arg = "?chapter=" + chapter + "&name=" + item.name + "&title=" + item.title;
                    var url = "${pageContext.request.contextPath}/chapter/question" + arg;

                    list[i] = {
                        name: item.title,
                        done: true,
                        url: url
                    }
                }

                var chapterList = new ChapterList({column: 4, list: list});
                chapterList.layout($("#container"));
            }
        }

        $.get(url, data, callback, "json");
    }
</script>

</body>
</html>