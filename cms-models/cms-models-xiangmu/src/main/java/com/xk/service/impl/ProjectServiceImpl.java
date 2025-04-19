package com.xk.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.remote.response.ResponseCode;
import com.alibaba.nacos.shaded.com.google.gson.Gson;
import com.alibaba.nacos.shaded.com.google.gson.reflect.TypeToken;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cms.common.core.enums.HttpStatusResponse;
import com.cms.common.core.exception.ServiceException;
import com.cms.common.core.utils.ExcelUtils;
import com.cms.common.core.web.domain.Response;
import com.cms.common.security.utils.SecurityUtils;
import com.cms.system.api.domain.pojo.SysDictData;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.Pictures;
import com.deepoove.poi.policy.HackLoopTableRenderPolicy;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xk.client.SysUserClient;
import com.xk.config.*;
import com.xk.constant.ProjectConstant;
import com.xk.constant.RoleConstant;
import com.xk.domain.dto.*;

import com.xk.domain.vo.Execl.ListExportVo;
import com.xk.domain.vo.ListExportExeclVo;
import com.xk.domain.vo.api.UserInfoVo;
import com.xk.domain.vo.detail.*;
import com.xk.domain.vo.project.ProjectListvo;
import com.xk.domain.vo.student.StudentProjectVo;
import com.xk.domain.dto.ApplyForDTO;
import com.xk.domain.dto.ApplyForStudent;
import com.xk.domain.dto.ApplyForTeacher;
import com.xk.domain.dto.ProjectAuditDto;

import com.xk.domain.vo.detail.*;
import com.xk.domain.vo.student.StudentVo;
import com.xk.entity.*;
import com.xk.enums.ProjectStatusEnum;
import com.xk.mapper.*;
import com.xk.service.*;
import com.xk.utils.BeanCopyUtils;
import com.xk.utils.BeanUtils;
import com.xk.utils.ResponseStreamUtil;
import com.xk.utils.ZipUtils;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.PictureRenderData;
import com.deepoove.poi.data.PictureType;
import com.deepoove.poi.data.Pictures;
import com.deepoove.poi.policy.HackLoopTableRenderPolicy;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xk.client.SysFileClient;
import com.alibaba.nacos.shaded.com.google.gson.Gson;
import com.alibaba.nacos.shaded.com.google.gson.reflect.TypeToken;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

import static com.xk.constant.ProjectConstant.DICT_TYPE_PROJECT_RANK;
import static com.xk.constant.ProjectConstant.DICT_TYPE_PROJECT_TYPE;

/**
 * 项目表(Project)表服务实现类
 *
 * @author yawu
 */
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService {
    /**
     *学生的配置
     */
    private final XMStudnetProperties xmStudnetProperties;
    /**
     * 老师的配置
     */
    private final XMTeacherProperties xmTeacherProperties;
    /**
     * 用户服务
     */
    private final UserService userService;
    /**
     * 学院组服务
     */
    private final CollegeGroupService collegeGroupService;
    /**
     * 年度组服务
     */
    private final YearGroupService yearGroupService;
    /**
     * 年度数据服务
     */
    private final YearDataService  yearDataService;
    /**
     * 字典类型服务
     */
    private final DictTypeService dictTypeService;
    /**
     * 字典数据服务
     */
    private final DictDataService dictDataService;
    /**
     * 学生报名服务
     */
    private final StudnetApplysService studnetApplysService;
    /**
     * 老师报名服务
     */
    private final TeacherApplysService teacherApplysService;
    /**
     * 项目状态配置
     */
//    private final XMStateProperties xmStateProperties;;
    /**
     * 项目进度表服务
     */
    private final ProjectScheduleService projectScheduleService;
    /**
     * 项目状态变化配置类，支持热更新
     */
    private final ProjectScheduleConfig projectScheduleConfig;
    /**
     * 项目审核配置类，支持热更新
     */
    private final XMAuditConfig xmAuditConfig;
    /**
     * 角色服务
     */
    private  final  RoleService roleService;
    /**
     * 学院数据服务
     */
    private final  CollegeDataService collegeDataService;

    /**
     * 用户mapper
     */
    private final UserMapper userMapper;
    /**
     * 专家组人员服务
     */
    private final SpecialistDataService specialistDataService;

    /**
     * 数据字典数据mapper
     */
    private final DictDataMapper  dyeDataMapper;

    /**
     * 审核意见服务
     */
    private final AuditOpinionService auditOpinionService;
    private final StudnetApplysMapper studnetApplysMapper;
    private final TeacherApplysMapper teacherApplysMapper;

    /**
     * 用户客户端
     */
    private final SysUserClient sysUserClient;

    /**
     * 专家组服务
     */
    private final SpecialistGroupService specialistGroupService;

    /**
     * 文件客户端
     */
    private final SysFileClient sysFileClient;


    @Override
    public boolean downloadProject(List<Long> projectIds, HttpServletResponse response) {
        //1.根据项目id集合进行获取项目集合
        List<Project> projectList = selectByProjectIds(projectIds);
        //2.根据项目集合获取年度组集合
        List<YearGroup> yearGroupList = fetchAndValidateYearGroupsByProjects(projectList);
        //3.根据项目集合获取年度数据集合
        List<YearData> yearDataList = fetchAndValidateYearDataByProjects(projectList);
        //4.根据项目获取学院组集合
        List<CollegeGroup> collegeGroupList = fetchAndValidateCollegeGroupByProjects(projectList);
        //5.获取学生报名集合 key:项目id vlaue:学生报名集合
        Map<Long, List<StudnetApplys>> studnetApplysMap = fetchAndValidateStudnetApplysByProjects(projectList);
        //6.获取老师报名集合 key:项目id vlaue:老师报名集合
        Map<Long, List<TeacherApplys>> teacherApplysMap = fetchAndValidateTeacherApplysByProjects(projectList);

        //8.获取 项目类型 和 项目级别 的字典数据 key: 字典类型的Key value:字典数据集合
        Map<String, List<DictData>> dictDataMap = fetchAndValidateDictDataByProjects(projectList);
        //9.数据封装

        //1.部分数据cv
        List<ListExportVo> voList = toListExportVoFromProjectList(projectList);
        //2.封装字段数据 项目类型 和 项目级别
        enrichExportVoWithDictData(dictDataMap, voList);
        //3.封装 学院和年度
        enrichExportDataWithYearAndCollegeInfo(yearGroupList, yearDataList, collegeGroupList, voList);
        //4. 封装 负责人和  其他成员 ,指导老师所有信息,同时封装序号

        voList.forEach(e -> {
            //学生报名信息
            List<StudnetApplys> studnetApplysList = studnetApplysMap.get(e.getProjectId());
            //1.获取负责人的信息
            StudnetApplys chiefApply = studnetApplysList.stream()
                    .filter(studnet -> studnet.getUserId().equals(e.getUserId()))
                    //只获取第一个
                    .collect(Collectors.toList())
                    .get(0);
            e.setPrincipalName(chiefApply.getName())
                    .setPrincipalNumber(chiefApply.getUserName())
                    .setPrincipalPhone(chiefApply.getPhone())//学生信息 联系电话必传 但是手机不是
                    .setPrincipalEmail(chiefApply.getMailbox());
            //2.其他成员信息

            //剔除负责人
            List<StudnetApplys> applys = studnetApplysList.stream()
                    .filter(studnet -> !(studnet.getUserId().equals(e.getUserId())))
                    //只获取第一个
                    .collect(Collectors.toList());
            String applysStr = toNameUserNameString(applys);
            e.setOtherMembers(applysStr);

            //3.指导老师
            List<TeacherApplys> teacherApplys = teacherApplysMap.get(e.getProjectId());
            //指导教师姓名
            String teacherNameUserNameString = toTeacherNameUserNameString(teacherApplys);
            //指导教师职称
            String postString = toPostString(teacherApplys);
            //指导老师手机号
            String phoneString = toPhoneString(teacherApplys);
            //指导老师信息写入
            e.setTeacherName(teacherNameUserNameString)
                    .setTeacherTitle(postString)
                    .setTeacherPhone(phoneString);
        });

        //5.序号
        for (int i = 0; i < voList.size(); i++) {
            voList.get(i).setSerialNumber((long) (i + 1));
        }
        //6.封装文件信息
        try {
            List<ListExportExeclVo> list = toListExportExeclVoListFromListExportVoList(voList);
            ExcelUtils.write(response, "项目信息表", "项目信息表", ListExportExeclVo.class, list);
        } catch (IOException e) {
            throw new ServiceException("导出失败"+e.getMessage());
        }
        return true;
    }

    private List<ListExportExeclVo> toListExportExeclVoListFromListExportVoList(List<ListExportVo> projectList) {
        return projectList.stream()
                .map(this::toListExportExeclVoFormListExportVo)
                .collect(Collectors.toList());

    }

    public  ListExportExeclVo toListExportExeclVoFormListExportVo(ListExportVo vo) {
        if (vo == null) {
            return null;
        }
        ListExportExeclVo listExportExeclVo = new ListExportExeclVo();
        listExportExeclVo.setSerialNumber(vo.getSerialNumber());
        listExportExeclVo.setCollege(vo.getCollege());
        listExportExeclVo.setProjectNumber(vo.getProjectNumber());
        listExportExeclVo.setProjectName(vo.getProjectName());
        listExportExeclVo.setProjectType(vo.getProjectType());
        listExportExeclVo.setProjectRankStr(vo.getProjectRankStr());
        listExportExeclVo.setProjectFunds(vo.getProjectFunds());
        listExportExeclVo.setPrincipalName(vo.getPrincipalName());
        listExportExeclVo.setPrincipalNumber(vo.getPrincipalNumber());
        listExportExeclVo.setPrincipalPhone(vo.getPrincipalPhone());
        listExportExeclVo.setPrincipalEmail(vo.getPrincipalEmail());
        listExportExeclVo.setOtherMembers(vo.getOtherMembers());
        listExportExeclVo.setTeacherName(vo.getTeacherName());
        listExportExeclVo.setTeacherTitle(vo.getTeacherTitle());
        listExportExeclVo.setTeacherPhone(vo.getTeacherPhone());
        listExportExeclVo.setProjectPeriod(vo.getProjectPeriod());
        listExportExeclVo.setBatch(vo.getBatch());
        listExportExeclVo.setState(vo.getState());
        return listExportExeclVo;
    }

    /**
     * 将 TeacherApplys 列表转换为 "姓名/工号,姓名/工号,..." 的格式字符串。
     * <p>
     * 示例："龙登燕/3032085，蒋宇/3032064"
     * </p>
     *
     * @param list 待转换的 TeacherApplys 对象列表
     * @return 拼接后的字符串，元素间以中文逗号分隔；若列表为空或 null，则返回空字符串
     */
    public  String toTeacherNameUserNameString(List<TeacherApplys> list) {
        if (list == null || list.isEmpty()) {
            return "无数据";
        }
        return list.stream()
                .map(e -> e.getName() + "/" + e.getUserName())
                .collect(Collectors.joining("，"));
    }

    /**
     * 将 TeacherApplys 列表中的职位字段按顺序拼接为 "职位,职位,..." 的格式字符串。
     * <p>
     * 示例："高级实验师，助教(高校)，讲师"
     * </p>
     *
     * @param list 待转换的 TeacherApplys 对象列表
     * @return 拼接后的职位列表字符串，元素间以中文逗号分隔；若列表为空或 null，则返回空字符串
     */
    public static String toPostString(List<TeacherApplys> list) {
        if (list == null || list.isEmpty()) {
            return "无数据";
        }
        return list.stream()
                .map(TeacherApplys::getPost)
                .collect(Collectors.joining("，"));
    }

    /**
     * 将 TeacherApplys 列表中的电话字段按顺序拼接为 "电话,电话,..." 的格式字符串。
     * <p>
     * 示例："15531537780，13080135877"
     * </p>
     *
     * @param list 待转换的 TeacherApplys 对象列表
     * @return 拼接后的电话号码列表字符串，元素间以中文逗号分隔；若列表为空或 null，则返回空字符串
     */
    public static String toPhoneString(List<TeacherApplys> list) {
        if (list == null || list.isEmpty()) {
            return "无数据";
        }
        return list.stream()
                .map(TeacherApplys::getPhone)
                .collect(Collectors.joining("，"));
    }

    /**
     * 将 StudnetApplys 列表转换为 "name/userName,name/userName,..." 的字符串
     * @param list 学生报名列表
     * @return 返回 案例:刘鹏/1953100109，梁展滔/1953400131，周友柏/1953400120，张沁怡/2053400203
     */
    public static String toNameUserNameString(List<StudnetApplys> list) {
        if (list == null || list.isEmpty()) {
            return "无数据";
        }
        return list.stream()
                .map(e -> e.getName() + "/" + e.getUserName())
                .collect(Collectors.joining("，"));
    }

    /**
     * 丰富导出数据，添加年份和学院信息
     * 该方法通过将年份组、年份数据和学院组信息与待导出的数据列表进行关联，以丰富导出信息
     *
     * @param yearGroupList 年份组列表，包含不同年份的分组信息
     * @param yearDataList 年份数据列表，包含每年的具体数据信息
     * @param collegeGroupList 学院组列表，包含不同学院的分组信息
     * @param voList 待导出的数据视图对象列表，将被丰富以包含年份和学院信息
     */
    private  void enrichExportDataWithYearAndCollegeInfo(List<YearGroup> yearGroupList, List<YearData> yearDataList, List<CollegeGroup> collegeGroupList, List<ListExportVo> voList) {
        //年度组id和yearGroup的映射关系
        Map<Long, YearGroup> yearGroupMap = yearGroupList.stream()
                .collect(Collectors.toMap(YearGroup::getYearGroupId, e -> e));
        // 年度数据id和yearData的映射关系
        Map<Long, YearData> yearDataMap = yearDataList.stream()
                .collect(Collectors.toMap(YearData::getYearDataId, e -> e));
        // 学院组id和collegeGroup的映射关系
        Map<Long, CollegeGroup> collegeGroupMap = collegeGroupList.stream()
                .collect(Collectors.toMap(CollegeGroup::getCollegeGroupId, e -> e));


        voList.forEach(e->{
            YearGroup yearGroup = yearGroupMap.get(e.getYearGroupId());//年度组
            YearData yearData = yearDataMap.get(e.getYearDataId());//年度数据
            CollegeGroup collegeGroup = collegeGroupMap.get(e.getCollegeGroupId());//学院
            if(ObjectUtil.isNull(yearGroup)){
                e.setBatch("未知数据");
            }
            if(ObjectUtil.isNull(yearData)){
                e.setProjectPeriod("未知数据");
            }
            if(ObjectUtil.isNull(collegeGroup)){
                e.setCollege("未知数据");
            }
            e.setCollege(collegeGroup.getName());//学院
            e.setBatch(yearGroup.getName());//年度组
            e.setProjectPeriod(yearData.getName());//年度数据
        });
    }

    /**
     * 使用字典数据丰富导出Vo对象<br>
     * 该方法主要通过将字典数据映射到导出Vo对象上来增强Vo对象的信息内容<br>
     * 它处理项目类型和项目级别字段，将它们的字典标签添加到Vo对象中<br><br>
     *
     * @param dictDataMap 包含字典数据的映射，键是字典类型，值是字典数据列表
     * @param voList 待丰富数据的导出Vo对象列表
     */
    private  void enrichExportVoWithDictData(Map<String, List<DictData>> dictDataMap, List<ListExportVo> voList) {
        // 项目类型 key: 字典键值 value:字典数据对象
        Map<String, DictData> projectTypeDictDataMap = dictDataMap.get(DICT_TYPE_PROJECT_TYPE).stream()
                .collect(Collectors.toMap(DictData::getDictValue, e -> e));
        //项目级别 key: 字典键值 value:字典数据对象
        Map<String, DictData> projectRankDictDataMap = dictDataMap.get(DICT_TYPE_PROJECT_RANK).stream()
                .collect(Collectors.toMap(DictData::getDictValue, e -> e));
        //遍历vo进行封装项目类型 和 项目级别
        voList.forEach(e -> {
            DictData typeDictData = projectTypeDictDataMap.get(e.getType().toString());//类型
            DictData rankDictData = projectRankDictDataMap.get(e.getProjectRank().toString());//级别

            if(ObjectUtil.isNull(typeDictData)){//类型为空
                e.setProjectType("未知数据");
            }
            if(ObjectUtil.isNull(rankDictData)){//级别为空
                e.setProjectRankStr("未知数据");
            }
            e.setProjectType(typeDictData.getDictLabel());//项目类型
            e.setProjectRankStr(rankDictData.getDictLabel()); //项目级别
        });
    }

    /**
     * 将项目列表转换为列表导出的视图对象列表
     *
     * @param projectList 项目实体列表，用于转换为视图对象
     * @return 返回一个列表，包含转换后的列表导出视图对象
     */
    public List<ListExportVo> toListExportVoFromProjectList(List<Project> projectList) {
        if (projectList == null ||  projectList.isEmpty()) {
            return Collections.emptyList();
        }
        return projectList.stream()
                .map(this::toListExportVoFromProject)
                .collect(Collectors.toList());
    }
    /**
     * 将项目对象转换为列表导出视图对象
     * 此方法用于将项目详情转换为适用于列表导出的视图对象，便于数据的导出和展示
     *
     * @param project 项目对象，包含项目详细信息如果传入的项目对象为null，则返回null
     * @return ListExportVo 列表导出视图对象，包含从项目对象中提取的特定信息
     */
    public  ListExportVo toListExportVoFromProject(Project project) {
        if (project == null) {
            return null;
        }
        ListExportVo listExportVo = new ListExportVo();
        listExportVo.setProjectNumber(project.getProjectNumber());
        listExportVo.setProjectName(project.getName());
        listExportVo.setState(ProjectStatusEnum.fromValue(project.getState()).getDescription());
        listExportVo.setType(project.getType());
        listExportVo.setProjectRank(project.getProjectRank());
        listExportVo.setProjectFunds(project.getTotalMoney().toString());
        listExportVo.setCollegeGroupId(project.getCollegeGroupId());
        listExportVo.setYearGroupId(project.getYearGroupId());
        listExportVo.setYearDataId(project.getYearDataId());
        listExportVo.setUserId(project.getUserId());
        listExportVo.setProjectId(project.getProjectId());
        return listExportVo;
    }

    /**
     * 根据项目列表获取并验证字典数据<br>
     * 此方法旨在从数据库中查询特定字典类型的数据，并以映射的形式返回，便于后续处理和使用<br>
     * 主要功能包括：<br>
     * 1. 查询‘项目类型’和‘项目级别’这两种字典类型的对应字典数据<br>
     * 2. 将查询到的数据组织成一个映射，其中字典类型的标识作为键，对应的字典数据列表作为值<br><br>
     *
     * @param projectList 项目列表，目前该参数未被使用，未来可能用于细化查询条件或验证
     * @return 返回一个映射，其中包含‘项目类型’和‘项目级别’这两种字典类型的字典数据列表
     */
    private Map<String, List<DictData>> fetchAndValidateDictDataByProjects(List<Project> projectList) {
        // 传入 项目类型 和 项目级别 的字典类型的集合查询对应
        List<DictData> dictDataList = dyeDataMapper.selectByDictTypes(Arrays.asList(
                DICT_TYPE_PROJECT_RANK,
                DICT_TYPE_PROJECT_TYPE
        ));
        //map集合 key: 字典类型的Key value:字典数据集合
        Map<String, List<DictData>> dictDataMap = dictDataList.stream()
                .collect(Collectors.groupingBy(DictData::getDictType));
        return dictDataMap;
    }

    /**
     * 根据项目列表获取并验证学院组<br><br>
     *
     * 通过项目列表中的学院组ID来查询学院组信息，并验证查询结果<br>
     * 如果没有找到对应的学院组，则抛出服务异常<br><br>
     *
     * @param projectList 项目列表，用于获取学院组ID
     * @return 返回查询到的学院组列表
     * @throws ServiceException 如果查询结果为空，则抛出获取学院组数据失败的异常
     */
    private List<CollegeGroup> fetchAndValidateCollegeGroupByProjects(List<Project> projectList) {
        List<CollegeGroup> collegeGroupList = collegeGroupService.lambdaQuery()
                .in(CollegeGroup::getCollegeGroupId, projectList.stream().map(Project::getCollegeGroupId).collect(Collectors.toList()))
                .list();

        if (collegeGroupList.isEmpty()) {
            throw new ServiceException("获取学院组数据失败");
        }
        return collegeGroupList;
    }

    /**
     * 根据项目列表获取并验证教师申请信息<br><br>
     *
     * 通过项目ID查询教师申请信息，并验证查询结果是否为空如果为空，则抛出异常<br>
     * 最后，将查询到的教师申请信息按项目ID分组返回<br><br>
     *
     * @param projectList 项目列表，用于查询教师申请信息
     * @return 返回一个映射，键为项目ID，值为与该项目ID关联的教师申请信息列表
     * @throws ServiceException 如果查询的教师报名数据为空，则抛出此异常
     */
    private Map<Long, List<TeacherApplys>> fetchAndValidateTeacherApplysByProjects(List<Project> projectList) {
        // 根据项目ID列表查询教师申请信息
        List<TeacherApplys> teacherApplysList= teacherApplysService.lambdaQuery()
                .in(TeacherApplys::getProjectId, projectList.stream().map(Project::getProjectId).collect(Collectors.toList()))
                .list();
        if (teacherApplysList.isEmpty()) {
            throw new ServiceException("老师报名数据为空");
        }
        // 将查询到的教师申请信息按项目ID分组，并返回
        return teacherApplysList.stream().collect(Collectors.groupingBy(TeacherApplys::getProjectId));
    }

    /**
     * 根据项目列表获取并验证学生申请数据<br><br>
     *
     * 通过项目ID查询学生申请记录，并将这些记录按项目ID分组返回这样做是为了确保每个项目的学生申请数据能够<br>
     * 方便地被访问和处理如果找不到任何学生申请记录，则抛出异常，表明数据缺失或查询条件不匹配<br>
     *
     * @param projectList 项目列表，用于查询学生申请记录
     * @return 返回一个映射，键是项目ID，值是与该项目ID关联的学生申请记录列表
     * @throws ServiceException 如果学生报名数据为空，则抛出此异常
     */
    private Map<Long, List<StudnetApplys>> fetchAndValidateStudnetApplysByProjects(List<Project> projectList) {
        // 查询指定项目列表的所有学生申请记录
        List<StudnetApplys> studnetApplysList = studnetApplysService.lambdaQuery()
                .in(StudnetApplys::getProjectId, projectList.stream().map(project -> project.getProjectId()).collect(Collectors.toSet()))
                .list();
        if (CollUtil.isEmpty(studnetApplysList)) {
            throw new ServiceException("学生报名数据为空");
        }
        // 将查询到的学生申请记录按项目ID分组，并返回
        return studnetApplysList.stream().collect(Collectors.groupingBy(StudnetApplys::getProjectId));
    }

    /**
     * 根据项目列表获取并验证年度数据<br><br>
     *
     * 通过项目列表中的年度数据ID查询对应的年度数据，并验证查询结果是否为空<br>
     * 如果查询结果为空，则抛出异常，提示传入的项目ID为脏数据<br><br>
     *
     * @param projectList 项目列表，用于获取年度数据ID进行查询
     * @return 返回查询到的年度数据列表
     * @throws ServiceException 如果查询不到任何年度数据，则抛出此异常
     */
    private List<YearData> fetchAndValidateYearDataByProjects(List<Project> projectList) {
        // 根据项目列表中的年份数据ID查询对应的年度数据
        List<YearData> yearDataList = yearDataService.lambdaQuery()
                .in(YearData::getYearDataId,
                        projectList.stream().map(project -> project.getYearDataId()).collect(Collectors.toSet()))
                .list();
        if (CollUtil.isEmpty(yearDataList)) {
            throw new ServiceException("传入项目id,查询不到年度数据,为脏数据,请联系管理员");
        }
        return yearDataList;
    }
    /**
     * 根据项目列表获取并验证年度组<br>
     * 该方法通过项目列表中的年度组ID查询对应的年度组信息，并进行数据有效性验证<br>
     * 如果查询结果为空，抛出异常提示数据不一致<br>
     *
     * @param projectList 项目列表，用于提取年度组ID进行查询
     * @return 年度组列表，包含与项目关联的年度组信息
     * @throws ServiceException 当查询不到年度组数据时，抛出服务异常
     */
    private  List<YearGroup> fetchAndValidateYearGroupsByProjects(List<Project> projectList) {
        // 根据项目列表中的年度组ID查询年度组信息
        List<YearGroup> yearGroupList = yearGroupService.lambdaQuery()
                .in(YearGroup::getYearGroupId, projectList.stream().map(project -> project.getYearGroupId()).collect(Collectors.toSet()))
                .list();
        if (CollUtil.isEmpty(yearGroupList)) {
            throw new ServiceException("传入项目id,查询不到年度组数据,为脏数据,请联系管理员");
        }
        return yearGroupList;
    }

    private List<Project> selectByProjectIds(List<Long> projectIds) {
        List<Project> projectList= this.listByIds(projectIds);
        if(CollUtil.isEmpty(projectList)){
            throw new ServiceException("传入项目id,数据库没有数据");
        }
        Set<Long> idSet = projectList.stream().map(Project::getProjectId).collect(Collectors.toSet());
        //记录错误
        List<String> errorList = new ArrayList<>();
        //校验哪些项目id不存在数据
        projectIds.forEach(id->{
            if(!idSet.contains(id)){
                errorList.add("项目id:"+id+"不存在数据库");
            }
        });
        //如果有错误就抛出异常
        if(CollUtil.isNotEmpty(errorList)){
            throw new ServiceException(CollUtil.join(errorList,";\n") );
        }
        return projectList;
    }

    /**
     * 批量下载指定项目的文件（支持结题材料或附加材料），并打包成 zip 压缩包通过响应返回。
     *
     * @param projectIds 项目 ID 列表
     * @param fileType   文件类型（1：结题材料；2：附加材料）
     * @param response   HttpServletResponse，用于将 zip 包输出给客户端
     */
    @Override
    public void downloadProjectsFile(List<Long> projectIds, int fileType, HttpServletResponse response) {
        // 从数据库查询项目列表
        List<Project> projectList = this.listByIds(projectIds);

        if (CollUtil.isEmpty(projectList) || projectList.size() != projectIds.size()){
            throw new ServiceException("查询出来的项目数量:"+projectList.size()+"与要导出项目的数量:"+projectIds.size()+"不一致");
        }
        // 用于存放所有需打包的文件，key 是 zip 中的路径（如：项目名/文件名），value 是文件字节内容
        Map<String, byte[]> filesMap = new ConcurrentHashMap<>();

        // 用多线程异步下载所有文件
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        //记录不存文件的路径
        List<String> errorMsg = new CopyOnWriteArrayList<>();

        for (Project project : projectList) {
            // 获取文件名列表（以英文逗号分隔的字符串）
            String fileName = fileType == 1 ? project.getConcludeUrl() : project.getMaterialsUrl();
            if (fileName == null || StrUtil.isBlank(fileName)){
                errorMsg.add("项目:"+project.getName()+"的文件不存在,文件的URL:为空不存在\n");
                continue;
            }
            //校验文件是否存在
            Response isNull = sysFileClient.fileIsNull(fileName);
            if(isNull.getCode() != 200){
                errorMsg.add("项目:"+project.getName()+"的文件不存在,文件的URL:"+(StrUtil.isBlank(fileName)?"为空不存在":fileName)+"\n");
                continue;
            }

            // 将英文逗号分隔的字符串分割为列表，并过滤掉空白项
//            List<String> fileNames = Arrays.stream(fileUrlsRaw.split(","))
//                    .map(String::trim)
//                    .filter(s -> !s.isEmpty())
//                    .collect(Collectors.toList());

            // 清理项目名称（避免 zip 内部路径中出现非法字符）
            String projectFolder = project.getName().replaceAll("[\\\\/:*?\"<>|]", "_");

            // 对每个文件进行异步下载
//            for (String fileName : fileNames) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                try {
                    // 假设 fileName 就是文件存储时的唯一标识（objectName）
                    Response<byte[]> fileResp = sysFileClient.downloadFileClient(fileName);
                    if (fileResp != null
                        && fileResp.getCode() == HttpStatusResponse.SUCCESS.getCode()
                        && fileResp.getData() != null) {

                        // 构造 zip 内部路径（例如：项目A/附件1.pdf）
                        String zipEntryPath = projectFolder + "/" + extractFileName(fileName);
                        filesMap.put(zipEntryPath, fileResp.getData());
                    }else{
                        errorMsg.add("项目:"+project.getName()+"的文件下载失败,文件的URL"+fileName+"\n");
                    }
                } catch (Exception e) {
                    errorMsg.add("文件下载异常: " + e.getMessage());
                }
            });
            futures.add(future);
