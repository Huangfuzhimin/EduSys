<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<style type="text/css">
    .item {
        width: 200px;
        height: 120px;
        border: 1px solid black;
        border-radius: 3px;
        margin: 10px;
        padding: 8px;
    }
</style>
<div id="tt" class="easyui-tabs" data-options="fit:true">

    <div title="Java" style="padding:20px;display:none;">

        <table id="tab_java" style="margin: 0 auto;"></table>

    </div>
    <div title="Kotlin" style="padding:20px;display:none;">

        <table id="tab_kotlin" style="margin: 0 auto;"></table>

    </div>

</div>


<script type="text/javascript">
    //页面加载完成时
    $(function ($) {
        // 加载 java
        loadTab("java");
        // 加载 kotlin
        loadTab("kotlin");

    });

    var columnCount = 3;	// 列数
    var maxStarCount = 10;	// 最大星星数

    function onError() {
        alert("服务器异常, 请稍后重试!");
    }

    function doSuccess(result, language) {
        var table = $("#tab_" + language);
        table.html("");

        if (result.code == 0) {
            var datas = result.data;
            var tr = {};
            $.each(datas, function (index, ele) {
                if (index % columnCount == 0) {
                    tr = $('<tr></tr>');
                }
                var td = $('<td></td>');

                var param = {"name": ele.name, "type": language};
                var arg = urlEncode(param);
                var a = $('<a href="${pageContext.request.contextPath}/chapter/list?'+arg+'"></a>');
                var div = $('<div class="easyui-panel item" style="position: relative" data-options="fit:true"></div>');
                var h = $('<h3>' + ele.name + '</h3>');
                var p = $('<p></p>');
                var progress = $('<div class="easyui-progressbar" style="position: absolute;bottom:10px;"></div>');
                div.append(h);
                div.append(p);
                div.append(progress);
                a.append(div);

                td.append(a);
                tr.append(td);

                if (index % columnCount == 0) {
                    table.append(tr);
                }

                var len = Math.floor(ele.current * 100 / ele.total);

                $(progress).progressbar({fit:true, value: len});
            });
        }
    }

    function onSuccess(result, language) {
        var table = $("#tab_" + language);
        //清空
        table.html("");
        if (result.code == 0) { // success
            var datas = result.data;
            var tr = {};
            $.each(datas, function (index, ele) {
                if (index % columnCount == 0) {
                    tr = $("<tr></tr>");
                }
                // 进度
                var totalProgress = Math.ceil(ele.total / 100 * maxStarCount);

                if (totalProgress > maxStarCount) {
                    totalProgress = maxStarCount;
                }

                var progress = Math.floor((ele.current / ele.total) * totalProgress);

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
//                var td = $('<div class="thumbnail"><div class="caption"><h4>Thumbnail label</h4><p>haha<p></div></div>');
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

    function loadTab(language) {
        var type;
        if (language == "java") {
            type = 0;
        } else if (language == "kotlin") {
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
//                onSuccess(result, language);
                doSuccess(result, language);
            },
            error: function () {
                onError();
            }
        });
    }

</script>