package com.earlywarning.controller;

import com.earlywarning.base.BaseController;
import com.earlywarning.common.ServerResponse;
import com.earlywarning.entity.system.Page;
import com.earlywarning.entity.system.PageData;
import com.earlywarning.service.ClassManageService;
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

@Api(value = "class_manage", description = "班级管理", tags = "班级管理")
@Controller
@RequestMapping("/class_manage")
public class ClassManageController extends BaseController {
    @Autowired
    private ClassManageService _service;

    /**
     * 获取班级管理页面
     */
    @RequestMapping(value = "/ClassManage", method = RequestMethod.GET)
    @ApiOperation(value = "获取班级管理页面")
    public String goClassManagePage() {
        return "";
    }

    /**
     * 添加班级管理 (重复数据不能添加)
     */
    @RequestMapping(value = "/addClassManageNo", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "添加班级管理(重复数据不能添加)", notes = "添加不为空的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "name", value = "班级名称", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "is_graduate", value = "是否毕业班", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "special_subject_id", value = "所属专业", required = false, dataType = "int"),
    })
    public ServerResponse<String> addClassManageNo() {
        PageData pd = getPageData();
        return _service.addClassManageNo(this.putUserPd(pd));
    }

    /**
     * 添加班级管理 (重复数据可以添加)
     */
    @RequestMapping(value = "/addClassManageAll", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "添加班级管理(重复数据可以添加)", notes = "添加不为空的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "name", value = "班级名称", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "is_graduate", value = "是否毕业班", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "special_subject_id", value = "所属专业", required = false, dataType = "int"),
    })
    public ServerResponse<String> addClassManageAll() {
        PageData pd = getPageData();
        return _service.addClassManageAll(this.putUserPd(pd));
    }

    /**
     * 删除班级管理
     */
    @RequestMapping(value = "/deleteClassManage", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "删除班级管理", notes = "删除不为空的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "xxx", required = true, dataType = "int"),
    })
    public ServerResponse<String> deleteClassManage() {
        PageData pd = getPageData();
        return _service.deleteClassManage(pd);
    }

    /**
     * 根据id更新数据
     */
    @RequestMapping(value = "/updateClassManage", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "更新班级管理", notes = "更新不为空的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "序号", required = true, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "班级名称", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "is_graduate", value = "是否毕业班", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "special_subject_id", value = "所属专业", required = false, dataType = "int"),
    })
    public ServerResponse<String> updateClassManage() {
        PageData pd = getPageData();
        return _service.updateClassManage(pd);
    }

    /**
     * 获取班级管理数据(非分页,搜索功能)
     */
    @RequestMapping(value = "/queryClassManageKey", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取班级管理数据(非分页,搜索功能)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "序号", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "班级名称", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "is_graduate", value = "是否毕业班", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "special_subject_id", value = "所属专业", required = false, dataType = "int"),
    })
    public ServerResponse<List<PageData>> queryClassManageKey() {
        PageData pd = this.getPageData();
        return _service.queryClassManageKey(pd);
    }

    /**
     * 获取班级管理列表数据
     */
    @RequestMapping(value = "/queryPageClassManageKeyList", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取班级管理列表数据", notes = "分页")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "showCount", value = "每页记录数", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "currentPage", value = "当前页", required = true, dataType = "String"),
    })
    public JSONObject queryPageClassManageKeyList() {
        PageData pd = getPageData();
        Page page = getPage();
        page.setPd(pd);
        List<PageData> systemUserList = null;
        try {
            systemUserList = _service.queryPageClassManageKeyList(page);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return viewReturnPageData(page, systemUserList);
    }
}
