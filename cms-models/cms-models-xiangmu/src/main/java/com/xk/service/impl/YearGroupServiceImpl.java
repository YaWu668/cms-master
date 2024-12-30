package com.xk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cms.common.core.exception.ServiceException;
import com.cms.common.core.web.domain.Response;
import com.xk.domain.dto.YearDataDto;
import com.xk.domain.dto.yearListDTO;
import com.xk.domain.vo.group.YearGroupVo;
import com.xk.entity.YearData;
import com.xk.entity.YearGroup;
import com.xk.mapper.YearGroupMapper;
import com.xk.service.YearDataService;
import com.xk.service.YearGroupService;
import com.xk.utils.BeanCopyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 年度组表(YearGroup)表服务实现类
 *
 * @author makejava
 * @since 2024-12-05 22:47:31
 */
@Service
@RequiredArgsConstructor
public class YearGroupServiceImpl extends ServiceImpl<YearGroupMapper, YearGroup> implements YearGroupService {
    /**
     * 年度数据的service
     */
    private final YearDataService yearDataService;
    @Override
    public Response<List<yearListDTO> > yearList(String name) {
        //1.获取全部年度组没有屏蔽的年度数据
        List<YearGroup> activeYearGroups = this.getActiveYearGroups(name);
        //获取年度组对应的年度数据
        List<yearListDTO> yearList = this.getYearList(activeYearGroups);
        return Response.success(yearList);
    }

    private List<yearListDTO> getYearList(List<YearGroup> activeYearGroups) {
        //1.年度组转id
        List<Long> yearGroupIds = activeYearGroups.stream().map(YearGroup::getYearGroupId).collect(Collectors.toList());
        //2.根据年度组id获取年度数据
        List<YearData> yearDatas = yearDataService.selectBatchyearGroupIds(yearGroupIds);
        //3.把年度组年度数据封装到年度列表dto中
        return YearGroupAndYearDataToList(activeYearGroups, yearDatas);
    }

    /**
     * 把年度组,年度数,据封装到年度列表dto中
     * @param activeYearGroups 年度组
     * @param yearDatas 年度数据
     * @return 年度列表dto
     */
    private List<yearListDTO> YearGroupAndYearDataToList(List<YearGroup> activeYearGroups, List<YearData> yearDatas) {
        //1.先分组年度组
        List<yearListDTO> yearListDTOS = BeanCopyUtils.copyBeans(activeYearGroups, yearListDTO.class);
        //2.根据年度组过滤出来年度数据,再进行一次封装
        yearListDTOS.forEach(yearListDTO -> {
            //3.根据年度组id过滤出年度数据
            List<YearData> list = yearDatas.stream()
                    .filter(yearData ->
                            yearData.getYearGroupId().equals(yearListDTO.getYearGroupId()))
                    .collect(Collectors.toList());
            //4.把年度数据封装到年度列表dto中
            List<YearDataDto> yearData = BeanCopyUtils.copyBeans(list, YearDataDto.class);
            //5.设置年度数据到年度列表dto中
            yearListDTO.setYearDatas(yearData);
        });
        return yearListDTOS;
    }

    @Override
    public List<YearGroup> getActiveYearGroups(String name) {
        LambdaQueryWrapper<YearGroup> yearGroupLambdaQueryWrapper = new LambdaQueryWrapper<YearGroup>()
                .eq(YearGroup::getStatus, 0)
                .like(name != null && name.length() > 0, YearGroup::getName, name)
                .orderByDesc(YearGroup::getCreateTime);
        return this.list(yearGroupLambdaQueryWrapper);
    }

    @Override
    public List<YearGroup> selectBatchyearGroupIds(List<Long> ids) {
        List<Long> list = ids.stream()
                .distinct()
                .collect(Collectors.toList());
        // 检查列表是否为空
        if (list.isEmpty()) {
            return Collections.emptyList(); // 返回空结果
        }
        return this.lambdaQuery()
                .in(YearGroup::getYearGroupId, list)
                .list();
    }

    @Override
    public Response<YearGroupVo> selectByIdYearGroup(Long yearGroupId) {
        if(yearGroupId == null){
            throw  new ServiceException("年度组id不能为空",400);
        }
        List<YearGroup> list = this.lambdaQuery()
                .eq(YearGroup::getYearGroupId, yearGroupId)
                .list();
        if(list == null || list.size() == 0){
            throw new ServiceException("年度组不存在",400);
        }
        YearGroupVo yearGroupVo = BeanCopyUtils.copyBean(list.get(0), YearGroupVo.class);
        return Response.success(yearGroupVo);
    }
}

