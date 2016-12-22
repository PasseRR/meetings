package com.gome.meetings;

import cn.dreampie.quartz.QuartzPlugin;
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

    @Override
    public void configConstant(Constants me) {

//        me.setDevMode(true);//配置当前为开发模式
        me.setViewType(ViewType.JSP);//配置默认视图为JSP
        me.setBaseViewPath("/WEB-INF/page");

        loadPropertyFile("jdbcConfig.properties");//加载数据库连接配置
    }

    @Override
    public void configRoute(Routes me) {
        me.add("/", CommonController.class);
        me.add("/user", UserController.class, "/admin");
//        me.add("/menu", MenuController.class, "/admin");
//        me.add("/userMenu", UserMenuController.class);
        me.add("/room", RoomController.class, "/admin");
        me.add("/roomSchedule", RoomScheduleController.class);
        me.add("/admin", AdminController.class);

    }

    @Override
    public void configPlugin(Plugins me) {
        // 配置Druid数据库连接池插件
        DruidPlugin druidPlugin = new DruidPlugin(getProperty("jdbcURL"),
                getProperty("jdbcUser"), getProperty("jdbcPassword"));
        me.add(druidPlugin);

        // 配置ActiveRecord插件
        ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
        me.add(arp);
//		arp.setContainerFactory(new  CaseInsensitiveContainerFactory());
//        arp.addMapping("menu", Menu.class);
        arp.addMapping("user", User.class);
//        arp.addMapping("user_menu", UserMenu.class);
        arp.addMapping("room", Room.class);
        arp.addMapping("room_schedule", RoomSchedule.class);
//
        // 定时任务插件
//        QuartzPlugin quartzPlugin = new QuartzPlugin();
//        quartzPlugin.setJobs("jobs.properties");
//        me.add(quartzPlugin);
    }

    @Override
    public void configInterceptor(Interceptors me) {
        me.add(new LoginInterceptor());
    }

    @Override
    public void configHandler(Handlers me) {
        // TODO Auto-generated method stub

    }

    /**
     * jfinal启动完成后执行
     */
    @Override
    public void afterJFinalStart() {
        super.afterJFinalStart();
    }

    //	public static void main(String[] args) {
//		JFinal.start("WebRoot", 80, "/", 5);
//	}
}
