package com.earlywarning.controller;

import com.earlywarning.base.BaseController;
import com.earlywarning.common.ServerResponse;
import com.earlywarning.entity.system.Page;
import com.earlywarning.entity.system.PageData;
import com.earlywarning.service.JurisdictionManageService;
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

@Api(value = "jurisdiction_manage", description = "权限管理", tags = "权限管理")
@Controller
@RequestMapping("/jurisdiction_manage")
public class JurisdictionManageController extends BaseController {
    @Autowired
    private JurisdictionManageService _service;

    /**
     * 获取权限管理页面
     */
    @RequestMapping(value = "/JurisdictionManage", method = RequestMethod.GET)
    @ApiOperation(value = "获取权限管理页面")
    public String goJurisdictionManagePage() {
        return "";
    }

    /**
     * 添加权限管理 (重复数据不能添加)
     */
    @RequestMapping(value = "/addJurisdictionManageNo", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "添加权限管理(重复数据不能添加)", notes = "添加不为空的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "role_manage_id", value = "角色ID", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "app_menu_tree", value = "菜单权限集合", required = false, dataType = "text"),
    })
    public ServerResponse<String> addJurisdictionManageNo() {
        PageData pd = getPageData();
        return _service.addJurisdictionManageNo(this.putUserPd(pd));
    }

    /**
     * 添加权限管理 (重复数据可以添加)
     */
    @RequestMapping(value = "/addJurisdictionManageAll", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "添加权限管理(重复数据可以添加)", notes = "添加不为空的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "role_manage_id", value = "角色ID", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "app_menu_tree", value = "菜单权限集合", required = false, dataType = "text"),
    })
    public ServerResponse<String> addJurisdictionManageAll() {
        PageData pd = getPageData();
        return _service.addJurisdictionManageAll(this.putUserPd(pd));
    }

    /**
     * 删除权限管理
     */
    @RequestMapping(value = "/deleteJurisdictionManage", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "删除权限管理", notes = "删除不为空的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "xxx", required = true, dataType = "int"),
    })
    public ServerResponse<String> deleteJurisdictionManage() {
        PageData pd = getPageData();
        return _service.deleteJurisdictionManage(pd);
    }

    /**
     * 根据id更新数据
     */
    @RequestMapping(value = "/updateJurisdictionManage", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "更新权限管理", notes = "更新不为空的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "主键ID24", required = true, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "role_manage_id", value = "角色ID", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "app_menu_tree", value = "菜单权限集合", required = false, dataType = "text"),
    })
    public ServerResponse<String> updateJurisdictionManage() {
        PageData pd = getPageData();
        return _service.updateJurisdictionManage(pd);
    }

    /**
     * 获取权限管理数据(非分页,搜索功能)
     */
    @RequestMapping(value = "/queryJurisdictionManageKey", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取权限管理数据(非分页,搜索功能)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "主键ID24", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "role_manage_id", value = "角色ID", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "app_menu_tree", value = "菜单权限集合", required = false, dataType = "text"),
    })
    public ServerResponse<List<PageData>> queryJurisdictionManageKey() {
        PageData pd = this.getPageData();
        return _service.queryJurisdictionManageKey(pd);
    }

    /**
     * 获取权限管理列表数据
     */
    @RequestMapping(value = "/queryPageJurisdictionManageKeyList", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取权限管理列表数据", notes = "分页")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "showCount", value = "每页记录数", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "currentPage", value = "当前页", required = true, dataType = "String"),
    })
    public JSONObject queryPageJurisdictionManageKeyList() {
        PageData pd = getPageData();
        Page page = getPage();
        page.setPd(pd);
        List<PageData> systemUserList = null;
        try {
            systemUserList = _service.queryPageJurisdictionManageKeyList(page);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return viewReturnPageData(page, systemUserList);
    }
}
