package com.gome.meetings.job;

import com.gome.meetings.vo.JobsVo;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 每月执行一次的任务
 * @author xiehai1
 * @date 2016/12/26 11:32
 * @Copyright(c) gome inc Gome Co.,LTD
 */
public class MonthlyJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobsVo jobs = JobsLoader.me().getJobsByType("monthly");
    }
}
