package com.earlywarning.controller;

import com.earlywarning.base.BaseController;
import com.earlywarning.common.Chars;
import com.earlywarning.common.ServerResponse;
import com.earlywarning.entity.system.Page;
import com.earlywarning.entity.system.PageData;
import com.earlywarning.service.ResultManageService;
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

import java.util.ArrayList;
import java.util.List;

@Api(value = "result_manage", description = "成绩管理", tags = "成绩管理")
@Controller
@RequestMapping("/result_manage")
public class ResultManageController extends BaseController {
    @Autowired
    private ResultManageService _service;

    /**
     * 获取成绩管理页面
     */
    @RequestMapping(value = "/ResultManage", method = RequestMethod.GET)
    @ApiOperation(value = "获取成绩管理页面")
    public String goResultManagePage() {
        return "";
    }

    /**
     * 添加成绩管理 (重复数据不能添加)
     */
    @RequestMapping(value = "/addResultManageNo", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "添加成绩管理(重复数据不能添加)", notes = "添加不为空的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "session_manage_id", value = "学年", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "course_manage_id", value = "课程", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "score", value = "成绩", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "grade_point", value = "获得绩点", required = false, dataType = "double"),
            @ApiImplicitParam(paramType = "query", name = "a_type", value = "类型（核心/必修/选修）", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "app_user_id", value = "学生", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "class_manage_id", value = "班级", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "special_subject_id", value = "专业", required = false, dataType = "int"),
    })
    public ServerResponse<String> addResultManageNo() {
        PageData pd = getPageData();
        return _service.addResultManageNo(this.putUserPd(pd));
    }

    /**
     * 添加成绩管理 (重复数据可以添加)
     */
    @RequestMapping(value = "/addResultManageAll", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "添加成绩管理(重复数据可以添加)", notes = "添加不为空的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "session_manage_id", value = "学年", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "course_manage_id", value = "课程", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "score", value = "成绩", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "grade_point", value = "获得绩点", required = false, dataType = "double"),
            @ApiImplicitParam(paramType = "query", name = "a_type", value = "类型（核心/必修/选修）", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "app_user_id", value = "学生", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "class_manage_id", value = "班级", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "special_subject_id", value = "专业", required = false, dataType = "int"),
    })
    public ServerResponse<String> addResultManageAll() {
        PageData pd = getPageData();
        return _service.addResultManageAll(this.putUserPd(pd));
    }

    /**
     * 删除成绩管理
     */
    @RequestMapping(value = "/deleteResultManage", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "删除成绩管理", notes = "删除不为空的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "xxx", required = true, dataType = "int"),
    })
    public ServerResponse<String> deleteResultManage() {
        PageData pd = getPageData();
        return _service.deleteResultManage(pd);
    }

    /**
     * 根据id更新数据
     */
    @RequestMapping(value = "/updateResultManage", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "更新成绩管理", notes = "更新不为空的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "序号", required = true, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "session_manage_id", value = "学年", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "course_manage_id", value = "课程", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "score", value = "成绩", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "grade_point", value = "获得绩点", required = false, dataType = "double"),
            @ApiImplicitParam(paramType = "query", name = "a_type", value = "类型（核心/必修/选修）", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "app_user_id", value = "学生", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "class_manage_id", value = "班级", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "special_subject_id", value = "专业", required = false, dataType = "int"),
    })
    public ServerResponse<String> updateResultManage() {
        PageData pd = getPageData();
        return _service.updateResultManage(pd);
    }

    /**
     * 获取成绩管理数据(非分页,搜索功能)
     */
    @RequestMapping(value = "/queryResultManageKey", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取成绩管理数据(非分页,搜索功能)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "序号", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "session_manage_id", value = "学年", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "course_manage_id", value = "课程", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "score", value = "成绩", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "grade_point", value = "获得绩点", required = false, dataType = "double"),
            @ApiImplicitParam(paramType = "query", name = "a_type", value = "类型（核心/必修/选修）", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "app_user_id", value = "学生", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "class_manage_id", value = "班级", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "special_subject_id", value = "专业", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "create_time", value = "创建时间", required = false, dataType = "varchar"),
    })
    public ServerResponse<List<PageData>> queryResultManageKey() {
        PageData pd = this.getPageData();
        return _service.queryResultManageKey(pd);
    }

    /**
     * 获取成绩管理列表数据
     */
    @RequestMapping(value = "/queryPageResultManageKeyList", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取成绩管理列表数据", notes = "分页")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "showCount", value = "每页记录数", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "currentPage", value = "当前页", required = true, dataType = "String"),
    })
    public JSONObject queryPageResultManageKeyList() {
        PageData pd = getPageData();
        Page page = getPage();
        page.setPd(pd);
        List<PageData> systemUserList = null;
        try {
            systemUserList = _service.queryPageResultManageKeyList(page);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return viewReturnPageData(page, systemUserList);
    }

    /**
     * 获取成绩管理列表数据
     */
    @RequestMapping(value = "/queryResultManageBySessionManageId", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取成绩管理列表数据根据年份", notes = "分页")
    /** 根据学年获取成绩列表 */
    public ServerResponse<List<Chars>> queryResultManageBySessionManageId(String session_manage_id) {
        List<PageData> list = _service.queryResultManageBySessionManageId(session_manage_id);
        Integer total = list.size();

        Integer state1 = 0;
        Integer state2 = 0;
        Integer state3 = 0;
        Integer state4 = 0;

        if (total > 0) {
            for (PageData pageData : list) {
                Integer early_warning = Integer.valueOf(String.valueOf(pageData.get("early_warning")));
                if (early_warning != null) {
                    if (early_warning == 1) {
                        state1 += 1;
                    } else if (early_warning == 2) {
                        state2 += 1;
                    } else if (early_warning == 3) {
                        state3 += 1;
                    } else if (early_warning == 4) {
                        state4 += 1;
                    } else {
                        state1 += 1;
                    }
                }
            }
        }

        Chars chars1 = new Chars("正常", String.valueOf(state1));
        Chars chars2 = new Chars("一级", String.valueOf(state2));
        Chars chars3 = new Chars("二级", String.valueOf(state3));
        Chars chars4 = new Chars("三级", String.valueOf(state4));

        List<Chars> charsList = new ArrayList<>();
        charsList.add(chars1);
        charsList.add(chars2);
        charsList.add(chars3);
        charsList.add(chars4);
        return ServerResponse.createBySuccess(charsList);
    }
}
