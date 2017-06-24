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

    <!-- jquery -->
    <script src="${pageContext.request.contextPath}/static/plugin/jquery/jquery.min.js"></script>

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

<script type="text/javascript">
    $(function ($) {
        var java = 'Java';
        var kotlin = 'Kotlin';
        var titles = [java, kotlin];

        //加载tab
        var tabs = new Tabs({titles: titles});
        tabs.layout($(document.body));

        $.each(titles, function (index, ele) {
            loadTab(ele);
        });


        function loadTab(language) {
            var type;
            if (language == "Java") {
                type = 0;
            } else if (language == "Kotlin") {
                type = 1;
            }

            var url = "${pageContext.request.contextPath}/chapters";
            var data = {"type": type};

            $.ajax({
                type: "get",
                url: url,
                data: data,
                async: true,
                success: function (result) {
                    doSuccess(result, language);
                },
                error: function (error) {
                    console.log(error);
                }
            });
        }

        function doSuccess(result, language) {
            var tab = $(tabs.get(language));
            //清空内容
            tab.html("");

            if (result.code == 0) {
                var datas = result.data;

                var list = [];
                //遍历数据
                for (var i = 0; i < datas.length; i++) {
                    var item = datas[i];
                    var progress = Math.floor(item.current * 100 / item.total);

                    var param = {"name": item.name, "type": language.toLowerCase()};
                    var arg = "name=" + item.name + "&type=" + language.toLowerCase();
                    var url = '${pageContext.request.contextPath}/chapter/list?' + arg;
                    list[i] = {
                        title: item.name,
                        content: 'content',
                        progressDesc: '已完成' + progress + '%',
                        progress: progress,
                        rating: 7,
                        url: url
                    };
                }

                var cardList = new CardList({column: 3, data: list});
                cardList.layout(tab);
            } else {
                //TODO:
            }
        }

    });


</script>
<!-- materialize JS -->
<script src="${pageContext.request.contextPath}/static/plugin/materialize/js/materialize.min.js"></script>

</body>
</html>