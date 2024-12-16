package com.xk.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cms.common.core.exception.ServiceException;
import com.cms.common.core.web.domain.Response;
import com.cms.common.security.utils.SecurityUtils;
import com.xk.config.XMStateProperties;
import com.xk.config.XMStudnetProperties;
import com.xk.config.XMTeacherProperties;
import com.xk.domain.dto.ApplyForDTO;
import com.xk.domain.dto.ApplyForStudent;
import com.xk.domain.dto.ApplyForTeacher;
import com.xk.entity.*;
import com.xk.mapper.ProjectMapper;
import com.xk.service.*;
import com.xk.utils.BeanCopyUtils;
import com.xk.utils.BeanUtils;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private final UserServiceImpl userServiceImpl;
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
    private final XMStateProperties xmStateProperties;;
    /**
     * 项目进度表服务
     */
    private final ProjectScheduleService projectScheduleService;
    /**
     * 申请项目
     * @param applyForDTO 申请信息
     * @return
     */
    @Override
    @Transactional
    public Response addProject(ApplyForDTO applyForDTO) {
        //a ,b ,c必须三选一,参数不能为null
        checkApplyFor(applyForDTO);
        //钱的比较验算 财政+学校 = 总金额
        checkMoney(applyForDTO);
        //校验用户(老师和学生)都存在
        isStuentAndTeacherExist(applyForDTO);
        //字典数据校验
        checkAddProjectDictData(applyForDTO);
        //校验负责人是不是只有一个,并且第一个id要和申请人id一致
        checkPrincipal(applyForDTO);
        //校验报名人数限制,有老师和学生
        checkStuentAndTeacher(applyForDTO);
        //校验附加4之外都需要企业老师支持
        teachersSupport(applyForDTO);
        //TODO 验证学院组id是否存在并且是否启用
        isCollegeGroupExistAndEnable(applyForDTO.getCollegeGroupId());
        //检查报名的学生的学院组是否存在并且是否启用
        isStudentCollegeGroupsEnabled(applyForDTO.getStudents());
        //todo 年度组id  是否存在并且是否启用  ,之后再检查  年度数据id是否存在
        isYearGroupIdExistAndYearDataIsEnable(applyForDTO.getYearGroupId(),applyForDTO.getYearDataId());
        //todo 有空写,验证文件存在通内

        //todo 有空再写,验证用户负责人只有一个项目在进行中才可以申请新的项目&&(同时只有一个是负责人|| 参加项目进行中最多2个)

        //todo 有空再写,验证队员和老师参数进行项目最多有2个

        //把数据转到实体类当中并写入数据库
        Long projectId = insertProject(applyForDTO);
        //插入学生表
        if(!insertProjectStudnet(applyForDTO,projectId)){
            throw new ServiceException("插入学生表失败,请检查",500);
        }
        //todo 有bug ,插入老师表
        if(!insertProjectTeacher(applyForDTO,projectId)){
            throw new ServiceException("插入老师表失败,请检查",500);
        }
        //todo 有bug ,项目进行表插入
        if(!insertProjectSchedule(projectId)){
            throw new ServiceException("项目进行表插入失败,请检查",444);
        }
        return Response.success("项目申请成功,等待审核");
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
                .setContent("项目申请提交成功,等待审核");
        return projectScheduleService.save(projectSchedule);
    }

    /**
     * 老师报名信息
     * @param applyForDTO
     * @param projectId
     */
    public boolean insertProjectTeacher(ApplyForDTO applyForDTO, Long projectId) {
        List<TeacherApplys> teacherApplys = BeanCopyUtils.copyBeans(applyForDTO.getTeachers(), TeacherApplys.class);
        //设置项目id
        for (TeacherApplys teacherApply : teacherApplys) {
            teacherApply.setProjectId(projectId);
        }
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
        List<StudnetApplys> studnetApplys= BeanCopyUtils.copyBeans(applyForDTO.getStudents(),StudnetApplys.class);
        //设置项目id
        for (StudnetApplys studnetApply : studnetApplys) {
            studnetApply.setProjectId(projectId);
            studnetApply.setUserName(SecurityUtils.getUsername());
        }
        //插入学生表
        return studnetApplysService.saveBatch(studnetApplys);
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
        List<Long> stateList = xmStateProperties.getStateList();
        if (stateList == null || stateList.isEmpty()){
            throw new ServiceException("xm.state.stateList项目状态配置不存在,请联系管理员",444);
        }

        //写入负责人的id,参加人员的所有的id,
        project.setUserId(SecurityUtils.getUserId())//负责人的id
                .setMemberId(tudentToJsonArrray(applyForDTO.getStudents()))//参加人员的所有的 数组id
                .setTeacherId(teacherToJsonArrray(applyForDTO.getTeachers()))//指导老师的 数组id
                .setFirmTeacherId(firstTeacherToJsonArrray(applyForDTO.getTeachers()))//企业老师的 数组id
                .setBeginTime(yearDataBaen.getBegin())
                .setEndTime(yearDataBaen.getEnd())
                .setState(stateList.get(0));
        //导入立项依据
        boolean result = setABC(project,applyForDTO);
        if(!result){
            throw new ServiceException("项目立项依据存在问题,请检查",444);
        }
        //先插入项目表
        this.save(project);
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
                isCollegeGroupExistAndEnable(student.getCollegeGroupId());
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
        if(teacherBusinessGuidance == null){
            throw new ServiceException("xm.studnet.teacherBusinessGuidance 配置错误,请联系管理员",444);
        }
        //进行判断nacos有没有配置当前项目
        if(!teacherBusinessGuidance.containsKey(applyForDTO.getType())){
            throw new ServiceException("当前项目类型没有配置企业老师支持情况,请联系管理员",444);
        }

        //判断当前项目类型是否需要企业老师支持
        if(teacherBusinessGuidance.get(applyForDTO.getType()) == 0){
            //不需要企业老师支持
            if(applyForDTO.getFirmTeacherExperience()!= null){
                throw new ServiceException("附加4项目不需要企业老师支持,请不要填写企业老师经历",444);
            }
        }else if(teacherBusinessGuidance.get(applyForDTO.getType()) == 1){
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
                throw new ServiceException(result.toString(), 444);
            }
        });
    }


    /**
     * 校验用户存在
     * @param userId 用户id
     * @return true(存在)/false(不存在)
     */
    public boolean isUserExist(Long userId){
        return 1L == userServiceImpl.count(Wrappers.<User>lambdaQuery().eq(User::getUserId,userId));
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
        if(BeanUtils.areAllFieldsNotNull(applyForDTO.getA())){// 不为NUL+1
            count++;
        }
        if(BeanUtils.areAllFieldsNotNull(applyForDTO.getB())){
            count++;
            if (count > 1){
                throw new ServiceException("a,b,c只能选一",444);
            }
        }
        if(BeanUtils.areAllFieldsNotNull(applyForDTO.getC())){
            count++;
            if (count > 1){
                throw new ServiceException("a,b,c只能选一",444);
            }
        }
    }

}

