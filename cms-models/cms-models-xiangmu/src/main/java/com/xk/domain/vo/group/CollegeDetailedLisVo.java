package com.xk.domain.vo.group;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 *学院详细信息列表
 */
@Data
public class CollegeDetailedLisVo {
    /**
     * 学院的id
     */
    private Long collegeGroupId;
    /**
     * 学院组名称
     */
    private String name;
    /**
     * 状态(0正常 1停用)
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;
    /**
     * 创建人的账号
     */
    private String createBy;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 更新人的账号
     */
    private String updateBy;
    /**
     * 学院组的人员
     */
    private List<CollegeDataVo> collegeDataVoList;
}
