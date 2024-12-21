package com.xk.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cms.common.core.exception.ServiceException;
import com.cms.common.core.web.domain.Response;
import com.cms.common.security.utils.SecurityUtils;
import com.xk.config.*;
import com.xk.constant.ProjectConstant;
import com.xk.constant.RoleConstant;
import com.xk.domain.dto.ApplyForDTO;
import com.xk.domain.dto.ApplyForStudent;
import com.xk.domain.dto.ApplyForTeacher;
import com.xk.domain.dto.ProjectAuditDto;
import com.xk.entity.*;
import com.xk.mapper.ProjectMapper;
import com.xk.mapper.StudnetApplysMapper;
import com.xk.mapper.TeacherApplysMapper;
import com.xk.mapper.UserMapper;
import com.xk.service.*;
import com.xk.utils.BeanCopyUtils;
import com.xk.utils.BeanUtils;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
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
     * 审核意见服务
     */
    private final AuditOpinionService auditOpinionService;
    private final StudnetApplysMapper studnetApplysMapper;
    private final TeacherApplysMapper teacherApplysMapper;

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
     * 权限校验审核通过后,操作审核表存指针(项目进度值抽象出来的指针)存在4个位置(多种情况)
     *
     *
     * 1.假设审核进度值为7,但是当前项目类型最大顺序值为6,就审核步骤少于当中值7,当前项目状态为已完成;<a href="">修改项目表和项目进度表状态(特殊记录),还是会记录多余出来的审核步骤(审核表)</a><br><br>
     * 2.当前项目已经审核完毕,不需要再次审核,审核进度值为0<br><br>
     * 3.当前项目是审核不通过重新审核的,审核进度值为1,审核表已经有记录,会原本的记录覆盖<a href="">修改项目表和审核表<a><br><br>
     * 4.正常审核,未完成审核进来的,审核进度值大于1,审核表会记录会新增一条记录<a href="">修改项目表和审核表<a><br><br>
     * 5.第一条审核记录,审核进度值为1,审核表没有记录,会新增一条记录<a href="">修改项目表和审核表<a><br><br>
     * 6.最后一个项目审核,审核进度等于最大审核顺序,审核完成<a href="">修改项目表,审核表,项目进度表</a><br><br>
     * <a href="">把nacos定义配置审核顺序想象成列表,把项目表里的审核进度抽象为指针,这样可以抽象所有审核状态</a>
     * @param projectAuditDto
     * @return
     */
    @Override
    public Response projectAudit(ProjectAuditDto projectAuditDto) {
        //1.判断当前项目是否存在,并返回项目的bean对象,不存在抛出异常
        Project project = getProject(projectAuditDto.getProjectid());
        //2.情况二,当前项目已经审核完毕,不需要再次审核,直接抛出异常
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

        }
        return null;
    }

    /**
     * 获取学生参与项目成功
     * @return
     */
    @Override
    public Response getStudentProjectList() {
        //当前登录用户ID
        Long userId = SecurityUtils.getLoginUser().getUserid();
        try{
            LambdaQueryWrapper<Project> queryWrapper = Wrappers.<Project>lambdaQuery()
                    .eq(Project::getDelFlag,0)//是否删除
                    .or()
                    .eq(Project::getCreateBy,userId) // 创建字段的id是否为当前用户id
                    .apply("JSON_CONTAINS(member_id, '["+userId.toString()+"]')");

            List<Project> projectList = list(queryWrapper);

            return Response.success(projectList,"获取学生参与项目以及报名项目成功！");
        }catch (Exception e){
            throw new ServiceException("查询用户当前参与项目失败",502);
        }
    }

    /**
     * 获取学生负责项目
     * @return
     */
    @Override
    public Response getStudentResponsibleProjectList() {
        //当前登录用户ID
        Long userId = SecurityUtils.getLoginUser().getUserid();

        try{
            LambdaQueryWrapper<Project> queryWrapper = Wrappers.<Project>lambdaQuery()
                    .eq(Project::getDelFlag,0)//是否删除
                    .eq(Project::getUserId,userId); // 项目负责人ID是否为当前登录用户ID
            List<Project> projectList = list(queryWrapper);
            return Response.success(projectList,"获取学生负责项目成功！");
        }catch (Exception e){
            throw new ServiceException("查询学生当前负责项目失败",502);
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
            Response.success("删除成功！");
        }

        return null;
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
    public Response getMyCrectProject() {
        //当前登录用户ID
        Long userId = SecurityUtils.getLoginUser().getUserid();

        try{
            LambdaQueryWrapper<Project> queryWrapper = Wrappers.<Project>lambdaQuery()
                    .eq(Project::getDelFlag,0)//是否删除
                    .eq(Project::getCreateBy,userId); // 项目创建者ID是否为当前登录用户ID
            List<Project> projectList = list(queryWrapper);
            return Response.success(projectList,"获取学生创建项目成功！");
        }catch (Exception e){
            throw new ServiceException("查询学生当前创建项目失败",502);
        }
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
        LambdaQueryWrapper<Project> queueWrapper = Wrappers.<Project>lambdaQuery()
                .eq(Project::getDelFlag, 0) // 未删除 字段为 0
                .eq(Project::getProjectId,projectId);
        //找到目标数据列
        Project project = getOne(queueWrapper);
        if (project==null){
            throw new ServiceException("需要删除的目标数据不存在，或者已经被删除！",403);
        }
        project.setDelFlag(1);//标识删除

        boolean n = update(project,queueWrapper);//更新数据

        if (n){
            return true;
        }else {
            throw new ServiceException("删除失败！",403);
        }

    }

    private boolean isAuditFailed(Long projectId){
        try{
            LambdaQueryWrapper<Project> queueWrapper = Wrappers.<Project>lambdaQuery()
                    .eq(Project::getDelFlag, 0) // 未删除 字段为 0
                    .eq(Project::getProjectId,projectId); //目标项目
            // todo 先暂时直接判断是否为5（未通过）
            if(getOne(queueWrapper).getState()== 5){
                return true;
            }else {
                throw new ServiceException("项目未处于审核未通过状态，无法删除！",403);
            }
        }catch (Exception e){
            throw new ServiceException("在查询项目是否通过的时候发生错误！",502);
        }
    }


    /**
     * 项目是否存在
     * @param projectId
     * @return
     */
    private boolean ProjectIsNull(Long projectId) {
        try{
            LambdaQueryWrapper<Project> queueWrapper = Wrappers.<Project>lambdaQuery()
                    .eq(Project::getDelFlag, 0) // 未删除 字段为 0
                    .eq(Project::getProjectId,projectId); //目标项目
            if(count(queueWrapper)>0){
                return true;
            }else {
                throw new ServiceException("该项目不存在！",404);
            }
        }catch (Exception e){
            throw new ServiceException("在查询项目是否存在时候发生错误！",502);
        }
    }

    /**
     * 检查用户是否为项目负责人
     * @param projectId
     * @param userId
     * @return
     */
    private boolean isProjectAdmin(Long projectId,Long userId) {
        try{
            LambdaQueryWrapper<Project> queueWrapper = Wrappers.<Project>lambdaQuery()
                    .eq(Project::getDelFlag, 0) // 未删除 字段为 0
                    .eq(Project::getProjectId,projectId); //目标项目
            if (getOne(queueWrapper).getUserId() == userId) {
                //项目负责人
                return true;
            }else {
                throw new ServiceException("您当前不是项目负责人无权删除该项目",403);
            }

        }catch (Exception e){
            throw new ServiceException("删除项目时查询项目失败",502);
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
        Long a= project.getAuditStatus();//项目当前审核状态值
        List<AuditOpinion> auditOpinions = auditOpinionService.selectByPropertieID(project.getProjectId());//获取项目的审核记录(有可能为空)
        int b = auditOpinions.size();//审核记录条数


        return false;
        /*//todo 有空才写,判断a的是否 a>=0,否则进行异常处理,把项目转为审核不通过状态,并且项目进度表进行记录,然后返回true,(要求记录系统自动生成的审核记录)


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
            throw new ServiceException("当前项目审核进度需要的角色不匹配,现在需要的角色是"+byId.getRoleName() == null ? "未知角色,请联系管理员" : byId.getRoleName(),400);
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
        if(role.getRoleKey().equals(RoleConstant.TEACHER)){
            //判断当前用户,是不是报名的老师
            return isHaveTeacher(project);
        }
        //3.学院审核员进行判断
        if(role.getRoleKey().equals(RoleConstant.COLLEGE)){
            //判断当前用户,是不是绑定对应的学院
            return isHaveCollege(project);
        }
        //4.管理员进行判断
        if(role.getRoleKey().equals(RoleConstant.ADMIN)){
            return isHaveAdmin();
        }
        //5.专家进行判断
        if(role.getRoleKey().equals(RoleConstant.EXPERT)){
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
     * 为0直接抛出异常
     * @param project 项目实体类
     */
    public void projectAlreadyAudit(Project project) {
        if(project.getAuditStatus() == 0L){
            throw new ServiceException("当前项目已经审核完毕,不需要再次审核",444);
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

