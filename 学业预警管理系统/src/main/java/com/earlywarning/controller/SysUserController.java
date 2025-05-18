package com.earlywarning.controller;

import com.earlywarning.base.BaseController;
import com.earlywarning.common.ServerResponse;
import com.earlywarning.entity.system.Page;
import com.earlywarning.entity.system.PageData;
import com.earlywarning.service.SysUserService;
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

@Api(value = "sys_user", description = "用户管理", tags = "用户管理")
@Controller
@RequestMapping("/sys_user")
public class SysUserController extends BaseController {
    @Autowired
    private SysUserService _service;

    /**
     * 获取用户管理页面
     */
    @RequestMapping(value = "/SysUser", method = RequestMethod.GET)
    @ApiOperation(value = "获取用户管理页面")
    public String goSysUserPage() {
        return "";
    }

    /**
     * 添加用户管理 (重复数据不能添加)
     */
    @RequestMapping(value = "/addSysUserNo", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "添加用户管理(重复数据不能添加)", notes = "添加不为空的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "USERNAME", value = "用户名", required = true, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "PASSWORD", value = "密码", required = true, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "NAME", value = "姓名", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "jurisdiction_manage_id", value = "角色", required = true, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "update_time", value = "更新时间4", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "signature", value = "备注", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "sex", value = "性别", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "phone", value = "联系电话", required = false, dataType = "varchar"),
    })
    public ServerResponse<String> addSysUserNo() {
        PageData pd = getPageData();
        return _service.addSysUserNo(this.putUserPd(pd));
    }

    /**
     * 添加用户管理 (重复数据可以添加)
     */
    @RequestMapping(value = "/addSysUserAll", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "添加用户管理(重复数据可以添加)", notes = "添加不为空的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "USERNAME", value = "用户名", required = true, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "PASSWORD", value = "密码", required = true, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "NAME", value = "姓名", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "jurisdiction_manage_id", value = "角色", required = true, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "update_time", value = "更新时间4", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "signature", value = "备注", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "sex", value = "性别", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "phone", value = "联系电话", required = false, dataType = "varchar"),
    })
    public ServerResponse<String> addSysUserAll() {
        PageData pd = getPageData();
        return _service.addSysUserAll(this.putUserPd(pd));
    }

    /**
     * 删除用户管理
     */
    @RequestMapping(value = "/deleteSysUser", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "删除用户管理", notes = "删除不为空的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "xxx", required = true, dataType = "int"),
    })
    public ServerResponse<String> deleteSysUser() {
        PageData pd = getPageData();
        return _service.deleteSysUser(pd);
    }

    /**
     * 根据id更新数据
     */
    @RequestMapping(value = "/updateSysUser", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "更新用户管理", notes = "更新不为空的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "主键24", required = true, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "USERNAME", value = "用户名", required = true, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "PASSWORD", value = "密码", required = true, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "NAME", value = "姓名", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "jurisdiction_manage_id", value = "角色", required = true, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "signature", value = "备注", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "sex", value = "性别", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "phone", value = "联系电话", required = false, dataType = "varchar"),
    })
    public ServerResponse<String> updateSysUser() {
        PageData pd = getPageData();
        return _service.updateSysUser(pd);
    }

    /**
     * 获取用户管理数据(非分页,搜索功能)
     */
    @RequestMapping(value = "/querySysUserKey", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取用户管理数据(非分页,搜索功能)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "主键24", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "USERNAME", value = "用户名", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "PASSWORD", value = "密码", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "NAME", value = "姓名", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "jurisdiction_manage_id", value = "角色", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "create_time", value = "建立时间4", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "update_time", value = "更新时间4", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "signature", value = "备注", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "sex", value = "性别", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "phone", value = "联系电话", required = false, dataType = "varchar"),
    })
    public ServerResponse<List<PageData>> querySysUserKey() {
        PageData pd = this.getPageData();
        return _service.querySysUserKey(pd);
    }

    /**
     * 获取用户管理列表数据
     */
    @RequestMapping(value = "/queryPageSysUserKeyList", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取用户管理列表数据", notes = "分页")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "showCount", value = "每页记录数", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "currentPage", value = "当前页", required = true, dataType = "String"),
    })
    public JSONObject queryPageSysUserKeyList() {
        PageData pd = getPageData();
        Page page = getPage();
        page.setPd(pd);
        List<PageData> systemUserList = null;
        try {
            systemUserList = _service.queryPageSysUserKeyList(page);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return viewReturnPageData(page, systemUserList);
    }
}
