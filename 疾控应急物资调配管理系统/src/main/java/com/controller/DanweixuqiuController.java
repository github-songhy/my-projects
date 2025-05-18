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

import com.entity.DanweixuqiuEntity;
import com.entity.view.DanweixuqiuView;

import com.service.DanweixuqiuService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;

/**
 * 单位需求
 * 后端接口
 * 
 * @date 2025-03-26
 */
@RestController
@RequestMapping("/danweixuqiu")
public class DanweixuqiuController {
    @Autowired
    private DanweixuqiuService danweixuqiuService;



    


    /**
     * 后端列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,DanweixuqiuEntity danweixuqiu, 
		HttpServletRequest request){

		String tableName = request.getSession().getAttribute("tableName").toString();
		if(tableName.equals("danwei")) {
			danweixuqiu.setDanweizhanghao((String)request.getSession().getAttribute("username"));
		}
        EntityWrapper<DanweixuqiuEntity> ew = new EntityWrapper<DanweixuqiuEntity>();

    	PageUtils page = danweixuqiuService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, danweixuqiu), params), params));
		request.setAttribute("data", page);
        return R.ok().put("data", page);
    }
    
    /**
     * 前端列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,DanweixuqiuEntity danweixuqiu, 
		HttpServletRequest request){
        EntityWrapper<DanweixuqiuEntity> ew = new EntityWrapper<DanweixuqiuEntity>();

    	PageUtils page = danweixuqiuService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, danweixuqiu), params), params));
		request.setAttribute("data", page);
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( DanweixuqiuEntity danweixuqiu){
       	EntityWrapper<DanweixuqiuEntity> ew = new EntityWrapper<DanweixuqiuEntity>();
      	ew.allEq(MPUtil.allEQMapPre( danweixuqiu, "danweixuqiu")); 
        return R.ok().put("data", danweixuqiuService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(DanweixuqiuEntity danweixuqiu){
        EntityWrapper< DanweixuqiuEntity> ew = new EntityWrapper< DanweixuqiuEntity>();
 		ew.allEq(MPUtil.allEQMapPre( danweixuqiu, "danweixuqiu")); 
		DanweixuqiuView danweixuqiuView =  danweixuqiuService.selectView(ew);
		return R.ok("查询单位需求成功").put("data", danweixuqiuView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        DanweixuqiuEntity danweixuqiu = danweixuqiuService.selectById(id);
        return R.ok().put("data", danweixuqiu);
    }

    /**
     * 前端详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        DanweixuqiuEntity danweixuqiu = danweixuqiuService.selectById(id);
        return R.ok().put("data", danweixuqiu);
    }
    



    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody DanweixuqiuEntity danweixuqiu, HttpServletRequest request){
    	danweixuqiu.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(danweixuqiu);

        danweixuqiuService.insert(danweixuqiu);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody DanweixuqiuEntity danweixuqiu, HttpServletRequest request){
    	danweixuqiu.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(danweixuqiu);

        danweixuqiuService.insert(danweixuqiu);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody DanweixuqiuEntity danweixuqiu, HttpServletRequest request){
        //ValidatorUtils.validateEntity(danweixuqiu);
        danweixuqiuService.updateById(danweixuqiu);//全部更新
        return R.ok();
    }
    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        danweixuqiuService.deleteBatchIds(Arrays.asList(ids));
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
		
		Wrapper<DanweixuqiuEntity> wrapper = new EntityWrapper<DanweixuqiuEntity>();
		if(map.get("remindstart")!=null) {
			wrapper.ge(columnName, map.get("remindstart"));
		}
		if(map.get("remindend")!=null) {
			wrapper.le(columnName, map.get("remindend"));
		}

		String tableName = request.getSession().getAttribute("tableName").toString();
		if(tableName.equals("danwei")) {
			wrapper.eq("danweizhanghao", (String)request.getSession().getAttribute("username"));
		}

		int count = danweixuqiuService.selectCount(wrapper);
		return R.ok().put("count", count);
	}
	
	





}
