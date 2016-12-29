package com.gome.meetings.vo;

import lombok.Data;
import lombok.ToString;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * 定时周任务
 * @author xiehai1
 * @date 2016/12/26 16:02
 * @Copyright(c) gome inc Gome Co.,LTD
 */
@Data
@ToString(callSuper = true)
@Root(name = "weekly")
public class WeeklyJobVo extends JobVo {
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
    @Element
    private Integer dayOfWeek;
}
