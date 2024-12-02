package com.xk.utils;

import org.omg.CORBA.SystemException;
import org.springframework.validation.BindingResult;

public class ValidUtils {

    /**
     * 参数验证
     * @param bindingResult
     * @return
     */
    /*public static boolean isValid(BindingResult bindingResult) {
        // 参数验证
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().stream()
                    .forEach(fieldError -> {
                        throw new SystemException(, fieldError.getDefaultMessage());
                    });
        }
    }*/
}
