package com.earlywarning.controller;

import com.earlywarning.base.BaseController;
import com.earlywarning.common.ServerResponse;
import com.earlywarning.entity.system.Page;
import com.earlywarning.entity.system.PageData;
import com.earlywarning.service.SessionManageService;
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

@Api(value = "session_manage", description = "学年管理", tags = "学年管理")
@Controller
@RequestMapping("/session_manage")
public class SessionManageController extends BaseController {
    @Autowired
    private SessionManageService _service;

    /**
     * 获取学年管理页面
     */
    @RequestMapping(value = "/SessionManage", method = RequestMethod.GET)
    @ApiOperation(value = "获取学年管理页面")
    public String goSessionManagePage() {
        return "";
    }

    /**
     * 添加学年管理 (重复数据不能添加)
     */
    @RequestMapping(value = "/addSessionManageNo", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "添加学年管理(重复数据不能添加)", notes = "添加不为空的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "name", value = "学年", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "order_number", value = "顺序", required = false, dataType = "int"),
    })
    public ServerResponse<String> addSessionManageNo() {
        PageData pd = getPageData();
        return _service.addSessionManageNo(this.putUserPd(pd));
    }

    /**
     * 添加学年管理 (重复数据可以添加)
     */
    @RequestMapping(value = "/addSessionManageAll", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "添加学年管理(重复数据可以添加)", notes = "添加不为空的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "name", value = "学年", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "order_number", value = "顺序", required = false, dataType = "int"),
    })
    public ServerResponse<String> addSessionManageAll() {
        PageData pd = getPageData();
        return _service.addSessionManageAll(this.putUserPd(pd));
    }

    /**
     * 删除学年管理
     */
    @RequestMapping(value = "/deleteSessionManage", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "删除学年管理", notes = "删除不为空的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "xxx", required = true, dataType = "int"),
    })
    public ServerResponse<String> deleteSessionManage() {
        PageData pd = getPageData();
        return _service.deleteSessionManage(pd);
    }

    /**
     * 根据id更新数据
     */
    @RequestMapping(value = "/updateSessionManage", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "更新学年管理", notes = "更新不为空的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "序号", required = true, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "学年", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "order_number", value = "顺序", required = false, dataType = "int"),
    })
    public ServerResponse<String> updateSessionManage() {
        PageData pd = getPageData();
        return _service.updateSessionManage(pd);
    }

    /**
     * 获取学年管理数据(非分页,搜索功能)
     */
    @RequestMapping(value = "/querySessionManageKey", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取学年管理数据(非分页,搜索功能)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "序号", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "学年", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "order_number", value = "顺序", required = false, dataType = "int"),
    })
    public ServerResponse<List<PageData>> querySessionManageKey() {
        PageData pd = this.getPageData();
        return _service.querySessionManageKey(pd);
    }

    /**
     * 获取学年管理列表数据
     */
    @RequestMapping(value = "/queryPageSessionManageKeyList", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取学年管理列表数据", notes = "分页")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "showCount", value = "每页记录数", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "currentPage", value = "当前页", required = true, dataType = "String"),
    })
    public JSONObject queryPageSessionManageKeyList() {
        PageData pd = getPageData();
        Page page = getPage();
        page.setPd(pd);
        List<PageData> systemUserList = null;
        try {
            systemUserList = _service.queryPageSessionManageKeyList(page);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return viewReturnPageData(page, systemUserList);
    }
}
