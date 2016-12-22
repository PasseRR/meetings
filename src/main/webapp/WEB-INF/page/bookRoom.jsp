<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>会议室预定</title>
    <link href='/css/style.css' rel='stylesheet'/>
    <link href='/css/jquery-ui.min.css' rel='stylesheet'/>
    <link href='/css/fullcalendar.css' rel='stylesheet'/>
    <link href='/css/fullcalendar.print.css' rel='stylesheet' media='print'/>
    <link href='/css/jquery.qtip.min.css' rel='stylesheet'/>

    <script src='/js/jquery.min.js'></script>
    <script src='/js/jquery-ui.custom.min.js'></script>
    <script src='/js/jquery.qtip.min.js'></script>
    <script src='/js/fullcalendar.min.js'></script>
    <script>
        var roomId;
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
            roomId = $("#room1").attr("roomId");
            $("#room1").css("backgroundColor", "#003399");

            var datetime = $.fullCalendar.parseDate("${ServerTime}");

            /* initialize the calendar
             -----------------------------------------------------------------*/

            $('#calendar').fullCalendar({
                theme: true,
                year: datetime.getFullYear(),
                month: datetime.getMonth(),
                date: datetime.getDate(),
                header: {
                    left: '',
                    center: 'title',
                    right: 'prev,next today'
                },
                defaultView: 'agendaWeek',
                firstDay: 1,
                allDaySlot: false,
                allDayDefault: false,
                buttonText: {
                    today: '今天',
                    month: '月',
                    week: '周',
                    day: '日'
                },
                monthNames: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
                monthNamesShort: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'],
                dayNames: ['周日', '周一', '周二', '周三', '周四', '周五', '周六'],
                dayNamesShort: ['周日', '周一', '周二', '周三', '周四', '周五', '周六'],
                timeFormat: {
                    agenda: 'HH:mm{ ~ HH:mm}'
                },
                columnFormat: {
                    month: 'ddd',
                    week: 'MM/dd ddd',
                    day: 'ddd'
                },
                titleFormat: {
                    month: 'yyyy 年 MMM 月',
                    week: "yyyy-MM-dd{' 至 'yyyy-MM-dd}",
                    day: 'ddd'
                },
                axisFormat: 'HH:mm',
                minTime: 9,
                maxTime: 20,
                events: function (start, end, callback) {
                    $.getJSON("/roomSchedule/getDurationEvent", {
                        roomId: roomId,
                        start: Math.round(start.getTime() / 1000),
                        end: Math.round(end.getTime() / 1000)
                    }, function (events) {
                        callback(events);
                    });
                },
                snapMinutes: 5,
                eventResize: function (event, dayDelta, minuteDelta, revertFunc, jsEvent, ui, view) {
                    $.getJSON("/roomSchedule/updateRoomEvent", {
                        id: event.id,
                        start: event.start.getTime(),
                        end: event.end.getTime(),
                        roomId: roomId
                    }, function (data) {
                        if (!data.isSuccess) {
                            alert(data.msg);
                            revertFunc();
                        }
                    });
                    $("div[id^='qtip-']").hide();
                },
                eventDrop: function (event, dayDelta, minuteDelta, allDay, revertFunc, jsEvent, ui, view) {
                    $.getJSON("/roomSchedule/updateRoomEvent", {
                        id: event.id,
                        start: event.start.getTime(),
                        end: event.end.getTime(),
                        roomId: roomId
                    }, function (data) {
                        if (!data.isSuccess) {
                            alert(data.msg);
                            revertFunc();
                        }
                    });
                    $("div[id^='qtip-']").hide();
                },
                eventMouseover: function (event, jsEvent, view) {
                    if (event.editable) {
                        $(this).children("div>*:nth-child(1)")
                                .children("div>*:nth-child(1)")
                                .append("<a class='delIcon' title='删除' href='#'>&times;</a>");
                    }
                },
                eventMouseout: function (event, jsEvent, view) {
                    $(".delIcon").remove();
                },
                selectable: true,
                selectHelper: true,
                select: function (start, end, allDay) {
                    if (start.getTime() <= new Date().getTime()) {
                        alert("开始时间已过期!");
                    } else {
                        var title = prompt('会议主题:');
                        if (title) {
                            $.getJSON("/roomSchedule/addRoomEvent", {
                                start: start.getTime(),
                                end: end.getTime(),
                                roomId: roomId,
                                title: title
                            }, function (data) {
                                if (data.isSuccess) {
                                    $('#calendar').fullCalendar('refetchEvents');
                                } else {
                                    alert(data.msg);
                                }
                            });
                        }
                    }
                    $('#calendar').fullCalendar('unselect');
                    $("div[id^='qtip-']").hide();
                },
                eventRender: function (event, element) {
                    element.qtip({
                        id: event.id,
                        style: {
                            classes: 'qtip-bootstrap qtip-shadow'
                        },
                        content: {
                            title: event.roomName,
                            text: getTipContent(event.userName, event.start, event.end, event.title, event.email).clone()
                        },
                        position: {
                            my: 'right center',
                            at: 'left center'
                        }
                    });

                    element.click(function (e) {
                        if (event.editable) {
                            if (e.toElement.className != "delIcon") { // 修改标题
                                var title = prompt('修改会议主题:', event.title);
                                if (title) {
                                    $.getJSON("/roomSchedule/updateRoomEventTitle", {
                                        id: event.id,
                                        title: title
                                    }, function (data) {
                                        if (data.isSuccess) {
                                            $('#calendar').fullCalendar('refetchEvents');
                                        } else {
                                            alert(data.msg);
                                        }
                                    });
                                }
                            } else { // 删除会议
                                var isOk = confirm('确定删除会议?');
                                if (isOk) {
                                    $.getJSON("/roomSchedule/deleteRoomEvent", {
                                        id: event.id
                                    }, function (data) {
                                        if (data.isSuccess) {
                                            $('#calendar').fullCalendar('refetchEvents');
                                        } else {
                                            alert(data.msg);
                                        }
                                    });
                                }
                            }
                        }
                    });
                },
                loading: function (bool) {
//                    if (bool) $('#loading').show();
//                    else $('#loading').hide();
                }
            });

            $("#schedule").hide();
            $("#schedule").animate({marginLeft: '-200%'});
            $("#next, .pagingRight").click(function () {
                $.get("/roomSchedule/todayMeetings", {}, function (data) {
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
                $("#scheduleRoom").animate({marginLeft: '-100%'}, 500);
                $("#schedule").show();
                $("#schedule").animate({marginLeft: 0}, 500);
            });

            $("#previous, .pagingLeft").click(function () {
                $("#scheduleRoom").animate({marginLeft: 0}, 500);
                $("#schedule").animate({marginLeft: '-200%'}, 500);
                $("#schedule").hide();
            });
        });

        function getEvents(start, end) {
            $.getJSON("/roomSchedule/getDurationEvent", {
                roomId: roomId,
                start: Math.round(start.getTime() / 1000),
                end: Math.round(end.getTime() / 1000)
            }, function (events) {
                return events;
            });
        }
        function changeRoom(id) {
            $(".external-event").each(function (i) {
                if (this.id == id) {
                    $(this).css("backgroundColor", "#003399");
                    roomId = $(this).attr("roomId");
                } else {
                    $(this).css("backgroundColor", "#828282");
                }
            });
            $('#calendar').fullCalendar('refetchEvents');
        }
        function getTipContent(initiator, start, end, subject, contact) {
            $("#initiator").text(initiator);
            $("#contact").text(contact);
            $("#date").text($.fullCalendar.formatDate(start, 'yyyy-MM-dd'));
            $("#tb").text($.fullCalendar.formatDate(start, 'HH:mm') +
                    " ~ " + $.fullCalendar.formatDate(end, 'HH:mm'));
            $("#subject").text(subject);
            return $('#qTipContent');
        }
    </script>
