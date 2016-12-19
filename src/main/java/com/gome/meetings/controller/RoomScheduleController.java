package com.gome.meetings.controller;

import com.gome.meetings.bo.RoomEvent;
import com.gome.meetings.common.CommonConstant;
import com.gome.meetings.model.RoomSchedule;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.*;

public class RoomScheduleController extends Controller {

    public void getDurationEvent() {
        List<RoomSchedule> list = RoomSchedule.dao.getDurationEventsByRoomId(
                getParaToInt("roomId"), getParaToLong("start"),
                getParaToLong("end"));
        List<RoomEvent> events = new ArrayList<>();
        for (RoomSchedule roomSchedule : list) {
            RoomEvent event = new RoomEvent(roomSchedule.getInt("id"),
                    roomSchedule.getStr("subject"),
                    roomSchedule.getTimestamp("start"),
                    roomSchedule.getTimestamp("end"),
                    roomSchedule.getStr("username"),
                    roomSchedule.getStr("roomname"),
                    roomSchedule.getStr("email"),
                    false);
            if (roomSchedule.getTimestamp("start").before(new Date())) {
                event.setColor(CommonConstant.COLOR_FOR_PAST_EVENT);
            } else if (roomSchedule.getInt("userid").equals(getSessionAttr("userId"))) {
                event.setColor(CommonConstant.COLOR_FOR_OWN_FUTURE_EVENT);
                event.setEditable(true);
            } else {
                event.setColor(CommonConstant.COLOR_FOR_FUTURE_EVENT);
            }
            events.add(event);
        }

        renderJson(events);
    }

    public void updateRoomEvent() {
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("isSuccess", true);
        Date start = new Date(getParaToLong("start"));
        Date end = new Date(getParaToLong("end"));
        if (start.before(new Date())) {
            returnMap.put("isSuccess", false);
            returnMap.put("msg", "调整后的开始时间已过期!");
        } else if (!RoomSchedule.dao.isLegalEvent(getParaToLong("start") / 1000, getParaToLong("end") / 1000, getParaToInt("roomId"), getParaToInt("id"))) {
            returnMap.put("isSuccess", false);
            returnMap.put("msg", "与其他会议时间有冲突！");
        } else {
            RoomSchedule roomSchedule = RoomSchedule.dao.findById(getParaToInt("id"));
            roomSchedule.set("start", start);
            roomSchedule.set("end", end);
            roomSchedule.update();
        }
        renderJson(returnMap);
    }

    public void updateRoomEventTitle() throws UnsupportedEncodingException {
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("isSuccess", true);
        RoomSchedule roomSchedule = RoomSchedule.dao.findById(getParaToInt("id"));
        roomSchedule.set("subject", new String(super.getPara("title").getBytes("ISO-8859-1"), "UTF-8"));
        roomSchedule.update();
        renderJson(returnMap);
    }

    public void addRoomEvent() throws UnsupportedEncodingException {
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("isSuccess", true);
        Date start = new Date(getParaToLong("start"));
        Date end = new Date(getParaToLong("end"));
        String title = new String(super.getPara("title").getBytes("ISO-8859-1"), "UTF-8");
        int roomId = getParaToInt("roomId");
        if (start.before(new Date())) {
            returnMap.put("isSuccess", false);
            returnMap.put("msg", "开始时间已过期!");
        } else if (!RoomSchedule.dao.isLegalEvent(getParaToLong("start") / 1000, getParaToLong("end") / 1000, roomId, -100)) {
            returnMap.put("isSuccess", false);
            returnMap.put("msg", "与其他会议时间有冲突！");
        } else {
            new RoomSchedule().set("start", start)
                    .set("end", end)
                    .set("subject", title)
                    .set("userid", getSessionAttr("userId"))
                    .set("roomid", roomId)
                    .set("create_date", new Date()).save();
        }
        renderJson(returnMap);
    }

    public void deleteRoomEvent() {
        Record record = Db.findById("room_schedule", getPara(0));
        if (record.getTimestamp("end").before(getToday())) {
            renderNull();
        }
        Db.deleteById("room_schedule", getPara(0));
        redirect("/");
    }

    private Date getToday() {
        Date date = null;
        try {
            date = DateUtils.parseDate(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
