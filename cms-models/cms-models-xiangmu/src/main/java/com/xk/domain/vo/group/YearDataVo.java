package com.xk.domain.vo.group;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;

/**
 * 年度数据vo
 */
@Data
public class YearDataVo {
    /**
     * 年度数据的id
     */
    @TableId
    private Long yearDataId;
    /**
     * 年度组的id
     */
    private Long yearGroupId;
    /**
     * 年度数据的名称
     */
    private String name;
    /**
     * 年度开始时间 格式 yyyy-MM-dd HH:mm:ss
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date begin;
    /**
     * 年度结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date end;
}
