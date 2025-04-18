package com.xk.domain.dto;

import com.xk.check.annotations.RichText;
import lombok.Data;

import javax.validation.Valid;

@Data
public class TestDto {

    @RichText
    private String  text;
    @Valid
    private ApplyForB b;
}
