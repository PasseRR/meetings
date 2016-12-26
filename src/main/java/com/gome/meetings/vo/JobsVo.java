package com.gome.meetings.vo;

import lombok.Data;
import org.simpleframework.xml.Attribute;
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
     * 集合类型
     */
    @Attribute(name = "type")
    private String type;
    /**
     * 具体job数据
     */
    @ElementList(inline = true, required = false)
    private List<JobVo> jobs = new ArrayList<>();
}
