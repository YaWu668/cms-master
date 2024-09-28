package com.cms.auth.concroller;

import com.cms.auth.service.SysLoginService;
import com.cms.auth.vo.LoginVo;
import com.cms.common.core.utils.JwtUtils;
import com.cms.common.core.utils.StringUtils;
import com.cms.common.core.web.controller.BaseController;
import com.cms.common.core.web.domain.Response;
import com.cms.common.security.auth.AuthUtil;
import com.cms.common.security.service.TokenService;
import com.cms.common.security.utils.SecurityUtils;
import com.cms.system.api.domain.dto.SysUserDto;
import com.cms.system.api.model.LoginUser;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Token控制器
 *
 * @author 邓志军
 * @date 2024年5月28日14:26:47
 */
@RestController
public class TokenController extends BaseController {

    @Resource
    private SysLoginService sysLoginService;

    @Resource
    private TokenService tokenService;

    /**
     * 用户登录
     *
     * @param login 用户登录信息
     * @return 登录对象信息
     */
    @PostMapping("/login")
    public Response<Map<String, Object>> login(@Validated @RequestBody LoginVo login) {
        // 1.获取用户登录信息
        LoginUser loginUser = this.sysLoginService.login(login.getUsername(), login.getPassword());
        // 2.创建token
        Map<String, Object> token = this.tokenService.createToken(loginUser);
        // 3.去除部分不需要的数据
        this.removeDate(loginUser.getSysUser());
        // 4.返回 Token 数据
        return this.success(token);
    }

    /**
     * 用户退出登录
     */
    @DeleteMapping("/logout")
    public Response<String> logout(HttpServletRequest request) {
        String token = SecurityUtils.getToken(request);
        if (StringUtils.isNotEmpty(token)) {
            String username = JwtUtils.getUserName(token);
            // 删除用户缓存记录
            AuthUtil.logoutByToken(token);
            // 记录用户退出日志
            this.sysLoginService.logout(username);
        }
        return this.success();
    }

    /**
     * 去除部分无用数据
     */
    private void removeDate(SysUserDto user) {
        user.setPassword(null);
        user.setCreateBy(null);
        user.setLoginDate(null);
        user.setCreateTime(null);
        user.setUserType(null);
    }
}
