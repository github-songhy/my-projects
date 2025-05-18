package com.earlywarning.controller;

import com.earlywarning.base.BaseController;
import com.earlywarning.common.ServerResponse;
import com.earlywarning.entity.system.Page;
import com.earlywarning.entity.system.PageData;
import com.earlywarning.service.AppUserService;
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

import java.util.List;

@Api(value = "app_user", description = "学生管理", tags = "学生管理")
@Controller
@RequestMapping("/app_user")
public class AppUserController extends BaseController {
    @Autowired
    private AppUserService _service;

    @Autowired
    private ResultManageService resultManageService;

    /**
     * 获取学生管理页面
     */
    @RequestMapping(value = "/AppUser", method = RequestMethod.GET)
    @ApiOperation(value = "获取学生管理页面")
    public String goAppUserPage() {
        return "";
    }

    /**
     * 添加学生管理 (重复数据不能添加)
     */
    @RequestMapping(value = "/addAppUserNo", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "添加学生管理(重复数据不能添加)", notes = "添加不为空的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "username", value = "用户名", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "password", value = "密码", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "nickname", value = "姓名", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "sex", value = "性别", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "phone", value = "联系方式", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "CET_4", value = "四级", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "class_manage_id", value = "班级", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "special_subject_id", value = "专业", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "id_card", value = "身份证", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "dormitory_number", value = "宿舍号", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "credit", value = "学分", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "early_warning", value = "预警（正常/一级/二级/三级）", required = false, dataType = "int"),
    })
    public ServerResponse<String> addAppUserNo() {
        PageData pd = getPageData();
        return _service.addAppUserNo(this.putUserPd(pd));
    }

    /**
     * 添加学生管理 (重复数据可以添加)
     */
    @RequestMapping(value = "/addAppUserAll", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "添加学生管理(重复数据可以添加)", notes = "添加不为空的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "username", value = "用户名", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "password", value = "密码", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "nickname", value = "姓名", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "sex", value = "性别", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "phone", value = "联系方式", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "CET_4", value = "四级", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "class_manage_id", value = "班级", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "special_subject_id", value = "专业", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "id_card", value = "身份证", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "dormitory_number", value = "宿舍号", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "credit", value = "学分", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "early_warning", value = "预警（正常/一级/二级/三级）", required = false, dataType = "int"),
    })
    public ServerResponse<String> addAppUserAll() {
        PageData pd = getPageData();
        return _service.addAppUserAll(this.putUserPd(pd));
    }

    /**
     * 删除学生管理
     */
    @RequestMapping(value = "/deleteAppUser", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "删除学生管理", notes = "删除不为空的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "xxx", required = true, dataType = "int"),
    })
    public ServerResponse<String> deleteAppUser() {
        PageData pd = getPageData();
        return _service.deleteAppUser(pd);
    }

    /**
     * 根据id更新数据
     */
    @RequestMapping(value = "/updateAppUser", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "更新学生管理", notes = "更新不为空的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "主键id24", required = true, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "username", value = "用户名", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "password", value = "密码", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "nickname", value = "姓名", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "sex", value = "性别", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "phone", value = "联系方式", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "CET_4", value = "四级", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "class_manage_id", value = "班级", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "special_subject_id", value = "专业", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "id_card", value = "身份证", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "dormitory_number", value = "宿舍号", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "credit", value = "学分", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "early_warning", value = "预警（正常/一级/二级/三级）", required = false, dataType = "int"),
    })
    public ServerResponse<String> updateAppUser() {
        PageData pd = getPageData();
        return _service.updateAppUser(pd);
    }

    /**
     * 获取学生管理数据(非分页,搜索功能)
     */
    @RequestMapping(value = "/queryAppUserKey", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取学生管理数据(非分页,搜索功能)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "主键id24", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "username", value = "用户名", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "password", value = "密码", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "nickname", value = "姓名", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "sex", value = "性别", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "phone", value = "联系方式", required = false, dataType = "varchar"),
            @ApiImplicitParam(paramType = "query", name = "CET_4", value = "四级", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "class_manage_id", value = "班级", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "special_subject_id", value = "专业", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "early_warning", value = "预警（正常/一级/二级/三级）", required = false, dataType = "int"),
    })
    public ServerResponse<List<PageData>> queryAppUserKey() {
        PageData pd = this.getPageData();
        return _service.queryAppUserKey(pd);
    }

    /**
     * 获取学生管理列表数据
     */
    @RequestMapping(value = "/queryPageAppUserKeyList", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取学生管理列表数据", notes = "分页")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "showCount", value = "每页记录数", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "currentPage", value = "当前页", required = true, dataType = "String"),
    })
    public JSONObject queryPageAppUserKeyList() {
        PageData pd = getPageData();
        Page page = getPage();
        page.setPd(pd);
        List<PageData> systemUserList = null;
        try {
            systemUserList = _service.queryPageAppUserKeyList(page);
            for (PageData pageData : systemUserList) {
                //System.out.println(pageData);

                Page resultPd = new Page();
                PageData pageData1 = new PageData();
                pageData1.put("app_user_id", String.valueOf(pageData.get("id")));
                resultPd.setPd(pageData1);
                //获取学生的考试列表根据不及格科数设置预警状态
                /*List<PageData> resultManageKeyList = resultManageService.queryPageResultManageKeyList(resultPd);
                Integer bjgnum = 0;

                Double totalA = new Double(0);
                Double totalB = new Double(0);

                for (PageData result:resultManageKeyList){
                    //System.out.println(result);
                    Double credit = new Double(0);
                    Double score = new Double(0);

                    try {
                         credit =  Double.valueOf(String.valueOf(result.get("credit")));
                         score =  Double.valueOf(String.valueOf(result.get("score")));
                    }catch (Exception e){

                    }




                    totalA += (score*credit);
                    totalB += credit;


                   if(score!=null && score<new Double("60")){
                       bjgnum += 1;
                   }
                }
                Double totalCreditPoint = totalA/totalB;
                if(totalCreditPoint.isNaN()){
                    totalCreditPoint = new Double(0);
                }else{
                    BigDecimal bigDecimal = new BigDecimal(totalCreditPoint).setScale(2, RoundingMode.HALF_UP);
                    totalCreditPoint = bigDecimal.doubleValue();
                }
                //System.out.println(totalCreditPoint);


                Integer jsearly_warning;
                if(bjgnum>=1&&bjgnum<=3){
                    jsearly_warning = 2;
                }else if(bjgnum>=4&&bjgnum<=5){
                     jsearly_warning = 3;
                }else if(bjgnum>5){
                     jsearly_warning = 4;
                }else{
                     jsearly_warning = 1;
                }

                //开始计算学分绩


                //修改预警值
                pageData.put("early_warning",jsearly_warning);
                PageData userData = new PageData();
                userData.put("id",String.valueOf(pageData.get("id")));
                userData.put("early_warning",jsearly_warning);
                userData.put("credit",totalCreditPoint);
                _service.updateAppUser(userData);


                 */
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return viewReturnPageData(page, systemUserList);
    }
}
