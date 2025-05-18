package com.earlywarning.controller;

import com.earlywarning.base.BaseController;
import com.earlywarning.common.ServerResponse;
import com.earlywarning.config.AllConfig;
import com.earlywarning.entity.system.PageData;
import com.earlywarning.service.user.SysUserOldService;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.UserAgent;
import eu.bitwalker.useragentutils.Version;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

//@ApiIgnore
@Api(value = "login", description = "登录")
@Controller
public class LoginController extends BaseController {

    private final SysUserOldService sysuserService;

    public LoginController(SysUserOldService sysuserService) {
        this.sysuserService = sysuserService;
    }

    @ApiOperation(value = "跳转到前台主页页面")
    @GetMapping("/")
    public String index() {
        return "welcome";
    }


    @ApiOperation(value = "跳转到菜单管理页面")
    @GetMapping("/idx_app_menu")
    public String idx_app_menu() {
        return "idx_app_menu";
    }

    @ApiOperation(value = "跳转到其他页面")
    @GetMapping("/idx_special_qt")
    public String idxAppQt() {
        return "idx_special_qt";
    }

    @ApiOperation(value = "跳转到角色管理页面")
    @GetMapping("/idx_role_manage")
    public String idx_role_manage() {
        return "idx_role_manage";
    }

    @ApiOperation(value = "跳转到角色管理页面")
    @GetMapping("/demo_table")
    public String demo_table() {
        return "demo_table";
    }

    @ApiOperation(value = "跳转到角色权限页面")
    @GetMapping("/idx_jurisdiction_manage")
    public String idx_jurisdiction_manage() {
        return "idx_jurisdiction_manage";
    }


    @ApiOperation(value = "跳转到后台登录页面")
    @GetMapping("/idx_login")
    public String idxLogin() {
        return "idx_login";
    }

    @ApiOperation(value = "跳转到后台注册页面")
    @GetMapping("/idx_register")
    public String idxRegister() {
        return "idx_register";
    }

    @GetMapping("/idx_index")
    @ApiOperation(value = "跳转到后台主页面")
    public String idxIndex(ModelMap model) {
        //验证session是否有效
        HttpSession session = request.getSession();
        PageData user = (PageData) session.getAttribute(AllConfig.SESSION_KEY);
        if (user != null) {
            //先读取session里面是否包含菜单，如果包含则直接使用，不包含则重新获取并设置进入session
            model.addAttribute("name", user.getString("NAME"));//用户名称
            return "idx_index";
        } else {
            //返回到首页
            return "idx_login";
        }

    }

    @GetMapping("/loginPost")
    @ApiOperation(value = "登录")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "USERNAME", value = "用户", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "PASSWORD", value = "密码", required = true, dataType = "String"),
    })
    public @ResponseBody
    Map<String, Object> loginPost(HttpSession session) {
        Map<String, Object> map = new HashMap<>();
        // 前端传入数据
        PageData pd = getPageData();
        // 获取浏览器信息
        Browser browser = UserAgent.parseUserAgentString(request.getHeader("User-Agent")).getBrowser();
        // 获取浏览器版本号
        Version version = browser.getVersion(request.getHeader("User-Agent"));
        String versionInfo = null;
        try {
            versionInfo = browser.getName() + "/" + version.getVersion();
        } catch (NullPointerException e) {
        }

        //获取用户信息
        PageData user = sysuserService.getLoginValidation(pd);
        if (user == null) {
            map.put("msg", "500");
            map.put("result", "密码错误");
            return map;
        } else {
            //判定当前用户是否已经登录过，如果登录不让重新登录
            PageData userPd = (PageData) session.getAttribute(AllConfig.SESSION_KEY);
            user.put("version", versionInfo);//浏览器版本号
            if (userPd != null) {//表示登录过则不需要多次登录直接提醒!
                map.put("msg", "200");
                map.put("result", "你已经登录过无需再次登录!");
            } else {
                //System.out.println(session.getAttribute(AllConfig.SESSION_KEY));
                // 谦容夸浏览器保证只有1个用户
                // 设置session,放入用户信息跟角色权限信息
                session.setAttribute(AllConfig.SESSION_KEY, user);
                map.put("msg", "200");
                map.put("result", "登录成功!");
                map.put("data", user);
            }
        }

        return map;
    }

    //    @ApiIgnore
    @GetMapping("/logout")
    @ApiOperation(value = "退出登录")
    public String logout(HttpSession session) {
        // 移除session账户信息
        session.removeAttribute(AllConfig.SESSION_KEY);
        return "idx_login";
    }

    @GetMapping("/registerUser")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "USERNAME", value = "用户", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "PASSWORD", value = "密码", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "NAME", value = "昵称", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "jurisdiction_manage_id", value = "角色ID", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "signature", value = "签名", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "student_number", value = "学生号", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "sex", value = "性别", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "major", value = "专业", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "phone", value = "手机号", required = false, dataType = "String"),
    })
    @ApiOperation(value = "注册")
    public ServerResponse registerUser() {
        PageData pd = this.getPageData();
        return sysuserService.registerUser(pd);
    }

    /**
     * 上传
     */
    @PostMapping("/upload")
    @ResponseBody
    @ApiOperation(value = "上传")
    public JSONObject upload(@RequestParam("file") MultipartFile file) {
        JSONObject json = this.uploadPhoto(file);
        //文件不能为空
        if (json.get("msg").equals(500)) return this.getFalJson();
        return json;
    }
}
