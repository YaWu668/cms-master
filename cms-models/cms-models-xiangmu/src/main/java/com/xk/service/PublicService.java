package com.xk.service;

import com.cms.common.core.web.domain.Response;
import com.xk.domain.dto.GetGroupNameDto;
import com.xk.domain.dto.PageDTO;
import com.xk.domain.dto.YearDataDto;
import com.xk.domain.dto.collegeListDto;

import java.util.List;

public interface PublicService {


    /**
     * 查询组名称
     * @param getGroupNameDto
     * @return
     */
    Response<String> getGroupName(GetGroupNameDto getGroupNameDto);


    /**
     * 查询组数据名称
     * @param getGroupNameDto
     * @return
     */
    Response<String> getGroupDataName(GetGroupNameDto getGroupNameDto);

}
