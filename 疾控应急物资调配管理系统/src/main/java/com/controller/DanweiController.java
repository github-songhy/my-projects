package com.controller;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.annotation.IgnoreAuth;

import com.entity.DanweiEntity;
import com.entity.view.DanweiView;

import com.service.DanweiService;
import com.service.TokenService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;

/**
 * 单位
 * 后端接口
 * 
 * @date 2025-03-26
 */
@RestController
@RequestMapping("/danwei")
public class DanweiController {
    @Autowired
    private DanweiService danweiService;



    
	@Autowired
	private TokenService tokenService;
	
	/**
	 * 登录
	 */
	@IgnoreAuth
	@RequestMapping(value = "/login")
	public R login(String username, String password, String captcha, HttpServletRequest request) {
		DanweiEntity user = danweiService.selectOne(new EntityWrapper<DanweiEntity>().eq("danweizhanghao", username));
		if(user==null || !user.getMima().equals(password)) {
			return R.error("账号或密码不正确");
		}
		String token = tokenService.generateToken(user.getId(), username,"danwei",  "单位" );
		return R.ok().put("token", token);
	}
	
	/**
     * 注册
     */
	@IgnoreAuth
    @RequestMapping("/register")
    public R register(@RequestBody DanweiEntity danwei){
    	//ValidatorUtils.validateEntity(danwei);
    	DanweiEntity user = danweiService.selectOne(new EntityWrapper<DanweiEntity>().eq("danweizhanghao", danwei.getDanweizhanghao()));
		if(user!=null) {
			return R.error("注册用户已存在");
		}
		Long uId = new Date().getTime();
		danwei.setId(uId);
        danweiService.insert(danwei);
        return R.ok();
    }

	
	/**
	 * 退出
	 */
	@RequestMapping("/logout")
	public R logout(HttpServletRequest request) {
		request.getSession().invalidate();
		return R.ok("退出成功");
	}
	
	/**
     * 获取用户的session用户信息
     */
    @RequestMapping("/session")
    public R getCurrUser(HttpServletRequest request){
    	Long id = (Long)request.getSession().getAttribute("userId");
        DanweiEntity user = danweiService.selectById(id);
        return R.ok().put("data", user);
    }
    
    /**
     * 密码重置
     */
    @IgnoreAuth
	@RequestMapping(value = "/resetPass")
    public R resetPass(String username, HttpServletRequest request){
    	DanweiEntity user = danweiService.selectOne(new EntityWrapper<DanweiEntity>().eq("danweizhanghao", username));
    	if(user==null) {
    		return R.error("账号不存在");
    	}
        user.setMima("123456");
        danweiService.updateById(user);
        return R.ok("密码已重置为：123456");
    }


    /**
     * 后端列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,DanweiEntity danwei, 
		HttpServletRequest request){

        EntityWrapper<DanweiEntity> ew = new EntityWrapper<DanweiEntity>();

    	PageUtils page = danweiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, danwei), params), params));
		request.setAttribute("data", page);
        return R.ok().put("data", page);
    }
    
    /**
     * 前端列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,DanweiEntity danwei, 
		HttpServletRequest request){
        EntityWrapper<DanweiEntity> ew = new EntityWrapper<DanweiEntity>();

    	PageUtils page = danweiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, danwei), params), params));
		request.setAttribute("data", page);
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( DanweiEntity danwei){
       	EntityWrapper<DanweiEntity> ew = new EntityWrapper<DanweiEntity>();
      	ew.allEq(MPUtil.allEQMapPre( danwei, "danwei")); 
        return R.ok().put("data", danweiService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(DanweiEntity danwei){
        EntityWrapper< DanweiEntity> ew = new EntityWrapper< DanweiEntity>();
 		ew.allEq(MPUtil.allEQMapPre( danwei, "danwei")); 
		DanweiView danweiView =  danweiService.selectView(ew);
		return R.ok("查询单位成功").put("data", danweiView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        DanweiEntity danwei = danweiService.selectById(id);
        return R.ok().put("data", danwei);
    }

    /**
     * 前端详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        DanweiEntity danwei = danweiService.selectById(id);
        return R.ok().put("data", danwei);
    }
    



    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody DanweiEntity danwei, HttpServletRequest request){
    	danwei.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(danwei);
    	DanweiEntity user = danweiService.selectOne(new EntityWrapper<DanweiEntity>().eq("danweizhanghao", danwei.getDanweizhanghao()));
		if(user!=null) {
			return R.error("用户已存在");
		}

		danwei.setId(new Date().getTime());
        danweiService.insert(danwei);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody DanweiEntity danwei, HttpServletRequest request){
    	danwei.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(danwei);
    	DanweiEntity user = danweiService.selectOne(new EntityWrapper<DanweiEntity>().eq("danweizhanghao", danwei.getDanweizhanghao()));
		if(user!=null) {
			return R.error("用户已存在");
		}

		danwei.setId(new Date().getTime());
        danweiService.insert(danwei);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody DanweiEntity danwei, HttpServletRequest request){
        //ValidatorUtils.validateEntity(danwei);
        danweiService.updateById(danwei);//全部更新
        return R.ok();
    }
    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        danweiService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
    /**
     * 提醒接口
     */
	@RequestMapping("/remind/{columnName}/{type}")
	public R remindCount(@PathVariable("columnName") String columnName, HttpServletRequest request, 
						 @PathVariable("type") String type,@RequestParam Map<String, Object> map) {
		map.put("column", columnName);
		map.put("type", type);
		
		if(type.equals("2")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
			Date remindStartDate = null;
			Date remindEndDate = null;
			if(map.get("remindstart")!=null) {
				Integer remindStart = Integer.parseInt(map.get("remindstart").toString());
				c.setTime(new Date()); 
				c.add(Calendar.DAY_OF_MONTH,remindStart);
				remindStartDate = c.getTime();
				map.put("remindstart", sdf.format(remindStartDate));
			}
			if(map.get("remindend")!=null) {
				Integer remindEnd = Integer.parseInt(map.get("remindend").toString());
				c.setTime(new Date());
				c.add(Calendar.DAY_OF_MONTH,remindEnd);
				remindEndDate = c.getTime();
				map.put("remindend", sdf.format(remindEndDate));
			}
		}
		
		Wrapper<DanweiEntity> wrapper = new EntityWrapper<DanweiEntity>();
		if(map.get("remindstart")!=null) {
			wrapper.ge(columnName, map.get("remindstart"));
		}
		if(map.get("remindend")!=null) {
			wrapper.le(columnName, map.get("remindend"));
		}


		int count = danweiService.selectCount(wrapper);
		return R.ok().put("count", count);
	}
	
	





}
