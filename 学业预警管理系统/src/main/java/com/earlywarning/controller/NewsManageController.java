package com.earlywarning.controller;

import com.earlywarning.base.BaseController;
import com.earlywarning.common.ServerResponse;
import com.earlywarning.entity.system.Page;
import com.earlywarning.entity.system.PageData;
import com.earlywarning.service.NewsManageService;
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

@Api(value = "news_manage", description = "公告管理", tags = "公告管理")
@Controller
@RequestMapping("/news_manage")
public class NewsManageController extends BaseController {
    @Autowired
    private NewsManageService _service;

    /**
     * 获取公告管理页面
     */
    @RequestMapping(value = "/NewsManage", method = RequestMethod.GET)
    @ApiOperation(value = "获取公告管理页面")
    public String goNewsManagePage() {
        return "";
    }

    /**
     * 添加公告管理 (重复数据不能添加)
     */
    @RequestMapping(value = "/addNewsManageNo", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "添加公告管理(重复数据不能添加)", notes = "添加不为空的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "title", value = "标题", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "content_details", value = "内容", required = false, dataType = "longtext"),
    })
    public ServerResponse<String> addNewsManageNo() {
        PageData pd = getPageData();
        return _service.addNewsManageNo(this.putUserPd(pd));
    }

    /**
     * 添加公告管理 (重复数据可以添加)
     */
    @RequestMapping(value = "/addNewsManageAll", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "添加公告管理(重复数据可以添加)", notes = "添加不为空的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "title", value = "标题", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "content_details", value = "内容", required = false, dataType = "longtext"),
    })
    public ServerResponse<String> addNewsManageAll() {
        PageData pd = getPageData();
        return _service.addNewsManageAll(this.putUserPd(pd));
    }

    /**
     * 删除公告管理
     */
    @RequestMapping(value = "/deleteNewsManage", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "删除公告管理", notes = "删除不为空的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "xxx", required = true, dataType = "int"),
    })
    public ServerResponse<String> deleteNewsManage() {
        PageData pd = getPageData();
        return _service.deleteNewsManage(pd);
    }

    /**
     * 根据id更新数据
     */
    @RequestMapping(value = "/updateNewsManage", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "更新公告管理", notes = "更新不为空的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "序号", required = true, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "title", value = "标题", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "content_details", value = "内容", required = false, dataType = "longtext"),
    })
    public ServerResponse<String> updateNewsManage() {
        PageData pd = getPageData();
        return _service.updateNewsManage(pd);
    }

    /**
     * 获取公告管理数据(非分页,搜索功能)
     */
    @RequestMapping(value = "/queryNewsManageKey", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取公告管理数据(非分页,搜索功能)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "序号", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "title", value = "标题", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "content_details", value = "内容", required = false, dataType = "longtext"),
            @ApiImplicitParam(paramType = "query", name = "create_time", value = "创建时间", required = false, dataType = "varchar"),
    })
    public ServerResponse<List<PageData>> queryNewsManageKey() {
        PageData pd = this.getPageData();
        return _service.queryNewsManageKey(pd);
    }

    /**
     * 获取公告管理列表数据
     */
    @RequestMapping(value = "/queryPageNewsManageKeyList", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取公告管理列表数据", notes = "分页")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "showCount", value = "每页记录数", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "currentPage", value = "当前页", required = true, dataType = "String"),
    })
    public JSONObject queryPageNewsManageKeyList() {
        PageData pd = getPageData();
        Page page = getPage();
        page.setPd(pd);
        List<PageData> systemUserList = null;
        try {
            systemUserList = _service.queryPageNewsManageKeyList(page);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return viewReturnPageData(page, systemUserList);
    }
}
