package com.earlywarning.controller;

import com.earlywarning.base.BaseController;
import com.earlywarning.common.ServerResponse;
import com.earlywarning.entity.system.Page;
import com.earlywarning.entity.system.PageData;
import com.earlywarning.service.CourseManageService;
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

@Api(value = "course_manage", description = "课程管理", tags = "课程管理")
@Controller
@RequestMapping("/course_manage")
public class CourseManageController extends BaseController {
    @Autowired
    private CourseManageService _service;

    /**
     * 获取课程管理页面
     */
    @RequestMapping(value = "/CourseManage", method = RequestMethod.GET)
    @ApiOperation(value = "获取课程管理页面")
    public String goCourseManagePage() {
        return "";
    }


    /**
     * 添加课程管理 (重复数据不能添加)
     */
    @RequestMapping(value = "/addCourseManageNo", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "添加课程管理(重复数据不能添加)", notes = "添加不为空的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "name", value = "课程名称", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "credit", value = "学分", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "grade_point", value = "绩点", required = false, dataType = "double"),
            @ApiImplicitParam(paramType = "query", name = "content", value = "课程简介", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "course_code", value = "课程代码", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "planned_term", value = "计划学期", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "commencement_term", value = "开课学期", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "course_type", value = "课程性质", required = false, dataType = "varchar"),
    })
    public ServerResponse<String> addCourseManageNo() {
        PageData pd = getPageData();
        return _service.addCourseManageNo(this.putUserPd(pd));
    }

    /**
     * 添加课程管理 (重复数据可以添加)
     */
    @RequestMapping(value = "/addCourseManageAll", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "添加课程管理(重复数据可以添加)", notes = "添加不为空的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "name", value = "课程名称", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "credit", value = "学分", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "grade_point", value = "绩点", required = false, dataType = "double"),
            @ApiImplicitParam(paramType = "query", name = "content", value = "课程简介", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "course_code", value = "课程代码", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "planned_term", value = "计划学期", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "commencement_term", value = "开课学期", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "course_type", value = "课程性质", required = false, dataType = "varchar"),
    })
    public ServerResponse<String> addCourseManageAll() {
        PageData pd = getPageData();
        return _service.addCourseManageAll(this.putUserPd(pd));
    }

    /**
     * 删除课程管理
     */
    @RequestMapping(value = "/deleteCourseManage", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "删除课程管理", notes = "删除不为空的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "xxx", required = true, dataType = "int"),
    })
    public ServerResponse<String> deleteCourseManage() {
        PageData pd = getPageData();
        return _service.deleteCourseManage(pd);
    }

    /**
     * 根据id更新数据
     */
    @RequestMapping(value = "/updateCourseManage", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "更新课程管理", notes = "更新不为空的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "序号", required = true, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "课程名称", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "credit", value = "学分", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "grade_point", value = "绩点", required = false, dataType = "double"),
            @ApiImplicitParam(paramType = "query", name = "content", value = "课程简介", required = false, dataType = "varchar"),
    })
    public ServerResponse<String> updateCourseManage() {
        PageData pd = getPageData();
        return _service.updateCourseManage(pd);
    }

    /**
     * 获取课程管理数据(非分页,搜索功能)
     */
    @RequestMapping(value = "/queryCourseManageKey", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取课程管理数据(非分页,搜索功能)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "序号", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "课程名称", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "credit", value = "学分", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "grade_point", value = "绩点", required = false, dataType = "double"),
            @ApiImplicitParam(paramType = "query", name = "content", value = "课程简介", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "create_time", value = "创建时间", required = false, dataType = "varchar"),
    })
    public ServerResponse<List<PageData>> queryCourseManageKey() {
        PageData pd = this.getPageData();
        return _service.queryCourseManageKey(pd);
    }

    /**
     * 获取课程管理列表数据
     */
    @RequestMapping(value = "/queryPageCourseManageKeyList", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取课程管理列表数据", notes = "分页")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "showCount", value = "每页记录数", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "currentPage", value = "当前页", required = true, dataType = "String"),
    })
    public JSONObject queryPageCourseManageKeyList() {
        PageData pd = getPageData();
        Page page = getPage();
        page.setPd(pd);
        List<PageData> systemUserList = null;
        try {
            systemUserList = _service.queryPageCourseManageKeyList(page);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return viewReturnPageData(page, systemUserList);
    }
}
