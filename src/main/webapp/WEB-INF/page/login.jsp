<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>用户登录</title>
    <link href="/css/login.css" rel='stylesheet'/>
    <link href='/css/style.css' rel='stylesheet'/>
    <script src='/js/jquery.min.js'></script>
    <script type="text/javascript">
        function switchSys(event) {
            if (event.id == 'order-food') {
                document.getElementById('book-room').checked = false;
            } else {
                document.getElementById('order-food').checked = false;
            }
        }
        $(document).ready(function () {
            $.format = function () {
                if (arguments.length == 0)
                    return null;
                var str = arguments[0];
                for (var i = 1; i < arguments.length; i++) {
                    var re = new RegExp('\\{' + (i - 1) + '\\}', 'gm');
                    str = str.replace(re, arguments[i]);
                }
                return str;
            };
            $("#login").hide();
            $("#login").animate({marginLeft: '-200%'});
            $("#next, .pagingRight").click(function () {
                $.get("/todayMeetings", {}, function (data) {
                    if(data){
                        // 想table写入数据
                        $("#todayMeetings table tr[id^=meetings]").remove();
                        data.forEach(function (item, index) {
                            var status = "<font color={0}>{1}</font>";
                            switch (item.status){
                                case 0 : {
                                    status = $.format(status, "red", "已结束");
                                    break;
                                }
                                case 1 : {
                                    status = $.format(status, "gray", "未开始");
                                    break;
                                }
                                case 2 : {
                                    status = $.format(status, "yellow", "即将开始");
                                    break;
                                }
                                case 3 : {
                                    status = $.format(status, "green", "进行中");
                                    break;
                                }
                            }
                            $("#todayMeetings table").append(
                                    "<tr id = 'meetings" + index + "'>" +
                                    "<td>" + item.subject + "</td>" +
                                    "<td>" + item.start + "</td>" +
                                    "<td>" + item.end + "</td>" +
                                    "<td>" + item.roomname + "</td>" +
                                    "<td>" + item.username + "</td>" +
                                    "<td>" + item.email + "</td>" +
                                    "<td>" + status + "</td>" +
                                    "</tr>"
                            );
                        });
                    }
                });
                $("#login").animate({marginLeft: '-200%'}, 500);
                $("#schedule").animate({marginLeft: 0}, 500);
                $("#login").hide();
            });

            $("#previous, .pagingLeft").click(function () {
                $("#login").show();
                $("#login").animate({marginLeft: 0}, 500);
                $("#schedule").animate({marginLeft: '-200%'}, 500);
            });
        });
    </script>
</head>
<body>
<div class="wrap" id="login">
    <div id="container">
        <form action="/login" method="post">
            <div class="login">用户登录</div>
            <div class="username-text">用户名</div>
            <div class="username-field">
                <input type="text" name="username"/>
            </div>
            <div class="password-text">密码</div>
            <div class="password-field">
                <input type="password" name="password"/>
            </div>
            <input type="hidden" name="pageType" id="book-room" onclick="switchSys(this)" value="bookRoom"/>
            <div class="forgot-usr-pwd">${msg}</div>
            <input type="submit" name="submit" value="GO"/>
        </form>
    </div>
    <div class="paging" title="今日会议"><span class="pagingRight"><a href="#" id="next">
        <div style="margin:250px auto;width:20px;line-height:21px;">今日会议</div>
    </a></span></div>
</div>
<div class="wrap" id="schedule"><!-- ◀ -->
    <div class="paging" title="创建会议"><span class="pagingLeft">
        <a href="#" id="previous">
            <div style="margin:250px auto;width:20px;line-height:21px;">创建会议</div>
        </a>
    </span></div>
    <div id="todayMeetings">
        <table>
            <caption>今日会议</caption>
            <tr>
                <th width="20%">主题</th>
                <th width="12.5%">开始时间</th>
                <th width="12.5%">结束时间</th>
                <th width="8%">会议室</th>
                <th width="7%">发起人</th>
                <th width="18%">e-mail</th>
                <th width="9%">状态</th>
            </tr>
        </table>
    </div>
</div>
</body>
</html>