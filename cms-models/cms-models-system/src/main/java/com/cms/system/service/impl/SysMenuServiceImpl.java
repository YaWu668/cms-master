package com.cms.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cms.common.core.constant.Constants;
import com.cms.common.core.constant.UserConstants;
import com.cms.common.core.exception.ServiceException;
import com.cms.common.core.utils.StringUtils;
import com.cms.common.core.web.page.PageParams;
import com.cms.system.domain.dto.SysMenuDto;
import com.cms.system.domain.pojo.SysMenu;
import com.cms.system.domain.query.SysMenuQuery;
import com.cms.system.domain.vo.MetaVo;
import com.cms.system.domain.vo.RouterVo;
import com.cms.system.domain.vo.TreeSelect;
import com.cms.system.mapper.SysMenuMapper;
import com.cms.system.service.SysMenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜单权限表 Service 实现
 *
 * @author 邓志军
 * @date 2024-05-29
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Resource
    private SysMenuMapper menuMapper;

    /**
     * 查询菜单权限表列表数据(无分页)
     *
     * @param query 菜单权限表查询条件
     * @return 菜单权限表列表数据
     */
    @Override
    public List<SysMenuDto> listEntities(SysMenuQuery query) {
        return this.menuMapper.listEntities(query);
    }

    /**
     * 根据id查询菜单权限表详细信息
     *
     * @param id 菜单权限表表数据id
     */
    @Override
    public SysMenu getEntityById(Long id) {
        return this.getById(id);
    }

    /**
     * 添加菜单权限表数据
     *
     * @param sysMenu 菜单权限表
     * @return 添加菜单权限表数据成功返回 true 否则返回 false
     */
    @Override
    public boolean addEntity(SysMenu sysMenu) {
        // 1、判断该菜单下是否存在相同名称的菜单
        if (!this.checkMenuNameUnique(sysMenu)) {
            throw new ServiceException(String.format("添加菜单 %s 失败，菜单名称已存在", sysMenu.getMenuName()));
        }

        // 2、判断是否是外链，如果是外链校验 http 是否合法
        if(UserConstants.YES_FRAME.equals(sysMenu.getIsFrame().toString()) && !StringUtils.ishttp(sysMenu.getPath())) {
            throw new ServiceException(String.format("添加菜单 %s 失败，地址必须以http(s)://开头", sysMenu.getMenuName()));
        }

        // 3、添加菜单信息
        return this.save(sysMenu);
    }

    /**
     * 修改菜单权限表数据
     *
     * @param sysMenu 菜单权限表
     * @return 修改菜单权限表数据成功返回 true 否则返回 false
     */
    @Override
    public boolean updateEntity(SysMenu sysMenu) {
        // 1、判断菜单是否已经存在
        if (!this.checkMenuNameUnique(sysMenu)) {
            throw new ServiceException(String.format("修改菜单 %s 失败，菜单名称已存在", sysMenu.getMenuName()));
        }

        // 2、判断菜单的上级是否是自己
        if (sysMenu.getParentId().equals(sysMenu.getMenuId())) {
            throw new ServiceException(String.format("修改菜单 %s 失败，上级菜单不能选择自己", sysMenu.getMenuName()));
        }

        // 3、判断菜单是否是外链，如果是外链校验 http 是否合法
        if(UserConstants.YES_FRAME.equals(sysMenu.getIsFrame().toString()) && !StringUtils.ishttp(sysMenu.getPath())) {
            throw new ServiceException(String.format("修改菜单 %s 失败，地址必须以http(s)://开头", sysMenu.getMenuName()));
        }

        // 4、修改数据
        return this.updateById(sysMenu);
    }

    /**
     * 判断父节点下该菜单是否是唯一
     *
     * @param menu 菜单信息
     */
    private boolean checkMenuNameUnique(SysMenu menu) {
        // 1.获取菜单的id
        Long menuId = StringUtils.isNull(menu.getMenuId()) ? -1L : menu.getMenuId();

        // 2.查询父元素下是否有相同的菜单
        LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysMenu::getMenuName, menu.getMenuName());
        queryWrapper.eq(SysMenu::getParentId, menu.getParentId());
        SysMenu uniqueMenu = this.getOne(queryWrapper);

        // 3、如果查询出来的参数不是空的，并且查询的菜单id != 当前菜单id
        if (StringUtils.isNotNull(uniqueMenu) && !Objects.equals(uniqueMenu.getMenuId(), menuId)) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 根据id删除菜单权限表数据
     *
     * @param id 菜单id
     * @return 删除菜单权限表数据成功返回 true 否则返回 false
     */
    @Override
    public boolean deleteEntityById(Long id) {
        // 1、判断是否存在子菜单
        if (this.hasChildByMenuId(id)) {
            throw new ServiceException("该菜单下存在子菜单不允许删除！");
        }

        // 2、判断菜单是否被分配
        if (this.checkMenuExistRole(id)) {
            throw new ServiceException("该菜单被角色所绑定不允许删除！");
        }

        // 3、删除菜单
        return this.removeById(id);
    }

    /**
     * 查询该菜单下是否存在子菜单
     *
     * @param menuId 菜单ID
     * @return 存在返回true，否则返回false
     */
    private boolean hasChildByMenuId(Long menuId) {
        Integer row = this.menuMapper.hasChildByMenuId(menuId);
        return row > 0;
    }

    /**
     * 查询该菜单是否被角色绑定
     *
     * @param menuId 菜单ID
     * @return 存在返回true，否则返回false
     */
    private boolean checkMenuExistRole(Long menuId) {
        Integer row = this.menuMapper.checkMenuExistRole(menuId);
        return row > 0;
    }

    /**
     * 根据角色ID查询权限
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectMenuPermsByRoleId(Long roleId) {
        // 1.查询角色的菜单权限
        List<String> perms = this.menuMapper.selectMenuPermsByRoleId(roleId);

        // 2.整合菜单权限
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms) {
            // 如果权限字符串不为空，则处理其权限信息
            if (StringUtils.isNotEmpty(perm)) {
                // 将权限字符串根据逗号拆分成数组，然后添加到权限集合中
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }

        // 3.返回数据
        return permsSet;
    }

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public Collection<String> selectMenuPermsByUserId(Long userId) {
        // 1.查询用户的菜单权限
        List<String> perms = menuMapper.selectMenuPermsByUserId(userId);

        // 2.整合菜单权限
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms) {
            // 如果权限字符串不为空，则处理其权限信息
            if (StringUtils.isNotEmpty(perm)) {
                // 将权限字符串根据逗号拆分成数组，然后添加到权限集合中
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }

        // 3.返回数据
        return permsSet;
    }

    /**
     * 根据用户ID查询菜单树信息
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    @Override
    public List<SysMenuDto> selectMenuTreeByUserId(Long userId) {
        List<SysMenuDto> menus = null;

        // 1.判断是否是超级管理员，管理员拥有所有权限
        if (Objects.equals(userId, UserConstants.ADMIN_ID)) {
            menus = this.menuMapper.selectMenuTreeAll();
        } else {
            // 2.不是管理员，根据用户id查询菜单
            menus = this.menuMapper.selectMenuTreeByUserId(userId);
        }

        // 3.重构节点树
        return this.getChildPerms(menus, 0);
    }

    /**
     * 构建前端路由所需要的菜单
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    @Override
    public List<RouterVo> buildMenus(List<SysMenuDto> menus) {
        List<RouterVo> routers = new LinkedList<>();
        for (SysMenuDto menu : menus) {
            RouterVo router = new RouterVo();
            router.setHidden("1".equals(menu.getVisible()));
            router.setName(getRouteName(menu));
            router.setPath(getRouterPath(menu));
            router.setComponent(getComponent(menu));
            router.setQuery(menu.getQuery());
            // 加id
            router.setId(menu.getMenuId());
            // 加类型
            router.setMenuType(menu.getMenuType());
            MetaVo metaVo = new MetaVo(menu.getMenuName(), menu.getIcon(), StringUtils.equals("1", menu.getIsCache().toString()), menu.getPath());
            router.setMeta(metaVo);
            List<SysMenuDto> cMenus = menu.getChildren();
            if (StringUtils.isNotEmpty(cMenus) && UserConstants.TYPE_DIR.equals(menu.getMenuType())) {
                router.setAlwaysShow(true);
                router.setRedirect("noRedirect");
                router.setChildren(buildMenus(cMenus));
            } else if (isMenuFrame(menu)) {
                router.setMeta(null);
                List<RouterVo> childrenList = new ArrayList<>();
                RouterVo children = new RouterVo();
                children.setPath(menu.getPath());
                children.setComponent(menu.getComponent());
                children.setName(StringUtils.capitalize(menu.getPath()));
                children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), StringUtils.equals("1", menu.getIsCache().toString()), menu.getPath()));
                children.setQuery(menu.getQuery());
                childrenList.add(children);
                router.setChildren(childrenList);
            } else if (menu.getParentId().intValue() == 0 && isInnerLink(menu)) {
                router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon()));
                router.setPath("/");
                List<RouterVo> childrenList = new ArrayList<RouterVo>();
                RouterVo children = new RouterVo();
                String routerPath = innerLinkReplaceEach(menu.getPath());
                children.setPath(routerPath);
                children.setComponent(UserConstants.INNER_LINK);
                children.setName(StringUtils.capitalize(routerPath));
                children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), menu.getPath()));
                childrenList.add(children);
                router.setChildren(childrenList);
            }
            routers.add(router);
        }
        return routers;
    }

    /**
     * 构建前端列表所需要的菜单
     *
     * @param menus 菜单列表
     * @return 列表树
     */
    @Override
    public List<SysMenuDto> buildListMenus(List<SysMenuDto> menus) {
        return this.getChildPerms(menus, 0);
    }

    /**
     * 获取菜单下拉列表树
     */
    @Override
    public List<TreeSelect> getMenuTree() {
        // 1.获取所有的树节点
        List<SysMenuDto> menuList = this.listEntities(new SysMenuQuery());

        // 2.构建菜单
        List<SysMenuDto> menuTree = this.buildListMenus(menuList);

        // 3.返回数据
        return menuTree.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 获取组件信息
     *
     * @param menu 菜单信息
     * @return 组件信息
     */
    public String getComponent(SysMenuDto menu) {
        String component = UserConstants.LAYOUT;
        if (StringUtils.isNotEmpty(menu.getComponent()) && !isMenuFrame(menu)) {
            component = menu.getComponent();
        } else if (StringUtils.isEmpty(menu.getComponent()) && menu.getParentId().intValue() != 0 && isInnerLink(menu)) {
            component = UserConstants.INNER_LINK;
        } else if (StringUtils.isEmpty(menu.getComponent()) && isParentView(menu)) {
            component = UserConstants.PARENT_VIEW;
        }
        return component;
    }

    /**
     * 是否为parent_view组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isParentView(SysMenuDto menu) {
        return menu.getParentId().intValue() != 0 && UserConstants.TYPE_DIR.equals(menu.getMenuType());
    }

    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(SysMenuDto menu) {
        String routerPath = menu.getPath();
        // 内链打开外网方式
        if (menu.getParentId().intValue() != 0 && isInnerLink(menu)) {
            routerPath = innerLinkReplaceEach(routerPath);
        }
        // 非外链并且是一级目录（类型为目录）
        if (0 == menu.getParentId().intValue() && UserConstants.TYPE_DIR.equals(menu.getMenuType())
                && UserConstants.NO_FRAME.equals(menu.getIsFrame())) {
            routerPath = "/" + menu.getPath();
        }
        // 非外链并且是一级目录（类型为菜单）
        else if (isMenuFrame(menu)) {
            routerPath = "/";
        }
        return routerPath;
    }

    /**
     * 内链域名特殊字符替换
     *
     * @return 替换后的内链域名
     */
    public String innerLinkReplaceEach(String path) {
        return StringUtils.replaceEach(path, new String[]{Constants.HTTP, Constants.HTTPS, Constants.WWW, ".", ":"},
                new String[]{"", "", "", "/", "/"});
    }

    /**
     * 是否为内链组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isInnerLink(SysMenuDto menu) {
        return menu.getIsFrame().equals(UserConstants.NO_FRAME) && StringUtils.ishttp(menu.getPath());
    }

    /**
     * 获取路由名称
     *
     * @param menu 菜单信息
     * @return 路由名称
     */
    public String getRouteName(SysMenuDto menu) {
        String routerName = StringUtils.capitalize(menu.getPath());
        // 非外链并且是一级目录（类型为目录）
        if (isMenuFrame(menu)) {
            routerName = StringUtils.EMPTY;
        }
        return routerName;
    }

    /**
     * 是否为菜单内部跳转
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isMenuFrame(SysMenuDto menu) {
        return menu.getParentId().intValue() == 0 && UserConstants.TYPE_MENU.equals(menu.getMenuType())
                && menu.getIsFrame().equals(UserConstants.NO_FRAME);
    }

    /**
     * 根据父节点的ID获取所有子节点
     *
     * @param list     分类表
     * @param parentId 传入的父节点ID
     * @return String
     */
    public List<SysMenuDto> getChildPerms(List<SysMenuDto> list, int parentId) {
        List<SysMenuDto> returnList = new ArrayList<>();
        for (Iterator<SysMenuDto> iterator = list.iterator(); iterator.hasNext(); ) {
            SysMenuDto t = iterator.next();
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (t.getParentId() == parentId) {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }

    /**
     * 递归列表
     *
     * @param list 分类表
     * @param t    子节点
     */
    private void recursionFn(List<SysMenuDto> list, SysMenuDto t) {
        // 得到子节点列表
        List<SysMenuDto> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysMenuDto tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysMenuDto> getChildList(List<SysMenuDto> list, SysMenu t) {
        List<SysMenuDto> tlist = new ArrayList<>();
        Iterator<SysMenuDto> it = list.iterator();
        while (it.hasNext()) {
            SysMenuDto n = it.next();
            if (n.getParentId().longValue() == t.getMenuId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysMenuDto> list, SysMenu t) {
        return getChildList(list, t).size() > 0;
    }


}
