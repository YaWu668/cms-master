package com.xk.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 立项已经校验
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class BasisForTheProjectDTO {

    /**
     * a,b,c 三选一
     */
    @Valid
    private ApplyForA a;
    /**
     * a,b,c 三选一
     */
    @Valid
    private ApplyForB b;
    /**
     * a,b,c 三选一
     */
    @Valid
    private ApplyForC c;
}
