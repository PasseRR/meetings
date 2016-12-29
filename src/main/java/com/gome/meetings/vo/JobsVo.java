package com.gome.meetings.vo;

import lombok.Data;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义job集合vo
 * @author xiehai1
 * @date 2016/12/26 16:14
 * @Copyright(c) gome inc Gome Co.,LTD
 */
@Data
@Root(name = "jobs")
public class JobsVo {
    /**
     * 周任务数据
     */
    @ElementList(inline = true, required = false)
    private List<WeeklyJobVo> weeklyJobVos = new ArrayList<>();
    /**
     * 月任务数据
     */
    @ElementList(inline = true, required = false)
    private List<MonthlyJobVo> monthlyJobVos = new ArrayList<>();
}
