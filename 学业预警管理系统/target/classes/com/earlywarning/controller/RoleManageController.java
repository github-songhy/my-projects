package com.earlywarning.controller;

import com.earlywarning.base.BaseController;
import com.earlywarning.common.ServerResponse;
import com.earlywarning.entity.system.Page;
import com.earlywarning.entity.system.PageData;
import com.earlywarning.service.RoleManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Api(value = "role_manage", description = "角色管理", tags = "角色管理")
@Controller
@RequestMapping("/role_manage")
public class RoleManageController extends BaseController {
    @Autowired
    private RoleManageService _service;

    /**
     * 获取角色管理页面
     */
    @RequestMapping(value = "/RoleManage", method = RequestMethod.GET)
    @ApiOperation(value = "获取角色管理页面")
    public String goRoleManagePage() {
        return "";
    }

    /**
     * 添加角色管理 (重复数据不能添加)
     */
    @RequestMapping(value = "/addRoleManageNo", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "添加角色管理(重复数据不能添加)", notes = "添加不为空的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "role_name", value = "角色名称", required = false, dataType = "varchar"),
    })
    public ServerResponse<String> addRoleManageNo() {
        PageData pd = getPageData();
        return _service.addRoleManageNo(this.putUserPd(pd));
    }

    /**
     * 添加角色管理 (重复数据可以添加)
     */
    @RequestMapping(value = "/addRoleManageAll", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "添加角色管理(重复数据可以添加)", notes = "添加不为空的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "role_name", value = "角色名称", required = false, dataType = "varchar"),
    })
    public ServerResponse<String> addRoleManageAll() {
        PageData pd = getPageData();
        return _service.addRoleManageAll(this.putUserPd(pd));
    }

    /**
     * 删除角色管理
     */
    @RequestMapping(value = "/deleteRoleManage", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "删除角色管理", notes = "删除不为空的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "xxx", required = true, dataType = "int"),
    })
    public ServerResponse<String> deleteRoleManage() {
        PageData pd = getPageData();
        return _service.deleteRoleManage(pd);
    }

    /**
     * 根据id更新数据
     */
    @RequestMapping(value = "/updateRoleManage", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "更新角色管理", notes = "更新不为空的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "主键ID24", required = true, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "role_name", value = "角色名称", required = false, dataType = "varchar"),
    })
    public ServerResponse<String> updateRoleManage() {
        PageData pd = getPageData();
        return _service.updateRoleManage(pd);
    }

    /**
     * 获取角色管理数据(非分页,搜索功能)
     */
    @RequestMapping(value = "/queryRoleManageKey", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取角色管理数据(非分页,搜索功能)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "主键ID24", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "role_name", value = "角色名称", required = false, dataType = "varchar"),
    })
    public ServerResponse<List<PageData>> queryRoleManageKey() {
        PageData pd = this.getPageData();
        return _service.queryRoleManageKey(pd);
    }

    /**
     * 获取角色管理列表数据
     */
    @RequestMapping(value = "/queryPageRoleManageKeyList", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取角色管理列表数据", notes = "分页")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "showCount", value = "每页记录数", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "currentPage", value = "当前页", required = true, dataType = "String"),
    })
    public JSONObject queryPageRoleManageKeyList() {
        PageData pd = getPageData();
        Page page = getPage();
        page.setPd(pd);
        List<PageData> systemUserList = null;
        try {
            systemUserList = _service.queryPageRoleManageKeyList(page);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return viewReturnPageData(page, systemUserList);
    }
}
