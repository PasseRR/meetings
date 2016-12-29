package com.gome.meetings.vo;

import lombok.Data;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * 自定义任务vo
 * @author xiehai1
 * @date 2016/12/26 16:02
 * @Copyright(c) gome inc Gome Co.,LTD
 */
@Data
@Root(name = "job")
public class JobVo {
    /**
     * 任务名
     */
    @Element
    private String name;
    /**
     * 会议室名
     */
    @Element
    private String roomName;
    /**
     * 发布人
     */
    @Element
    private String publisher;
    /**
     * 每月的第几周 1~4
     */
    @Element(required = false)
    private Integer weekOfMonth;
    /**
     * job开始执行日期 只有在隔周执行才会需要
     */
    @Element(required = false)
    private String startDate;
    /**
     * 星期几<BR>
     * 星期天 0<BR>
     * 星期一 1<BR>
     * 星期二 2<BR>
     * 星期三 3<BR>
     * 星期四 4<BR>
     * 星期五 5<BR>
     * 星期六 6<BR>
     */
    @Element(required = false)
    private Integer dayOfWeek;
    /**
     * 日期 1~31
     */
    @Element(required = false)
    private Integer dayOfMonth;
    /**
     * 会议开始时间
     */
    @Element
    private String startTime;
    /**
     * 会议结束时间
     */
    @Element
    private String endTime;
}
