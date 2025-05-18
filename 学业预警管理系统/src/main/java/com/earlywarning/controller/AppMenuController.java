package com.earlywarning.controller;

import com.earlywarning.base.BaseController;
import com.earlywarning.common.ServerResponse;
import com.earlywarning.entity.system.Page;
import com.earlywarning.entity.system.PageData;
import com.earlywarning.service.AppMenuService;
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

@Api(value = "app_menu", description = "菜单管理", tags = "菜单管理")
@Controller
@RequestMapping("/app_menu")
public class AppMenuController extends BaseController {
    @Autowired
    private AppMenuService _service;

    /**
     * 获取菜单管理页面
     */
    @RequestMapping(value = "/AppMenu", method = RequestMethod.GET)
    @ApiOperation(value = "获取菜单管理页面")
    public String goAppMenuPage() {
        return "";
    }

    /**
     * 添加菜单管理 (重复数据不能添加)
     */
    @RequestMapping(value = "/addAppMenuNo", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "添加菜单管理(重复数据不能添加)", notes = "添加不为空的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "name", value = "菜单名称", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "href", value = "路由地址", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "update_time", value = "更新时间4", required = false, dataType = "varchar"),
    })
    public ServerResponse<String> addAppMenuNo() {
        PageData pd = getPageData();
        return _service.addAppMenuNo(this.putUserPd(pd));
    }

    /**
     * 添加菜单管理 (重复数据可以添加)
     */
    @RequestMapping(value = "/addAppMenuAll", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "添加菜单管理(重复数据可以添加)", notes = "添加不为空的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "name", value = "菜单名称", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "href", value = "路由地址", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "update_time", value = "更新时间4", required = false, dataType = "varchar"),
    })
    public ServerResponse<String> addAppMenuAll() {
        PageData pd = getPageData();
        return _service.addAppMenuAll(this.putUserPd(pd));
    }

    /**
     * 删除菜单管理
     */
    @RequestMapping(value = "/deleteAppMenu", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "删除菜单管理", notes = "删除不为空的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "xxx", required = true, dataType = "int"),
    })
    public ServerResponse<String> deleteAppMenu() {
        PageData pd = getPageData();
        return _service.deleteAppMenu(pd);
    }

    /**
     * 根据id更新数据
     */
    @RequestMapping(value = "/updateAppMenu", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "更新菜单管理", notes = "更新不为空的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "主键id24", required = true, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "菜单名称", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "href", value = "路由地址", required = false, dataType = "varchar"),
    })
    public ServerResponse<String> updateAppMenu() {
        PageData pd = getPageData();
        return _service.updateAppMenu(pd);
    }

    /**
     * 获取菜单管理数据(非分页,搜索功能)
     */
    @RequestMapping(value = "/queryAppMenuKey", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取菜单管理数据(非分页,搜索功能)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "主键id24", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "菜单名称", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "href", value = "路由地址", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "create_time", value = "创建时间4", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "update_time", value = "更新时间4", required = false, dataType = "varchar"),
    })
    public ServerResponse<List<PageData>> queryAppMenuKey() {
        PageData pd = this.getPageData();
        return _service.queryAppMenuKey(pd);
    }

    /**
     * 获取菜单管理列表数据
     */
    @RequestMapping(value = "/queryPageAppMenuKeyList", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取菜单管理列表数据", notes = "分页")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "showCount", value = "每页记录数", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "currentPage", value = "当前页", required = true, dataType = "String"),
    })
    public JSONObject queryPageAppMenuKeyList() {
        PageData pd = getPageData();
        Page page = getPage();
        page.setPd(pd);
        List<PageData> systemUserList = null;
        try {
            systemUserList = _service.queryPageAppMenuKeyList(page);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return viewReturnPageData(page, systemUserList);
    }
}
