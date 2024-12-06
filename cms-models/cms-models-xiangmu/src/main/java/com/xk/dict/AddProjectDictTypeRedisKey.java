package com.xk.dict;

import java.util.ArrayList;
import java.util.List;

/**
 * 字典类型redis key
 */
public class AddProjectDictTypeRedisKey {
    /**
     * 前置key
     */
    private static final String PRE_KEY = "sys_dict:";

    /**
     *项目类型redis key
     */
    public static final String PROJECT_TYPE_KEY = PRE_KEY + "xm_item_type";

    /**
     * 项目级别redis key
     */
    public static final String PROJECT_LEVEL_KEY = PRE_KEY + "xm_item_rank";

    /**
     * 项目类别redis key
     */
    public static final String PROJECT_CATEGORY_KEY = PRE_KEY + "xm_item_category";

    /**
     * 学科类别redis key
     */
    public static final String SUBJECT_CATEGORY_KEY = PRE_KEY + "xm_item_subject_category";

    /**
     * 项目来源redis key
     */
    public static final String PROJECT_SOURCE_KEY = PRE_KEY + "xm_item_project_source";

    /**
     * 性别redis key
     */
    public static final String GENDER_KEY = PRE_KEY + "sys_user_sex";
    /**
     * 民族redis key
     */
    public static final String NATION_KEY = PRE_KEY + "xm_item_nation";

    /**
     * 把公开范围的key,已集合形式返回
     */
    public static ArrayList<String> getRedisKeys() {
        ArrayList<String> keys = new ArrayList<>();
        keys.add(PROJECT_TYPE_KEY);
        keys.add(PROJECT_LEVEL_KEY);
        keys.add(PROJECT_CATEGORY_KEY);
        keys.add(SUBJECT_CATEGORY_KEY);
        keys.add(PROJECT_SOURCE_KEY);
        keys.add(GENDER_KEY);
        keys.add(NATION_KEY);
        return  keys;
    }
}
