package com.xk.domain.vo.detail;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 成员和老师
 */
@lombok.Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Personnels {
    /**
     * 学生
     */
    private List<Student> students;
    /**
     * 老师
     */
    private List<Teacher> teacher;
}
