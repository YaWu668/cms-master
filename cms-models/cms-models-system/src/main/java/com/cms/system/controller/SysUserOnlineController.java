package com.cms.system.controller;

import com.cms.common.core.web.controller.BaseController;
import com.cms.common.core.web.domain.Response;
import com.cms.common.core.web.page.TableDataInfo;
import com.cms.common.security.annotation.RequiresPermissions;
import com.cms.system.domain.vo.SysUserOnline;
import com.cms.system.service.SysUserOnlineService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.websocket.server.PathParam;
import java.util.Collections;
import java.util.List;

/**
 * 在线用户监控
 *
 * @author 邓志军
 * @date 2024年8月14日08:31:10
 */
@Api(value = "/online", tags = {"在线用户监控"})
@RestController
@RequestMapping("/online")
public class SysUserOnlineController extends BaseController {

    @Resource
    private SysUserOnlineService sysUserOnlineService;

    /**
     * 获取在线用户列表
     *
     * @param userName 用户名称
     * @return 在线用户回合集合
     */
    @RequiresPermissions("monitor:online:query")
    @ApiOperation(value = "获取在线用户列表", notes = "获取在线用户列表", httpMethod = "GET")
    @GetMapping
    public Response<TableDataInfo<SysUserOnline>> getOnlineUser(@PathParam("userName") String userName) {
        List<SysUserOnline> list = this.sysUserOnlineService.getOnlineUser(userName);
        Collections.reverse(list);
        list.removeAll(Collections.singleton(null));
        return this.success(this.buildTable(list));
    }

    /**
     * 强退在线用户
     *
     * @param tokenId 会话id
     */
    @RequiresPermissions("monitor:online:batchLogout")
    @ApiOperation(value = "强退在线用户", notes = "强退在线用户", httpMethod = "DELETE")
    @DeleteMapping("/{tokenId}")
    public Response<?> forceLogout(@PathVariable String tokenId) {
        return this.success(this.sysUserOnlineService.forceLogout(tokenId));
    }

    /**
     * 将数据构建成分页表数据
     *
     * @param sysUserOnlines 在线用户会话集合
     * @return 分页表
     */
    private TableDataInfo<SysUserOnline> buildTable(List<SysUserOnline> sysUserOnlines) {
        TableDataInfo<SysUserOnline> rspData = new TableDataInfo<>();
        rspData.setRecords(sysUserOnlines);
        rspData.setTotal(new PageInfo<>(sysUserOnlines).getTotal());
        return rspData;
    }
}
