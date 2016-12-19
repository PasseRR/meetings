package com.gome.meetings.controller;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.gome.meetings.bo.DwzResponseBO;
import com.gome.meetings.common.CommonConstant;
import com.gome.meetings.model.Room;

import java.util.List;

/**
 * 会议室Controller控制类
 *
 * @author JinLiang
 * @datetime 2014年4月28日 下午10:03:18
 */
public class RoomController extends Controller {

    public void index() {
        Page<Room> page = Room.dao.paginate(CommonConstant.PAGENUMBER, CommonConstant.PAGESIZE, "select *", "from room");
        setAttr("page", page);
        render("room.jsp");
    }

    public void getPageList() {
        Page<Room> page = Room.dao.paginate(getParaToInt("pageNum"), getParaToInt("numPerPage"), "select *", "from room");
        setAttr("page", page);
        render("room.jsp");
    }

    public void goAdd() {
        render("room_add.jsp");
    }

    public void add() {
        DwzResponseBO responseBO = new DwzResponseBO(getPara("navTabId"), "closeCurrent");
        boolean flag = new Room().set("name", getPara("name")).save();
        if (!flag) {
            responseBO.setStatusCode("300");
            responseBO.setMessage("操作失败！");
        }
        renderJson(responseBO);

    }

    public void del() {
        DwzResponseBO responseBO = new DwzResponseBO();
        boolean flag = Db.deleteById("room", getParaToInt());
        if (!flag) {
            responseBO.setStatusCode("300");
            responseBO.setMessage("操作失败！");
        }
        renderJson(responseBO);
    }

    public void isUnique() {
        List<Record> list = Db.find("select * from room where name= ?", getPara("name"));
        if (list.size() > 0) {
            renderText("false");
        } else {
            renderText("true");
        }
    }
}
