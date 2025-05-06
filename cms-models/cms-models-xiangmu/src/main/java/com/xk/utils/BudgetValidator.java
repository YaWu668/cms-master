package com.xk.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import cn.hutool.core.util.StrUtil;
import com.cms.common.core.exception.ServiceException;
import com.xk.domain.dto.BudgetItem;

/**
 * 预算校验工具类（批量收集错误并统一抛出）
 */
public class BudgetValidator {


    /**
     * 对预算列表和总金额进行校验，收集所有错误后统一抛出
     *
     * @param items      预算项列表
     * @param totalMoney "申请金额合计"字符串
     * @throws ServiceException 校验不通过时抛出，异常消息包含所有错误行及原因
     */
    public static void validate(List<BudgetItem> items, String totalMoney) {
        if (items == null || items.isEmpty()) {
            throw new ServiceException("预算项列表不能为空");
        }

        List<String> errorList = new ArrayList<>();
        // 行号映射: id -> 行号
        Map<Long, Integer> rowMap = new HashMap<>();
        for (int i = 0; i < items.size(); i++) {
            rowMap.put(items.get(i).getId(), i + 1);
        }

        // 构建父节点 -> 子节点列表映射
        Map<Long, List<BudgetItem>> childrenMap = new HashMap<>();
        for (BudgetItem item : items) {
            Long pid = item.getParentId();
            if (pid != null) {
                childrenMap.computeIfAbsent(pid, k -> new ArrayList<>()).add(item);
            }
        }

        // 1. 行内自洽校验
        for (int i = 0; i < items.size(); i++) {
            BudgetItem item = items.get(i);
            int row = i + 1;
            BigDecimal bf = item.getBudgetFunds();        // 预算经费
            BigDecimal prev = item.getPreviousStage();   // 前期阶段的金额
            BigDecimal post = item.getPostStage();       // 后期阶段的金额
            String name = item.getName();                 // 开支科目
            if (bf.compareTo(prev.add(post)) != 0) {
                errorList.add(String.format(
                        "第%d行(开支科目=%s): 行内自洽校验失败(预算经费=%s, 前期阶段的金额=%s, 后期阶段的金额=%s)",
                        row, name, bf, prev, post));
            }
        }

        // 2. 父子加总校验
        for (Map.Entry<Long, List<BudgetItem>> entry : childrenMap.entrySet()) {
            Long parentId = entry.getKey();
            List<BudgetItem> children = entry.getValue();
            BudgetItem parent = items.stream()
                    .filter(it -> it.getId().equals(parentId))
                    .findFirst().orElse(null);
            if (parent != null) {
                int parentRow = rowMap.getOrDefault(parentId, -1);
                String parentName = parent.getName();      // 开支科目
                BigDecimal sumBf = BigDecimal.ZERO;
                BigDecimal sumPrev = BigDecimal.ZERO;
                BigDecimal sumPost = BigDecimal.ZERO;
                for (BudgetItem c : children) {
                    sumBf = sumBf.add(c.getBudgetFunds());
                    sumPrev = sumPrev.add(c.getPreviousStage());
                    sumPost = sumPost.add(c.getPostStage());
                }
                if (parent.getBudgetFunds().compareTo(sumBf) != 0) {
                    errorList.add(String.format(
                            "第%d行(开支科目=%s): 父子加总校验失败(预算经费父=%s, 子合计=%s)",
                            parentRow, parentName, parent.getBudgetFunds(), sumBf));
                }
                if (parent.getPreviousStage().compareTo(sumPrev) != 0) {
                    errorList.add(String.format(
                            "第%d行(开支科目=%s): 父子加总校验失败(前期阶段的金额父=%s, 子合计=%s)",
                            parentRow, parentName, parent.getPreviousStage(), sumPrev));
                }
                if (parent.getPostStage().compareTo(sumPost) != 0) {
                    errorList.add(String.format(
                            "第%d行(开支科目=%s): 父子加总校验失败(后期阶段的金额父=%s, 子合计=%s)",
                            parentRow, parentName, parent.getPostStage(), sumPost));
                }
            }
        }

        // 3. 全局总和校验
        BigDecimal sumTop = items.stream()
                .filter(item -> item.getParentId() == null)
                .map(BudgetItem::getBudgetFunds)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal total;
        try {
            total = new BigDecimal(totalMoney.replaceAll(",", ""));
        } catch (Exception e) {
            errorList.add("申请金额合计格式不正确: " + totalMoney);
            total = BigDecimal.ZERO;
        }
        if (sumTop.compareTo(total) != 0) {
            errorList.add(String.format(
                    "全局总和校验失败: 顶级预算经费合计=%s, 申请金额合计=%s",
                    sumTop, total));
        }

        if (!errorList.isEmpty()) {
            throw new ServiceException(String.join(";\n", errorList));
        }
    }