//            }
        }

        //不存在文件校验
        if(CollUtil.isNotEmpty(errorMsg)){
            throw new ServiceException("以下项目的文件存在错误:"+String.join("",errorMsg));
        }

        // 等待所有异步任务完成
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        //不存在文件校验
        if(CollUtil.isNotEmpty(errorMsg)){
            throw new ServiceException("以下项目的文件存在错误:"+String.join("",errorMsg));
        }
        // 压缩所有文件为 zip 包
        byte[] zipBytes = ZipUtils.compress(filesMap);

        // 设置响应头并输出 zip 文件内容
        try {
            response.setContentType("application/zip");
            String encodedFileName = URLEncoder.encode("项目文件打包下载.zip", "UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName);
            response.getOutputStream().write(zipBytes);
            response.getOutputStream().flush();
        } catch (Exception e) {
            throw new ServiceException("文件下载失败"+e.getMessage());
        }
    }

    /**
     * 提取文件名
     * @param path 文件路径 : /cms-file/yyyy/MM/dd/filename
     * @return 返回结果: filename
     */
    public  String extractFileName(String path) {
        if (path == null || path.isEmpty()) {
            throw new ServiceException("路径不能为空");
        }

        // 正则匹配固定格式 /cms-file/yyyy/MM/dd/ 后接文件名
        String regex = "^/cms-file/\\d{4}/\\d{2}/\\d{2}/(.+)$";
        if (path.matches(regex)) {
            // 提取文件名部分
            return path.replaceFirst("^/cms-file/\\d{4}/\\d{2}/\\d{2}/", "");
        } else {
            throw new ServiceException("文件路径格式非法，应为 /cms-file/yyyy/MM/dd/filename");
        }
    }




    @Override
    @Transactional
    public boolean submitFile(SubmitFileDTO submitFileDTO) {
        //1.校验项目存在
        Project byId = this.getById(submitFileDTO.getProblemId());
        if (byId == null) {
            throw new ServiceException("项目不存在");
        }
        //2.判断项目是否属于当前用户
        if (!byId.getUserId().equals(SecurityUtils.getUserId())) {
            throw new ServiceException("提交解题文件只能负责人进行提交,你没有权限进行提交");
        }
        //2.校验项目状态为进行中
        if (byId.getState() != ProjectConstant.PROJECT_STATUS_IN_PROGRESS_VALUE) {
            throw new ServiceException("项目状态不是进行中，不能提交");
        }
        //3.校验提交文件存在项目当中
        Response isNull = sysFileClient.fileIsNull(submitFileDTO.getFileURL());
        if (isNull.getCode() != 200) {
            throw new ServiceException("请检查你提交解题文件是否提交成功,系统没有查询到您提交解题文件");
        }
        //4.提交文件
        byId.setConcludeUrl(submitFileDTO.getFileURL());
        return  this.updateById(byId);
    }

    /**
     * 申请项目
     * @param applyForDTO 申请信息
     * @return
     */
    @Override
    @Transactional
    public Response addProject(ApplyForDTO applyForDTO) {
        //1.a ,b ,c必须三选一,参数不能为null
        checkApplyFor(applyForDTO);
        //2.钱的比较验算 财政+学校 = 总金额
        checkMoney(applyForDTO);
        //3.校验用户(老师和学生)都存在
        isStuentAndTeacherExist(applyForDTO);
        //4.字典数据校验
        checkAddProjectDictData(applyForDTO);
        //5.校验负责人是不是只有一个,并且第一个id要和申请人id一致
        checkPrincipal(applyForDTO);
        //6.校验报名人数限制,有老师和学生
        checkStuentAndTeacher(applyForDTO);
        //7.校验附加4之外都需要企业老师支持
        teachersSupport(applyForDTO);
        //8.验证学院组id是否存在并且是否启用
        checkCollegeGroup(applyForDTO.getCollegeGroupId());
//        isCollegeGroupExistAndEnable(applyForDTO.getCollegeGroupId());
        //9.年度组id  是否存在并且是否启用  ,之后再检查  年度数据id是否存在
        checkYearGroup(applyForDTO.getYearGroupId(),applyForDTO.getYearDataId());
//        isYearGroupIdExistAndYearDataIsEnable(applyForDTO.getYearGroupId(),applyForDTO.getYearDataId());
        //10.检查报名的学生的学院组是否存在并且是否启用
        isStudentCollegeGroupsEnabled(applyForDTO.getStudents());
        //11.项目名称不可以重复
        checkProjectName(applyForDTO);
        //todo 有空写,验证文件存在通内
        checkFileUrl(applyForDTO.getMaterialsUrl());
        //todo 有空再写,验证用户负责人只有一个项目在进行中才可以申请新的项目&&(同时只有一个是负责人|| 参加项目进行中最多2个)

        //todo 有空再写,验证队员和老师参数进行项目最多有2个


        //1.把数据转到实体类当中并写入数据库,插入项目表新增项目
        Long projectId = insertProject(applyForDTO);
        //2.插入学生表
        if(!insertProjectStudnet(applyForDTO,projectId)){
            throw new ServiceException("插入学生表失败,请检查",500);
        }
        //3.插入老师表
        if(!insertProjectTeacher(applyForDTO,projectId)){
            throw new ServiceException("插入老师表失败,请检查",500);
        }
        //4.项目进行表插入
        if(!insertProjectSchedule(projectId)){
            throw new ServiceException("项目进行表插入失败,请检查",444);
        }
        return Response.success("项目申请成功,等待审核");
    }

    /**
     * 项目名称重复校验
     * @param applyForDTO
     */
    private void checkProjectName(ApplyForDTO applyForDTO) {
        Long count = this.lambdaQuery()
                .eq(Project::getName, applyForDTO.getName())
                .select(Project::getName)
                .count();
        if (count > 0) {
            throw new ServiceException("项目名称重复,请修改,当前项目名称数量:"+count);
        }
    }

    /**
     * 校验文件是否是存在通内
     * @param fileName 文件名
     */
    private void checkFileUrl(String fileName) {
        Response response = sysFileClient.fileIsNull(fileName);
        if (response.getCode() != 200){
            throw new ServiceException("上传的文件不存在系统当中,请检查");
        }
    }


    /**
     * 权限校验审核通过后,操作审核表存指针(项目进度值抽象出来的指针)存在4个位置(多种情况)<br><br><br><br>
     *新算法,下面算法会出现bug,使用指针法进行抽象,<br>
     * 1.项目表里进行审核进度值抽象为指针,1代表该指针指向数组框起始位置,抽象值为a<br>
     * 2.每个不同类型的项目审核顺序和大小,抽象为一个组数空间,抽象值max<br>
     * 3.每审核一次抽象为给数组框添加一个数,抽象值为b<br>
     * 1.条件:审核成功指针就会向前一点一次,如果审核失败话,指针就会从新回到数组的起始位置1,但是数组里面意见存储审核记录,不会消失,指针只会覆盖掉原来的记录,所以a>b时最大,大于1<br>
     * @param projectAuditDto
     * @return
     */
    @Override
    @Transactional
    public Response projectAudit(ProjectAuditDto projectAuditDto) {
        //1.判断当前项目是否存在,并返回项目的bean对象,不存在抛出异常
        Project project = getProject(projectAuditDto.getProjectId());
        //2.情况二,当前项目已经审核完毕,不需要再次审核,直接抛出异常,再进行状态判断,只有项目审核中才可以进行审核
        projectAlreadyAudit(project);
        //3.根据项目的审核状态,去查热更新配置类,返回当前项目的审核角色的id
        Long roleId = getAuditRoleId(project.getAuditStatus(), project.getType());
        //4.再判断当前用户拥有的角色id,是否是项目当前状态需要的角色id,如果不是抛出异常
        isHaveUserRole(roleId);
        //5.用户对应的角色是否有权限审核当前项目
        if(!isHaveRole(roleId,project)){
            throw new ServiceException("当前用户没有权限审核当前项目,请联系管理员",403);
        }
        //6.根据剩余5个情况,进行操作,并返回结果,参数项目bean,审核备注,(根据项目进度状态来判断审核进度)
        if (!projectAlreadyAuditz(projectAuditDto, project,roleId)) {
             return Response.success("失败");
        }
        return  Response.success("成功");
    }

    /**
     * 获取学生参与项目成功或者创建的项目
     * @return
     */
    @Override
    public Response getStudentProjectList(int currentPage,int pageSize) {
        //当前登录用户ID
        Long userId = SecurityUtils.getLoginUser().getUserid();
        // 创建分页对象
        Page<Project> page = new Page<>(currentPage, pageSize);



        try{
            LambdaQueryWrapper<Project> queryWrapper = Wrappers.<Project>lambdaQuery()
                    .eq(Project::getDelFlag,0)//是否删除
                    .and(wq -> wq
                            .apply("JSON_CONTAINS(member_id, '["+userId.toString()+"]')") //
                            .or()
                            .eq(Project::getCreateBy,userId) // 创建字段的id是否为当前用户id
                    );
            // IPage<Project> projectList = page(page, queryWrapper);
            Page<Project> projectPage = page(page,queryWrapper);
            return Response.success(PageDTO.of(projectPage,StudentProjectVo.class),"获取学生参与项目以及报名项目成功！");
        }catch (Exception e){
            throw new ServiceException("查询用户当前参与项目失败",502);
        }
    }



    /**
     * 删除项目
     * @param projectId
     * @return
     */
    @Override
    public Response delectStudentProjectById(Long projectId) {
        // 登录用户Id
        Long userId = SecurityUtils.getLoginUser().getUserid();

        //检查项目是否存在(检查是否已经逻辑删除或者项目不存在)
        ProjectIsNull(projectId);

        // 检查是否为项目负责人
        isProjectAdmin(projectId,userId);

        //检查是否为未通过状态
        isAuditFailed(projectId);

        //删除数据
        if (deleteProjectById(projectId)){
            return Response.success("删除成功！");
        }else {
            return Response.error("删除项目失败请联系管理员");
        }
    }

    /**
     * 查询学生或者教师
     * @param role 角色：学生 0 老师 1
     * @param name 学号/名字/工号
     * @return
     */
    @Override
    public Response getUserApply(int role, String name) {
        switch (role) {
            case 0:
                //学生
                return Response.success(getStudentApplyListByNameOrId(name));
            case 1:
                //教师
                return Response.success(getTeacherApplyListByNameOrId(name));
            default:
                throw new ServiceException("未知的角色！", 403);
        }
    }

    /**
     * 获取自己创建的项目
     * @return
     */
    @Override
    public Response getMyCrectProject(int currentPage,int pageSize) {
        //当前登录用户ID
        Long userId = SecurityUtils.getLoginUser().getUserid();

        //创建分页对象
        Page<Project> page = new Page<>(currentPage, pageSize);



        try{
            LambdaQueryWrapper<Project> queryWrapper = Wrappers.<Project>lambdaQuery()
                    .eq(Project::getDelFlag,0)//是否删除
                    .eq(Project::getCreateBy,userId); // 项目创建者ID是否为当前登录用户ID

            //  执行分页查询
            Page<Project> projectList = page(page,queryWrapper);

            //List<Project> projectList = list(queryWrapper);
            return Response.success(PageDTO.of(projectList,StudentProjectVo.class),"获取学生创建项目成功！");
        }catch (Exception e){
            throw new ServiceException("查询学生当前创建项目失败",502);
        }
    }

    @Override
    public Response<DetailProjectVo> getProjectById(Long id,String type) {
        // 1. 判断项目是否存在
        Project project = this.getById(id);

        if (project == null ) { // 检查返回的 list 是否为空
            throw new ServiceException("该项目不存在！或已被删除", 404);
        }

        //1.权限校验
        if(!checkUserRole(id,type)){
            throw new ServiceException("当前用户没有权限查看该项目",403);
        }
        //2.把项目信息转成vo返回信息
        DetailProjectVo projectVo = new DetailProjectVo();
        //2.1    基本情况(Elementary)
        Elementary elementary = convertElementaryInfo(project);
        //2.2    成员信息(Personnels)
        Personnels personnels = convertStudentApplyInfo(project);
        //2.3    立项依据(According)
        According according = convertAccordingInfo(project);
        //2.4    预算(Expenditure)
        Expenditure expenditure = convertExpenditureInfo(project);
        //2.5    审核和项目进度(Audit)
        Audit audit= convertAuditInfo(project);
        //2.6    两个文件,申请文件和解题文件
        convertProjectFiles(projectVo,project);
        projectVo.setElementary(elementary)
                .setPersonnels(personnels)
                .setAccording(according)
                .setExpenditure(expenditure)
                .setAudit(audit);
        return Response.success(projectVo);
    }

    /**
     * 项目文件转换
     * @param projectVo vo
     * @param project 项目实体类
     */
    public void convertProjectFiles(DetailProjectVo projectVo, Project project) {
        //结题文件
//        JSONArray array = JSONUtil.parseArray(project.getConcludeUrl());
//        List<String> concludeUrl = JSONUtil.toList(array, String.class);
        ArrayList<String> list = new ArrayList<>();
        list.add(project.getConcludeUrl());
        //todo 申请文件改话,这里也要改
        projectVo.setConcludeUrl(list)
                .setMaterialsUrl(project.getMaterialsUrl());

    }

    /**
     * 项目转换成vo,审核和项目进度
     * @param project 项目实体类
     * @return  审核和项目进度vo
     */
    public Audit convertAuditInfo(Project project) {
        // 审核意见
        List<AuditOpinion> auditOpinions = auditOpinionService.selectByPropertieID(project.getProjectId());
        List<AuditOpinion> sortedAuditOpinions = auditOpinions.stream()
                .sorted(Comparator.comparing(AuditOpinion::getRootId))
                .collect(Collectors.toList());
        List<Opinion> audits = BeanCopyUtils.copyBeans(sortedAuditOpinions, Opinion.class);
        //设置用户和角色
        setUserAndRole(audits);


        // 项目进度
        List<ProjectSchedule> scheduleList = projectScheduleService.listByProjectId(project.getProjectId());
        List<ProjectSchedule> schedules = scheduleList.stream()
                .sorted(Comparator.comparing(ProjectSchedule::getRootId))
                .collect(Collectors.toList());
        List<Schedule> list = BeanCopyUtils.copyBeans(schedules, Schedule.class);
        //给流程节点设置用户信息
        processSetUserInfo(schedules, list);

        //获取当前项目需要审核角色的顺序
        Map<Long, List<XMAuditConfig.AuditRole>> projectAuditTypes = xmAuditConfig.getProjectAuditTypes();
        List<XMAuditConfig.AuditRole> auditRoles = projectAuditTypes.get(project.getType());

        Audit audit = new Audit()
                .setOpinion(audits)
                .setSchedule(list)
                .setAuditRoles(auditRoles)
                .setAuditProgress(project.getAuditStatus())
                .setState(project.getState());
        return audit;
    }

    /**
     * 给流程节点设置用户信息
     * @param schedules 流程节点(有完整信息节点)
     * @param list 流程节点(返回给前端的节点)
     */
    private void processSetUserInfo(List<ProjectSchedule> schedules, List<Schedule> list) {
        List<Long> userIds = schedules.stream()
                .map(e -> e.getUserId())
                .collect(Collectors.toList());

        List<User> userList = userIds.size()==0?Collections.emptyList(): userService.listByIds(userIds);

        list.forEach(e->{
            userList.stream().filter(user->user.getUserId().equals(e.getUserId()))
                    .limit(1)
                    .forEach(user->{
                        e.setUserName(user.getUserName());
                        e.setNickName(user.getNickName());
                    });
        });
    }

    /**
     * 写入用户名和账号和角色名
     * @param audits
     */
    private void setUserAndRole(List<Opinion> audits) {
        //用户id和角色id
        List<Long> userIds = audits.stream()
                .map(e -> e.getUserId())
                .collect(Collectors.toList());
        List<Long> roleIds = audits.stream()
                .map(e -> e.getRoleId())
                .collect(Collectors.toList());

        List<User> users = userIds.size()==0?Collections.emptyList():userService.listByIds(userIds);
        List<Role> roles = roleIds.size()==0?Collections.emptyList():roleService.listByIds(roleIds);

        //写入
        audits.forEach(audit->{
            //用户
            users.stream().filter(e->e.getUserId().equals(audit.getUserId()))
                    .limit(1)
                    .forEach(user->{
                        audit.setUserName(user.getUserName());
                        audit.setNickName(user.getNickName());
                    });
            roles.stream().filter(e->e.getRoleId().equals(audit.getRoleId()))
                    .limit(1)
                    .forEach(role->{
                        audit.setRole(role.getRoleName());
                    });

        });
    }

    /**
     * 项目转换成vo,预算
     * @param project 项目实体类
     * @return 预算vo
     */
    public Expenditure convertExpenditureInfo(Project project) {
        Expenditure expenditure = BeanCopyUtils.copyBean(project, Expenditure.class);
        return expenditure;
    }



    /**
     * 项目转换成vo,立项依据
     * @param project 项目实体类
     * @return 立项依据vo
     */
    public According convertAccordingInfo(Project project) {
        According according = new According();
        if (ProjectConstant.PROJECT_TYPE_INNOVATION_TRAINING.equals(project.getType())) {
            ADetail aDetail = BeanCopyUtils.copyBean(project, ADetail.class);
            according.setA(aDetail);
        } else if (ProjectConstant.PROJECT_TYPE_STARTUP_TRAINING.equals(project.getType())){
            BDetail bDetail = BeanCopyUtils.copyBean(project, BDetail.class);
            according.setB(bDetail);
        }else if (ProjectConstant.PROJECT_TYPE_STARTUP_PRACTICE.equals(project.getType())){
            CDetail cDetail = BeanCopyUtils.copyBean(project, CDetail.class);
            according.setC(cDetail);
        }else{
            log.error("未知的项目类型,请联系管理员,项目对象{"+project+"}");
            throw new ServiceException("未知的项目类型,请联系管理员",500);
        }
        return according;
    }
    /**
     * 项目转换成vo
     * @param project 项目实体类
     * @return 成员信息vo
     */
    public Personnels convertStudentApplyInfo(Project project){
        //1.获取学生 和 老师
        List<StudnetApplys> studnetApplys = studnetApplysService.list(Wrappers.<StudnetApplys>lambdaQuery()
                .eq(StudnetApplys::getProjectId, project.getProjectId()));
        List<TeacherApplys> teacherApplys = teacherApplysService.list(Wrappers.<TeacherApplys>lambdaQuery()
                .eq(TeacherApplys::getProjectId, project.getProjectId()));
        //2.判断是不是有学生和老师
        if(studnetApplys.size() == 0 || teacherApplys.size() == 0){
            throw new ServiceException("该项目没有学生或老师报名,出现该情况请联系管理员",403);
        }
        //3.bean拷贝
        List<Student> students = BeanCopyUtils.copyBeans(studnetApplys, Student.class);
        List<Teacher> teachers = BeanCopyUtils.copyBeans(teacherApplys, Teacher.class);

        return new Personnels(students,teachers);
    }

    /**
     * 项目转换成vo
     * @param project 项目实体类
     * @return 基本情况vo
     */
    public Elementary convertElementaryInfo(Project project) {
        Elementary elementary = toElementaryFromProject(project);
        elementary.setBeginTime(DateUtil.formatDateTime(project.getBeginTime()))//立项时间
                .setEndTime(DateUtil.formatDateTime(project.getEndTime()));//结束时间
        return elementary;
    }

    /**
     * Project 转 Elementary
     * @param project
     * @return
     */
    public static Elementary toElementaryFromProject(Project project) {
        if (project == null) {
            return null;
        }
        Elementary elementary = new Elementary();
        elementary.setProjectId(project.getProjectId());
        elementary.setProjectNumber(project.getProjectNumber());
        elementary.setName(project.getName());
        elementary.setCollegeGroupId(project.getCollegeGroupId());
        elementary.setYearGroupId(project.getYearGroupId());
        elementary.setYearDataId(project.getYearDataId());
        elementary.setType(project.getType());
        elementary.setProjectRank(project.getProjectRank());
        elementary.setSubjectCategory(project.getSubjectCategory());
        elementary.setCategory(project.getCategory());
        elementary.setProjectSource(project.getProjectSource());
        elementary.setProjectSynopsis(project.getProjectSynopsis());
        elementary.setPrincipalExperience(project.getPrincipalExperience());
        elementary.setTeacherExperience(project.getTeacherExperience());
        elementary.setFirmTeacherExperience(project.getFirmTeacherExperience());
        elementary.setTeacherSupport(project.getTeacherSupport());
        return elementary;
    }

    @Override
    public boolean isProjectOwnedByStudent(Long projectId) {
        Long userId = SecurityUtils.getUserId();
        if (projectId == null){
            throw new ServiceException("项目id不能为空",400);
        }
        //1.判断项目是否存在
        Project project = this.lambdaQuery().eq(Project::getProjectId, projectId)
                .list().get(0);
        if (project == null){
            throw new ServiceException("该项目不存在！或已被删除",404);
        }
        //2.判断项目是否为学生创建或者是不是自己参加的
        boolean isOwned = false; //判当前项目是不是自己报名参加的
        try {
            JSONArray array = JSONUtil.parseArray(project.getMemberId());
            List<Long> userList = JSONUtil.toList(array, Long.class);
            if (userList.contains(userId)){
                isOwned = true;
            }
        }catch (Exception e){
            throw new ServiceException("项目成员id解析失败",500);
        }

        if (project.getUserId().equals(userId) || isOwned){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean isProjectOfTeacherStudents(Long projectId) {
        Long userId = SecurityUtils.getUserId();

        //1.判断项目是否存在
        Project project = this.lambdaQuery().eq(Project::getProjectId, projectId)
                .list().get(0);
        if (project == null){
            throw new ServiceException("该项目不存在！或已被删除",404);
        }
        List<TeacherApplys> list = teacherApplysService.list(Wrappers.<TeacherApplys>lambdaQuery().eq(TeacherApplys::getProjectId, projectId));
        if ( list == null || list.size() == 0){
            throw new ServiceException("当前项目没有老师报名,出现该情况请联系管理员",403);
        }
        //2.判断项目是否为自己学生的项目
        long count = list.stream().filter(e -> e.getUserId().equals(userId)).count();
        if(count>0){
            return true;
        }
        return false;
    }
    @Override
    public  boolean isProjectInSameCollege(Long projectId){
        Long userId = SecurityUtils.getUserId();
        //1.判断项目是否存在
        Project project = this.lambdaQuery().eq(Project::getProjectId, projectId)
                .list().get(0);
        if (project == null){
            throw new ServiceException("该项目不存在！或已被删除",404);
        }
        //2.判断项目是否为自己学院的项目
        Long collegeGroupId = project.getCollegeGroupId();

        List<CollegeData> list = collegeDataService.list(Wrappers.<CollegeData>lambdaQuery()
                .eq(CollegeData::getUserId, userId)
                .eq(CollegeData::getCollegeGroupId, collegeGroupId));
        if ( list == null || list.size() == 0){
            return false;
        }
        return true;
    }

    @Override
    public boolean canExpertReviewProject(Long projectId){
        Long userId = SecurityUtils.getUserId();
        //1.判断项目是否存在
        Project project = this.lambdaQuery().eq(Project::getProjectId, projectId)
                .list().get(0);
        if (project == null){
            throw new ServiceException("该项目不存在！或已被删除",404);
        }
        if(project.getSpecialistGroupId() == null){
            throw new ServiceException("该项目没有专家组，无法审核！",403);
        }
        //2.判断项目是否为自己专家的项目
        List<SpecialistData> list = specialistDataService.list(Wrappers.<SpecialistData>lambdaQuery()
                .eq(SpecialistData::getUserId, userId)
                .eq(SpecialistData::getSpecialistGroupId, project.getSpecialistGroupId()));
        if ( list == null || list.size() == 0){
            return false;
        }
        return true;
    }

    /**
     * 获取当前绑定的项目中某个学生的项目列表

     * @return Page<StudentProjectVo>
     */
    @Override
    public PageDTO<StudentProjectVo> getStudentProjectList(StudentProjectDto studentProjectDto) {
        //todo 赶时间,查多张表,可以值查一个张项目表,有空看,全角色项目列表(这么查json语句)
        if(studentProjectDto.getStudentId() == null){
            throw new ServiceException("学生id不能为空",400);
        }
        //1.获取自己id
        Long userId = SecurityUtils.getUserId();
        //2.去查自己参加的项目
        List<TeacherApplys> teacherApplys = teacherApplysService.lambdaQuery()
                .eq(TeacherApplys::getUserId, userId)
                .list();
        if(teacherApplys == null || teacherApplys.size() == 0){
            return PageDTO.empty();
        }
        //3.再去查这个学生参加的项目
        List<StudnetApplys> studnetApplys = studnetApplysService.lambdaQuery()
                .eq(StudnetApplys::getUserId, studentProjectDto.getStudentId())
                .list();
        if(studnetApplys == null || studnetApplys.size() == 0){
            StringBuffer msg = new StringBuffer();
            msg.append("\n老师参加的项目名称:");
            teacherApplys.forEach(e->{
                msg.append(e.getName()).append(",");
            });

            throw new ServiceException("数据发生异常请联系管理员,老师参加了项目,不存在学生"+msg,500);
        }
        //4.两个项目的交集,项目id集合
        // 4.1提取两个集合的 projectId
        Set<Long> teacherProjectIds = teacherApplys.stream()
                .map(TeacherApplys::getProjectId)
                .collect(Collectors.toSet());

        Set<Long> studentProjectIds = studnetApplys.stream()
                .map(StudnetApplys::getProjectId)
                .collect(Collectors.toSet());
        //4.2 交集
        teacherProjectIds.retainAll(studentProjectIds);

        //4.3非空返回空集
        if (teacherProjectIds.isEmpty()){
            return PageDTO.empty();
        }
        //5.根据项目id去分页查询,这里去查项目表,已经去除(被逻辑删除的表)
        Page<Project> page = studentProjectDto.toMpPageDefaultSortByCreateTimeDesc();
        //构建条件
        LambdaQueryWrapper<Project> queryWrapper = new LambdaQueryWrapper<Project>()
                .in(Project::getProjectId, teacherProjectIds);
        this.page(page,queryWrapper);

        return PageDTO.of(page,project -> {
            StudentProjectVo studentProjectVo = BeanCopyUtils.copyBean(project, StudentProjectVo.class);
            User byId = userService.getById(project.getUserId());
            studentProjectVo.setNickName(byId.getNickName());
            return studentProjectVo;
        });
    }
    @Override
    public PageDTO<ProjectListvo> getProjectList(ProjectSelectDto projectSelectDto) {
        //1.判断用户输入角色标识符,是否正确,字典其他数据校验
        if( !projectSelectDto.isRole()){
            throw new ServiceException("用户输入角色标识符不正确",400);
        }
        ivalidateProjectSelectDtoConfig(projectSelectDto);
        //2.用户信息
        UserInfoVo data = sysUserClient.getUserInfo().getData();
        //3.获取用户角色列表
        Set<String> roles = data.getRoles(
        );
        //4.判断用户是否有输入角色,根据角色来查询
        PageDTO<ProjectListvo> pageDTO = null;
        if(roles.contains(ProjectConstant.ROLE_STUDENT) && projectSelectDto.getRole().equals(ProjectConstant.ROLE_STUDENT)){
            //4.1 学生只能返回自己或者自己参加的
            pageDTO = this.getProjectListByStudent(projectSelectDto);
        }else if(roles.contains(ProjectConstant.ROLE_TEACHER) && projectSelectDto.getRole().equals(ProjectConstant.ROLE_TEACHER)){
            //4.2老师只能返回自己学学生的
            pageDTO = this.getProjectListByTeacher(projectSelectDto);
        }else if(roles.contains(ProjectConstant.ROLE_COLLEGE) && projectSelectDto.getRole().equals(ProjectConstant.ROLE_COLLEGE) ){
            //4.3 学院只能返回自己学院的,
            pageDTO = this.getProjectListByCollege(projectSelectDto);
        }else if(roles.contains(ProjectConstant.ROLE_SPECIALIST) && projectSelectDto.getRole().equals(ProjectConstant.ROLE_SPECIALIST)){
            //4.4专家组只能返回自己专家组的
            pageDTO = this.getProjectListBySpecialist(projectSelectDto);
        }else if(roles.contains(ProjectConstant.ROLE_ADMIN) && projectSelectDto.getRole().equals(ProjectConstant.ROLE_ADMIN)){
            //4.5管理员可以查看所有
            pageDTO = this.getProjectListByAdmin(projectSelectDto);
        }else {
            throw new ServiceException("用户没有访问权限",400);
        }
        //非空处理
        if (pageDTO == null) {
            pageDTO = PageDTO.empty();
        }
        return pageDTO;
    }

    /**
     * 项目列表的字典类型校验
     * @param projectSelectDto
     */
    private void ivalidateProjectSelectDtoConfig(ProjectSelectDto projectSelectDto) {
        if(projectSelectDto.getProjectRank() != null //项目级别 1:国家
                && !checkDictData(projectSelectDto.getProjectRank(),"xm_item_rank")){
            throw new ServiceException("项目级别字典值不存在",400);
        }
        if(projectSelectDto.getStates() != null //项目状态 0:未审核  1:审核中
            && !projectSelectDto.getStates().isEmpty()){
            projectSelectDto.getStates().stream()
                    .forEach(state -> {
                        if(!checkDictData(state,"xm_project_state")){
                            throw new ServiceException("states数组当中的"+state+"项目状态字典值不存在",400);
                        }
                    });
        }
        if(projectSelectDto.getSubjectCategory() != null //学科类别 1:工科
            && !checkDictData(projectSelectDto.getSubjectCategory(),"xm_item_subject_category")){
            throw new ServiceException("学科类别字典值不存在",400);
        }
        if(projectSelectDto.getType() != null //项目类型 1:创新训练项目
            && !checkDictData(projectSelectDto.getType(),"xm_item_type")){
            throw new ServiceException("项目类型字典值不存在",400);
        }
        if(projectSelectDto.getYearGroupId() != null
                && yearGroupService.getById(projectSelectDto.getYearGroupId()) == null){
            throw new ServiceException("年度id不存在",400);
        }
    }

    @Override
    public PageDTO<ProjectListvo> getProjectListByStudent(ProjectSelectDto projectSelectDto) {
        //1.获取学生获取自己报名的项目id
        List<Long> userApplyListId = studnetApplysService.getUserApplyListId();
        if(userApplyListId.isEmpty()){
            return PageDTO.empty();
        }
        //2.根据项目id查询项目和多条件分页搜索
        Page<Project> page = projectSelectDto.toMpPageDefaultSortByCreateTimeDesc();
        // 3. 构造查询条件
        LambdaQueryWrapper<Project> queryWrapper = getProjectLambdaQueryWrapper(projectSelectDto, userApplyListId,null, null,false);//学生
        this.page(page, queryWrapper);
        //4.转变为vo
        PageDTO<ProjectListvo> pageDTO = pageResultToVo(page);
        return pageDTO;
    }

    /**
     * 设置学院名称
     * @param collegeGroupIds 学院组的id
     * @param projectListvo vo集合
     */
    private void projectListVoSetCollegeGroupName(List<Long> collegeGroupIds, List<ProjectListvo> projectListvo) {
        //1.根据学院id获取学院集合
        List<CollegeGroup> collegeGroupList = collegeGroupService.selectByIds(
                collegeGroupIds
                        .stream()
                        .distinct()
                        .collect(Collectors.toList())
        );
        //2.给每个vo进行匹配一个学院名称
        projectListvo
                .stream()
                .forEach(projectvo->{
                    //2.根据学院id匹配,只需要一个即可
                    collegeGroupList.stream()
                            .filter(collegeGroup -> collegeGroup.getCollegeGroupId().equals(projectvo.getCollegeGroupId()))
                            .limit(1)
                            .forEach(collegeGroup -> {
                                projectvo.setCollegeGroupName(collegeGroup.getName());
                            });
                });
    }

    @Override
    public PageDTO<ProjectListvo> getProjectListByTeacher(ProjectSelectDto projectSelectDto) {
        //1.查询老师报名想项目列表,就知道老师学生的项目列表(实际老师参加的项目)
        List<Long> listId = teacherApplysService.getUserApplyListId();
        if(listId.isEmpty()){
            return PageDTO.empty();
        }
        //2.根据项目id查询项目和多条件分页搜索
        Page<Project> page = projectSelectDto.toMpPageDefaultSortByCreateTimeDesc();
        // 3. 构造查询条件
        LambdaQueryWrapper<Project> queryWrapper = getProjectLambdaQueryWrapper(projectSelectDto, listId,null, null,false);//老师
        this.page(page, queryWrapper);
        //4.转变为vo
        PageDTO<ProjectListvo> pageDTO = pageResultToVo(page);
        return pageDTO;
    }

    private PageDTO<ProjectListvo> pageResultToVo(Page<Project> page) {
        ArrayList<Long> userIds = new ArrayList<>();//存储查询出来的用户id
        ArrayList<Long> specialistIds = new ArrayList<>();//专家组id
        ArrayList<Long> collegeGroupIds = new ArrayList<>();//学院组id
        ArrayList<Long> yearGroupId = new ArrayList<>(); //年度id
        ArrayList<Long> yearDataId = new ArrayList<>(); //年度id

        //拷贝常用字段
        PageDTO<ProjectListvo> pageDTO = PageDTO.of(page, project -> {
            ProjectListvo projectVo = BeanCopyUtils.copyBean(project, ProjectListvo.class);
            userIds.add(project.getUserId());//用户id
            specialistIds.add(project.getSpecialistGroupId());//专家组id
            collegeGroupIds.add(project.getCollegeGroupId()); //学院组id
            yearGroupId.add(project.getYearGroupId());
            yearDataId.add(project.getYearDataId());
            return projectVo;
        });

        //1.获取用户信息,写入vo
        List<ProjectListvo> projectListvo = pageDTO.getList();
        //2.写入用户名
        projectListVoSetNickName(userIds, projectListvo);
        //3.设置学院名称
        projectListVoSetCollegeGroupName(collegeGroupIds, projectListvo);
        //4.设置专家组名称
        projectListVoSetSpecialistGroupName(specialistIds, projectListvo);
        //5写入年度名称和年度数据名称
        projectListVoSetYearGroupName(yearGroupId,yearDataId, projectListvo);
        return pageDTO;
    }

    private void projectListVoSetYearGroupName(ArrayList<Long> yearGroupId,ArrayList<Long> yearDataId, List<ProjectListvo> projectListvo) {
        //1.根据年度id获取年度集合
        List<YearGroup> yearGroups = yearGroupService.selectBatchyearGroupIds(yearGroupId);
        List<YearData> yearDatas = yearDataId.size() == 0?new ArrayList<>():yearDataService.listByIds(yearDataId);

        //2.给每个vo进行匹配一个年度名称
        projectListvo
                .stream()
                .forEach(projectVo->{
                    //年度组名称
                    yearGroups.stream()
                            .filter(yearGroup -> yearGroup.getYearGroupId().equals(projectVo.getYearGroupId()))
                            .limit(1)
                            .forEach(yearGroup -> {
                                projectVo.setYearGroupName(yearGroup.getName());
                            });
                    //年度数据名称
                    yearDatas.stream()
                            .filter(yearData -> yearData.getYearDataId().equals(projectVo.getYearDataId()))
                            .limit(1)
                            .forEach(yearData -> {
                                projectVo.setYearDataName(yearData.getName());
                            });
                });

    }

    @Override
    public PageDTO<ProjectListvo> getProjectListByCollege(ProjectSelectDto projectSelectDto) {
        //1.获取当前用户的学院id
        List<Long> collegeIdByUserId = collegeDataService.getCollegeIdByUserId();
        if(collegeIdByUserId.isEmpty()){
            return PageDTO.empty();
        }
        //2.构造条件
        Page<Project> page = projectSelectDto.toMpPageDefaultSortByCreateTimeDesc();
        LambdaQueryWrapper<Project> queryWrapper = getProjectLambdaQueryWrapper(projectSelectDto, null, collegeIdByUserId, null, false);//学院
        this.page(page, queryWrapper);
        //4.转变为vo
        PageDTO<ProjectListvo> pageDTO = pageResultToVo(page);
        return pageDTO;
    }

    @Override
    public PageDTO<ProjectListvo> getProjectListBySpecialist(ProjectSelectDto projectSelectDto) {
        //1.获取当前用户的专家组id
        List<Long> specialistIdByUserId = specialistDataService.getSpecialistIdByUserId();
        if(specialistIdByUserId.isEmpty()){
            return PageDTO.empty();
        }
        //2.构建条件
        Page<Project> page = projectSelectDto.toMpPageDefaultSortByCreateTimeDesc();
        LambdaQueryWrapper<Project> queryWrapper = getProjectLambdaQueryWrapper(projectSelectDto, null, null, specialistIdByUserId, false);
        this.page(page, queryWrapper);
        PageDTO<ProjectListvo> pageDTO = pageResultToVo(page);
        return pageDTO;
    }

    @Override
    public PageDTO<ProjectListvo> getProjectListByAdmin(ProjectSelectDto projectSelectDto) {
        //1.管理员可以查看全部
        Page<Project> page = projectSelectDto.toMpPageDefaultSortByCreateTimeDesc();
        LambdaQueryWrapper<Project> queryWrapper = getProjectLambdaQueryWrapper(projectSelectDto, null, null, null, true);
        this.page(page, queryWrapper);
        PageDTO<ProjectListvo> pageDTO = pageResultToVo(page);
        return pageDTO;
    }

    @Override
    public PageDTO<StudentVo> getStudentBindStudent(TeacherSearchStuentDto teacherSearchStuentDto) {
        //1.先获取老师参加的项目id
        List<TeacherApplys> teacherApplys = teacherApplysService.lambdaQuery()
                .eq(TeacherApplys::getUserId, SecurityUtils.getUserId())
                .list();
        List<Long> projectIds = teacherApplys.stream()
                .map(teacherApply -> teacherApply.getProjectId())
                .collect(Collectors.toList());
        if(CollUtil.isEmpty(projectIds)){
            return PageDTO.empty();
        }
        //1.1 去重已经删除项目(逻辑删除不会删除报名表)
        List<Long> projectIdList = this.lambdaQuery()
                .in(Project::getProjectId, projectIds)
                .list()
                .stream()
                .map(project -> project.getProjectId())
                .collect(Collectors.toList());
        if (projectIdList.isEmpty()){
            return PageDTO.empty();
        }
        //2.根据项目id去查询学生报名表获取学生id
        List<StudnetApplys> studnetApplysList = studnetApplysService.lambdaQuery()
                .in(StudnetApplys::getProjectId, projectIdList)
                .list();
        if (studnetApplysList.isEmpty()){
            return PageDTO.empty();
        }
        //转为和老师关联的学生的id
        List<Long> studnetIdList = studnetApplysList.stream()
                .map(studnetApplys -> studnetApplys.getUserId())
                .collect(Collectors.toList());//
        //3.根据学生id的进行条件构造搜索和分页
        Page<User> page = teacherSearchStuentDto.toMpPageDefaultSortByCreateTimeDesc();
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
                .in(User::getUserId, studnetIdList)
                .like(StrUtil.isNotBlank(teacherSearchStuentDto.getUserName()), User::getUserName, teacherSearchStuentDto.getUserName())
                .like(StrUtil.isNotBlank(teacherSearchStuentDto.getNickName()), User::getNickName, teacherSearchStuentDto.getNickName());
        userService.page(page,queryWrapper);

        return PageDTO.of(page,StudentVo.class);
    }

    @Override
    public boolean calibrationBasicInformation(BasicInformationDTO basicInformationDTO,String msg) {
        //1.信息拷贝
        ApplyForDTO dto = BeanCopyUtils.copyBean(basicInformationDTO, ApplyForDTO.class);
        //1.校验项目名是否重复
        checkProjectName(dto);
        //2.字典数据校验
        checkAddProjectDictData(dto);
        //3.校验学院是否存在和启用
        checkCollegeGroup(dto.getCollegeGroupId());
        //4.校验年度组是否存在和启用,还有年度数据的id
        checkYearGroup(dto.getYearGroupId(), dto.getYearDataId());
        //5.附件4之外的需要企业老师的支持
        teachersSupport(dto);

        return true;
    }

    @Override
    public boolean calibrationMember(MemberDTO memberDTO, String msg) {
        //1.数据拷贝
        ApplyForDTO data = BeanCopyUtils.copyBean(memberDTO, ApplyForDTO.class);
        //2.校验用户(老师和学生)都存在
        isStuentAndTeacherExist(data);
        //3.校验负责人是不是排列第一个,并且第一个id要和申请人id一致
        checkPrincipal(data);
        //4.校验报名人数,有老师和学生
        checkStuentAndTeacher(data);
        //5.todo 有空再写,验证用户负责人只有一个项目在进行中才可以申请新的项目&&(同时只有一个是负责人|| 参加项目进行中最多2个)

        //6.todo 有空再写,验证队员和老师参数进行项目最多有2个

        return true;
    }

    @Override
    public boolean calibrationBasis(BasisForTheProjectDTO basisForTheProjectDTO, String msg) {
        //1.数据拷贝
        ApplyForDTO data = BeanCopyUtils.copyBean(basisForTheProjectDTO, ApplyForDTO.class);
        //2.a,b,c 必填参数,三选一
        checkApplyFor(data);
        return true;
    }

    @Override
    public boolean calibrationBudget(BudgetDTO budgetDTO, String msg) {
        //1.数据拷贝
        ApplyForDTO data = BeanCopyUtils.copyBean(budgetDTO, ApplyForDTO.class);
        //2.校验金额格式
        checkMoney(data);
        return true;
    }

    @Override
    @Transactional
    public Response updateProject(ModifyApplyForDTO modifyApplyForDTO) {
        //1.判断当前状态是否可以进行修改
        Long projectId = modifyApplyForDTO.getProjectId();
        Project byId = this.getById(projectId);
        if (byId == null){
            throw new ServiceException("当前项目不存在");
        }
        Long userId = SecurityUtils.getUserId();
        if(!byId.getUserId().equals(userId)){
            throw new ServiceException("当前项目不属于你,请不要修改");
        }
        if (byId.getState().intValue() != ProjectConstant.PROJECT_STATUS_NOT_PASS_VALUE){
            throw new ServiceException("当前项目状态不可以修改");
        }
        //2.修改项目
        ApplyForDTO update = BeanCopyUtils.copyBean(modifyApplyForDTO, ApplyForDTO.class);
        //2.1.a ,b ,c必须三选一,参数不能为null
        checkApplyFor(update);
        //2.2.钱的比较验算 财政+学校 = 总金额
        checkMoney(update);
        //2.3.校验用户(老师和学生)都存在
        isStuentAndTeacherExist(update);
        //2.4.字典数据校验
        checkAddProjectDictData(update);
        //2.5.校验负责人是不是只有一个,并且第一个id要和申请人id一致
        checkPrincipal(update);
        //2.6.校验报名人数限制,有老师和学生
        checkStuentAndTeacher(update);
        //2.7.校验附加4之外都需要企业老师支持
        teachersSupport(update);
        //2.8.验证学院组id是否存在并且是否启用
        checkCollegeGroup(update.getCollegeGroupId());
//        isCollegeGroupExistAndEnable(applyForDTO.getCollegeGroupId());
        //2.9.年度组id  是否存在并且是否启用  ,之后再检查  年度数据id是否存在
        checkYearGroup(update.getYearGroupId(),update.getYearDataId());
        //2.10.检查报名的学生的学院组是否存在并且是否启用
        isStudentCollegeGroupsEnabled(update.getStudents());
        // 2.11.检查项目附加的URL地址
        checkFileUrl(modifyApplyForDTO.getMaterialsUrl());

        //3.1先更新项目表
        if(!updateProject(update,projectId)){
            throw new ServiceException("更新项目表失败");
        }
        //3.2记录项目过程
        addAProjectProcess(projectId, projectScheduleConfig.getNewApply());
        //3.3更新学生报名表
        if(!updateProjectStudent(update,projectId)){
            throw new ServiceException("更新项目学生报名表失败");
        }
        //3.4更新老师报名表
        if(!updateProjectTeacher(update,projectId)){
            throw new ServiceException("更新项目老师报名表失败");
        }
        return Response.success("项目修改完成,请等待重新审核");
    }


    @Override
    @Transactional
    public Response updateProjectEndTime(UpdateProjectEndTimeDto updateProjectEndTimeDto) {
        //1.判断项目是否存在
        Project byId = this.getById(updateProjectEndTimeDto.getProjectId());
        if (byId == null){
            throw new ServiceException("项目不存在");
        }
        //2.判断项目状态是否可以修改,只有进行中的才可以进行修改
        if (byId.getState().intValue() != ProjectConstant.PROJECT_STATUS_IN_PROGRESS_VALUE){
            throw new ServiceException("项目状态不可以修改,只有项目在进行中才可以修改项目结束时间");
        }
        //3.修改项目结束时间
        byId.setEndTime(updateProjectEndTimeDto.getProjectEndTime());
        boolean b = this.updateById(byId);
        if (!b){
            throw new ServiceException("修改项目结束时间失败");
        }
        //4.记录项目修改状态
        Date endTime = byId.getEndTime();
        String endTimeStr = DateUtil.formatDateTime(endTime);//上次结题时间
        String currentTimeStr = DateUtil.formatDateTime(updateProjectEndTimeDto.getProjectEndTime());
        this.addAProjectProcess(byId.getProjectId(), projectScheduleConfig.getAdvanced()+",上一次结题时间为:"+endTimeStr+",本次修改结题时间为:"+currentTimeStr);
        return Response.success("修改项目结束时间成功");
    }

    @Override
    @Transactional
    public Response updateProjectEndTimeDelay(UpdateProjectEndTimeDelayDto updateProjectEndTimeDelayDto) {
        //1.判断项目是否存在
        Project byId = this.getById(updateProjectEndTimeDelayDto.getProjectId());
        if (byId == null){
            throw new ServiceException("项目不存在");
        }
        //2.判断项目状态是否可以修改,待结题才可以修改
        if (byId.getState().intValue() != ProjectConstant.PROJECT_STATUS_PENDING_VALUE){
            throw new ServiceException("项目状态不可以修改,只有项目待结题才可以修改项目结束时间");
        }
        //3.修改项目结束时间 和 修改状态
        byId.setEndTime(updateProjectEndTimeDelayDto.getProjectEndTime());
        byId.setState(ProjectConstant.PROJECT_STATUS_IN_PROGRESS_VALUE);
        //4.记录项目修改状态
        String endTimeStr = DateUtil.formatDateTime(byId.getEndTime());
        String currentTimeStr = DateUtil.formatDateTime(updateProjectEndTimeDelayDto.getProjectEndTime());
        this.addAProjectProcess(byId.getProjectId(), projectScheduleConfig.getPostpone()+",上一次结题时间为:"+endTimeStr+",本次修改结题时间为:"+currentTimeStr);
        return Response.success("延期解题");
    }

    @Override
    @Transactional
    public Response updateProjectPass(UpdateProjectPassDto updateProjectPassDto) {
        //1.判断项目是否存在
        Project byId = this.getById(updateProjectPassDto.getProjectId());
        if (byId == null){
            throw new ServiceException("项目不存在");
        }
        //2.判断项目状态是否可以修改,只有 待结题 结题同 结题 不通过 3个状态才可以进行解题操作
        if (byId.getState().intValue() != ProjectConstant.PROJECT_STATUS_PENDING_VALUE
                && byId.getState().intValue() != ProjectConstant.PROJECT_STATUS_PASS_VALUE
                && byId.getState().intValue() != ProjectConstant.PROJECT_STATUS_NOT_PASS_VALUE_2){
            throw new ServiceException("项目状态不可以修改,只有项目待结题 结题 不通过 才可以进行项目结题操作");
        }
        //3.如果修改状态,和当前状态一样也无法修改
        if (byId.getState().intValue() == updateProjectPassDto.getStatus().intValue()){
            throw new ServiceException("项目状态和修改状态一样,请不要做重复一样操作");
        }
        //4.修改项目状态
        byId.setState(updateProjectPassDto.getStatus());
        boolean b = this.updateById(byId);
        if (!b){
            throw new ServiceException("修改项目状态失败");
        }
        //5.记录项目修改状态
        if (updateProjectPassDto.getStatus() == 3L){
            this.addAProjectProcess(byId.getProjectId(), projectScheduleConfig.getWithdraw()+",备注:"+updateProjectPassDto.getMsg());
        }else if (updateProjectPassDto.getStatus() == 4L){
            this.addAProjectProcess(byId.getProjectId(), projectScheduleConfig.getPass()+",备注:"+updateProjectPassDto.getMsg());
        }else if (updateProjectPassDto.getStatus() == 5L){
            this.addAProjectProcess(byId.getProjectId(), projectScheduleConfig.getNoPass()+",备注:"+updateProjectPassDto.getMsg());
        }else {
            throw new ServiceException("非法状态无法记录,请联系管理员,状态项目状态为:"+updateProjectPassDto.getStatus());
        }
        return Response.success();
    }

    @Override
    public Response updateProjectSpecialistGroup(UpdateProjectSpecialistGroupDto updateProjectSpecialistGroupDto) {
        //1.1判断项目是否存在和专家组是否存在
        List<Project> projectList = this.listByIds(updateProjectSpecialistGroupDto.getProjectIds());
        if(projectList.size() != updateProjectSpecialistGroupDto.getProjectIds().size()){
            //查询到项目转为id集合
            List<Long> idList = projectList.stream().map(e -> e.getProjectId()).collect(Collectors.toList());
            List<Long> collected = updateProjectSpecialistGroupDto
                    .getProjectIds().stream()
                    .filter(e -> !idList.contains(e))
                    .collect(Collectors.toList());
            throw new ServiceException("项目id集合中存在不存在项目,请检查项目id集合,项目id集合为:"+updateProjectSpecialistGroupDto.getProjectIds()+",不存在项目id集合为:"+collected);
        }
        //1.2专家组校验
        SpecialistGroup byId = this.specialistGroupService.getById(updateProjectSpecialistGroupDto.getSpecialistGroupId());
        if (byId == null){
            throw new ServiceException("专家组不存在");
        }
        //2.添加(修改)专家组
        projectList.stream().forEach(e -> {
            e.setSpecialistGroupId(updateProjectSpecialistGroupDto.getSpecialistGroupId());
        });
        boolean b = this.updateBatchById(projectList);
        if (!b){
            throw new ServiceException("修改项目专家组失败");
        }
        return Response.success();
    }

    @Override
    public void exportXmWord(Long projectId, HttpServletResponse response) throws Exception {
        // 获取项目信息，判断项目状态是否可以导出
        Project project = getProject(projectId);
        String fileName = project.getName()
                +"_"
                +LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日HH时mm分"))
                +".docx";
        // 设置响应头：保持Word文件MIME类型
        response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");

        // 使用RFC 5987 编码方式设置文件名
        String encodedFileName = URLEncoder.encode(fileName, "UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName);

        // 项目状态校验
        if (project.getState() == ProjectConstant.PROJECT_STATUS_NOT_PASS_VALUE
                || project.getState() == ProjectConstant.PROJECT_STATUS_AUDIT_VALUE) {
            throw new ServiceException("项目:<" + project.getName() + "> 状态没有审核通过，无法导出为 Word 文件");
        }

        // 确认是否是管理员或者是项目老师或者学生，否则不允许导出word
//        isContainsMembers(project);
        byte[] fileBytes = null;
        // 判断项目类型,根据3种不同的类型导出不一样的word，1为创新训练项目, 2为创业训练项目, 3为创业实践
        if (project.getType().equals(ProjectConstant.PROJECT_TYPE_INNOVATION_TRAINING)) {
            fileBytes = exportWordForTypeOne(project);
        } else if (project.getType().equals(ProjectConstant.PROJECT_TYPE_STARTUP_TRAINING)) {
            fileBytes = exportWordForTypeTwo(project);
        } else if (project.getType().equals(ProjectConstant.PROJECT_TYPE_STARTUP_PRACTICE)) {
            fileBytes = exportWordForTypeThree(project);
        }

        if (fileBytes == null) {
            throw new ServiceException("项目<" + project.getName() + ">读取失败，无法导出为 Word 文件");
        }

        // 将文件字节写入响应流（仿照方法一的方式）
        try {
                response.getOutputStream().write(fileBytes);
                response.getOutputStream().flush();
            } catch (IOException e) {
            throw new ServiceException("文件下载失败：" + e.getMessage());
        }
    }



    @Override
    @Transactional
    public boolean batchBingNumber(MultipartFile file) {
        //1.文件读 去空和去重复校验
        List<BatchBingNumberDto> dtoList = readBatchBingNumberDtoFile(file);
        //2.校验编号不存在系统当中,校验项目存在系统当中,校验项目都没有重复绑定
        List<Project> projectList = checkEXISTAndRightData(dtoList);
        //3.项目进行编号绑定
        return updateNumber(projectList, dtoList);
    }

    @Override
    public boolean getProjectNumberTemplate(HttpServletResponse response) {
        try {
            ExcelUtils.write(response, "项目编号绑定模板", "模板", BatchBingNumberDto.class, new  ArrayList<>());
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * 项目进行编号绑定
     * @param projectList 项目集合
     * @param dtoList 编号集合
     * @return
     */
    private boolean updateNumber(List<Project> projectList, List<BatchBingNumberDto> dtoList) {
        //1.项目转为map key:项目名称 value:项目对象
        Map<String, Project> projectMap = projectList
                .stream()
                .collect(Collectors.toMap(Project::getName, e -> e));

        //2.遍历导入数据,写入项目编号
        dtoList.forEach(e->{
            //获取对应项目对象
            Project project = projectMap.get(e.getName());
            //写入项目编号
            project.setProjectNumber(e.getProjectNumber());
        });
        //3.更新数据库
        return this.updateBatchById(projectList);
    }

    /**
     * 校验编号不存在系统当中,校验项目存在系统当中,校验项目都没有重复绑定<br>
     * 操作数据库校验
     * @param dtoList 导入数据
     * @return 返回需要绑定项目集合
     */
    private List<Project> checkEXISTAndRightData(List<BatchBingNumberDto> dtoList) {
        //1.校验编号全部不存在数据库当中

        //2查询全部编号 转为set集合
        Set<String> numberSet = this.lambdaQuery().in(Project::getProjectNumber, dtoList.stream().map(BatchBingNumberDto::getProjectNumber).collect(Collectors.toList()))
                .select(Project::getProjectNumber)
                .list()
                .stream()
                .map(Project::getProjectNumber)
                .collect(Collectors.toSet());
        //3.根据项目名称查询全部项目
        List<Project> projectList = this.lambdaQuery().in(Project::getName, dtoList.stream().map(BatchBingNumberDto::getName).collect(Collectors.toList()))
                .list();

        //项目List转为map key 为项目名称 value为项目对象
        Map<String, Project> projectMap = projectList
                .stream()
                .collect(Collectors.toMap(Project::getName, e -> e));


        //4.错误信息记录
        List<String> errorList = new ArrayList<>();

        for (int i = 0; i < dtoList.size(); i++) {
            BatchBingNumberDto dto = dtoList.get(i);

            //当前行错误信息
            List<String> error = new ArrayList<>();

            //1.校验编号不存在系统
            if (numberSet.contains(dto.getProjectNumber())) {
                error.add("编号:《"+dto.getProjectNumber()+"》已经存在,不要重复绑定");
            }
            //2.1校验项目不存在 和 项目没有绑定编号
            if (!projectMap.containsKey(dto.getName())) {
                error.add("项目名称:《"+dto.getName()+"》不存在,请检查");
            }else{
                //2.2校验当前项目有没有绑定项目编号
                Project project = projectMap.get(dto.getName());
                if(StrUtil.isNotBlank(project.getProjectNumber())){
                    error.add("项目名称:《"+dto.getName()+"》已经绑定了项目编号:《"+project.getProjectNumber()+"》,请检查");
                }
            }

            //3.当前行有错误信息进行记录
            if (CollUtil.isNotEmpty(error)) {
                errorList.add("第" + (i + 2) + "行："+CollUtil.join(error,","));
            }
        }
        //5.判断错误信息集合有数据抛出异常
        if (CollUtil.isNotEmpty(errorList)) {
            throw new ServiceException("数据错误异常:"+CollUtil.join(errorList,";\n"));
        }

        return projectList;
    }

    /**
     * 读取导入文件,校验去空,去重校验
     * @param file
     * @return 导入数据
     */
    private static List<BatchBingNumberDto> readBatchBingNumberDtoFile(MultipartFile file) {
        //1.读取文件
        List<BatchBingNumberDto> list = null;
        try {
             list = ExcelUtils.read(file, BatchBingNumberDto.class);
        } catch (Exception e) {
            throw new ServiceException("解析Execl失败");
        }
        if(CollUtil.isEmpty(list)){
            throw new ServiceException("当前Execl没有读取到如何束脚");
        }

        //2.1校验数据 项目编号或者项目名称重复
        HashMap<String, Long> numberMap = new HashMap<>(); //项目编号集合 记录项目编号 key 为项目编号 value 为第一个匹配到的项目编号的起始位置
        HashMap<String, Long> nameMap = new HashMap<>(); //项目名称集合 记录项目名称 key 为项目名称 value 为第一个匹配到的项目名称的起始位置

        //2.2 错误信息集合
        List<String> errorList = new ArrayList<>();

        //2.3 遍历数据
        for (int i =0; i < list.size(); i++){

            BatchBingNumberDto dto = list.get(i);

            //当前行的错误信息集合
            List<String> error = new ArrayList<>();


            //2.3.1 为空判断 默认不为空
            boolean isNumberNUllist = false; //编号不为空
            boolean isNameNUllist = false; //名称不为空

            if (StrUtil.isBlank(dto.getProjectNumber())){
                isNumberNUllist = true;
                error.add("项目编号不能为空");

            }
            if (StrUtil.isBlank(dto.getName())){
                isNameNUllist = true;
                error.add("项目名称不能为空");
            }
            //2.3.2 去重校验
            if(!isNumberNUllist && numberMap.containsKey(dto.getProjectNumber())){
                error.add("项目编号:"+dto.getProjectNumber()+"重复,第一次出现是:"+numberMap.get(dto.getProjectNumber())+"行");
            }else if (!isNumberNUllist){ //不为空,第一次出现添加集合当中
                numberMap.put(dto.getProjectNumber(), (long) (i+2));
            }
            if(!isNameNUllist && nameMap.containsKey(dto.getName())){
                error.add("项目名称:"+dto.getName()+"重复,第一次出现是:"+nameMap.get(dto.getName())+"行");
            }else if (!isNameNUllist){ //不为空,第一次出现添加集合当中
                nameMap.put(dto.getName(), (long) (i+2));
            }
            //2.3.3 错误信息
            if(CollUtil.isNotEmpty(error)){
                errorList.add("第" + (i + 2) + "行：" + CollUtil.join(error, ","));
            }
        }
        //3.错误信息校验
        if(CollUtil.isNotEmpty(errorList)){
            throw new ServiceException("数据错误信息:"+CollUtil.join(errorList,";\n"));
        }


        return list;
    }


    /**
     * 导出为创业训练项目word
     * @param project 项目信息
     * @return word文件字节数组
     * @throws Exception 生成word文件失败的错误信息
     */
    private byte[] exportWordForTypeThree(Project project) throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("templates/typeThree.docx");
        try {
            if (inputStream == null) {
                inputStream.close();
                throw new ServiceException("模板文件不存在或路径错误", 444);
            }
            //构建通用数据
            HashMap<String, Object> wordMap = getProjectMap(project);
            //二：立项依据
            wordMap.put("cOne", extractTextFromHtml(project.getCOne()));
            wordMap.put("cTwo", extractTextFromHtml(project.getCTwo()));
            wordMap.put("cThree", extractTextFromHtml(project.getCThree()));
            wordMap.put("cFour", extractTextFromHtml(project.getCFour()));
            wordMap.put("cFive", extractTextFromHtml(project.getCFive()));
            wordMap.put("cSix", extractTextFromHtml(project.getCSix()));
            wordMap.put("cSeven", extractTextFromHtml(project.getCSeven()));
            wordMap.put("cEight", extractTextFromHtml(project.getCEight()));
            wordMap.put("cNine", extractTextFromHtml(project.getCNine()));
            wordMap.put("cTen", extractTextFromHtml(project.getCTen()));
            wordMap.put("cEleven", extractTextFromHtml(project.getCEleven()));
            //TODO 立项依据的图片,每一个立项依据的图片垂直拼接成一张单独的图片
            wordMap.put("imageA", getPictureRenderData(project.getCOne()));
            wordMap.put("imageB", getPictureRenderData(project.getCTwo()));
            wordMap.put("imageC", getPictureRenderData(project.getCThree()));
            wordMap.put("imageD", getPictureRenderData(project.getCFour()));
            wordMap.put("imageE", getPictureRenderData(project.getCFive()));
            wordMap.put("imageF", getPictureRenderData(project.getCSix()));
            wordMap.put("imageG", getPictureRenderData(project.getCSeven()));
            wordMap.put("imageH", getPictureRenderData(project.getCEight()));
            wordMap.put("imageI", getPictureRenderData(project.getCNine()));
            wordMap.put("imageJ", getPictureRenderData(project.getCTen()));
            wordMap.put("imageK", getPictureRenderData(project.getCEleven()));
            //三、经费预算（单位：元）
            DetailedFundingOneDto budgetOne = getBudget(project.getBudget(), project.getType());//获取详细经费预算
            HashMap<String, Object> budgetOneMap = convertObjectToHashMap(budgetOne);//获取详细经费预算map
            wordMap.putAll(budgetOneMap);
            // 创建循环行表格渲染策略
            HackLoopTableRenderPolicy policy = new HackLoopTableRenderPolicy();
            // 修正绑定错误，将 teacherListb 改为 teacherListB
            Configure config = Configure.builder()
                    .bind("studentList", policy)
                    .bind("teacherListA", policy)
                    .bind("teacherListB", policy)
                    .build();
            // 编译模板并渲染数据
            XWPFTemplate template = XWPFTemplate.compile(inputStream, config).render(wordMap);
            // 将 Word 文档写入 ByteArrayOutputStream
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            template.write(baos);
            inputStream.close();
            return baos.toByteArray();
        } catch (IOException e) {
            inputStream.close();
            throw new ServiceException("导出失败", 444);
        }
    }

    /**
     * 导出为创业训练项目word
     * @param project 项目信息
     * @return word文件字节数组
     * @throws Exception 生成word文件失败的错误信息
     */
    private byte[] exportWordForTypeTwo(Project project) {

        try {
            ClassLoader classLoader = getClass().getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream("templates/typeTwo.docx");
            if (inputStream == null) {
                throw new ServiceException("模板文件不存在或路径错误", 444);
            }
            //构建通用数据
            HashMap<String, Object> wordMap = getProjectMap(project);
            //二：立项依据
            wordMap.put("bOne", extractTextFromHtml(project.getBOne()));
            wordMap.put("bTwo", extractTextFromHtml(project.getBTwo()));
            wordMap.put("bThree", extractTextFromHtml(project.getBThree()));
            wordMap.put("bFour", extractTextFromHtml(project.getBFour()));
            wordMap.put("bFive", extractTextFromHtml(project.getBFive()));
            wordMap.put("bSix", extractTextFromHtml(project.getBSix()));
            wordMap.put("bSeven", extractTextFromHtml(project.getBSeven()));
            wordMap.put("bEight", extractTextFromHtml(project.getBEight()));
            // 立项依据的图片,每一个立项依据的图片垂直拼接成一张单独的图片
            wordMap.put("imageA", getPictureRenderData(project.getBOne()));
            wordMap.put("imageB", getPictureRenderData(project.getBTwo()));
            wordMap.put("imageC", getPictureRenderData(project.getBThree()));
            wordMap.put("imageD", getPictureRenderData(project.getBFour()));
            wordMap.put("imageE", getPictureRenderData(project.getBFive()));
            wordMap.put("imageF", getPictureRenderData(project.getBSix()));
            wordMap.put("imageG", getPictureRenderData(project.getBSeven()));
            wordMap.put("imageH", getPictureRenderData(project.getBEight()));

            //三、经费预算（单位：元）
            DetailedFundingOneDto budgetOne = getBudget(project.getBudget(), project.getType());//获取详细经费预算
            HashMap<String, Object> budgetOneMap = convertObjectToHashMap(budgetOne);//获取详细经费预算map
            wordMap.putAll(budgetOneMap);
            // 创建循环行表格渲染策略
            HackLoopTableRenderPolicy policy = new HackLoopTableRenderPolicy();
            // 修正绑定错误，将 teacherListb 改为 teacherListB
            Configure config = Configure.builder()
                    .bind("studentList", policy)
                    .bind("teacherListA", policy)
                    .bind("teacherListB", policy)
                    .build();
            // 编译模板并渲染数据
            XWPFTemplate template = XWPFTemplate.compile(inputStream, config).render(wordMap);
            // 将 Word 文档写入 ByteArrayOutputStream
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            template.write(baos);
            inputStream.close();
            return baos.toByteArray();
        } catch (IOException e) {
            throw new ServiceException("导出失败", 444);
        }
    }

    /**
     * 导出为创新训练项目word
     * @param project 项目信息
     * @return word文件字节数组
     * @throws Exception 生成word文件失败的错误信息
     */
    private byte[] exportWordForTypeOne(Project project) {
        try {
            //获取模板文件
            ClassLoader classLoader = getClass().getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream("templates/typeOne.docx");
            if (inputStream == null) {
                throw new ServiceException("模板文件不存在或路径错误", 444);
            }
            //构建通用数据
            HashMap<String, Object> wordMap = getProjectMap(project);
            //二：立项依据
            wordMap.put("aOne", extractTextFromHtml(project.getAOne()));
            wordMap.put("aTwo", extractTextFromHtml(project.getATwo()));
            wordMap.put("aThree", extractTextFromHtml(project.getAThree()));
            wordMap.put("aFour", extractTextFromHtml(project.getAFour()));
            wordMap.put("aFive", extractTextFromHtml(project.getAFive()));
            wordMap.put("aSix", extractTextFromHtml(project.getASix()));
            wordMap.put("aSeven", extractTextFromHtml(project.getASeven()));
            wordMap.put("aEight", extractTextFromHtml(project.getAEight()));
            // 立项依据的图片,每一个立项依据的图片垂直拼接成一张单独的图片
            wordMap.put("imageA", getPictureRenderData(project.getAOne()));
            wordMap.put("imageB", getPictureRenderData(project.getATwo()));
            wordMap.put("imageC", getPictureRenderData(project.getAThree()));
            wordMap.put("imageD", getPictureRenderData(project.getAFour()));
            wordMap.put("imageE", getPictureRenderData(project.getAFive()));
            wordMap.put("imageF", getPictureRenderData(project.getASix()));
            wordMap.put("imageG", getPictureRenderData(project.getASeven()));
            wordMap.put("imageH", getPictureRenderData(project.getAEight()));
            //三、经费预算（单位：元）
            DetailedFundingOneDto budgetOne = getBudget(project.getBudget(), project.getType());//获取详细经费预算
            HashMap<String, Object> budgetOneMap = convertObjectToHashMap(budgetOne);//获取详细经费预算map
            wordMap.putAll(budgetOneMap);
            // 创建循环行表格渲染策略
            HackLoopTableRenderPolicy policy = new HackLoopTableRenderPolicy();
            // 修正绑定错误，将 teacherListb 改为 teacherListB
            Configure config = Configure.builder()
                    .bind("studentList", policy)
                    .bind("teacherListA", policy)
                    .build();
            // 编译模板并渲染数据
            XWPFTemplate template = XWPFTemplate.compile(inputStream, config).render(wordMap);
            // 将 Word 文档写入 ByteArrayOutputStream
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            template.write(baos);
            inputStream.close();
            return baos.toByteArray();
        } catch (IOException e) {
            throw new ServiceException("导出失败", 444);
        }
    }

    /**
     * 创新训练项目详细经费预算json转换为实体
     * @param budget 详细经费预算json
     * @param type 经费预算对应的项目类型
     * @return 详细经费预算实体
     */
    private static DetailedFundingOneDto getBudget(String budget, Long type) {
        DetailedFundingOneDto detailedFundingOneDto = new DetailedFundingOneDto();
        Gson gson = new Gson();
        Type listType = new TypeToken<List<DetailedFundingDto>>() {
        }.getType();
        List<DetailedFundingDto> dtoList = gson.fromJson(budget, listType);
        // 获取映射关系
        if (type == 1L) {
            Map<String, BiConsumer<DetailedFundingOneDto, DetailedFundingDto>> mapping = getMappingOne();
            dtoList.forEach(dto -> {
                BiConsumer<DetailedFundingOneDto, DetailedFundingDto> consumer = mapping.get(dto.getName());
                System.out.println(dto);
                if (consumer != null) {
                    consumer.accept(detailedFundingOneDto, dto);
                }
            });
        } else if (type == 2L) {
            Map<String, BiConsumer<DetailedFundingOneDto, DetailedFundingDto>> mapping = getMappingTwo();
            dtoList.forEach(dto -> {
                BiConsumer<DetailedFundingOneDto, DetailedFundingDto> consumer = mapping.get(dto.getName());
                System.out.println(dto);
                if (consumer != null) {
                    consumer.accept(detailedFundingOneDto, dto);
                }
            });
        } else if (type == 3L) {
            Map<String, BiConsumer<DetailedFundingOneDto, DetailedFundingDto>> mapping = getMappingThree();
            dtoList.forEach(dto -> {
                BiConsumer<DetailedFundingOneDto, DetailedFundingDto> consumer = mapping.get(dto.getName());
                System.out.println(dto);
                if (consumer != null) {
                    consumer.accept(detailedFundingOneDto, dto);
                }
            });
        }
        return detailedFundingOneDto;
    }

    /**
     * 定义类型创业训练项目的详细经费的映射关系
     * @return 创业训练项目的映射关系map
     */
    private static Map<String, BiConsumer<DetailedFundingOneDto, DetailedFundingDto>> getMappingThree() {
        Map<String, BiConsumer<DetailedFundingOneDto, DetailedFundingDto>> mapping = new HashMap<>();
        mapping.put("1. 业务费", (oneDto, dto) -> {
            oneDto.setOneA(dto.getBudgetFunds());
            oneDto.setOneB(dto.getMainPurpose());
            oneDto.setOneC(dto.getPreviousStage());
            oneDto.setOneD(dto.getPostStage());
        });
        mapping.put("(1)能源动力费", (oneDto, dto) -> {
            oneDto.setOneAa(dto.getBudgetFunds());
            oneDto.setOneBa(dto.getMainPurpose());
            oneDto.setOneCa(dto.getPreviousStage());
            oneDto.setOneDa(dto.getPostStage());
        });
        mapping.put("(2)会议费", (oneDto, dto) -> {
            oneDto.setOneAb(dto.getBudgetFunds());
            oneDto.setOneBb(dto.getMainPurpose());
            oneDto.setOneCb(dto.getPreviousStage());
            oneDto.setOneDb(dto.getPostStage());
        });
        mapping.put("(3)差旅费", (oneDto, dto) -> {
            oneDto.setOneAc(dto.getBudgetFunds());
            oneDto.setOneBc(dto.getMainPurpose());
            oneDto.setOneCc(dto.getPreviousStage());
            oneDto.setOneDc(dto.getPostStage());
        });
        mapping.put("(4)文献检索费", (oneDto, dto) -> {
            oneDto.setOneAd(dto.getBudgetFunds());
            oneDto.setOneBd(dto.getMainPurpose());
            oneDto.setOneCd(dto.getPreviousStage());
            oneDto.setOneDd(dto.getPostStage());
        });
        mapping.put("(5)论文出版费", (oneDto, dto) -> {
            oneDto.setOneAe(dto.getBudgetFunds());
            oneDto.setOneBe(dto.getMainPurpose());
            oneDto.setOneCe(dto.getPreviousStage());
            oneDto.setOneDe(dto.getPostStage());
        });
        mapping.put("2. 仪器设备购置费", (oneDto, dto) -> {
            oneDto.setTwoA(dto.getBudgetFunds());
            oneDto.setTwoB(dto.getMainPurpose());
            oneDto.setTwoC(dto.getPreviousStage());
            oneDto.setTwoD(dto.getPostStage());
        });
        mapping.put("3. 材料费", (oneDto, dto) -> {
            oneDto.setThreeA(dto.getBudgetFunds());
            oneDto.setThreeB(dto.getMainPurpose());
            oneDto.setThreeC(dto.getPreviousStage());
            oneDto.setThreeD(dto.getPostStage());
        });
        mapping.put("4. 咨询费", (oneDto, dto) -> {
            oneDto.setThreeA(dto.getBudgetFunds());
            oneDto.setThreeB(dto.getMainPurpose());
            oneDto.setThreeC(dto.getPreviousStage());
            oneDto.setThreeD(dto.getPostStage());
        });
        return mapping;
    }

    /**
     * 定义类型创业训练项目的详细经费的映射关系
     * @return 创业训练项目的映射关系map
     */
    private static Map<String, BiConsumer<DetailedFundingOneDto, DetailedFundingDto>> getMappingTwo() {
        Map<String, BiConsumer<DetailedFundingOneDto, DetailedFundingDto>> mapping = new HashMap<>();
        mapping.put("1. 业务费", (oneDto, dto) -> {
            oneDto.setOneA(dto.getBudgetFunds());
            oneDto.setOneB(dto.getMainPurpose());
            oneDto.setOneC(dto.getPreviousStage());
            oneDto.setOneD(dto.getPostStage());
        });
        mapping.put("(1)能源动力费", (oneDto, dto) -> {
            oneDto.setOneAa(dto.getBudgetFunds());
            oneDto.setOneBa(dto.getMainPurpose());
            oneDto.setOneCa(dto.getPreviousStage());
            oneDto.setOneDa(dto.getPostStage());
        });
        mapping.put("(2)会议费", (oneDto, dto) -> {
            oneDto.setOneAb(dto.getBudgetFunds());
            oneDto.setOneBb(dto.getMainPurpose());
            oneDto.setOneCb(dto.getPreviousStage());
            oneDto.setOneDb(dto.getPostStage());
        });
        mapping.put("(3)差旅费", (oneDto, dto) -> {
            oneDto.setOneAc(dto.getBudgetFunds());
            oneDto.setOneBc(dto.getMainPurpose());
            oneDto.setOneCc(dto.getPreviousStage());
            oneDto.setOneDc(dto.getPostStage());
        });
        mapping.put("(4)文献检索费", (oneDto, dto) -> {
            oneDto.setOneAd(dto.getBudgetFunds());
            oneDto.setOneBd(dto.getMainPurpose());
            oneDto.setOneCd(dto.getPreviousStage());
            oneDto.setOneDd(dto.getPostStage());
        });
        mapping.put("(5)论文出版费", (oneDto, dto) -> {
            oneDto.setOneAe(dto.getBudgetFunds());
            oneDto.setOneBe(dto.getMainPurpose());
            oneDto.setOneCe(dto.getPreviousStage());
            oneDto.setOneDe(dto.getPostStage());
        });
        mapping.put("2. 仪器设备购置费", (oneDto, dto) -> {
            oneDto.setTwoA(dto.getBudgetFunds());
            oneDto.setTwoB(dto.getMainPurpose());
            oneDto.setTwoC(dto.getPreviousStage());
            oneDto.setTwoD(dto.getPostStage());
        });
        mapping.put("3. 材料费", (oneDto, dto) -> {
            oneDto.setThreeA(dto.getBudgetFunds());
            oneDto.setThreeB(dto.getMainPurpose());
            oneDto.setThreeC(dto.getPreviousStage());
            oneDto.setThreeD(dto.getPostStage());
        });
        return mapping;
    }

    /**
     * 定义类型创新训练项目的详细经费的映射关系
     * @return 创新训练项目的映射关系map
     */
    private static Map<String, BiConsumer<DetailedFundingOneDto, DetailedFundingDto>> getMappingOne() {
        Map<String, BiConsumer<DetailedFundingOneDto, DetailedFundingDto>> mapping = new HashMap<>();
        mapping.put("1. 业务费", (oneDto, dto) -> {
            oneDto.setOneA(dto.getBudgetFunds());
            oneDto.setOneB(dto.getMainPurpose());
            oneDto.setOneC(dto.getPreviousStage());
            oneDto.setOneD(dto.getPostStage());
        });
        mapping.put("(1)计算、分析、测试费", (oneDto, dto) -> {
            oneDto.setOneAa(dto.getBudgetFunds());
            oneDto.setOneBa(dto.getMainPurpose());
            oneDto.setOneCa(dto.getPreviousStage());
            oneDto.setOneDa(dto.getPostStage());
        });
        mapping.put("(2)能源动力费", (oneDto, dto) -> {
            oneDto.setOneAb(dto.getBudgetFunds());
            oneDto.setOneBb(dto.getMainPurpose());
            oneDto.setOneCb(dto.getPreviousStage());
            oneDto.setOneDb(dto.getPostStage());
        });
        mapping.put("(3)会议、差旅费", (oneDto, dto) -> {
            oneDto.setOneAc(dto.getBudgetFunds());
            oneDto.setOneBc(dto.getMainPurpose());
            oneDto.setOneCc(dto.getPreviousStage());
            oneDto.setOneDc(dto.getPostStage());
        });
        mapping.put("(4)文献检索费", (oneDto, dto) -> {
            oneDto.setOneAd(dto.getBudgetFunds());
            oneDto.setOneBd(dto.getMainPurpose());
            oneDto.setOneCd(dto.getPreviousStage());
            oneDto.setOneDd(dto.getPostStage());
        });
        mapping.put("(5)论文出版费", (oneDto, dto) -> {
            oneDto.setOneAe(dto.getBudgetFunds());
            oneDto.setOneBe(dto.getMainPurpose());
            oneDto.setOneCe(dto.getPreviousStage());
            oneDto.setOneDe(dto.getPostStage());
        });
        mapping.put("2. 仪器设备购置费", (oneDto, dto) -> {
            oneDto.setTwoA(dto.getBudgetFunds());
            oneDto.setTwoB(dto.getMainPurpose());
            oneDto.setTwoC(dto.getPreviousStage());
            oneDto.setTwoD(dto.getPostStage());
        });
        mapping.put("3. 实验装置试制费", (oneDto, dto) -> {
            oneDto.setThreeA(dto.getBudgetFunds());
            oneDto.setThreeB(dto.getMainPurpose());
            oneDto.setThreeC(dto.getPreviousStage());
            oneDto.setThreeD(dto.getPostStage());
        });
        mapping.put("4. 材料费", (oneDto, dto) -> {
            oneDto.setFourA(dto.getBudgetFunds());
            oneDto.setFourB(dto.getMainPurpose());
            oneDto.setFourC(dto.getPreviousStage());
            oneDto.setFourD(dto.getPostStage());
        });
        return mapping;
    }


    /**
     * 提取html文本,把里面所有文本标签进行提取，每一个文本不同的标签会进行换行
     * @param  html html文本
     * @return 只带有文本的html
     */
    private static String extractTextFromHtml(String html) {
        // 解析 HTML
        Document doc = Jsoup.parse(html);
        // 查找所有的 <p> 标签
        Elements paragraphs = doc.select("p");
        StringBuilder extractedText = new StringBuilder();

        for (Element p : paragraphs) {
            // 处理段落内的文本和 <br> 标签
            for (int i = 0; i < p.childNodeSize(); i++) {
                if (p.childNode(i).nodeName().equals("br")) {
                    extractedText.append("\n");
                } else {
                    extractedText.append(p.childNode(i).toString().replaceAll("<[^>]*>", ""));
                }
            }
            extractedText.append("\n\n");
        }
        return extractedText.toString();
    }

    /**
     * 三个项目中共有的项目信息，除了类型一没有企业导师的信息
     * @param project 项目信息实体
     * @return 项目信息map
     */
    private HashMap<String, Object> getProjectMap(Project project) {
        //把实体类转为map
        HashMap<String, Object> wordMap = convertObjectToHashMap(project);
        //word封面
        StudnetApplys projectHead = this.studnetApplysService.getOne(new LambdaQueryWrapper<StudnetApplys>()
                .eq(StudnetApplys::getUserId, project.getUserId())
                .eq(StudnetApplys::getProjectId, project.getProjectId()));
        wordMap.put("studentname", projectHead.getName());//项目负责人名字
        wordMap.put("studentPhone", projectHead.getPhone());//项目负责人联系电话
        wordMap.put("schoolName", collegeGroupService.getById(projectHead.getCollegeGroupId()).getName());//项目负责人学院组名称
        wordMap.put("userName", projectHead.getUserName());//项目负责人学号
        wordMap.put("professionalClass", projectHead.getProfessionalClass());//项目负责人专业班级
        Long firstTeacherId = getProjectMembersIds(project.getTeacherId()).get(0);
        TeacherApplys firstTeacher = this.teacherApplysService.list(new LambdaQueryWrapper<TeacherApplys>()
                        .eq(TeacherApplys::getUserId, firstTeacherId)
                        .eq(TeacherApplys::getProjectId, project.getProjectId())
                        .eq(TeacherApplys::getIsTeacher, 0))
                .get(0);
        wordMap.put("teacherName", firstTeacher.getName());
        wordMap.put("teacherPhone", firstTeacher.getPhone());
        if (!project.getType().equals(ProjectConstant.PROJECT_TYPE_INNOVATION_TRAINING)) {
            TeacherApplys firstfirmTeacher = this.teacherApplysService.list(new LambdaQueryWrapper<TeacherApplys>()
                            .eq(TeacherApplys::getUserId, firstTeacherId)
                            .eq(TeacherApplys::getProjectId, project.getProjectId())
                            .eq(TeacherApplys::getIsTeacher, 1))
                    .get(0);
            wordMap.put("firmTeacherName", firstfirmTeacher.getName());
            wordMap.put("firmTeacherPhone", firstfirmTeacher.getPhone());
        }
        wordMap.put("beginTime", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .format(LocalDateTime.ofInstant(project.getBeginTime().toInstant(), ZoneId.systemDefault())));//活动的开始时间
        wordMap.put("endTime", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .format(LocalDateTime.ofInstant(project.getEndTime().toInstant(), ZoneId.systemDefault())));//活动的结束时间
        //一：基本情况
        wordMap.put("beginYear", DateTimeFormatter.ofPattern("yyyy")
                .format(LocalDateTime.ofInstant(project.getBeginTime().toInstant(), ZoneId.systemDefault())));//活动开始年份
        wordMap.put("beinMonth", project.getBeginTime().getMonth() + 1);//活动开始月份
        wordMap.put("endYear", project.getEndTime().getYear() + 1900);//活动结束年份
        wordMap.put("endMonth", project.getEndTime().getMonth() + 1);//活动结束月份
        wordMap.put("majorName", userService.getById(project.getUserId()).getMajorName());
        wordMap.put("studentList", getAllStudents(project));//获得项目所有学生并添加回去map
        wordMap.put("teacherListA", getAllTeachers(project));//获得项目所有指导老师并添加回去map
        wordMap.put("teacherListB", getAllFirmTeacherId(project));//获得项目所有指导老师并添加回去map
        return wordMap;
    }

    /**
     * 获取当前项目里全部企业老师的具体报名信息
     * @param project 项目信息实体
     * @return 一个列表，里面是所有企业导师的报名信息，为key-value形式
     */
    private List<Map<String, Object>> getAllFirmTeacherId(Project project) {
        List<Map<String, Object>> allFirmTeachers = new ArrayList<>();
        List<Long> projectMembers = getProjectMembersIds(project.getFirmTeacherId());
        projectMembers.forEach(id -> {
            TeacherApplys projectTeacher = this.teacherApplysService.getOne(new LambdaQueryWrapper<TeacherApplys>()
                    .eq(TeacherApplys::getUserId, id)
                    .eq(TeacherApplys::getProjectId, project.getProjectId())
                    .eq(TeacherApplys::getIsTeacher, 1));
            Map<String, Object> map = new HashMap<>();
            map.put("name", projectTeacher.getName());
            map.put("unit", projectTeacher.getUnit());
            map.put("post", projectTeacher.getPost());
            map.put("phone", projectTeacher.getPhone());
            map.put("mailbox", projectTeacher.getMailbox());
            allFirmTeachers.add(map);
        });
        return allFirmTeachers;
    }

    /**
     * 获取项目里全部老师的具体报名信息
     * @param project 项目信息实体
     * @return 一个列表，里面是所有老师的报名信息，为key-value形式
     */
    private List<Map<String, Object>> getAllTeachers(Project project) {
        List<Map<String, Object>> allTeachers = new ArrayList<>();
        List<Long> projectMembers = getProjectMembersIds(project.getTeacherId());
        projectMembers.forEach(id -> {
            TeacherApplys projectTeacher = this.teacherApplysService.getOne(new LambdaQueryWrapper<TeacherApplys>()
                    .eq(TeacherApplys::getUserId, id)
                    .eq(TeacherApplys::getProjectId, project.getProjectId())
                    .eq(TeacherApplys::getIsTeacher, 0));
            Map<String, Object> map = new HashMap<>();
            map.put("name", projectTeacher.getName());
            map.put("unit", projectTeacher.getUnit());
            map.put("post", projectTeacher.getPost());
            map.put("phone", projectTeacher.getPhone());
            map.put("mailbox", projectTeacher.getMailbox());
            allTeachers.add(map);
        });
        return allTeachers;
    }

    //获取项目里全部学生的具体报名信息
    private List<Map<String, Object>> getAllStudents(Project project) {
        List<Map<String, Object>> allStudents = new ArrayList<>();
        List<Long> projectMembers = getProjectMembersIds(project.getMemberId());
        projectMembers.forEach(id -> {
            StudnetApplys projectStudent = this.studnetApplysService.getOne(new LambdaQueryWrapper<StudnetApplys>()
                    .eq(StudnetApplys::getUserId, id)
                    .eq(StudnetApplys::getProjectId, project.getProjectId()));
            Map<String, Object> map = new HashMap<>();
            map.put("name", projectStudent.getName());
            map.put("sex", projectStudent.getSex() == 0L ? "男" : "女");
            map.put("userName", projectStudent.getUserName());
            DictData xmItemNation = this.dictDataService.getOne(new LambdaQueryWrapper<DictData>()
                    .eq(DictData::getDictType, "xm_item_nation")
                    .eq(DictData::getDictValue, projectStudent.getNation()));
            map.put("nation", xmItemNation.getDictLabel());
            map.put("dateOfBirth", projectStudent.getDateOfBirth());
            map.put("collegeGroupId", collegeGroupService.getById(projectStudent.getCollegeGroupId()).getName());
            map.put("professionalClass", projectStudent.getProfessionalClass());
            map.put("phone", projectStudent.getPhone());
            map.put("mobilePhone", projectStudent.getMobilePhone());
            map.put("mailbox", projectStudent.getMailbox());
            map.put("job", projectStudent.getJob());
            map.put("isPrincipal", projectStudent.getIsPrincipal() == 1 ? "是" : "否");
            allStudents.add(map);
        });
        return allStudents;
    }

    /**
     * 将对象转换为HashMap
     * @param object 任意对象实体
     * @return 任意对象实体的HashMap
     */
    public static HashMap<String, Object> convertObjectToHashMap(Object object) {
        // 创建 ObjectMapper 实例
        ObjectMapper objectMapper = new ObjectMapper();
        // 将对象转换为 Map
        Map<String, Object> map = objectMapper.convertValue(object, Map.class);
        // 创建新的 HashMap 并复制 Map 中的键值对
        return new HashMap<>(map);
    }

    /**
     * 用于判断当前用户是否有权限导出当前项目的word
     * @param project 项目信息实体
     */
    private void isContainsMembers(Project project) {
        //获取用户角色以及id
        UserInfoVo data = sysUserClient.getUserInfo().getData();
        Long userId = SecurityUtils.getUserId();
        //获取用户角色列表
        Set<String> roles = data.getRoles();
        //获取项目用户id集合,包括学生id,老师id,指导老师id
        List<Long> studentIds = getProjectMembersIds(project.getMemberId());
        List<Long> teacherIds = getProjectMembersIds(project.getTeacherId());
        List<Long> firmTeacherIds = getProjectMembersIds(project.getFirmTeacherId());
        List<Long> allIds = Stream.concat(studentIds.stream(), Stream.concat(teacherIds.stream(), firmTeacherIds.stream())).collect(Collectors.toList());
        //查找id是否存在项目成员里，如果不在则查找用户角色表是否有管理员身份，如果都不符合则不允许导出word
        if (!allIds.contains(userId) &&
                roles.stream().noneMatch(role -> role.equalsIgnoreCase(RoleConstant.ADMIN))) {
            throw new ServiceException("当前用户没有权限导出word", 444);
        }
    }

    //idJson转换为list

    /**
     * 将项目信息里的id字符串转换为对应的idl的ist
     * @param ids 项目信息里的id字符串
     * @return 包含所有id的list
     */
    private List<Long> getProjectMembersIds(String ids) {
        Gson gson = new Gson();
        //把数据库的id集合转换为list的id集合
        Long[] idArray = gson.fromJson(ids, Long[].class);
        return Arrays.asList(idArray);
    }


    /**
     * 创建用于插入word的图片，此次固定了图片的宽高，如果不固定的话word可能会出现图片过大
     * @param html html文本
     * @return 用于插入word的图片对象
     * @throws IOException 图片获取异常
     */
    private PictureRenderData getPictureRenderData(String html) {
        //当前html文本的全部图片路径
        String[] extractAllImagePaths = extractAllImagePaths(html);
        if (extractAllImagePaths.length == 0){
            return null;
        }
        //获取图片
        ArrayList<InputStream> inputStreams = new ArrayList<>();
        for (String extractAllImagePath : extractAllImagePaths) {
            ResponseEntity<byte[]> responseEntity = this.sysFileClient.viewXmByPath(extractAllImagePath);
            if (ObjectUtil.isEmpty(responseEntity)){
                continue;
            }
            try {
                InputStream inputStream = ResponseStreamUtil.convertResponseToStream(responseEntity);
                inputStreams.add(inputStream);
            } catch (ServletException e) {
                log.error("获取图片异常:{}", e);
            }
        }
        //拼接图片
        BufferedImage bufferedImage = combineImagesVertically(inputStreams);
        return Pictures.ofBufferedImage(bufferedImage, PictureType.PNG)
                .size(500, 500).create();

    }

    /**
     * 将多个图片流垂直拼接成java图片
     * @param inputStreams 图片流集合
     * @return 垂直拼接后的java图片
     */
    public BufferedImage combineImagesVertically(List<InputStream> inputStreams) {

        List<BufferedImage> images = new ArrayList<>();
        int totalHeight = 0;
        int maxWidth = 0;

        // 将 InputStream 转换为 BufferedImage，并计算总高度和最大宽度
        for (InputStream inputStream : inputStreams) {
            try {
                BufferedImage image = ImageIO.read(inputStream);
                images.add(image);
                totalHeight += image.getHeight();
                maxWidth = Math.max(maxWidth, image.getWidth());
            } catch (IOException e) {
                log.error("拼接图片异常:{}", e);
            }

        }

        // 创建一个新的 BufferedImage 对象
        BufferedImage combinedImage = new BufferedImage(maxWidth, totalHeight, BufferedImage.TYPE_INT_RGB);
        java.awt.Graphics2D g2d = combinedImage.createGraphics();
        int currentHeight = 0;

        // 依次绘制每个图imageType = 1片
        for (BufferedImage image : images) {
            g2d.drawImage(image, 0, currentHeight, null);
            currentHeight += image.getHeight();
        }
        g2d.dispose();

        for (InputStream inputStream : inputStreams) {
            try {
                inputStream.close();
            } catch (IOException e) {
                log.error("拼接图片异常:{}", e);
            }
        }
        return combinedImage;
    }

    /**
     * 提取html中的所有图片路径
     * @param html html文本
     * @return 不包含网络图片的图片路径集合
     */
    public String[] extractAllImagePaths(String html) {
        List<String> paths = new ArrayList<>();
        try {
            Document doc = Jsoup.parse(html);
            Elements images = doc.select("img");

            for (Element img : images) {
                String src = img.attr("src");
                // 新增条件：排除以 "http" 或 "https" 开头的路径（区分大小写，严格匹配小写开头）
                if (!src.isEmpty() && !src.startsWith("http")) {
                    paths.add(src);
                }
            }
        } catch (Exception e) {
            System.err.println("解析HTML时发生错误: " + e.getMessage());
        }
        return paths.toArray(new String[0]);
    }

    private boolean updateProjectTeacher(ApplyForDTO update, Long projectId) {
        //1.拷贝信息
        List<TeacherApplys> teacherApplys = BeanCopyUtils.copyBeans(update.getTeachers(), TeacherApplys.class);
        //2.删除老师报名表信息
        boolean remove = teacherApplysService.remove(new LambdaQueryWrapper<TeacherApplys>()
                .eq(TeacherApplys::getProjectId, projectId));
        if (!remove){
            throw new ServiceException("删除项目老师信息失败");
        }
        //3.添加老师报名表信息

        //3.1  获取用户id集合 key是用户id value是用户对象
        List<Long> userIds = teacherApplys.stream().map(e -> e.getUserId()).collect(Collectors.toList());
        List<User> users = userService.selectByUserIds(userIds);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(User::getUserId, e -> e));
        //3.2 遍历集合,进行添加
        teacherApplys.stream().forEach(e -> {
            e.setProjectId(projectId);
            e.setName(userMap.get(e.getUserId()).getNickName());
        });
        //3.3 添加到项目老师表
        return teacherApplysService.saveBatch(teacherApplys);
    }

    /**
     * 根据id更新项目学生表
     * @param update 项目更新信息
     * @param projectId 项目id
     */
    private boolean updateProjectStudent(ApplyForDTO update, Long projectId) {
        //1.拷贝信息
        List<StudnetApplys> studnetApplyas = BeanCopyUtils.copyBeans(update.getStudents(), StudnetApplys.class);
        //2.根据项目id进行删除学生报名信息
        boolean remove = studnetApplysService.remove(new LambdaQueryWrapper<StudnetApplys>()
                .eq(StudnetApplys::getProjectId, projectId));
        if (!remove){
            throw new ServiceException("删除项目学生信息失败");
        }
        //3.添加新的学生报名信息

        //3.1 获取用户id集合 key是用户id value是用户对象
        List<Long> userIds = studnetApplyas.stream().map(e -> e.getUserId()).collect(Collectors.toList());
        List<User> users = userService.selectByUserIds(userIds);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(User::getUserId, e -> e));
        //3.2 遍历集合,设置用户名
        studnetApplyas.forEach(e ->{
            e.setProjectId(projectId);//设置id
            e.setUserName(userMap.get(e.getUserId()).getUserName());
            e.setName(userMap.get(e.getUserId()).getNickName());
        });
        //3.3 写入数据库
        return studnetApplysService.saveBatch(studnetApplyas);
    }

    @Override
    public void addAProjectProcess(Long projectId, String msg) {
        //1.获取最后一条记录
        ProjectSchedule lastByProjectId = projectScheduleService.getLastByProjectId(projectId);
        //2.添加一条记录
        ProjectSchedule projectSchedule = new ProjectSchedule();
        projectSchedule
                .setProjectId(projectId) //项目id
                .setRootId(lastByProjectId.getProjectScheduleId())//上一条记录的id
                .setUserId(SecurityUtils.getUserId()) //用户id
                .setContent(msg);//内容
        //3.写入数据库
        boolean save = projectScheduleService.save(projectSchedule);
        if (!save){
            throw new ServiceException("添加项目过程失败");
        }
    }

    /**
     * 把数据转到实体类当中并写入数据库
     * @param applyForDTO 申请信息
     * @return 添加成功的项目id
     */
    private boolean updateProject(ApplyForDTO applyForDTO,Long id) {
        //cv固定参数
        Project project = BeanCopyUtils.copyBean(applyForDTO, Project.class);
        //转写金额参数
        project.setFiscalAppropriation(new BigDecimal(applyForDTO.getFiscalAppropriation()))
                .setSchoolAllocation(new BigDecimal(applyForDTO.getSchoolAllocation()))
                .setTotalMoney(new BigDecimal(applyForDTO.getTotalMoney()));
        //根据年度数据id获取到开始和结束时间
        YearData yearDataBaen = yearDataService.getById(applyForDTO.getYearDataId());


        //写入负责人的id,参加人员的所有的id,
        project .setProjectId(id)
                .setUserId(SecurityUtils.getUserId())//负责人的id
                .setMemberId(tudentToJsonArrray(applyForDTO.getStudents()))//参加人员的所有的 数组id
                .setTeacherId(teacherToJsonArrray(applyForDTO.getTeachers()))//指导老师的 数组id
                .setFirmTeacherId(firstTeacherToJsonArrray(applyForDTO.getTeachers()))//企业老师的 数组id
                .setBeginTime(yearDataBaen.getBegin())
                .setEndTime(yearDataBaen.getEnd())
                .setState(ProjectConstant.PROJECT_STATUS_AUDIT_VALUE);
        //1.todo 修改项目特殊设计
        project.setAuditStatus(1L);//审核指针,只要重新修改都会重新审核进度
        //导入立项依据
        boolean result = setABC(project,applyForDTO);
        if(!result){
            throw new ServiceException("项目立项依据存在问题,请检查",444);
        }
        //根据id修改
        return  this.updateById(project);
    }

    /**
     * 校验年度组是否存在和启用
     * @param yearGroupId 年度组id
     * @param yearDataId 年度数据id
     */
    private void checkYearGroup(Long yearGroupId,Long yearDataId) {
        //1.根据id查询
        YearGroup one = yearGroupService.getOne(
                new LambdaQueryWrapper<YearGroup>()
                        .eq(YearGroup::getYearGroupId, yearGroupId)
        );
        //2.判断是否存在
        if (one == null){
            throw new ServiceException("年度组不存在");
        }
        //3.判断是否启用
        if (one.getStatus().intValue() != ProjectConstant.COLLEGE_GROUP_STATUS_NORMAL){
            throw new ServiceException("当前年度组未启用,请启用再进行操作");
        }
        //4.判断年度数据是否存在
        YearData yearData = yearDataService.getOne(
                new LambdaQueryWrapper<YearData>()
                        .eq(YearData::getYearDataId, yearDataId)
        );
        if (yearData == null){
            throw new ServiceException("年度数据不存在");
        }

        //5.判断年度数据是否属于年度组
        if(!yearData.getYearGroupId().equals(yearGroupId)){
            throw new ServiceException("当前年度数据不属于当前年度组,请选择正确的年度数据");
        }
    }

    /**
     * 校验学院是否存在和启用
     * @param collegeGroupId 学院id
     */
    private void checkCollegeGroup( Long collegeGroupId) {
        //1.根据id查询
        CollegeGroup one = collegeGroupService.getById(collegeGroupId);
        //2.判断是否存在
        if (one == null){
            throw new ServiceException("学院不存在");
        }
        //3.判断是否启用
        if (one.getStatus().intValue() != ProjectConstant.COLLEGE_GROUP_STATUS_NORMAL){
            throw new ServiceException("当前学院组未启用,请启用再进行操作");
        }
    }


    /**
     *  项目查询构建条件,全角色通用,
     *  还有 userProjectListId, CollegeGroupIds, specialistGroupIds,这个三个参数只能三选一传入一个
     * @param projectSelectDto 多条件
     * @param userProjectListId 老师和学生进行使用,传入和自己相关的项目id
     * @param CollegeGroupIds 学院审核员进行使用,传入用户自己的学院id
     * @param specialistGroupIds 专家进行使用的,传入自己的专家组
     * @param isAdmin 管理员进行使用的,不需要传入 userProjectListId, CollegeGroupIds, specialistGroupIds
     * @return
     */
    private static LambdaQueryWrapper<Project> getProjectLambdaQueryWrapper(ProjectSelectDto projectSelectDto,
                                                                            List<Long> userProjectListId,
                                                                            List<Long> CollegeGroupIds,
                                                                            List<Long> specialistGroupIds,
                                                                            boolean isAdmin) {
        LambdaQueryWrapper<Project> queryWrapper = new LambdaQueryWrapper<>();

        // 判断三个集合是否三选一
        boolean hasUserProjectListId = userProjectListId != null && !userProjectListId.isEmpty();
        boolean hasCollegeGroupIds = CollegeGroupIds != null && !CollegeGroupIds.isEmpty();
        boolean hasSpecialistGroupIds = specialistGroupIds != null && !specialistGroupIds.isEmpty();

        int count = (hasUserProjectListId ? 1 : 0) + (hasCollegeGroupIds ? 1 : 0) + (hasSpecialistGroupIds ? 1 : 0);

        //1.不是管理员
        if(!isAdmin){
            if (count != 1 && count != 0) {
                throw new ServiceException("userProjectListId, CollegeGroupIds, specialistGroupIds只能三选一,请联系管理员",500);
            }
            // 根据传入的集合设置查询条件
            if (hasUserProjectListId) {
                queryWrapper.in(Project::getProjectId, userProjectListId);
            } else if (hasCollegeGroupIds) {
                queryWrapper.in(Project::getCollegeGroupId, CollegeGroupIds);
            } else if (hasSpecialistGroupIds) {
                queryWrapper.in(Project::getSpecialistGroupId, specialistGroupIds);
            }
        }else {
            if(count != 0){
                throw new ServiceException("isAdmin为true时,userProjectListId, CollegeGroupIds, specialistGroupIds不能同时传入,请联系管理员",500);
            }
        }
        //

        //4.多条件构建

        //4.1 构建查询是否分配专家组
        if(projectSelectDto.getIsAudit() != null){
            if(projectSelectDto.getIsAudit()){
                queryWrapper.isNotNull(Project::getSpecialistGroupId);
            }else {
                queryWrapper.isNull(Project::getSpecialistGroupId);
            }
        }

        //4.2提前添加查询
        queryWrapper
                .like(projectSelectDto.getProjectNumber() != null,Project::getProjectNumber,projectSelectDto.getProjectNumber())
                //项目名称 模糊
                .like(StrUtil.isNotBlank(projectSelectDto.getName()), Project::getName, projectSelectDto.getName())
                //项目级别
                .eq(projectSelectDto.getProjectRank()!=null, Project::getProjectRank, projectSelectDto.getProjectRank())
                //项目状态
                .in(projectSelectDto.getStates()!=null && !projectSelectDto.getStates().isEmpty(), Project::getState, projectSelectDto.getStates())
                //学科类别
                .eq(projectSelectDto.getSubjectCategory()!=null, Project::getSubjectCategory, projectSelectDto.getSubjectCategory())
                //项目类型
                .eq(projectSelectDto.getType() != null, Project::getType, projectSelectDto.getType())
                //负责人的id
                .eq(projectSelectDto.getUserId() != null, Project::getUserId, projectSelectDto.getUserId())
                // 年度组 ID
                .eq(projectSelectDto.getYearGroupId() != null, Project::getYearGroupId, projectSelectDto.getYearGroupId())
                // todo 年度数据id
                .eq(projectSelectDto.getYearDataId() != null, Project::getYearDataId, projectSelectDto.getYearDataId())
                // 指导老师和企业老师 JSON 包含查询
                .and(projectSelectDto.getTeacherId() != null,wrapper -> wrapper
                        .apply("JSON_CONTAINS(teacher_id, JSON_ARRAY({0}))", projectSelectDto.getTeacherId())
                        .or()
                        .apply("JSON_CONTAINS(firm_teacher_id, JSON_ARRAY({0}))", projectSelectDto.getTeacherId()))
                .and(projectSelectDto.getMemberId() != null,wrapper -> wrapper
                        .apply("JSON_CONTAINS(member_id, JSON_ARRAY({0}))", projectSelectDto.getMemberId()));
        return queryWrapper;
    }

    /**
     * 设置项目列表(vo)的专家组名称
     * @param specialistIds 专家组id
     * @param projectListvo 项目列表(vo)
     */
    private void projectListVoSetSpecialistGroupName(ArrayList<Long> specialistIds, List<ProjectListvo> projectListvo) {
        //1. 根据专家组id获取专家组集合
        List<SpecialistGroup> specialistGroupList = specialistGroupService
                .selectByIds(specialistIds
                        .stream()
                        .distinct()
                        .collect(Collectors.toList()));
        //2. 给每个vo进行匹配一个专家组名称
        projectListvo.stream()
                .forEach(projecVo->{
                    // 2. 根据专家组id匹配,只需要一个即可
                    specialistGroupList.stream()
                            .filter(specialistGroup -> specialistGroup.getSpecialistGroupId().equals(projecVo.getSpecialistGroupId()))
                            .limit(1)
                            .forEach(specialistGroup -> {
                                projecVo.setSpecialistGroupName(specialistGroup.getName());
                            });
                });
    }

    /**
     * 设置项目列表(vo)的昵称
     * @param userIds 用户id
     * @param projectListvo 项目列表(vo)
     */
    private void projectListVoSetNickName(ArrayList<Long> userIds, List<ProjectListvo> projectListvo) {
        //1. 根据用户id获取用户集合
        List<User> users = userService
                .selectByUserIds(userIds
                        .stream()
                        .distinct()
                        .collect(Collectors.toList()));
       //2. 给每个vo进行匹配一个昵称
        projectListvo.stream()
                .forEach(projectVo->{
                    //根据用户id进行匹配,只需要一个即可
                    users.stream()
                            .filter(user -> user.getUserId().equals(projectVo.getUserId()))
                            .limit(1)
                            .forEach(user -> {
                                projectVo.setNickName(user.getNickName());
                            });

                });
    }


    /**
     * 校验当前用户是否有权限查看该项目
     * @param id 项目id
     * @param type 查询类型,1:学生 , 2:教师, 3:学院审核人, 4:专家, 5:管理员
     * @return true:有权限 false:无权限
     */
    private boolean checkUserRole(Long id, String type) {
        //1.判断当前用户是否有当前当前角色
        if(type.equals(ProjectConstant.ROLE_STUDENT)){ //学生
            if(!roleService.hasRole(ProjectConstant.ROLE_STUDENT)){
                return false;
            }
        }else if(type.equals(ProjectConstant.ROLE_TEACHER)){ //教师
            if(!roleService.hasRole(ProjectConstant.ROLE_TEACHER)){
                return false;
            }
        }else if(type.equals(ProjectConstant.ROLE_COLLEGE)){  //学院审核人
            if(!roleService.hasRole(ProjectConstant.ROLE_COLLEGE)){
                return false;
            }
        }else if(type.equals(ProjectConstant.ROLE_SPECIALIST)) {//专家
            if (!roleService.hasRole(RoleConstant.EXPERT)){
                return false;
            }
        }else if(type.equals(ProjectConstant.ROLE_ADMIN)){  //管理员
            if (!roleService.hasRole(RoleConstant.ADMIN)){
                return false;
            }
        }
        //2.根据不同角色判断是否有权限查看该项目
        if(ProjectConstant.ROLE_STUDENT.equals(type)) { //学生 1.判断项目是不是自己的
            return isProjectOwnedByStudent(id);
        }else if(ProjectConstant.ROLE_TEACHER.equals(type)) { //教师 2.判断项目是不是自己的学生的
            return isProjectOfTeacherStudents(id);
        }else if(ProjectConstant.ROLE_COLLEGE.equals(type)) { //学院审核人 3.判断这个项目是不是自己学院的
            return isProjectInSameCollege(id);
        }else if(ProjectConstant.ROLE_SPECIALIST.equals(type)) { //专家 4.判断这个项目可不可以审核
            return canExpertReviewProject(id);
        }else if(ProjectConstant.ROLE_ADMIN.equals(type)) { //管理员 直接返回
            return true;
        }
        return false;
    }


    /**
     * 获取学生报名表，根据学号或者姓名
     * @param text 学号/姓名
     * @return
     */
    private List<StudnetApplys> getStudentApplyListByNameOrId(String text) {
        try{
            LambdaQueryWrapper<StudnetApplys> queueWrapper = Wrappers.<StudnetApplys>lambdaQuery()
                    .eq(StudnetApplys::getUserId,text)// eq可以对比整数类型和字符串。不需要转类型了
                    .or()
                    .eq(StudnetApplys::getName,text); //姓名
            return studnetApplysMapper.selectList(queueWrapper);
        }catch (Exception e){
            throw new ServiceException("在查询用户名的过程中失败",502);
        }

    }

    /**
     * 获取教师报名表，根据工号或者姓名
     * @param text 工号/姓名
     * @return
     */
    private List<TeacherApplys> getTeacherApplyListByNameOrId(String text) {
        try{
            LambdaQueryWrapper<TeacherApplys> queueWrapper = Wrappers.<TeacherApplys>lambdaQuery()
                    .eq(TeacherApplys::getUserId,text)// eq可以对比整数类型和字符串。不需要转类型了
                    .or()
                    .eq(TeacherApplys::getName,text); //姓名
            return teacherApplysMapper.selectList(queueWrapper);
        }catch (Exception e){
            throw new ServiceException("在查询用户名的过程中失败",502);
        }

    }

    /**
     * 删除项目(逻辑删除)
     * @param projectId
     * @return
     */
    private boolean deleteProjectById(Long projectId) {
        LambdaUpdateWrapper<Project> updateWrapper = Wrappers.<Project>lambdaUpdate()
                //.eq(Project::getDelFlag, 0) // 未删除 字段为 0
                .eq(Project::getProjectId,projectId) //对应数据
                .set(Project::getDelFlag,1);//标记删除

        boolean n = update(updateWrapper);//更新数据

        if (n){
            return true;
        }else {
            throw new ServiceException("删除失败！数据不存在或已被删除",403);
        }

    }

    private boolean isAuditFailed(Long projectId){
//        try{
            LambdaQueryWrapper<Project> queueWrapper = Wrappers.<Project>lambdaQuery()
                    .eq(Project::getDelFlag, 0) // 未删除 字段为 0
                    .eq(Project::getProjectId,projectId); //目标项目
            // todo 先暂时直接判断是否为5（未通过） 还没加上常量
            if(getOne(queueWrapper).getAuditStatus()== 5){
                return true;
            }else {
                throw new ServiceException("项目未处于审核未通过状态，无法删除！",403);
            }
//        }catch (Exception e){
//            throw new ServiceException("在查询项目是否通过的时候发生错误！",502);
//        }
    }


    /**
     * 项目是否存在
     *
     * @param projectId
     */
    private void ProjectIsNull(Long projectId) {

        LambdaQueryWrapper<Project> queueWrapper = Wrappers.<Project>lambdaQuery()
                .eq(Project::getDelFlag, 0) // 未删除 字段为 0
                .eq(Project::getProjectId,projectId); //目标项目
        if(count(queueWrapper)>0){
        }else {
            throw new ServiceException("该项目不存在！或已被删除",404);
        }
    }

    /**
     * 检查用户是否为项目负责人
     * @param projectId
     * @param userId
     * @return
     */
    private boolean isProjectAdmin(Long projectId,Long userId) {
        LambdaQueryWrapper<Project> queueWrapper = Wrappers.<Project>lambdaQuery()
                .eq(Project::getDelFlag, 0) // 未删除 字段为 0
                .eq(Project::getProjectId,projectId); //目标项目
        if (getOne(queueWrapper).getUserId() == userId) {
            //项目负责人
            return true;
        }else {
            throw new ServiceException("您当前不是项目负责人无权删除该项目",403);
        }

    }


    /**
     * 根据剩余5个情况,进行操作,并返回结果,参数项目bean,审核备注,(根据项目进度状态来判断审核进度)
     * @param projectAuditDto 审核信息
     * @param project 项目实体类
     * @return 写入成功返回true,写入失败返回false
     */
    private boolean projectAlreadyAuditz(ProjectAuditDto projectAuditDto, Project project,Long roleId) {
        //获取项目当前最大审核顺序值
        int max = xmAuditConfig.getProjectAuditTypes().get(project.getType()).size();//需要的最大审核顺序值
        int a= project.getAuditStatus().intValue();//项目当前审核状态值
        List<AuditOpinion> auditOpinions = auditOpinionService.selectByPropertieID(project.getProjectId());//获取项目的审核记录(有可能为空)
        int b = auditOpinions.size();//审核记录条数

        //看懂方法,列出全部数学逻辑
        if(a<1){
            throw new ServiceException("项目当前状态不允许审核",403);
        }

        if(max <=1){
            throw new ServiceException("项目当前状态不允许审核",403);
        }
        //指针存在4个位置,判断当前项目的审核进度,并进行操作
        //a==1时
        if(a==1){
            if(a == max){
                throw  new ServiceException("审核流程要来两个2以上才可以,请联系管理员配置新的审核流程",403);
            }
            //新增操作
            if(b == 0){
                initAuditOpinion(projectAuditDto,project,roleId);
                return true;
            }
            //覆盖操作
            else if(b > 0){//覆盖情况2,3,4
                return coverByAuditStatusVlaue(projectAuditDto, project, roleId);
            }
        }
        //1<a<max时
        else if(a<max){
            if(b <= 0 ){
                throw new ServiceException("当前审核进度为:"+a+" 但是审核数量为:"+b+"属于违法状态操作,请联系管理员",403);
            }
            //新增
            if(1<=b && b<a){
                return addAuditOpinion(projectAuditDto,project,roleId);
            }
            //覆盖 1<a<b<=max 或者 1<a<max<b
            if(a<=b){
                return coverByAuditStatusVlaue(projectAuditDto, project, roleId);
            }
        }
        //a==max时
        else if(a==max){
            //新增操作,完结操作
            if(b<max){
                return addAuditOpinionAnd(projectAuditDto,project,roleId,a,b);
            }
            //覆盖操作
            if(b>=max){
                return addCoverByAuditStatusVlaue(projectAuditDto, project, roleId,b,max);
            }
        }
        //a>max时
        else if (a>max){
            //覆盖全部
            return addCoverByAuditStatusMaxVlaue(projectAuditDto, project, roleId,b,max);
        }
        return false;
        /*
        //6.情况六(特别特殊)
        if(a==max) { //可能出现 b>max && b>a情况或者是 b<max && b<a情况,都是后一个,但是a==max是不会变的
            handleLastAudit(projectAuditDto, project, roleId);
            return true;
        }

        //1.情况一,a>max,b>=max,当前项目已经审核完毕,不需要再次审核,直接抛出异常
        if(a>max){//只要超出,需要管理员
            handleOverAudit(projectAuditDto, project);
            return true;
        }
        //2.情况三,
        else if( a<max && a<=b && b<= max){
            handleReAudit(projectAuditDto, project, roleId);
            return true;
        }
        //4.情况四,
        else if(a<max && a>b && b>max){
            handleNormalAudit(projectAuditDto, project, roleId);
            return true;
        }
        //5.情况五,
        else if(a<max && a>b && b == 0){
            handleFirstAudit(projectAuditDto, project, roleId);
            return true;

        }else {//未知情况,会进行记录

        }
        return false;*/
    }

    /**
     * 项目审核不通过,项目进度表进行记录,如果是项目审核通过,不要调用这个方法,只有a>=max才会进行记录到项目进度表当中
     * @param project 项目实体类
     *
     */
    private void handleProjectSchedule( Project project) {
        //获取最后一个项目进度记录
        ProjectSchedule last = projectScheduleService.getLastByProjectId(project.getProjectId());
        //项目进度表进行记录
        ProjectSchedule projectSchedule = new ProjectSchedule()
                .setProjectId(project.getProjectId())
                .setRootId(last.getProjectScheduleId())
                .setUserId(SecurityUtils.getUserId())
                .setContent(projectScheduleConfig.getNoPassTheAudit());
        if(!projectScheduleService.save(projectSchedule)){
                throw new ServiceException("项目进度表插入失败,请检查",500);
        }
    }

    /**
     * 指针大于等于max,并且b>max,说明已经审核完毕,这里不需要记录审核表信息到审核表,会覆盖到已经审核通过的数据,<br>
     * 审核表的信息回记录到项目进度表<br>
     * 超出范围特殊,只有审核不通过才会进行记录到审核表当中,如果通过了,直接记录到项目进度表
     * 需要管理员审核,直接抛出异常
     * @param projectAuditDto 审核信息
     * @param project 项目实体类
     * @param roleId 项目当前需要的审核角色的id(当前用户id)
     * @param b 审核记录条数
     * @param max 最大审核顺序值
     * @return 写入成功返回true,写入失败返回false
     */
    private boolean addCoverByAuditStatusMaxVlaue(ProjectAuditDto projectAuditDto, Project project, Long roleId, int b, int max) {
        //1.b>max是固定不变的
        if(b<max){
            log.error("当前项目审核记录数量:"+b+"和当前项目审核进度:"+max+"不匹配,请联系管理员");
            throw new ServiceException("当前项目审核记录数量:"+b+"和当前项目审核进度:"+max+"不匹配,请联系管理员",500);
        }
        //获取max存储位置的审核记录
        AuditOpinion nodeByIndex = auditOpinionService.getNodeByIndex(project.getProjectId(), Long.valueOf(max));
        //如果审核通过,这里审核表的数据不会覆盖,会存储到项目进度表
        handleNormalAuditcopyBean(projectAuditDto, nodeByIndex, roleId, SecurityUtils.getUserId());//拷贝审核记录信息
        if(projectAuditDto.getAuditState() != 0){//不通就会记录
            //审核表
            if(!auditOpinionService.updateById(nodeByIndex)){
                throw new ServiceException("审核表修改失败,请检查",500);
            }
        }
        //4.项目表修改审核状态为审核通过,项目进度表修改值为0
        project
                .setAuditStatus(projectAuditDto.getAuditState()==0?//通过审核进度条+1,不通过直接设置为1
                        ProjectConstant.PROJECT_STATUS_PROGRESS: //最后一个审核通过,进度条置为0
                        ProjectConstant.PROJECT_STATUS_PROGRESS_INIT)//1代表从头开始审核
                .setState(projectAuditDto.getAuditState()==0? //审核通过设置为状态值不变,审核不通过设置为0
                        ProjectConstant.PROJECT_STATUS_IN_PROGRESS_VALUE://变为项目进行中
                        ProjectConstant.PROJECT_STATUS_NOT_PASS_VALUE);//0代表审核不通过
        //5.修改项目表
        if(!this.updateById(project)){
            throw new ServiceException("项目表状态修改失败,请检查",500);
        }
        //6.项目进度表插入一条记录,记录状态
        ProjectSchedule last = projectScheduleService.getLastByProjectId(project.getProjectId());
        ProjectSchedule projectSchedule = new ProjectSchedule()
                .setProjectId(project.getProjectId())
                .setRootId(last.getProjectScheduleId())
                .setUserId(SecurityUtils.getUserId())
                .setContent(projectAuditDto.getAuditState() == 0 ? //审核通过记录项目进度
                        projectScheduleConfig.getAuditSpecialOne()+"\n审核信息:"+projectAuditDto.getAuditOpinion()+"\n审核人的工号:"+SecurityUtils.getUsername()://这里指针指针>max,所以是特殊记录,通过记录信息
                        projectScheduleConfig.getAuditSpecialTwo()+"\n审核信息:"+projectAuditDto.getAuditOpinion()+"\n审核人的工号:"+SecurityUtils.getUsername());//这里指针指针>max,所以是特殊记录,审核不通过记录信息
        if(!projectScheduleService.save(projectSchedule)){
            throw new ServiceException("项目进度表插入失败,请检查",500);
        }
        return true;
    }

    /**
     * 最后一个审核的覆盖操作<br>
     * 1:新增审核记录,<br>
     * 2:修改项目表状态为审核通过,项目进度表状态为完成修改值为0<br>
     * 3:项目进度表记录项目完成时间,如果没有通过审核,不需要进度表记录
     * @param projectAuditDto
     * @param project
     * @param roleId
     * @return 成功返回true,失败抛出异常
     */
    private boolean addCoverByAuditStatusVlaue(ProjectAuditDto projectAuditDto, Project project, Long roleId,int b,int max) {
        //获取指针指向的审核记录
        AuditOpinion nodeByIndex = auditOpinionService.getNodeByIndex(project.getProjectId(), project.getAuditStatus());
        //覆盖原本的审核记录
        handleNormalAuditcopyBean(projectAuditDto, nodeByIndex, roleId, SecurityUtils.getUserId());
        //更新审核表
        if(!auditOpinionService.updateById(nodeByIndex)){
            throw new ServiceException("审核表修改失败,请检查",500);
        }
        //4.项目表修改审核状态为审核通过,项目进度表修改值为0
        project
                .setAuditStatus(projectAuditDto.getAuditState()==0?//通过审核进度条+1,不通过直接设置为1
                        ProjectConstant.PROJECT_STATUS_PROGRESS: //最后一个审核通过,进度条置为0
                        ProjectConstant.PROJECT_STATUS_PROGRESS_INIT)//1代表从头开始审核
                .setState(projectAuditDto.getAuditState()==0? //审核通过设置为状态值不变,审核不通过设置为0
                        ProjectConstant.PROJECT_STATUS_IN_PROGRESS_VALUE://变为项目进行中
                        ProjectConstant.PROJECT_STATUS_NOT_PASS_VALUE);//0代表审核不通过
        //5.修改项目表
        if(!this.updateById(project)){
            throw new ServiceException("项目表状态修改失败,请检查",500);
        }
        //6.项目进度表插入一条记录,记录状态
        ProjectSchedule last = projectScheduleService.getLastByProjectId(project.getProjectId());
        ProjectSchedule projectSchedule = new ProjectSchedule()
                .setProjectId(project.getProjectId())
                .setRootId(last.getProjectScheduleId())
                .setUserId(SecurityUtils.getUserId())
                .setContent(projectAuditDto.getAuditState() == 0 ? //审核通过记录项目进度
                        (b>max ? projectScheduleConfig.getAuditSpecialOne():projectScheduleConfig.getPassTheAudit()) ://审核通过记录信息,b>max记录信息比较特殊
                        (b>max? projectScheduleConfig.getAuditSpecialTwo() :projectScheduleConfig.getNoPassTheAudit()));//审核不通过记录信息
        if(!projectScheduleService.save(projectSchedule)){
            throw new ServiceException("项目进度表插入失败,请检查",500);
        }
        return true;
    }

    /**
     * 指针指向最后一个审核角色,并且审核节点小于指针值<br>
     * 1:新增审核记录,<br>
     * 2:修改项目表状态为审核通过,项目进度表状态为完成修改值为0<br>
     * 3:项目进度表记录项目完成时间
     * @param projectAuditDto 审核信息
     * @param project 项目实体类
     * @param roleId 项目当前需要的审核角色的id(当前用户id)
     * @return 成功返回true,失败抛出异常
     */
    private boolean  addAuditOpinionAnd(ProjectAuditDto projectAuditDto, Project project, Long roleId,int a, int b) {
        //1.判断b=(a-1)才是合法操作,否则抛出异常
        if(b != (a-1)){
            log.error("当前项目审核记录数量:"+b+"和当前项目审核进度:"+a+"不匹配,请联系管理员");
            throw new ServiceException("当前项目审核记录数量:"+b+"和当前项目审核进度:"+a+"不匹配,请联系管理员",500);
        }
        //2.获取最后一条审核记录
        AuditOpinion lastByProjectId = auditOpinionService.getLastByProjectId(project.getProjectId());
        AuditOpinion auditOpinion = BeanCopyUtils.copyBean(projectAuditDto, AuditOpinion.class);
        auditOpinion.setUserId(SecurityUtils.getUserId())
                .setRoleId(roleId)
                .setRootId(lastByProjectId.getAuditOpinionId());
        //3.新增审核记录
        if(!auditOpinionService.save(auditOpinion)){
            throw new ServiceException("审核表新增失败,请检查",500);
        }
        //4.项目表修改审核状态为审核通过,项目进度表修改值为0
        project
                .setAuditStatus(projectAuditDto.getAuditState()==0?//通过审核进度条+1,不通过直接设置为1
                        ProjectConstant.PROJECT_STATUS_PROGRESS: //最后一个审核通过,进度条置为0
                        ProjectConstant.PROJECT_STATUS_PROGRESS_INIT)//1代表从头开始审核
                .setState(projectAuditDto.getAuditState()==0? //审核通过设置为状态值不变,审核不通过设置为0
                        ProjectConstant.PROJECT_STATUS_IN_PROGRESS_VALUE://变为项目进行中
                        ProjectConstant.PROJECT_STATUS_NOT_PASS_VALUE);//0代表审核不通过
        //5.修改项目表
        if(!this.updateById(project)){
            throw new ServiceException("项目表状态修改失败,请检查",500);
        }
        //6.项目进度表插入一条记录,记录状态
        ProjectSchedule last = projectScheduleService.getLastByProjectId(project.getProjectId());
        ProjectSchedule projectSchedule = new ProjectSchedule()
                .setProjectId(project.getProjectId())
                .setRootId(last.getProjectScheduleId())
                .setUserId(SecurityUtils.getUserId())
                .setContent(projectAuditDto.getAuditState() == 0 ? //审核通过记录项目进度
                        projectScheduleConfig.getPassTheAudit() ://审核通过记录信息
                        projectScheduleConfig.getNoPassTheAudit());//审核不通过记录信息
        if(!projectScheduleService.save(projectSchedule)){
            throw new ServiceException("项目进度表插入失败,请检查",500);
        }
        return true;
    }

    /**
     * 给当前项目新增审核记录,并修改项目表和审核表的状态值
     * @param projectAuditDto 审核信息
     * @param project 项目实体类
     * @param roleId 项目当前需要的审核角色的id(当前用户id)
     * @return
     */
    public boolean addAuditOpinion(ProjectAuditDto projectAuditDto, Project project, Long roleId) {
        //1.判断是否存在审核表,没有审核表抛出异常
        if(auditOpinionService.isEmptyProjectAudit(project.getProjectId())){
            log.error("当前项目没有审核记录\n"+project.toString()+"\n"+projectAuditDto.toString()+"\n"+roleId);
            throw new ServiceException("当前项目没有审核记录\n"+project.toString()+"\n"+projectAuditDto.toString(),500);
        }
        //2.获取最后一条审核记录
        AuditOpinion lastByProjectId = auditOpinionService.getLastByProjectId(project.getProjectId());
        AuditOpinion auditOpinion = BeanCopyUtils.copyBean(projectAuditDto, AuditOpinion.class);
        auditOpinion.setUserId(SecurityUtils.getUserId())
               .setRoleId(roleId)
                .setRootId(lastByProjectId.getAuditOpinionId());
        //3.修改审核表
        if(!auditOpinionService.save(auditOpinion)){
            throw new ServiceException("审核表新增失败,请检查",500);
        }
        //5.项目表修改审核状态
        project
                .setAuditStatus(projectAuditDto.getAuditState()==0?//通过审核进度条+1,不通过直接设置为1
                        project.getAuditStatus()+1: //通过审核进度条+1
                        ProjectConstant.PROJECT_STATUS_PROGRESS_INIT)//1代表从头开始审核
                .setState(projectAuditDto.getAuditState()==0? //审核通过设置为状态值不变,审核不通过设置为0
                        project.getState()://不变
                        ProjectConstant.PROJECT_STATUS_NOT_PASS_VALUE);//0代表审核不通过
        if(!updateById(project)){
            throw new ServiceException("项目表状态修改失败,请检查",500);
        }

        //6.审核不通过,需要记录到项目进度表
        if(projectAuditDto.getAuditState() != 0){
            handleProjectSchedule(project);
        }
        return true;
    }

    /**
     * 根据项目进度指针,覆盖指定的审核记录,并修改项目表和审核表的状态值
     * @param projectAuditDto 审核信息
     * @param project 项目实体类
     * @param roleId 项目当前需要的审核角色的id
     * @return 成功覆盖指针指定节点返回true,失败抛出异常
     */
    public boolean coverByAuditStatusVlaue(ProjectAuditDto projectAuditDto, Project project, Long roleId){
        //1.判断是否存在审核表,没有审核表抛出异常
        if(auditOpinionService.isEmptyProjectAudit(project.getProjectId())){
            log.error("当前项目没有审核记录\n"+project.toString()+"\n"+projectAuditDto.toString()+"\n"+roleId);
            throw new ServiceException("当前项目没有审核记录\n"+project.toString()+"\n"+projectAuditDto.toString(),500);
        }
        //2.根据指针值,获取审核表对应的节点
        AuditOpinion nodeByIndex = auditOpinionService.getNodeByIndex(project.getProjectId(), project.getAuditStatus());
        //3审核信息拷贝
        handleNormalAuditcopyBean(projectAuditDto, nodeByIndex, roleId, SecurityUtils.getUserId());
        //4,修改审核表
        if(!auditOpinionService.updateById(nodeByIndex)){
            throw new ServiceException("审核表修改失败,请检查",500);
        }
        //5.项目表修改审核状态
        project
                .setAuditStatus(projectAuditDto.getAuditState()==0?//通过审核进度条+1,不通过直接设置为1
                        project.getAuditStatus()+1: //通过审核进度条+1
                        ProjectConstant.PROJECT_STATUS_PROGRESS_INIT)//1代表从头开始审核
                .setState(projectAuditDto.getAuditState()==0? //审核通过设置为状态值不变,审核不通过设置为0
                        project.getState()://不变
                        ProjectConstant.PROJECT_STATUS_NOT_PASS_VALUE);//0代表审核不通过
        if(!this.updateById(project)){
            throw new ServiceException("项目表状态修改失败,请检查",500);
        }
        //6.审核不通过,需要记录到项目进度表
        if(projectAuditDto.getAuditState() != 0){
            handleProjectSchedule(project);
        }
        return true;
    }

    /**
     * 审核信息拷贝到审核表,并修改审核表的角色id和用户id
     * @param source 审核信息
     * @param target 审核表节点
     * @param roleId 角色id
     * @param userId 用户id
     */
    private void handleNormalAuditcopyBean(ProjectAuditDto source, AuditOpinion target, Long roleId, Long userId){
        BeanCopyUtils.copyBean(source, target);
        target.setUserId(userId)
                .setRoleId(roleId);
    }

    /**
     *  初始化审核,项目表,审核表,不会出现初始化就有只有一次审核情况
     * @param projectAuditDto 审核信息
     * @param project 项目实体类
     * @param roleId 项目当前需要的审核角色的id(当前用户id)
     */
    public void initAuditOpinion(ProjectAuditDto projectAuditDto, Project project, Long roleId) {
        //1.初始审核表,判断
        if(!auditOpinionService.isEmptyProjectAudit(project.getProjectId())){
            throw new ServiceException("当前项目已经有审核记录,请不要重复提交",500);
        }

        AuditOpinion auditOpinion = BeanCopyUtils.copyBean(projectAuditDto, AuditOpinion.class);
        auditOpinion.setUserId(SecurityUtils.getUserId())
                .setRoleId(roleId)
                .setRootId(-1L);
        if(!auditOpinionService.save(auditOpinion)){
            throw new ServiceException("审核表插入失败,请检查",500);
        }
        //2.项目表
        project
                //指针值
                .setAuditStatus(projectAuditDto.getAuditState()==0?//通过审核进度条+1,不通过直接设置为1)
                        project.getAuditStatus()+1: //通过审核进度条+1
                        ProjectConstant.PROJECT_STATUS_PROGRESS_INIT)//1代表从头开始审核
                //项目状态值
                .setState(projectAuditDto.getAuditState()==0? //审核通过设置为状态值不变,审核不通过设置为0
                        project.getState()://不变
                        ProjectConstant.PROJECT_STATUS_NOT_PASS_VALUE);//0代表审核不通过
        if(!updateById(project)){
            throw new ServiceException("项目表状态修改失败,请检查",500);
        }
        //6.审核不通过,需要记录到项目进度表
        if(projectAuditDto.getAuditState() != 0){
            handleProjectSchedule(project);
        }
    }



    /**
     *
     *  处理情况一,审核进度操作,配置信息审核进度,<br>
     * 两张表,1.项目表状态和 2.项目进度表的值,项目进度表添加这条记录(特殊标记:审核意见会添加到项目进度表里面,不管是通过还不通过)<br>
     * 这个情况只有管理员状态才可以进行操作的,前面需要判断是不是管理员的角色<br>
     *
     * @param projectAuditDto  审核信息
     * @param project 项目实体类
     */
    private void handleOverAudit(ProjectAuditDto projectAuditDto, Project project) {
        //双重判断是不是管理员
        if(!isHaveAdmin()){
            throw new ServiceException("当前用户没有权限审核当前项目,请联系管理员",403);
        }
        //1.项目进度表最后一个节点
        ProjectSchedule lastByProjectId = projectScheduleService.getLastByProjectId(project.getProjectId());
        ProjectSchedule projectSchedule = new ProjectSchedule()
                .setProjectId(project.getProjectId()) //项目id
                .setRootId(lastByProjectId.getProjectScheduleId()) //上一级进度id项目进度id
                .setUserId(SecurityUtils.getUserId()) //当前用户id
                .setContent(projectAuditDto.getAuditState()== 0? //审核通过和不通过存储不同信息(0表示通过,1表示不通过)
                        projectScheduleConfig.getAuditSpecialOne() +"\n 审核通过信息记录:"+projectAuditDto.getAuditOpinion()+" \n 审核用户的id"+SecurityUtils.getUserId() :
                        projectScheduleConfig.getAuditSpecialTwo() +"\n 审核不通过信息记录:"+projectAuditDto.getAuditOpinion()+" \n 审核用户的id"+SecurityUtils.getUserId()); //审核意见

        //2.通过和 项目表(更新) ,项目进度表(插入),
        if(projectScheduleService.save(projectSchedule)){
            throw new ServiceException("项目进度表插入失败,请检查",500);
        }
        project
                .setState(projectAuditDto.getAuditState()==0? //项目状态(最后的审核),0为审核通过,1为审核不通过
                        ProjectConstant.PROJECT_STATUS_IN_PROGRESS_VALUE: //2代表项目进行中
                        ProjectConstant.PROJECT_STATUS_NOT_PASS_VALUE) //0代表审核不通过
                .setAuditStatus(projectAuditDto.getAuditState()==0? //项目进度值(最后的审核)0为审核通过,1为审核不通过
                                ProjectConstant.PROJECT_STATUS_PROGRESS://0代表审核结束
                                ProjectConstant.PROJECT_STATUS_PROGRESS_INIT); //1代表从头开始审核
        if(updateById(project)){
            throw new ServiceException("项目表状态修改失败,请检查",500);
        }
    }

    /**
     * 处理情况三,重新审核操作,配置信息审核进度,会覆盖原本的记录<br>
     * 操作两种表 项目表 和 审核表<br>
     * @param projectAuditDto 项目审核信息
     * @param project 项目实体类
     * @param roleId 项目当前需要的审核角色的id(当前用户id)
     */
    private void handleReAudit(ProjectAuditDto projectAuditDto, Project project,Long roleId) {
        //1.获取用户的id
        Long userId = SecurityUtils.getUserId();

        //2.获取项目当前的审核进度
        Long auditStatus = project.getAuditStatus();
        //3.根据审核进度值作为索引,获取审核表的节点
        AuditOpinion nodeByIndex = auditOpinionService.getNodeByIndex(project.getProjectId(), auditStatus);
        //4.覆盖原本的记录
        nodeByIndex
                .setAuditState(projectAuditDto.getAuditState())//审核状态
                .setRoleId(roleId)//角色id
                .setAuditOpinion(projectAuditDto.getAuditOpinion());//审核意见
        //5.修改审核表
        if(auditOpinionService.updateById(nodeByIndex)){
            throw new ServiceException("审核表修改失败,请检查",500);
        }
        //5.项目表修改审核状态
        project
                .setAuditStatus(projectAuditDto.getAuditState()==0?//通过审核进度条+1,不通过直接设置为1
                        auditStatus+1: //通过审核进度条+1
                        ProjectConstant.PROJECT_STATUS_PROGRESS_INIT)//1代表从头开始审核
                .setState(projectAuditDto.getAuditState()==0? //审核通过设置为状态值不变,审核不通过设置为0
                        project.getState()://不变
                        ProjectConstant.PROJECT_STATUS_NOT_PASS_VALUE);//0代表审核不通过
        if(updateById(project)){
            throw new ServiceException("项目表状态修改失败,请检查",500);
        }
    }



    /**
     * 情况四,正常审核操作(为完成审核),配置信息审核进度,会新增一条记录<br>
     * 项目表和审核表
     * @param projectAuditDto 项目审核信息
     * @param project 项目实体类
     * @param roleId 项目当前需要的审核角色的id(当前用户id)
     */
    private void handleNormalAudit(ProjectAuditDto projectAuditDto, Project project,Long roleId) {
        //1.获取用户的id
        Long userId = SecurityUtils.getUserId();
        //2.获取项目当前的审核进度
        Long auditStatus = project.getAuditStatus();
        //3.获取审核表最后一条记录
        AuditOpinion lastByProjectId = auditOpinionService.getLastByProjectId(project.getProjectId());
        //5.封装审核表数据
        AuditOpinion auditOpinion = new AuditOpinion()
                .setProjectId(project.getProjectId())//项目id
                .setUserId(userId) //当前用户id
                .setRoleId(roleId)//角色id
                .setRootId(lastByProjectId.getAuditOpinionId())//上一级审核id
                .setAuditState(projectAuditDto.getAuditState())//审核状态
                .setAuditOpinion(projectAuditDto.getAuditOpinion());//审核意见
        if(auditOpinionService.save(auditOpinion)){
            throw new ServiceException("审核表插入失败,请检查",500);
        }
        //6.根据是否审核通过,修改项目表
        project
                .setState(projectAuditDto.getAuditState()==0? //审核通过设置为状态值不变,审核不通过设置为0
                        project.getState()://不变
                        ProjectConstant.PROJECT_STATUS_NOT_PASS_VALUE)//0代表审核不通过
                .setAuditStatus(projectAuditDto.getAuditState()==0? //审核通过 进度值+1,不通过直接设置为1
                        auditStatus+1://通过审核进度条+1
                        ProjectConstant.PROJECT_STATUS_PROGRESS_INIT);//1代表从头开始审核
        if(updateById(project)){
            throw new ServiceException("项目表状态修改失败,请检查",500);
        }

    }

    /**
     * 情况五,第一条审核记录,配置信息审核进度,会新增一条记录<br>
     * 项目表(更新)和审核表(插入)<br>
     * @param projectAuditDto 项目审核信息
     * @param project 项目实体类
     * @param roleId 项目当前需要的审核角色的id(当前用户id)
     */
    private void handleFirstAudit(ProjectAuditDto projectAuditDto, Project project,Long roleId){
        //1.获取用户的id
        Long userId = SecurityUtils.getUserId();
        //2.创建审核表数据,给审核表初始化(如果意见初始化过抛出异常)
        if(auditOpinionService.isProjectAudit(project.getProjectId())){
            throw new ServiceException("当前项目有审核记录,当下项目是进入第一次审核项目的情况,又存在审核记录,请联系管理员",500);
        }
        AuditOpinion auditOpinion = new AuditOpinion()
                .setProjectId(project.getProjectId())//项目id
                .setUserId(userId) //当前用户id
                .setRoleId(roleId)//角色id
                .setAuditState(projectAuditDto.getAuditState())//审核状态
                .setAuditOpinion(projectAuditDto.getAuditOpinion());//审核意见
        //3.插入审核表
        if(auditOpinionService.save(auditOpinion)){
            throw new ServiceException("审核表插入失败,请检查",500);
        }
        //4.更新项目表
        project
                .setAuditStatus(projectAuditDto.getAuditState()==0?//通过审核进度条+1,不通过直接设置为1
                project.getAuditStatus()+1://进度值+1
                ProjectConstant.PROJECT_STATUS_PROGRESS_INIT)//1代表从头开始审核
                .setState(projectAuditDto.getAuditState()==0? //审核通过设置为状态值不变,审核不通过设置为0
                        project.getState()://不变
                        ProjectConstant.PROJECT_STATUS_NOT_PASS_VALUE);//0代表审核不通过
        if(updateById(project)){
            throw new ServiceException("项目表状态修改失败,请检查",500);
        }
    }

    /**
     * 情况六,最后一个项目审核,配置信息审核进度,会修改项目表,审核表,项目进度表
     * @param projectAuditDto
     * @param project
     */
    private void handleLastAudit(ProjectAuditDto projectAuditDto, Project project,Long roleId) {
        //1.获取用户的id

        //2.校验审核
    }

    /**
     * 判断当前用户,是否有权限审核当前项目,传入当前项目的审核角色的id,如果不是抛出异常<br>
     * @param roleId 项目当前需要的审核角色的id
     */
    private void isHaveUserRole(Long roleId) {
        //1.获取当前用户的id
        Long userId = SecurityUtils.getUserId();
        //2.根据用户id,查询用户实体类
        List<Long> userRolesIds = userMapper.getUserRolesIds(userId);
        //3.判断用户是否有当前项目的审核角色
        if(!userRolesIds.contains(roleId)){
            Role byId = roleService.getById(roleId);
            throw new ServiceException("当前项目审核进度需要的角色不匹配,现在需要的角色是"+(byId.getRoleName() == null ? "未知角色,请联系管理员" : byId.getRoleName()));
        }
    }

    /**
     * 判断当前用户,是否有权限审核当前项目,传入当前项目的审核角色的id<br>
     * 下面都进行这些角色校验如何进行校验权限,是否有权限审核当前项目<br><br>
     * 老师角色:只有报名绑定老师才能审核<br>
     * 学院审核员:只有绑定对应的学院才能审核<br>
     * 管理员:可以审核所有项目<br>
     * 专家:只有绑定对应的专家才能审核<br><br>
     * @param roleId 项目当前需要的审核角色的id
     * @param project  项目实体类
     * @Param project 项目实体类
     * @return true表示当前用户有权限审核当前项目,<br>
     * 反之,false为没有权限审核当前项目
     */
    private boolean isHaveRole(Long roleId,Project project) {
        //1.根据角色id获取角色实体类
        Role role = roleService.getById(roleId);
        // 下面都根据项目状态需要的角色,进行校验用户是否有权限审核当前项目
        //2.老师角色进行判断
        if(RoleConstant.TEACHER.equals(role.getRoleKey())){
            //判断当前用户,是不是报名的老师
            return isHaveTeacher(project);
        }
        //3.学院审核员进行判断
        if(RoleConstant.COLLEGE.equals(role.getRoleKey())){
            //判断当前用户,是不是绑定对应的学院
            return isHaveCollege(project);
        }
        //4.管理员进行判断
        if(RoleConstant.ADMIN.equals(role.getRoleKey())){
            return isHaveAdmin();
        }
        //5.专家进行判断
        if(RoleConstant.EXPERT.equals(role.getRoleKey())){
            return isHaveExpert(project);
        }
        //6.返回结果
        return false;
    }

    /**
     * 判断当前用户,是不是绑定对应的专家<br>
     * 根据项目中专家id和用户id进行查询,专家数据表判断是否存在该用户<br>
     *
     * @param project 项目实体类
     * @return true表示当前用户是绑定对应的专家,false表示不是绑定对应的专家
     */
    private boolean isHaveExpert(Project project) {
        //1.获取当前用户的id
        Long userId = SecurityUtils.getUserId();
        //2.获取专家组的id,判断项目是否分组专家组
        Long specialistGroupId = project.getSpecialistGroupId();
        if(specialistGroupId == null){
            throw new ServiceException("当前项目没有分组专家组,请联系管理员,进行分配专家",500);
        }
        //3.根据专家组id和用户id,查询专家数据表
        LambdaQueryWrapper<SpecialistData> queryWrapper = new LambdaQueryWrapper<SpecialistData>()
                .eq(SpecialistData::getSpecialistGroupId, specialistGroupId)
                .eq(SpecialistData::getUserId, userId)
                .last("LIMIT 1");
        SpecialistData specialistData = specialistDataService.getOne(queryWrapper);
        if (specialistData != null){
            return true;
        }
        return false;
    }

    /**
     * 判断当前用户,是不是管理员<br>
     * @return true表示当前用户是管理员,false表示不是管理员
     */
    private boolean isHaveAdmin() {
        //1.获取当前用户的id
        Long userId = SecurityUtils.getUserId();
        //2.根据用户id,查询用户实体类
        List<Long> userRolesIds = userMapper.getUserRolesIds(userId);
        //3.判断用户是否有管理员角色
        Long adminId = roleService.getAdminId();
        if(userRolesIds.contains(adminId)){
            return true;
        }
        return false;
    }

    /**
     * 判断当前用户,是不是绑定对应的学院组的里面角色<br>
     * 根据项目中学院id和用户id进行查询,学院数据表判断是否存在该用户<br>
     *
     * @param project 项目实体类
     * @return true表示当前用户是绑定对应的学院组的里面角色,false表示不是绑定对应的学院组的里面角色
     */
    private boolean isHaveCollege(Project project) {
        //1.获取当前用户的id
        Long userId = SecurityUtils.getUserId();
        //2.获取学院组id
        Long collegeGroupId = project.getCollegeGroupId();
        //3.根据学院组id,查询学院组实体类
        LambdaQueryWrapper<CollegeData> queryWrapper = new LambdaQueryWrapper<CollegeData>()
                .eq(CollegeData::getCollegeGroupId, collegeGroupId)
                .eq(CollegeData::getUserId, userId)
                .last("LIMIT 1");
        CollegeData collegeData = collegeDataService.getOne(queryWrapper);
        //4.判断学院数据是否存在,如果存在,返回true,否则返回false
        if(collegeData!= null){
            return true;
        }
        return false;
    }

    /**
     * 判断当前用户,是不是报名的老师
     * @param project 项目实体类
     * @return true表示当前用户是报名的老师,false表示不是报名的老师
     */
    private boolean isHaveTeacher(Project project) {
        //1.获取当前用户的id
        Long userId = SecurityUtils.getUserId();
        //2.获取指导老师的id和企业老师的id并转为集合
        String teacherId = project.getTeacherId();
        String firmTeacherId = project.getFirmTeacherId();
        List<Long> teacherIds = JSONUtil.toList(JSONUtil.parseArray(teacherId), Long.class);
        List<Long> firmTeacherIds = JSONUtil.toList(JSONUtil.parseArray(firmTeacherId), Long.class);
        //3.判断当前用户的id是否在集合中,如果在,返回true,否则返回false
        if(teacherIds.contains(userId)||firmTeacherIds.contains(userId)){
            return true;
        }
        return false;
    }


    /**
     * 根据项目的审核状态,去查热更新配置类,返回当前项目的审核角色的id<br><br>
     * 有一个特殊情况,就1情况,当前审核进度7,配置情况最大审核顺序6,就审核步骤少于当中值7,当前项目状态为已完成<br><br>
     * 所以为保存项目进行,只能管理员才能进行最后的审核,所以返回管理员的角色id
     * @param auditStatus 审核状态
     * @param type 项目类型
     * @return 当前项目审核状态需要的审核角色的id (特殊情况,当前进度超出最大审核值,返回管理员的角色id,只有管理员才可以进行最后的审核)
     */
    private Long getAuditRoleId(Long auditStatus, Long type) {
        //1.先根据项目类型获取当前项目的配置信息
        Map<Long, List<XMAuditConfig.AuditRole>> projectAuditTypes = xmAuditConfig.getProjectAuditTypes();
        List<XMAuditConfig.AuditRole> auditRoles = projectAuditTypes.get(type);

        //2.判断状态是否超出最大审核值,超出就返回管理员的角色id
        if(auditStatus > auditRoles.size()){//状态值从1开始,项目配置审核列表元素个数不能小于1(审核值是单调递增的1),所以使用个数就可以判断是否超出最大审核值
            return roleService.getAdminId();
        }
        //3.找出当前状态需要审核角色信息
        XMAuditConfig.AuditRole role = auditRoles.stream()
                .filter(auditRole -> auditRole.getOrder().equals(auditStatus))//根据审核状态找出对应的角色信息,排序信息全部单调递增1,只会找出一个角色信息
                .collect(Collectors.toList()).get(0);//获取第一个角色的权限信息
        //4.返回角色id
        return roleService.selectByRoleKey(role.getName()).getRoleId();
    }




    /**
     * 前项目已经审核完毕,不需要再次审核<br>
     * 项目审核进度值为0表示项目已经审核完毕,不需要再次审核<br>
     * 为0直接抛出异常<br>
     * 再进行状态判断,只有项目审核中才可以进行审核
     * @param project 项目实体类
     */
    public void projectAlreadyAudit(Project project) {
        if(project.getAuditStatus() == 0L){
            throw new ServiceException("当前项目已经审核完毕,不需要再次审核");
        }
        if(ProjectConstant.PROJECT_STATUS_AUDIT_VALUE != project.getState()){
            throw new ServiceException("当中项目状态不为审核中,不能进行审核操作");
        }
    }


    /**
     * 判断当前项目是否存在,并返回项目的bean对象<br>
     * 项目不存在抛出异常<br>
     * @param projectId 项目id
     * @return 项目的bean对象
     */
    public Project getProject(Long projectId) {
        Project project = this.getById(projectId);
        if(project == null){
            throw new ServiceException("当前项目不存在,请检查",444);
        }
        return project;
    }

    /**
     * 插入项目进度表,初始化,项目进行表进行初始化
     * @param projectId 项目id
     *
     */
    private boolean insertProjectSchedule( Long projectId) {
        //项目进度表初始化
        ProjectSchedule projectSchedule = new ProjectSchedule();
        projectSchedule.setProjectId(projectId)
                .setUserId(SecurityUtils.getUserId())
                .setContent(projectScheduleConfig.getApplyForProjectApproval());
        return projectScheduleService.save(projectSchedule);
    }

    /**
     * 老师报名信息
     * @param applyForDTO
     * @param projectId
     */
    public boolean insertProjectTeacher(ApplyForDTO applyForDTO, Long projectId) {
        List<TeacherApplys> teacherApplys = BeanCopyUtils.copyBeans(applyForDTO.getTeachers(), TeacherApplys.class);
        //1  获取用户id集合 key是用户id value是用户对象
        List<Long> userIds = teacherApplys.stream().map(e -> e.getUserId()).collect(Collectors.toList());
        List<User> users = userService.selectByUserIds(userIds);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(User::getUserId, e -> e));
        //2 遍历集合,进行添加
        teacherApplys.stream().forEach(e -> {
            e.setProjectId(projectId);
            e.setName(userMap.get(e.getUserId()).getNickName());
            e.setUserName(userMap.get(e.getUserId()).getUserName());
        });
        //插入老师表
        return teacherApplysService.saveBatch(teacherApplys);
    }

    /**
     * 学生报名表插入
     * @param applyForDTO 申请信息
     * @param projectId 项目id
     * @return true表示成功,false表示失败
     */
    public boolean insertProjectStudnet(ApplyForDTO applyForDTO, Long projectId) {
//        List<StudnetApplys> studnetApplys= BeanCopyUtils.copyBeans(applyForDTO.getStudents(),StudnetApplys.class);
        List<StudnetApplys> studnetApplys                                            = toStudnetApplysListFromApplyForDTOList(applyForDTO.getStudents());
        //3.1 获取用户id集合 key是用户id value是用户对象
        List<Long> userIds = studnetApplys.stream().map(e -> e.getUserId()).collect(Collectors.toList());
        List<User> users = userService.selectByUserIds(userIds);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(User::getUserId, e -> e));
        //3.2 遍历集合,设置用户名
        studnetApplys.forEach(e ->{
            e.setProjectId(projectId);//设置id
            e.setUserName(userMap.get(e.getUserId()).getUserName());
            e.setName(userMap.get(e.getUserId()).getNickName());
        });
        //插入学生表
        return studnetApplysService.saveBatch(studnetApplys);
    }

    /**
     * 将学生报名信息列表转换为学生申请表列表
     * @param students 学生报名信息列表
     * @return 学生申请表列表
     */
    public List<StudnetApplys> toStudnetApplysListFromApplyForDTOList(List<ApplyForStudent> students) {
        if (students == null) {
            return Collections.emptyList();
        }
        return students.stream()
                .map(this::toStudnetApplysFromApplyForDTO)
                .collect(Collectors.toList());
    }


    /**
     * 学生报名表转换
     * @param student
     * @return
     */
    public StudnetApplys toStudnetApplysFromApplyForDTO(ApplyForStudent student) {
        if (student == null) {
            return null;
        }
        StudnetApplys studnetApplys = new StudnetApplys();
        studnetApplys.setCollegeGroupId(student.getCollegeGroupId());
        studnetApplys.setDateOfBirth(student.getDateOfBirth());
        studnetApplys.setIsPrincipal(student.getIsPrincipal().intValue());
        studnetApplys.setJob(student.getJob());
        studnetApplys.setMailbox(student.getMailbox());
        studnetApplys.setMobilePhone(student.getMobilePhone());
        studnetApplys.setName(student.getName());
        studnetApplys.setNation(student.getNation());
        studnetApplys.setPhone(student.getPhone());
        studnetApplys.setProfessionalClass(student.getProfessionalClass());
        studnetApplys.setSex(student.getSex());
        studnetApplys.setUserName(student.getUserName());
        studnetApplys.setUserId(student.getUserId());
        return studnetApplys;
    }


    /**
     * 把数据转到实体类当中并写入数据库
     * @param applyForDTO 申请信息
     * @return 添加成功的项目id
     */
    private Long insertProject(ApplyForDTO applyForDTO) {
        //cv固定参数
        Project project = BeanCopyUtils.copyBean(applyForDTO, Project.class);
        //转写金额参数
        project.setFiscalAppropriation(new BigDecimal(applyForDTO.getFiscalAppropriation()))
                .setSchoolAllocation(new BigDecimal(applyForDTO.getSchoolAllocation()))
                .setTotalMoney(new BigDecimal(applyForDTO.getTotalMoney()));
        //根据年度数据id获取到开始和结束时间
        YearData yearDataBaen = yearDataService.getById(applyForDTO.getYearDataId());


        //写入负责人的id,参加人员的所有的id,
        project.setUserId(SecurityUtils.getUserId())//负责人的id
                .setMemberId(tudentToJsonArrray(applyForDTO.getStudents()))//参加人员的所有的 数组id
                .setTeacherId(teacherToJsonArrray(applyForDTO.getTeachers()))//指导老师的 数组id
                .setFirmTeacherId(firstTeacherToJsonArrray(applyForDTO.getTeachers()))//企业老师的 数组id
                .setBeginTime(yearDataBaen.getBegin())
                .setEndTime(yearDataBaen.getEnd())
                .setState(ProjectConstant.PROJECT_STATUS_AUDIT_VALUE);
        //导入立项依据
        boolean result = setABC(project,applyForDTO);
        if(!result){
            throw new ServiceException("项目立项依据存在问题,请检查",444);
        }
        //先插入项目表
        if(!this.save(project)){
            throw new ServiceException("项目信息插入失败,请检查",444);
        }
        return project.getProjectId();
    }

    /**
     * 导入立项依据,abc 3个类型
     * @param project 项目实体类
     * @param applyForDTO 申请信息
     * @return true表示成功,false表示失败
     */
    private boolean setABC(Project project, ApplyForDTO applyForDTO) {
        //立项依据
        if(BeanUtils.areAllFieldsNotNull(applyForDTO.getA())){//立项依据a不为空
            BeanCopyUtils.copyBean(applyForDTO.getA(),project);
            return true;
        }
        if (BeanUtils.areAllFieldsNotNull(applyForDTO.getB())){//立项依据b不为空
            BeanCopyUtils.copyBean(applyForDTO.getB(),project);
            return true;
        }
        if (BeanUtils.areAllFieldsNotNull(applyForDTO.getC())){//立项依据c不为空
            BeanCopyUtils.copyBean(applyForDTO.getC(),project);
            return true;
        }
        return false;
    }

    /**
     * 根据老师报名信息,返回企业老师的id的json数组
     * @param teachers 老师报名信息
     * @return 企业老师的id的json数组
     */
    private String firstTeacherToJsonArrray(List<ApplyForTeacher> teachers) {
        List<Long> userIds = teachers.stream()
                .filter(teacher -> teacher.getIsTeacher()==1)
                .map(teacher -> teacher.getUserId())
                .collect(Collectors.toList());
        return JSONUtil.toJsonStr(userIds);
    }

    /**
     * 根据老师信息结合,返回指导老师的id的json数组
     * @param teachers
     * @return 指导老师的id的json数组
     */
    private String teacherToJsonArrray(List<ApplyForTeacher> teachers) {
        List<Long> userIds = teachers.stream()
                .filter(teacher -> teacher.getIsTeacher() == 0)
                .map(teacher -> teacher.getUserId())
                .collect(Collectors.toList());
        return JSONUtil.toJsonStr(userIds);
    }

    /**
     * 学生报名信息转json数组,存储全部学生的id
     * @param students 学生报名信息
     * @return 学生的id的json数组
     */
    private String tudentToJsonArrray(List<ApplyForStudent> students) {
        List<Long> userIds = students.stream()
                .map(stuent -> stuent.getUserId())
                .collect(Collectors.toList());
        String userIdsJson = JSONUtil.toJsonStr(userIds);
        return userIdsJson;
    }

    /**
     * 校验申请信息的字典数据是否存在
     * @param applyForDTO
     */
    public void checkAddProjectDictData(ApplyForDTO applyForDTO) {
        // 项目类型
        if(!checkDictData(applyForDTO.getType(),"xm_item_type")){
            throw new ServiceException("项目类型不存在,请联系管理员",444);
        }
        // 项目级别
         if(!checkDictData(applyForDTO.getProjectRank(),"xm_item_rank")){
             throw new ServiceException("项目级别不存在,请联系管理员",444);
         }
        // 项目类别
         if(!checkDictData(applyForDTO.getCategory(),"xm_item_category")){
             throw new ServiceException("项目类别不存在,请联系管理员",444);
         }
        // 学科类别
        if(!checkDictData(applyForDTO.getSubjectCategory(),"xm_item_subject_category")){
             throw new ServiceException("学科类别不存在,请联系管理员",444);
        }
        // 项目来源
        if(!checkDictData(applyForDTO.getProjectSource(),"xm_item_project_source")){
             throw new ServiceException("项目来源不存在,请联系管理员",444);
        }
    }

    /**
     * 根据字典类型判断字典值是否存在
     * @param dictValue 字典键值
     * @param dictType 字典类型
     * @return true表示存在,false表示不存在
     */
    public boolean checkDictData(Long dictValue, String dictType) {
         LambdaQueryWrapper<DictData> queryWrapper= new LambdaQueryWrapper<DictData>()
                .eq(DictData::getDictType,dictType)
                .eq(DictData::getDictValue,dictValue)
                .last("LIMIT 1");
         DictData dictData = dictDataService.getOne(queryWrapper);
         if(dictData == null){
             return false;
         }
        return true;
    }

    /**
     * 年度组id  是否存在并且是否启用  ,之后再检查  年度数据id是否存在
     * @param yearGroupId 年度组id
     * @param yearDataId 年度数据id
     */
    public void isYearGroupIdExistAndYearDataIsEnable(Long yearGroupId,Long yearDataId) {
        // 年度组id  是否存在并且是否启用
        YearGroup yearGroup = yearGroupService.getById(yearGroupId);
        if( yearGroup == null){
            throw new ServiceException("当前年度组不存在,请检查",444);
        }
        if(yearGroup.getStatus() == 1){// 1表示停用
            throw new ServiceException("当前年度组已禁用,请联系管理员",444);
        }
        // 使用年度组的id去查年度数据是否存在
         LambdaQueryWrapper<YearData> queryWrapper= new LambdaQueryWrapper<YearData>()
                .eq(YearData::getYearGroupId,yearGroupId)
                .eq(YearData::getYearDataId,yearDataId)
                .last("LIMIT 1");
         YearData yearData = yearDataService.getOne(queryWrapper);
         if(yearData == null){
             throw new ServiceException("你选择的年度期限不存在,请联系管理员",444);
         }
    }

    /**
     * 检查报名的学生的学院组是否存在并且是否启用
     * @param students 报名学生的集合
     */
    public void isStudentCollegeGroupsEnabled(List<ApplyForStudent> students){
        students.forEach(student -> {
            try {
                //检查学院组是否存在并且是否启用
                checkCollegeGroup(student.getCollegeGroupId());
//                isCollegeGroupExistAndEnable(student.getCollegeGroupId());
            }catch (ServiceException e){
                throw new ServiceException("学生"+student.getName()+"所属学院组不存在或已停用,请检查",444);
            }
        });
    }


    /**
     * 验证学院组id是否存在并且是否启用
     * @param collegeGroupId 年度组id
     */
    public void isCollegeGroupExistAndEnable(Long collegeGroupId){
        CollegeGroup byId = collegeGroupService.getById(collegeGroupId);
        if( byId== null){
            throw new ServiceException("当前学院组不存在,请检查",444);
        }
        if(byId.getStatus() == 1){
            throw new ServiceException("当前学院组已禁用,请联系管理员",444);
        }
    }

    /**
     * 附加4不需要企业老师支持,但是其他类型都需要企业老师支持<br>
     * 附加4 firm_teacher_experience可以为null<br\>
     * 项目类型已经存在
     * @param applyForDTO
     */
    private void teachersSupport(ApplyForDTO applyForDTO) {
        //获取项目类型的企业老师支持情况
        Map<Long, Long> teacherBusinessGuidance = xmStudnetProperties.getTeacherBusinessGuidance();
        /*if(teacherBusinessGuidance == null){
            throw new ServiceException("xm.studnet.teacherBusinessGuidance 配置错误,请联系管理员",444);
        }*/
        //进行判断nacos有没有配置当前项目
        if(!teacherBusinessGuidance.containsKey(applyForDTO.getType())){
            throw new ServiceException("当前项目类型没有配置企业老师支持情况,请联系管理员",444);
        }

        //判断当前项目类型是否需要企业老师支持
        if(teacherBusinessGuidance.get(applyForDTO.getType()) == 0L){
            //不需要企业老师支持
            if(applyForDTO.getFirmTeacherExperience()!= null){
                throw new ServiceException("附加4项目不需要企业老师支持,请不要填写企业老师经历",444);
            }
        }else if(teacherBusinessGuidance.get(applyForDTO.getType()) == 1L){
            //需要企业老师支持
            if(applyForDTO.getFirmTeacherExperience() == null){
                throw new ServiceException("附加4项目需要企业老师支持,请填写企业老师经历",444);
            }
        }else {
            throw new ServiceException("xm.studnet.teacherBusinessGuidance 的值配置错误,只能为0或1,请联系管理员");
        }
    }

    /**
     * 校验报名人数限制,有老师和学生,执行这个前提,<br>已经校验过项目类型是否存在
     */
    private void checkStuentAndTeacher(ApplyForDTO applyForDTO) {
        int studentCount = applyForDTO.getStudents().size();
        //验证学生报名人数是否超过限制
        Long type = applyForDTO.getType();
            Map<Long, Long> maxApplys = xmStudnetProperties.getMaxApplys();
        //判断参数是否存在
        if(maxApplys == null){
            throw new ServiceException("xm.studnet.maxApplys 配置错误,请联系管理员",444);
        }
        // 判断项目类型已经在nacso配置了
        if(!maxApplys.containsKey(type)){
            throw new ServiceException("当前项目类型没有配置学生报名人数限制,请联系管理员",444);
        }
        //验证学生报名人数是否超过限制
        if(studentCount > maxApplys.get(type).intValue()){
            throw new ServiceException("当前项目类型学生报名人数超过限制,最大报名人数为"+maxApplys.get(type),444);
        }
        int teacherCount = applyForDTO.getTeachers().size();
        //验证老师报名人数是否超过限制
        Long teacherMaxApply = xmTeacherProperties.getMaxApply();
        if (teacherMaxApply == null){
            throw new ServiceException("xm.teacher.maxApply 配置错误,请联系管理员",444);
        }
        if( teacherCount > teacherMaxApply.intValue()){
             throw new ServiceException("当前项目类型老师报名人数超过限制,最大报名人数为"+xmTeacherProperties.getMaxApply(),444);
         }
    }

    /**
     * 校验负责人是不是只有一个,并且第一个id要和申请人id一致
     * @param applyForDTO 申请信息
     */
    private void checkPrincipal(ApplyForDTO applyForDTO) {
        // 验证申请项目的
        long count = applyForDTO.getStudents().stream()
                .filter(student -> student.getIsPrincipal() == 1)
                .count();
        if (count != 1) {
            throw new ServiceException("负责人数量必须为1", 444);
        }
        // 验证负责人是否是第一个id
        ApplyForStudent student = applyForDTO.getStudents().get(0);
        Long userId = student.getUserId();
        if(!SecurityUtils.getUserId().equals(userId)){
            throw new ServiceException("负责人必须是申报列表的第一个", 444);
        }
    }

    /**
     * 校验学生和老师是否存在
     *
     * @param applyForDTO 申请信息
     */
    public void isStuentAndTeacherExist(ApplyForDTO applyForDTO){
        List<Long> studentId = applyForDTO.getStudents().stream()
                .map(ApplyForStudent::getUserId)
                .collect(Collectors.toList());
        List<Long> teacherId =applyForDTO.getTeachers().stream()
                .map(ApplyForTeacher::getUserId)
                .collect(Collectors.toList());
        //校验学生是否存在
        studentId.stream().forEach(id -> {
            if (!isUserExist(id)){
                List<ApplyForStudent> students = applyForDTO.getStudents().stream()
                        .filter(studentBean -> studentBean.getUserId().equals(id))
                        .collect(Collectors.toList());
                StringBuffer result = new StringBuffer("当前学生账号不存在:");
                students.stream().forEach(student -> result.append(student.getName()).append("，"));
                throw new ServiceException(result.toString(),444);
            }
        });
        //校验老师是否存在
        teacherId.forEach(id -> {
            if (!isUserExist(id)) {
                List<ApplyForTeacher> teachers = applyForDTO.getTeachers().stream()
                        .filter(teacherBean -> teacherBean.getUserId().equals(id))
                        .collect(Collectors.toList());
                StringBuilder result = new StringBuilder("当前教师账号不存在:");
                teachers.forEach(teacher -> result.append(teacher.getName()).append("，"));
                throw new ServiceException(result.toString());
            }
        });

        //1.必须存在一个指导老师
        long teacherCount = applyForDTO.getTeachers().stream()
                .filter(teacher -> teacher.getIsTeacher() == 0)
                .count();
        if (teacherCount <= 0) {
            throw new ServiceException("必须存在一个指导老师");
        }

        //验证 学生学号是否和id对应,先根据id批量查询用户信息
        userService.listByIds(studentId).stream().forEach(user -> {
            //转换用户ID和学号
            Map<Long, String> map = applyForDTO.getStudents().stream()
                    .collect(Collectors.toMap(ApplyForStudent::getUserId, ApplyForStudent::getUserName));

            //判断用户发的学号是否正确
            String userName = map.get(user.getUserId());//用户的请求学号
            if(StrUtil.isBlank(user.getUserName())){
                throw new ServiceException("当前用户的学号:"+userName+"不存在数据库中,请联系管理员");
            }
            if(!user.getUserName().equals(userName)){
                throw new ServiceException("当前用户的学号:"+userName+"与数据库中不一致,当前用户的id:"+user.getUserId()+"不一致,请联系管理员");
            }
        });

        //todo 校验名字是否和账号对应

    }


    /**
     * 校验用户存在
     * @param userId 用户id
     * @return true(存在)/false(不存在)
     */
    public boolean isUserExist(Long userId){
        return 1L == userService.count(Wrappers.<User>lambdaQuery().eq(User::getUserId,userId));
    }

    /**
     * 校验钱的比较验算 财政+学校 = 总金额<br>
     * todo 还有那个json里面金额还没有校验
     * @param applyForDTO
     */
    private void checkMoney(ApplyForDTO applyForDTO) {
        BigDecimal school = new BigDecimal(applyForDTO.getSchoolAllocation());
        BigDecimal fiscal = new BigDecimal(applyForDTO.getFiscalAppropriation());
        BigDecimal total = new BigDecimal(applyForDTO.getTotalMoney());
        if (school.add(fiscal).compareTo(total)!= 0){
            throw new ServiceException("财政拨款的金额(钱)和学校的金额(钱)不相等",444);
        }
    }

    /**
     * a ,b ,c必须三选一,参数不能为null
     * @param applyForDTO 申请信息
     */
    private void checkApplyFor(ApplyForDTO applyForDTO) {
        int count = 0;
        int count1 = 0;
        if(BeanUtils.areAllFieldsNotNull(applyForDTO.getA())){// 不为NUL+1
            count++;
            count1 = 1;
        }
        if(BeanUtils.areAllFieldsNotNull(applyForDTO.getB())){
            count++;
            count1 = 2;
            if (count > 1){
                throw new ServiceException("a,b,c只能选一",444);
            }
        }
        if(BeanUtils.areAllFieldsNotNull(applyForDTO.getC())){
            count++;
            count1 = 3;
            if (count > 1){
                throw new ServiceException("a,b,c只能选一",444);
            }
        }
        if (count == 0){
            throw new ServiceException("a,b,c不能都为空",444);
        }


        //2. 根据count1的值进行判断 三选一,其中有满参数的,另外两个要全部为空
        if (count1 == 1){
            //b不能有数据
            if (!BeanUtils.areAllFieldsNull(applyForDTO.getB())){
                throw new ServiceException("a不为空时,b不能有数据");
            }
            //c不能有数据
            if (!BeanUtils.areAllFieldsNull(applyForDTO.getC())){
                throw new ServiceException("a不为空时,c不能有数据");
            }
        }else if(count1 == 2){
            //a不能有数据
            if (!BeanUtils.areAllFieldsNull(applyForDTO.getA())){
                throw new ServiceException("b不为空时,a不能有数据");
            }
            //c不能有数据
            if (!BeanUtils.areAllFieldsNull(applyForDTO.getC())){
                throw new ServiceException("b不为空时,c不能有数据");
            }
        }else if(count1 == 3){
            //b不能有数据
            if (!BeanUtils.areAllFieldsNull(applyForDTO.getB())){
                throw new ServiceException("c不为空时,b不能有数据");
            }
            //a不能有数据
            if (!BeanUtils.areAllFieldsNull(applyForDTO.getA())){
                throw new ServiceException("c不为空时,a不能有数据");
            }
        }


    }

}

