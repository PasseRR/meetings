package com.gome.meetings;

import com.gome.meetings.controller.*;
import com.gome.meetings.model.*;
import com.jfinal.config.*;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.render.ViewType;

/**
 * 系统配置类，继承JFinalConfig，用于对整个项目进行配置
 *
 * @author Jin
 * @datetime 2014年1月15日 下午9:24:30
 */
public class SysConfig extends JFinalConfig {

    public static String URL = "jdbc:mysql://10.128.31.87:3306/MyCalendar?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull";
    public static String USER = "o2m300";
    public static String PW = "o2m300";

//	static{
//		Map<String, String> m = System.getenv();
//		URL = "jdbc:mysql://" + m.get("MOPAAS_MYSQL6594_HOST")+":"+m.get("MOPAAS_MYSQL6594_PORT")+ "/" + m.get("MOPAAS_MYSQL6594_NAME") + "?characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull";
//		USER = m.get("MOPAAS_MYSQL6594_USERNAME");
//		PW = m.get("MOPAAS_MYSQL6594_PASSWORD");
//	}

    @Override
    public void configConstant(Constants me) {

        me.setDevMode(true);//配置当前为开发模式
        me.setViewType(ViewType.JSP);//配置默认视图为JSP
        me.setBaseViewPath("/WEB-INF/page");

        loadPropertyFile("jdbcConfig.properties");//加载数据库连接配置
    }

    @Override
    public void configRoute(Routes me) {
        me.add("/", CommonController.class);
        me.add("/user", UserController.class, "/admin");
        me.add("/menu", MenuController.class, "/admin");
        me.add("/userMenu", UserMenuController.class);
        me.add("/room", RoomController.class, "/admin");
        me.add("/roomSchedule", RoomScheduleController.class);
        me.add("/admin", AdminController.class);

    }

    @Override
    public void configPlugin(Plugins me) {
        // 配置Druid数据库连接池插件
//		DruidPlugin druidPlugin = new DruidPlugin(getProperty("jdbcURL"),
//				getProperty("jdbcUser"), getProperty("jdbcPassword"));
        DruidPlugin druidPlugin = new DruidPlugin(URL, USER, PW);
        me.add(druidPlugin);

        // 配置ActiveRecord插件
        ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
        me.add(arp);
//		arp.setContainerFactory(new  CaseInsensitiveContainerFactory());
        arp.addMapping("menu", Menu.class);
        arp.addMapping("user", User.class);
        arp.addMapping("user_menu", UserMenu.class);
        arp.addMapping("room", Room.class);
        arp.addMapping("room_schedule", RoomSchedule.class);
    }

    @Override
    public void configInterceptor(Interceptors me) {
        me.add(new LoginInterceptor());
    }

    @Override
    public void configHandler(Handlers me) {
        // TODO Auto-generated method stub

    }

//	public static void main(String[] args) {
//		JFinal.start("WebRoot", 80, "/", 5);
//	}
}
