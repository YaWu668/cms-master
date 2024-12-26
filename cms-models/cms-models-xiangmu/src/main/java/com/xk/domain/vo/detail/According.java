package com.xk.domain.vo.detail;

import lombok.experimental.Accessors;

/**
 * 立项依据
 */
@lombok.Data
@Accessors(chain = true)
public class According {
    /**
     * a,b,c 三选一
     */
    private ADetail a;
    /**
     * a,b,c 三选一
     */
    private BDetail b;
    /**
     * a,b,c 三选一
     */
    private CDetail c;
}
