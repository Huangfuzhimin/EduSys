<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
    <title>Title</title>

    <link href="${pageContext.request.contextPath}/assets/themes/insdep/easyui.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/assets/themes/insdep/icon.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/assets/themes/insdep/easyui_animation.css" rel="stylesheet"
          type="text/css">
    <link href="${pageContext.request.contextPath}/assets/themes/insdep/easyui_plus.css" rel="stylesheet"
          type="text/css">
    <link href="${pageContext.request.contextPath}/assets/themes/insdep/insdep_theme_default.css" rel="stylesheet"
          type="text/css">

    <script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/jquery.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/jquery.easyui.min.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/assets/themes/insdep/jquery.insdep-extend.min.js"></script>

    <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/itheima.js"></script>
</head>
<body>

<div id="header" style="background-color: #EEEEEE;">
    <h2 style="text-align: center; padding: 10px;">
        黑马程序员教考系统
    </h2>
</div>
<a href="/html/java.html/">
		<span style='font-size:200%;'>
		Itheima
		</span>
</a>
> 章节列表

<div class=tabc style="margin-top: 20px;">
    <nav>
        <ul>
            <li class="h2" id="selected">
                <a href="/html/java.html/">Java</a>
            </li>
            <li class="h2">
                <a href="/html/kotlin.html/">Kotlin</a>
            </li>
        </ul>
    </nav>
    <div class=tabin>
        <div class=floatleft>
            <table id="table_id">

            </table>
        </div>
        <div class=floatclear></div>
    </div>
</div>

<script type="text/javascript">

    var columnCount = 2;	// 列数
    var maxStarCount = 10;	// 最大星星数
    // 将string转成json对象
    //			console.log($.parseJSON(result));
    function onSuccess(result) {
        var table = $("#table_id");
        table.html("");
        // 将json对象转成string
        //  "{"code":0,"msg":"success","data":["Array-1","Array-2数组","logic-1","Logic-2","map","String-1","String-2","String-3","Warmup-1"]}"
//			console.log(JSON.stringify(result));
        if (result.code == 0) { // success

            var datas = result.data;
            var tr = {};
            $.each(datas, function (index, ele) {
//					console.log(ele);
                if (index % columnCount == 0) {
                    tr = $("<tr></tr>");
                }

                // 进度
                var totalProgress = Math.ceil(ele.total / 100 * maxStarCount);

                if (totalProgress > maxStarCount) {
                    totalProgress = maxStarCount;
                }

                var progress = Math.floor((ele.current / ele.total) * totalProgress);
//					console.log(ele.name + "_c:" + ele.current + " t:" + ele.total + " P:" + progress);

                // 添加标题
                var type = $("nav .h2[id='selected'] a").text();
                var param = {"name": ele.name, "type": type};
                var arg = urlEncode(param);

                var str = "<a href='/html/chapter_detail.html?" + arg + "'>" +
                    "<span class=h2>" + ele.name + "</span>" +
                    "</a> " + ele.current + "/" + ele.total + "<br>";
                // 或单独添加点击事件
//				        function jump1(){
//					    url = "b.html?name="+name+"&age="+age;//此处拼接内容
//					    window.location.href = url;
//					}

                for (var i = 1; i <= totalProgress; i++) {
                    if (i <= progress) {// 完成
                        str += "<img src='/images/s2.jpg'/>";
                    } else {
                        str += "<img src='/images/s1.jpg'/>";
                    }
                }

                // 描述
                str += "<br>" + ele.desc;

                if (ele.current == ele.total) {
                    str += "<img src='/images/c2.jpg'/>";
                } else {
                    str += "<img src='/images/c1.jpg'/>";
                }

                var td = $("<td><div class='summ'>" + str + "</div></td>")
                tr.append(td);


                if (index % columnCount == 0) {
                    table.append(tr);
                }
            });
        } else {
            table.html("<h2>服务器异常</h2>" +
                "<p>" + JSON.stringify(result) + "<p>")
        }
    }
    function onError() {
        alert("服务器异常, 请稍后重试!");
    }

    function initChapters() {
        $.ajax({
            type: "get",
            url: "/chapters",
            data: {"type": 0},
            async: true,
            success: onSuccess,
            error: onError
        });
    }

    $(document).ready(function () {
        $("#table_id").html("");
        initChapters();
    })
</script>
</body>
</html>
