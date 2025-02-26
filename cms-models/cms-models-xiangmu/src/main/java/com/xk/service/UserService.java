package com.xk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xk.domain.dto.ProjectPersonDto;
import com.xk.domain.vo.SearchPersonListVo;
import com.xk.domain.vo.detail.Student;
import com.xk.domain.dto.InquireUserDTO;
import com.xk.domain.dto.PageDTO;
import com.xk.domain.vo.admin.UserListVo;
import com.xk.entity.User;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * 用户信息表(User)表服务接口
 *
 * @author yawu
 * @since 2024-12-05 01:00:12
 */
public interface UserService extends IService<User> {

    /**
     * 根据用户id集合查询用户信息
     * @param userIds 用户id集合
     * @return 用户信息集合
     */
    List<User> selectByUserIds(List<Long> userIds);

    /**
     * 分页查询全部用户信息
     * @param inquireUserDto
     * @return
     */
    PageDTO<UserListVo> getUserList(InquireUserDTO inquireUserDto);

//    PageDTO<SearchPersonListVo> searchPerson(ProjectPersonDto projectPersonDto,List<Long> userIds);
    /**
     * 条件获取(搜索用户)
     * @param projectPersonDto
     * @return
     */
    PageDTO<SearchPersonListVo> getUsersByRolesAndKeyword( ProjectPersonDto projectPersonDto);

    /**
     * 根据用户信息进行绑定角色
     * @param file 用户信息excel
     * @param rolekey 角色key
     * @return
     */
    boolean importUserBindingRole(MultipartFile file, @NotBlank(message = "角色key不允许为空") String rolekey);

    /**
     * 导入用户信息
     * @param file
     * @return
     */
    boolean importUser(MultipartFile file);

    /**
     * 根据用户信息进行解绑角色
     * @param file
     * @param rolekey
     * @return
     */
    boolean importUserUnbindingRole(MultipartFile file, @NotBlank(message = "角色key不允许为空") String rolekey);

    boolean getExeclTemplate(@NotNull(message = "type不允许为空") @Min(value = 0,message = "type不允许小于0") @Max(value = 1,message = "type不允许大于1") Integer type, HttpServletResponse response);
}

