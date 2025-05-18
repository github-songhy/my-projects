package com.earlywarning.controller;

import com.earlywarning.base.BaseController;
import com.earlywarning.common.Chars;
import com.earlywarning.common.ServerResponse;
import com.earlywarning.entity.system.Page;
import com.earlywarning.entity.system.PageData;
import com.earlywarning.service.ViolationTypeService;
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

@Api(value = "violation_type", description = "违纪处分类型", tags = "违纪处分类型")
@Controller
@RequestMapping("/violation_type")
public class ViolationTypeController extends BaseController {
    @Autowired
    private ViolationTypeService _service;

    /**
     * 获取违纪处分类型页面
     */
    @RequestMapping(value = "/ViolationType", method = RequestMethod.GET)
    @ApiOperation(value = "获取违纪处分类型页面")
    public String goViolationTypePage() {
        return "";
    }

    /**
     * 添加违纪处分类型 (重复数据不能添加)
     */
    @RequestMapping(value = "/addViolationTypeNo", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "添加违纪处分类型(重复数据不能添加)", notes = "添加不为空的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "name", value = "类型", required = false, dataType = "varchar"),
    })
    public ServerResponse<String> addViolationTypeNo() {
        PageData pd = getPageData();
        return _service.addViolationTypeNo(this.putUserPd(pd));
    }

    /**
     * 添加违纪处分类型 (重复数据可以添加)
     */
    @RequestMapping(value = "/addViolationTypeAll", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "添加违纪处分类型(重复数据可以添加)", notes = "添加不为空的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "name", value = "类型", required = false, dataType = "varchar"),
    })
    public ServerResponse<String> addViolationTypeAll() {
        PageData pd = getPageData();
        return _service.addViolationTypeAll(this.putUserPd(pd));
    }

    /**
     * 删除违纪处分类型
     */
    @RequestMapping(value = "/deleteViolationType", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "删除违纪处分类型", notes = "删除不为空的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "xxx", required = true, dataType = "int"),
    })
    public ServerResponse<String> deleteViolationType() {
        PageData pd = getPageData();
        return _service.deleteViolationType(pd);
    }

    /**
     * 根据id更新数据
     */
    @RequestMapping(value = "/updateViolationType", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "更新违纪处分类型", notes = "更新不为空的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "序号", required = true, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "类型", required = false, dataType = "varchar"),
    })
    public ServerResponse<String> updateViolationType() {
        PageData pd = getPageData();
        return _service.updateViolationType(pd);
    }

    /**
     * 获取违纪处分类型数据(非分页,搜索功能)
     */
    @RequestMapping(value = "/queryViolationTypeKey", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取违纪处分类型数据(非分页,搜索功能)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "序号", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "类型", required = false, dataType = "varchar"),
    })
    public ServerResponse<List<PageData>> queryViolationTypeKey() {
        PageData pd = this.getPageData();
        return _service.queryViolationTypeKey(pd);
    }

    /**
     * 获取违纪处分类型列表数据
     */
    @RequestMapping(value = "/queryPageViolationTypeKeyList", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取违纪处分类型列表数据", notes = "分页")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "showCount", value = "每页记录数", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "currentPage", value = "当前页", required = true, dataType = "String"),
    })
    public JSONObject queryPageViolationTypeKeyList() {
        PageData pd = getPageData();
        Page page = getPage();
        page.setPd(pd);
        List<PageData> systemUserList = null;
        try {
            systemUserList = _service.queryPageViolationTypeKeyList(page);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return viewReturnPageData(page, systemUserList);
    }


    /**
     * 获取违纪处分类型数据(非分页,搜索功能)
     */
    @RequestMapping(value = "/queryViolationPie", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取违纪处分类型数据(饼状图,搜索功能)")
    public ServerResponse<List<Chars>> queryViolationPie() {

        return ServerResponse.createBySuccess(_service.queryViolationPie());
    }
}
