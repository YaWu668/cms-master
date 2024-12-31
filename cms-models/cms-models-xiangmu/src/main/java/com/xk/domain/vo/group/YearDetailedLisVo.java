package com.xk.domain.vo.group;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xk.entity.YearData;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 年度组的详细信息
 */
@Data
public class YearDetailedLisVo {
    /**
     * 年度组的id
     */
    private Long yearGroupId;
    /**
     * 年度组的名称
     */
    private String name;
    /**
     * 状态(0正常 1停用)停用状态,无法添加年度组人员和给项目绑定
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
     * 年度组的数据
     */
    private List<YearDataVo> yearDataList;
}
