package com.gome.meetings.job;

import com.gome.meetings.vo.JobsVo;
import com.jfinal.kit.PathKit;
import lombok.extern.log4j.Log4j;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.text.MessageFormat;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 解析xml配置的定制定时任务<BR>
 * 只能job包能访问
 * @author xiehai1
 * @date 2016/12/26 17:04
 * @Copyright(c) gome inc Gome Co.,LTD
 */
@Log4j
public class JobsLoader {
    private static JobsLoader instance = null;
    private ConcurrentHashMap<String, JobsVo> jobs = new ConcurrentHashMap<>();
    /**
     * jobs配置文件路径
     */
    private static final String JOBS_PATH = "/jobs";

    private JobsLoader() {

    }

    /**
     * 单例模式
     *
     * @return
     */
    public static JobsLoader me() {
        if (null == instance) {
            synchronized (JobsLoader.class) {
                if (null == instance) {
                    instance = new JobsLoader();
                }
            }
        }


        return instance;
    }

    /**
     * 加载定制定时任务
     */
    public void load() {
        log.debug("开始加载定制定时任务列表!");
        File root = new File(PathKit.getRootClassPath() + JOBS_PATH);
        if (!root.exists()) {
            log.warn(MessageFormat.format("{0}路径不存在", root.getPath()));
            return;
        }
        File[] files = root.listFiles();
        if (files == null || files.length < 1) {
            log.debug("没有定制定时任务的xml配置文件");
        }

        for (File file : files)
            try {
                JobsVo parseJobs = new JobsVo();
                Serializer serializer = new Persister();
                serializer.read(parseJobs, file);
                if (this.jobs.containsKey(parseJobs.getType())) {
                    this.jobs.get(parseJobs.getType()).getJobs().addAll(parseJobs.getJobs());
                } else {
                    this.jobs.put(parseJobs.getType(), parseJobs);
                }
                log.info(MessageFormat.format("文件/jobs/{0}加载完成!", file.getName()));
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                log.error(MessageFormat.format("解析/jobs/{0}出错!", file.getName()));
                return;
            }
        log.info(this.jobs);
        log.debug("定制定时任务列表加载结束!");
    }

    /**
     * 通过定制定时任务类型获得jobs
     *
     * @param type
     * @return
     */
    public JobsVo getJobsByType(String type) {
        return this.jobs.get(type);
    }

    public static void main(String[] args) {
        new JobsLoader().load();
    }
}
