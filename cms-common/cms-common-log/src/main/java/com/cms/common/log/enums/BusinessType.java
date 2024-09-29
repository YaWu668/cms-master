package com.cms.common.log.enums;

/**
 * 业务操作类型
 *
 * @author 邓志军
 * @date 2024年5月28日23:40:22
 */
public enum BusinessType {
    /**
     * 其它
     */
    OTHER,

    /**
     * 新增
     */
    INSERT,

    /**
     * 修改
     */
    UPDATE,

    /**
     * 删除
     */
    DELETE,

    /**
     * 授权
     */
    GRANT,

    /**
     * 导出
     */
    EXPORT,

    /**
     * 导入
     */
    IMPORT,

    /**
     * 强退
     */
    FORCE,

    /**
     * 上传
     */
    UPLOAD,

    /**
     * 下载
     */
    DOWNLOAD,

    /**
     * 清空数据
     */
    CLEAN,
}
