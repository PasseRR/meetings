package com.gome.meetings.model;

import com.jfinal.plugin.activerecord.Model;

import java.util.List;

@SuppressWarnings("serial")
public class Room extends Model<Room> {
	public static final Room dao=new Room();
	
	public List<Room> getAllRoom(){
		return dao.find("select * from room");
	}
}
