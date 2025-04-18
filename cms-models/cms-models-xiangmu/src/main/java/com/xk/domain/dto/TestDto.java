package com.xk.domain.dto;

import com.xk.check.annotations.RichText;
import lombok.Data;

@Data
public class TestDto {

    @RichText
    private String  text;
}