    /**
     * Main方法：示例测试用例
     */
    public static void main(String[] args) {

        System.out.println("第1行(开支科目=1. 业务费): 行内自洽校验失败(预算经费=1, 前期阶段的金额=11, 后期阶段的金额=111);\n第2行(开支科目=(1) 计算、分析、测试费): 行内自洽校验失败(预算经费=11, 前期阶段的金额=11, 后期阶段的金额=11);\n第3行(开支科目=(2) 能源动力费): 行内自洽校验失败(预算经费=11, 前期阶段的金额=111, 后期阶段的金额=11);\n第4行(开支科目=(3) 会议、差旅费): 行内自洽校验失败(预算经费=11, 前期阶段的金额=22, 后期阶段的金额=11);\n第5行(开支科目=(4) 文献检索费): 行内自洽校验失败(预算经费=22, 前期阶段的金额=22, 后期阶段的金额=22);\n第6行(开支科目=(5) 论文出版费): 行内自洽校验失败(预算经费=22, 前期阶段的金额=22, 后期阶段的金额=22);\n第7行(开支科目=2. 仪器设备购置费): 行内自洽校验失败(预算经费=22, 前期阶段的金额=22, 后期阶段的金额=22);\n第8行(开支科目=3. 实验装置试制费): 行内自洽校验失败(预算经费=22, 前期阶段的金额=22, 后期阶段的金额=22);\n第9行(开支科目=4. 材料费): 行内自洽校验失败(预算经费=22, 前期阶段的金额=22, 后期阶段的金额=222);\n第1行(开支科目=1. 业务费): 父子加总校验失败(预算经费父=1, 子合计=77);\n第1行(开支科目=1. 业务费): 父子加总校验失败(前期阶段的金额父=11, 子合计=188);\n第1行(开支科目=1. 业务费): 父子加总校验失败(后期阶段的金额父=111, 子合计=77);\n全局总和校验失败: 顶级预算经费合计=67, 申请金额合计=100");
      /*  // 测试1：单行有效
        List<BudgetItem> items = new ArrayList<>();
        items.add(new BudgetItem(1L, null, "科目A", "100", "用途", "40", "60"));
        try {
            validate(items, "100");
            System.out.println("测试1通过");
        } catch (ServiceException e) {
            System.err.println("测试1失败: " + e.getMessage());
        }

        // 测试2：行内自洽失败
        items.clear();
        items.add(new BudgetItem(1L, null, "科目B", "100", "用途", "30", "60"));
        try {
            validate(items, "100");
            System.out.println("测试2通过");
        } catch (ServiceException e) {
            System.err.println("测试2失败: " + e.getMessage());
        }

        // 测试3：父子合计有效
        items.clear();
        items.add(new BudgetItem(1L, null, "父科目", "300", "用途", "100", "200"));
        items.add(new BudgetItem(2L, 1L, "子科目1", "100", "用途", "50", "50"));
        items.add(new BudgetItem(3L, 1L, "子科目2", "200", "用途", "50", "150"));
        try {
            validate(items, "310");
            System.out.println("测试3通过");
        } catch (ServiceException e) {
            System.err.println("测试3失败: " + e.getMessage());
        }

        // 测试4：父子合计失败
        items.clear();
        items.add(new BudgetItem(1L, null, "父科目", "300", "用途", "100", "200"));
        items.add(new BudgetItem(2L, 1L, "子科目1", "100", "用途", "50", "50"));
        items.add(new BudgetItem(3L, 1L, "子科目2", "100", "用途", "50", "25"));
        try {
            validate(items, "300");
            System.out.println("测试4通过");
        } catch (ServiceException e) {
            System.err.println("测试4失败: " + e.getMessage());
        }*/
    }
}
