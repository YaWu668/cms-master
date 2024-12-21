package com.xk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xk.entity.Project;
import com.xk.service.ProjectService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;

@SpringBootTest
class ProjectServiceImplTest {

    @Autowired
    protected ProjectService itemService;





    public IPage<Project> getUsersByConditions(int currentPage, int pageSize, String name, Integer age) {
        // 创建分页对象
        Page<Project> page = new Page<>(currentPage, pageSize);

        // 创建查询条件
        QueryWrapper<Project> queryWrapper = new QueryWrapper<>();
        if (name != null && !name.isEmpty()) {
            queryWrapper.like("name", name);
        }
        if (age != null) {
            queryWrapper.eq("age", age);
        }

        // 执行分页查询
        return itemService.page(page, queryWrapper);
    }
}
