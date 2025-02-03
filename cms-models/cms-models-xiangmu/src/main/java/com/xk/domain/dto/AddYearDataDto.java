package com.xk.domain.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 根据年度组id新增年度数据
 */
@Data
public class AddYearDataDto {
    /**
     * 年度组的id
     */
    @NotNull(message = "年度组的id不能为空")
    private Long yearGroupId;
    /**
     * 年度数据的名称
     */
    @NotNull(message = "年度数据的名称不能为空")
    @Length(min = 5,max = 30,message = "年度数据的名称长度在5~30之间")
    @NotEmpty(message = "年度数据的名称不能为空")
    private String name;
    /**
     * 年度开始时间 格式 yyyy-MM-dd HH:mm:ss
     */

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @NotNull(message = "年度开始时间不能为空")
    private Date begin;
    /**
     * 年度结束时间
     */

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @NotNull(message = "年度结束时间不能为空")
    private Date end;
}
