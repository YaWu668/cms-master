package com.xk.domain.dto;

import cn.hutool.core.util.StrUtil;
import com.cms.common.core.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 * 项目预算对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class BudgetItem {
    private static final Pattern MONEY_PATTERN = Pattern.compile("^(0|[1-9]\\d*)(\\.\\d{1,2})?$");

    /**
     * 项目预算id ,比如 "1"、"2"、"3"……
     */
    @NotNull(message = "项目预算id不能为空")
    private Long id;
    /**
     * 父项 ,顶级行为 null；子项行填它最近的父行 id
     */
    private Long parentId;
    /**
     * 开支科目
     */
    @NotNull(message = "开支科目不能为空")
    private String name;
    /**
     * 预算经费(元)
     */
    @NotNull(message = "预算经费不能为空")
    @javax.validation.constraints.Pattern(regexp = "^[1-9]\\d*$", message = "预算经费请填写正数")
    private String budgetFunds;
    /**
     * 主要用途
     */
    @NotBlank(message = "主要用途不能为空")
    private String mainPurpose;
    /**
     * 前期阶段的费用
     */
    @NotBlank(message = "前期阶段不能为空")
    @javax.validation.constraints.Pattern(regexp = "^[1-9]\\d*$", message = "前期阶段的费用请填写正数")
    private String previousStage;
    /**
     * 后期阶段
     */
    @NotBlank(message = "后期阶段不能为空")
    @javax.validation.constraints.Pattern(regexp = "^[1-9]\\d*$", message = "后期阶段的费用请填写正数")
    private String postStage;

    private  boolean isValidMoney(String str) {
        if (str == null) {
            return false;
        }
        return MONEY_PATTERN.matcher(str).matches();
    }

    /**
     * 预算经费
     * @return
     */
    public BigDecimal getBudgetFunds() {
        if (StrUtil.isBlank(budgetFunds)) {
            return new BigDecimal(0);
        }else if(!isValidMoney(budgetFunds)){//使用正则表达式校验, 判断是否为数字
            throw new ServiceException("非法金额格式: " + budgetFunds);
        }
        return new BigDecimal(budgetFunds);
    }

    /**
     * 前期阶段的金额
     * @return
     */
    public BigDecimal getPreviousStage() {
        if (StrUtil.isBlank(previousStage)) {
            return new BigDecimal(0);
        }else if(!isValidMoney(budgetFunds)){//使用正则表达式校验, 判断是否为数字
            throw new ServiceException("非法金额格式: " + budgetFunds);
        }
        return new BigDecimal(previousStage);
    }

    /**
     * 后期阶段的金额
     * @return
     */
    public BigDecimal getPostStage() {
        if (StrUtil.isBlank(postStage)) {
            return new BigDecimal(0);
        }else if(!isValidMoney(budgetFunds)){//使用正则表达式校验, 判断是否为数字
            throw new ServiceException("非法金额格式: " + budgetFunds);
        }
        return new BigDecimal(postStage);
    }
}
