package com.gome.meetings.job;

import com.gome.meetings.vo.JobsVo;
import com.gome.meetings.vo.MonthlyJobVo;
import com.gome.meetings.vo.WeeklyJobVo;
import com.jfinal.kit.PathKit;
import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 解析xml配置的定制定时任务<BR>
 * 只能job包能访问
 *
 * @author xiehai1
 * @date 2016/12/26 17:04
 * @Copyright(c) gome inc Gome Co.,LTD
 */
@Log4j
public class JobsLoader {
    private static JobsLoader instance = null;
    @Getter
    private List<WeeklyJobVo> weeklyJobVos = new ArrayList<>();
    @Getter
    private List<MonthlyJobVo> monthlyJobVos = new ArrayList<>();
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

        log.info(MessageFormat.format("加载目录{0}", JOBS_PATH));
        this.loadFilesAndDirectory(root, files);

        log.info(MessageFormat.format("周任务列表:{0}", this.weeklyJobVos));
        log.info(MessageFormat.format("月任务列表:{0}", this.monthlyJobVos));
        log.debug("定制定时任务列表加载结束!");
    }

    /**
     * 加载目录下的xml定制定时任务配置文件
     * @param root 根目录
     * @param files 根目录下的文件
     */
    private void loadFilesAndDirectory(File root, File[] files) {
        if (files == null || files.length < 1) {
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                log.info(MessageFormat.format("加载目录/{0}/{1}", root.getName(), file.getName()));
                this.loadFilesAndDirectory(file, file.listFiles());
            } else {
                if (file.getName().endsWith(".xml")) {
                    try {
                        JobsVo parseJobs = new JobsVo();
                        Serializer serializer = new Persister();
                        serializer.read(parseJobs, file);
                        if(!parseJobs.getWeeklyJobVos().isEmpty()){
                            this.weeklyJobVos.addAll(parseJobs.getWeeklyJobVos());
                        }

                        if(!parseJobs.getMonthlyJobVos().isEmpty()){
                            this.monthlyJobVos.addAll(parseJobs.getMonthlyJobVos());
                        }
                        log.info(MessageFormat.format("文件/{0}加载完成!", file.getName()));
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                        log.error(MessageFormat.format("解析/{0}出错!", file.getName()));
                        return;
                    }
                } else {
                    log.info(MessageFormat.format("跳过文件/{0}", file.getName()));
                }
            }
        }
    }

    public static void main(String[] args) {
        new JobsLoader().load();
    }
}
