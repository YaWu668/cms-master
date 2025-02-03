package com.xk.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class MemberDTO {
    /**
     * 学生人数,第一个必须是负责人,否则抛出异常
     */
    @Valid
    @NotNull(message = "学生报名人数不能为空")
    @Size( min = 1,message = "学生报名人数不能少于1")
    @JsonProperty("students")
    private List<ApplyForStudent> students;
    /**
     * 老师
     */
    @Valid
    @NotNull(message = "老师报名人数不能为空")
    @Size( min = 1,message = "老师报名人数不能少于1")
    private List<ApplyForTeacher> teachers;

}
