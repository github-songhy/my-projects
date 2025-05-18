package com.earlywarning.controller.user;

import com.earlywarning.base.BaseController;
import com.earlywarning.common.ServerResponse;
import com.earlywarning.entity.system.Page;
import com.earlywarning.entity.system.PageData;
import com.earlywarning.service.user.SystemUserService;
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

//import com.earlywarning.core.TokenLimit;

/**
 * 系统用户
 */
@Api(value = "sys_user1", description = "系统用户", tags = "系统用户")
@Controller
@RequestMapping("/sys_user1")
public class SystemUserController extends BaseController {

    @Autowired
    private SystemUserService systemUserService;

    @RequestMapping(value = "/goSystemUserPage", method = RequestMethod.GET)
    @ApiOperation(value = "跳转到系统用户页面")
    public String goSystemUserPage() {
        return "";
    }

    /**
     * 获取系统用户列表数据
     *
     * @return
     */
    @RequestMapping(value = "/queryPageSysUserKeyList", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取系统用户列表数据", notes = "分页")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "showCount", value = "每页记录数", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "currentPage", value = "当前页", required = true, dataType = "String"),
    })
    public JSONObject getPageSystemUserList() {
        PageData pd = this.getPageData();
        Page page = this.getPage();
        page.setPd(pd);
        List<PageData> systemUserList = null;
        try {
            systemUserList = systemUserService.getPageSystemUserList(page);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return viewReturnPageData(page, systemUserList);
    }

    /**
     * 添加系统用户
     *
     * @return
     */
    @RequestMapping(value = "/addSysUserNo", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "添加系统用户", notes = "添加不为空的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "USERNAME", value = "用户名", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "PASSWORD", value = "密码", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "NAME", value = "昵称", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "jurisdiction_manage_id", value = "角色", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "create_time", value = "建立时间", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "update_time", value = "更新时间", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "signature", value = "签名", required = false, dataType = "String"),
    })
    //@TokenLimit
    public ServerResponse addSystemUser() {
        PageData pd = this.getPageData();

//		PageData userPd = this.getUserPd();
//		if(userPd == null){
//			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
//		}

        return systemUserService.addSystemUser(this.putUserPd(pd));
    }

    /**
     * 更新系统用户
     *
     * @return
     */
    @RequestMapping(value = "/updateSysUser", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "更新系统用户", notes = "更新不为空的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "系统ID", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "USERNAME", value = "用户名", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "PASSWORD", value = "密码", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "NAME", value = "昵称", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "jurisdiction_manage_id", value = "角色", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "create_time", value = "建立时间", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "update_time", value = "更新时间", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "signature", value = "签名", required = false, dataType = "String"),
    })
    public ServerResponse updateSystemUserByid() {
        PageData pd = this.getPageData();

//		PageData userPd = this.getUserPd();
//		if(userPd == null){
//			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
//		}

        return systemUserService.updateSystemUserByid(pd);
    }

    /**
     * 获取系统用户列表数据(非分页)
     *
     * @return
     */
    @RequestMapping(value = "/queryAppMenuKey", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取系统用户列表数据非分页方法")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "系统ID", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "USERNAME", value = "用户名", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "PASSWORD", value = "密码", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "NAME", value = "昵称", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "jurisdiction_manage_id", value = "角色", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "create_time", value = "建立时间", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "update_time", value = "更新时间", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "signature", value = "签名", required = false, dataType = "String"),
    })
    public ServerResponse getSystemUserList() {
        PageData pd = this.getPageData();

//		PageData userPd = this.getUserPd();
//		if(userPd == null){
//			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
//		}

        return systemUserService.getSystemUserList(pd);
    }

    /**
     * 获取系统用户单条数据(通过任意列)
     *
     * @return
     */
    @RequestMapping(value = "/getSystemUserByColumn", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取系统用户单条数据(通过任意列)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "系统ID", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "USERNAME", value = "用户名", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "PASSWORD", value = "密码", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "NAME", value = "昵称", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "jurisdiction_manage_id", value = "角色", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "create_time", value = "建立时间", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "update_time", value = "更新时间", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "signature", value = "签名", required = false, dataType = "String"),
    })
    public ServerResponse getSystemUserByColumn() {
        PageData pd = this.getPageData();

//		PageData userPd = this.getUserPd();
//		if(userPd == null){
//			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
//		}

        return systemUserService.getSystemUserByColumn(pd);
    }

    /**
     * 删除系统用户
     *
     * @return
     */
    @RequestMapping(value = "/deleteSysUser", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "删除系统用户", notes = "删除不为空的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "系统ID", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "USERNAME", value = "用户名", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "PASSWORD", value = "密码", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "NAME", value = "昵称", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "jurisdiction_manage_id", value = "角色", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "create_time", value = "建立时间", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "update_time", value = "更新时间", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "signature", value = "签名", required = false, dataType = "String"),
    })
    public ServerResponse deleteSystemUserByColumn() {
        PageData pd = this.getPageData();

//		PageData userPd = this.getUserPd();
//		if(userPd == null){
//			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
//		}

        return systemUserService.deleteSystemUserByColumn(pd);
    }

}
