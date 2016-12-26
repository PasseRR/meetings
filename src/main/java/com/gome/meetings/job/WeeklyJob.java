package com.gome.meetings.job;

import com.gome.meetings.model.RoomSchedule;
import com.gome.meetings.util.DateUtil;
import com.gome.meetings.vo.JobVo;
import com.gome.meetings.vo.JobsVo;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import lombok.extern.log4j.Log4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 每周执行一次的任务
 * @author xiehai1
 * @date 2016/12/26 11:32
 * @Copyright(c) gome inc Gome Co.,LTD
 */
@Log4j
public class WeeklyJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.debug("开始执行周任务!");
        // 触发时间
        Date fireTime = jobExecutionContext.getFireTime();
        // 从缓存中拿到所有的周任务列表
        JobsVo jobs = JobsLoader.me().getJobsByType("weekly");
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for(JobVo job : jobs.getJobs()){
            executorService.execute(new WeeklyThread(fireTime, job));
        }
        executorService.shutdown();
        log.debug("周任务执行结束!");
    }
}

/**
 * 单个周任务job线程
 */
@Log4j
class WeeklyThread implements Runnable{
    private Date fireTime;
    private JobVo jobVo;
    WeeklyThread(Date fireTime, JobVo jobVo){
        this.fireTime = fireTime;
        this.jobVo = jobVo;
    }
    @Override
    public void run() {
        log.debug(MessageFormat.format("开始创建任务【{0}】, 创建人【{1}】!", this.jobVo.getName(), this.jobVo.getPublisher()));
        // 通过会议室名字 查询找到会议室id
        final String roomName = this.jobVo.getRoomName();
        List<Record> rooms = Db.find("select * from room where name = ?", roomName);
        if(rooms.isEmpty()){
            return;
        }
        final Integer roomId = rooms.get(0).getInt("id");

        // 通过发布人名字 找到发布人id
        final String publisher = this.jobVo.getPublisher();
        List<Record> users = Db.find("select * from user where name = ?", publisher);
        if(users.isEmpty()){
            return;
        }
        final Integer userId = users.get(0).get("id");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.fireTime);
        // 设置会议开始日期
        calendar.add(Calendar.DAY_OF_YEAR, this.jobVo.getDayOfWeek());
        final String date = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
        // 开始时间字符串
        final String start = date + " " + this.jobVo.getStartTime();
        // 结束时间字符串
        final String end = date + " " + this.jobVo.getEndTime();
        // 通过触发时间 自动创建发布任务
        new RoomSchedule().set("start", DateUtil.parse(start, DateUtil.FORMAT_LONG))
                .set("end", DateUtil.parse(end, DateUtil.FORMAT_LONG))
                .set("subject", MessageFormat.format("{0}【{1}】", this.jobVo.getName(), "自动创建"))
                .set("userid", userId)
                .set("roomid", roomId)
                .set("create_date", new Date())
                .save();
        log.debug(MessageFormat.format("任务【{0}】, 创建人【{1}】, 创建结束!", this.jobVo.getName(), this.jobVo.getPublisher()));
    }
}