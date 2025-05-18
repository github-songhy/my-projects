package com.earlywarning.controller;

import com.earlywarning.base.BaseController;
import com.earlywarning.common.ServerResponse;
import com.earlywarning.entity.system.Page;
import com.earlywarning.entity.system.PageData;
import com.earlywarning.service.StuViolationService;
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

@Api(value = "stu_violation", description = "违纪处分管理", tags = "违纪处分管理")
@Controller
@RequestMapping("/stu_violation")
public class StuViolationController extends BaseController {
    @Autowired
    private StuViolationService _service;

    /**
     * 获取违纪处分管理页面
     */
    @RequestMapping(value = "/StuViolation", method = RequestMethod.GET)
    @ApiOperation(value = "获取违纪处分管理页面")
    public String goStuViolationPage() {
        return "";
    }

    /**
     * 添加违纪处分管理 (重复数据不能添加)
     */
    @RequestMapping(value = "/addStuViolationNo", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "添加违纪处分管理(重复数据不能添加)", notes = "添加不为空的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "violation_type_id", value = "违纪类型", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "content", value = "处分结果", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "app_user_id", value = "学生", required = false, dataType = "int"),
    })
    public ServerResponse<String> addStuViolationNo() {
        PageData pd = getPageData();
        return _service.addStuViolationNo(this.putUserPd(pd));
    }

    /**
     * 添加违纪处分管理 (重复数据可以添加)
     */
    @RequestMapping(value = "/addStuViolationAll", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "添加违纪处分管理(重复数据可以添加)", notes = "添加不为空的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "violation_type_id", value = "违纪类型", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "content", value = "处分结果", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "app_user_id", value = "学生", required = false, dataType = "int"),
    })
    public ServerResponse<String> addStuViolationAll() {
        PageData pd = getPageData();
        return _service.addStuViolationAll(this.putUserPd(pd));
    }

    /**
     * 删除违纪处分管理
     */
    @RequestMapping(value = "/deleteStuViolation", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "删除违纪处分管理", notes = "删除不为空的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "xxx", required = true, dataType = "int"),
    })
    public ServerResponse<String> deleteStuViolation() {
        PageData pd = getPageData();
        return _service.deleteStuViolation(pd);
    }

    /**
     * 根据id更新数据
     */
    @RequestMapping(value = "/updateStuViolation", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "更新违纪处分管理", notes = "更新不为空的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "序号", required = true, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "violation_type_id", value = "违纪类型", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "content", value = "处分结果", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "app_user_id", value = "学生", required = false, dataType = "int"),
    })
    public ServerResponse<String> updateStuViolation() {
        PageData pd = getPageData();
        return _service.updateStuViolation(pd);
    }

    /**
     * 获取违纪处分管理数据(非分页,搜索功能)
     */
    @RequestMapping(value = "/queryStuViolationKey", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取违纪处分管理数据(非分页,搜索功能)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "序号", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "violation_type_id", value = "违纪类型", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "content", value = "处分结果", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "app_user_id", value = "学生", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "create_time", value = "创建时间", required = false, dataType = "varchar"),
    })
    public ServerResponse<List<PageData>> queryStuViolationKey() {
        PageData pd = this.getPageData();
        return _service.queryStuViolationKey(pd);
    }

    /**
     * 获取违纪处分管理列表数据
     */
    @RequestMapping(value = "/queryPageStuViolationKeyList", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取违纪处分管理列表数据", notes = "分页")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "showCount", value = "每页记录数", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "currentPage", value = "当前页", required = true, dataType = "String"),
    })
    public JSONObject queryPageStuViolationKeyList() {
        PageData pd = getPageData();
        Page page = getPage();
        page.setPd(pd);
        List<PageData> systemUserList = null;
        try {
            systemUserList = _service.queryPageStuViolationKeyList(page);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return viewReturnPageData(page, systemUserList);
    }
}
