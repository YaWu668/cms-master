package com.xk.utils;

import cn.hutool.core.collection.CollUtil;
import com.cms.common.core.exception.ServiceException;
import com.xk.domain.dto.BudgetItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ProgramFundsFormatUtils {

    //==============校验金额====================



    // ===============情况一======================
    private static final List<String> TEMPLATE = Arrays.asList(
            "1. 业务费",
            "(1) 计算、分析、测试费",
            "(2) 能源动力费",
            "(3) 会议、差旅费",
            "(4) 文献检索费",
            "(5) 论文出版费",
            "2. 仪器设备购置费",
            "3. 实验装置试制费",
            "4. 材料费"
    );

    public static void isValid(List<BudgetItem> items) {
        if (CollUtil.isEmpty(items)) {
            throw new ServiceException("请输入正确的经费预算, 不能为空");
        }
        if (items.size() != TEMPLATE.size()) {
            throw new ServiceException("经费预算行数错误，应为 "
                    + TEMPLATE.size() + " 行");
        }
        ArrayList<String> errorList = new ArrayList<>();
        for (int i = 0; i < TEMPLATE.size(); i++) {
            Long index = i+1L;
            if (!Objects.equals(items.get(i).getName(), TEMPLATE.get(i))) {
                errorList.add("第"+index+"行的《开支科目》描述不对,正确的描述为:"+TEMPLATE.get(i)+"\n 你当前错误的描述为:"+items.get(i).getName());
            }
            //校验当前节点的id,id的值从1开始

            if (!Objects.equals(items.get(i).getId(), index)) {
                errorList.add("第"+index+"行的《预算对象》的的id不对,正确的id为:"+(i+1)+"\n 你当前错误的id为:"+items.get(i).getId());
            }
            // 如果还要校验 parentId：
            Long expectedParent = deriveParentIdByIndex(i);
            if (!Objects.equals(items.get(i).getParentId(), expectedParent)) {
                errorList.add("第"+index+"行的《预算对象》的父级id不对,正确的父级id为:"+deriveParentIdByIndex(i)+"\n 你当前错误的父级id为:"+items.get(i).getParentId());
            }
        }

        //  如果还有错误，则抛出异常
        if(CollUtil.isNotEmpty(errorList)){
            throw new ServiceException("数据错误异常:"+CollUtil.join(errorList,";\n"));
        }

    }




    private static Long deriveParentIdByIndex(int i) {
        // 下标 0 对应 "1. 业务费"，它是顶级，parentId = null
        if (i == 0) return null;
        // 下标 1~5 都 parentId = "1"
        if (i >= 1 && i <= 5) return 1L;
        // 下标 6~9 parentId = null ("2. 仪器…")
        return null;
    }


    // 情况二的标准模板
    private static final List<String> TEMPLATE_CASE2 = Arrays.asList(
            "1. 业务费",
            "(1) 能源动力费",
            "(2) 会议费",
            "(3) 差旅费",
            "(4) 文献检索费",
            "(5) 论文出版费",
            "2. 仪器设备购置费",
            "3. 材料费"
    );

    public static void isValidCase2(List<BudgetItem> items) {
        if (CollUtil.isEmpty(items)) {
            throw new ServiceException("请输入正确的经费预算, 不能为空");
        }
        if (items.size() != TEMPLATE_CASE2.size()) {
            throw new ServiceException("经费预算行数错误，应为 "
                    + TEMPLATE_CASE2.size() + " 行");
        }

        ArrayList<String> errorList = new ArrayList<>();

        for (int i = 0; i < TEMPLATE_CASE2.size(); i++) {
            Long index = i+1L;
            if (!Objects.equals(items.get(i).getName(), TEMPLATE_CASE2.get(i))) {
                errorList.add("第"+index+"行的《开支科目》描述不对,正确的描述为:"+TEMPLATE_CASE2.get(i)+"\n 你当前错误的描述为:"+items.get(i).getName());
            }
            // 校验当前节点的id,id的值从1开始
            if (!Objects.equals(items.get(i).getId(), index)) {
                errorList.add("第"+index+"行的《预算对象》的的id不对,正确的id为:"+(i+1)+"\n 你当前错误的id为:"+items.get(i).getId());
            }
            //校验父节点的id是否正确
            Long deriveParentIdByIndexCase2 = deriveParentIdByIndexCase2(i);
            if (!Objects.equals(items.get(i).getParentId(),deriveParentIdByIndexCase2 )) {
                errorList.add("第"+index+"行的《预算对象》的父级id不对,正确的父级id为:"+deriveParentIdByIndexCase2(i)+"\n 你当前错误的父级id为:"+items.get(i).getParentId());
            }
        }
        //  如果还有错误，则抛出异常
        if(CollUtil.isNotEmpty(errorList)){
            throw new ServiceException("数据错误异常:"+CollUtil.join(errorList,";\n"));
        }
    }
    private static Long deriveParentIdByIndexCase2(int i) {
        // 0 行（"1. 业务费"）顶级
        if (i == 0) return null;
        // 下标 1~5 都是 1. 业务费 的子项
        if (i >= 1 && i <= 5) return 1L;
        // 下标 6 ("2. 仪器设备购置费")、7 ("3. 材料费") 都是顶级
        return null;
    }

    // ======= 情况三 模板 ============
    private static final List<String> TEMPLATE_CASE3 = Arrays.asList(
            "1. 业务费",
            "(1) 能源动力费",
            "(2) 会议费",
            "(3) 差旅费",
            "(4) 文献检索费",
            "(5) 论文出版费",
            "2. 仪器设备购置费",
            "3. 材料费",
            "4. 咨询费"
    );
    public static void isValidCase3(List<BudgetItem> items) {
        if (CollUtil.isEmpty(items)) {
            throw new ServiceException("请输入正确的经费预算, 不能为空");
        }
        if (items.size() != TEMPLATE_CASE3.size()) {
            throw new ServiceException("经费预算行数错误，应为 "
                    + TEMPLATE_CASE3.size() + " 行");
        }
        ArrayList<String> errorList = new ArrayList<>();
        for (int i = 0; i < TEMPLATE_CASE3.size(); i++) {
            Long index = i+1L;
            if (!Objects.equals(items.get(i).getName(), TEMPLATE_CASE3.get(i))) {
                errorList.add("第"+index+"行的《开支科目》描述不对,正确的描述为:"+TEMPLATE_CASE3.get(i)+"\n 你当前错误的描述为:"+items.get(i).getName());
            }
            if (!Objects.equals(items.get(i).getId(), index)) {
                errorList.add("第"+index+"行的《预算对象》的的id不对,正确的id为:"+(i+1)+"\n 你当前错误的id为:"+items.get(i).getId());
            }
            Long deriveParentIdByIndexCase3 = deriveParentIdByIndexCase3(i);
            if (!Objects.equals(items.get(i).getParentId(),deriveParentIdByIndexCase3 )) {
                errorList.add("第"+index+"行的《预算对象》的父级id不对,正确的父级id为:"+deriveParentIdByIndexCase3(i)+"\n 你当前错误的父级id为:"+items.get(i).getParentId());
            }
        }
        //  如果还有错误，则抛出异常
        if(CollUtil.isNotEmpty(errorList)){
            throw new ServiceException("数据错误异常:"+CollUtil.join(errorList,";\n"));
        }
    }
    private static Long deriveParentIdByIndexCase3(int i) {
         // 0 行（"1. 业务费"）顶级
        if (i == 0) return null;
        // 下标 1~5 都是 1. 业务费 的子项
        if (i >= 1 && i <= 5) return 1L;
        // 下标 6 ("2. 仪器设备购置费")、7 ("3. 材料费") 、8 ("4. 咨询费") 都是顶级
        return null;
    }
}
