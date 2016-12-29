package com.gome.meetings.vo;

import lombok.Data;
import org.simpleframework.xml.Element;

/**
 * job基类属性
 * @author xiehai1
 * @date 2016/12/29 17:44
 * @Copyright(c) gome inc Gome Co.,LTD
 */
@Data
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
     * job生效开始日期 格式yyyy-MM-dd
     */
    @Element
    private String effectiveDate;
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
    /**
     * 周期间隔 如果为周 则为间隔几周 若为月 则为间隔几个月<BR>
     * 默认为1
     */
    @Element(required = false)
    private Integer interval = 1;
}
