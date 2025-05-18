package com.earlywarning.controller;

import com.earlywarning.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Api(value = "login_href", description = "登录")
@Controller
@RequestMapping("/login_href")
public class LoginHrefController extends BaseController {
    @ApiOperation(value = "跳转到菜单管理页面")
    @GetMapping("/idx_app_menu")
    public String idxAppMenu() {
        return "idx_app_menu";
    }

    @ApiOperation(value = "跳转到学生管理页面")
    @GetMapping("/idx_app_user")
    public String idxAppUser() {
        return "idx_app_user";
    }

    @ApiOperation(value = "跳转到班级管理页面")
    @GetMapping("/idx_class_manage")
    public String idxClassManage() {
        return "idx_class_manage";
    }

    @ApiOperation(value = "跳转到课程管理页面")
    @GetMapping("/idx_course_manage")
    public String idxCourseManage() {
        return "idx_course_manage";
    }

    @ApiOperation(value = "跳转到权限管理页面")
    @GetMapping("/idx_jurisdiction_manage")
    public String idxJurisdictionManage() {
        return "idx_jurisdiction_manage";
    }

    @ApiOperation(value = "跳转到公告管理页面")
    @GetMapping("/idx_news_manage")
    public String idxNewsManage() {
        return "idx_news_manage";
    }

    @ApiOperation(value = "跳转到成绩管理页面")
    @GetMapping("/idx_result_manage")
    public String idxResultManage() {
        return "idx_result_manage";
    }

    @ApiOperation(value = "跳转到角色管理页面")
    @GetMapping("/idx_role_manage")
    public String idxRoleManage() {
        return "idx_role_manage";
    }

    @ApiOperation(value = "跳转到学年管理页面")
    @GetMapping("/idx_session_manage")
    public String idxSessionManage() {
        return "idx_session_manage";
    }

    @ApiOperation(value = "跳转到专业管理页面")
    @GetMapping("/idx_special_subject")
    public String idxSpecialSubject() {
        return "idx_special_subject";
    }

    @ApiOperation(value = "跳转到违纪处分管理页面")
    @GetMapping("/idx_stu_violation")
    public String idxStuViolation() {
        return "idx_stu_violation";
    }

    @ApiOperation(value = "跳转到用户管理页面")
    @GetMapping("/idx_sys_user")
    public String idxSysUser() {
        return "idx_sys_user";
    }

    @ApiOperation(value = "跳转到违纪处分类型页面")
    @GetMapping("/idx_violation_type")
    public String idxViolationType() {
        return "idx_violation_type";
    }
}
