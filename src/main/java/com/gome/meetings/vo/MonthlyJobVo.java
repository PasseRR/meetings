package com.gome.meetings.vo;

import lombok.Data;
import lombok.ToString;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * 定时月任务
 * @author xiehai1
 * @date 2016/12/26 16:02
 * @Copyright(c) gome inc Gome Co.,LTD
 */
@Data
@ToString(callSuper = true)
@Root(name = "monthly")
public class MonthlyJobVo extends JobVo {
    /**
     * 每月的第几周 1~4
     */
    @Element(required = false)
    private Integer weekOfMonth;
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
     * 每月第几天 1~31
     */
    @Element(required = false)
    private Integer dayOfMonth;
    /**
     * 每月最后一周的周几
     */
    @Element(required = false)
    private Integer dayOfLastWeek;
}