</head>
<body>
<div class='wrap' id="scheduleRoom">
    <div id='external-events'>
        <h4>会议室</h4>
        <c:forEach items="${roomList}" var="room" varStatus="status">
            <div class='external-event' id="room${status.count}" roomId="${room.id}"
                 onclick="changeRoom(this.id)">${room.name}</div>
        </c:forEach>
    </div>
    <div id='calendar'></div>
    <div class="paging" title="今日会议"><span class="pagingRight"><a href="#" id="next">▶</a></span></div>

    <div style='clear: both'></div>
    <div id='loading'>loading...</div>
    <div id='exit'><a href="/exit" title="注销"><span class="ui-icon ui-icon-power"/></a></div>
    <div id="qTipContent">
        发起人：<span id="initiator"></span><br/>
        邮&nbsp;&nbsp;&nbsp;箱：<span id="contact"></span><br/>
        日&nbsp;&nbsp;&nbsp;期：<span id="date"></span><br/>
        时&nbsp;&nbsp;&nbsp;间：<span id="tb"></span><br/>
        主&nbsp;&nbsp;&nbsp题：<span id="subject"></span>
    </div>
</div>
<div class="wrap" id="schedule">
    <div class="paging" title="今日会议"><span class="pagingLeft"><a href="#" id="previous">◀</a></span></div>
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
