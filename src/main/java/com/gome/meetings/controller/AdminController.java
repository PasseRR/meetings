package com.gome.meetings.controller;

import com.gome.meetings.model.RoomSchedule;
import com.gome.meetings.model.UserMenu;
import com.jfinal.core.Controller;

public class AdminController extends Controller {

    public void index() {
        setAttr("orderList", UserMenu.dao.getTodayOrder());
        setAttr("meetingList", RoomSchedule.dao.getTodayEvent());
        renderJsp("index.jsp");
    }
}
