package com.cms.system;

import com.cms.system.api.domain.dto.SysDeptDto;
import com.cms.system.service.SysDeptService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 部门Service层测试类
 */
@SpringBootTest
public class SysDeptServiceTest {

    @Autowired
    private SysDeptService deptService;

    /**
     * 测试部门树
     */
    @Test
    void testDeptTree(){
        deptService.selectDeptTreeList(new SysDeptDto());
    }
}
