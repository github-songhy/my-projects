package com.earlywarning.controller;

import com.earlywarning.base.BaseController;
import com.earlywarning.common.ServerResponse;
import com.earlywarning.entity.system.Page;
import com.earlywarning.entity.system.PageData;
import com.earlywarning.service.SpecialSubjectService;
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

@Api(value = "special_subject", description = "专业管理", tags = "专业管理")
@Controller
@RequestMapping("/special_subject")
public class SpecialSubjectController extends BaseController {
    @Autowired
    private SpecialSubjectService _service;

    /**
     * 获取专业管理页面
     */
    @RequestMapping(value = "/SpecialSubject", method = RequestMethod.GET)
    @ApiOperation(value = "获取专业管理页面")
    public String goSpecialSubjectPage() {
        return "";
    }

    /**
     * 添加专业管理 (重复数据不能添加)
     */
    @RequestMapping(value = "/addSpecialSubjectNo", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "添加专业管理(重复数据不能添加)", notes = "添加不为空的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "name", value = "专业", required = false, dataType = "varchar"),
    })
    public ServerResponse<String> addSpecialSubjectNo() {
        PageData pd = getPageData();
        return _service.addSpecialSubjectNo(this.putUserPd(pd));
    }

    /**
     * 添加专业管理 (重复数据可以添加)
     */
    @RequestMapping(value = "/addSpecialSubjectAll", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "添加专业管理(重复数据可以添加)", notes = "添加不为空的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "name", value = "专业", required = false, dataType = "varchar"),
    })
    public ServerResponse<String> addSpecialSubjectAll() {
        PageData pd = getPageData();
        return _service.addSpecialSubjectAll(this.putUserPd(pd));
    }

    /**
     * 删除专业管理
     */
    @RequestMapping(value = "/deleteSpecialSubject", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "删除专业管理", notes = "删除不为空的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "xxx", required = true, dataType = "int"),
    })
    public ServerResponse<String> deleteSpecialSubject() {
        PageData pd = getPageData();
        return _service.deleteSpecialSubject(pd);
    }

    /**
     * 根据id更新数据
     */
    @RequestMapping(value = "/updateSpecialSubject", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "更新专业管理", notes = "更新不为空的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "序号", required = true, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "专业", required = false, dataType = "varchar"),
    })
    public ServerResponse<String> updateSpecialSubject() {
        PageData pd = getPageData();
        return _service.updateSpecialSubject(pd);
    }

    /**
     * 获取专业管理数据(非分页,搜索功能)
     */
    @RequestMapping(value = "/querySpecialSubjectKey", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取专业管理数据(非分页,搜索功能)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "序号", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "专业", required = false, dataType = "varchar"),
    })
    public ServerResponse<List<PageData>> querySpecialSubjectKey() {
        PageData pd = this.getPageData();
        return _service.querySpecialSubjectKey(pd);
    }

    /**
     * 获取专业管理列表数据
     */
    @RequestMapping(value = "/queryPageSpecialSubjectKeyList", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取专业管理列表数据", notes = "分页")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "showCount", value = "每页记录数", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "currentPage", value = "当前页", required = true, dataType = "String"),
    })
    public JSONObject queryPageSpecialSubjectKeyList() {
        PageData pd = getPageData();
        Page page = getPage();
        page.setPd(pd);
        List<PageData> systemUserList = null;
        try {
            systemUserList = _service.queryPageSpecialSubjectKeyList(page);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return viewReturnPageData(page, systemUserList);
    }
}
