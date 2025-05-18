package com.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import com.utils.ValidatorUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.annotation.IgnoreAuth;

import com.entity.FenpeijihuaEntity;
import com.entity.view.FenpeijihuaView;

import com.service.FenpeijihuaService;
import com.service.TokenService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MD5Util;
import com.utils.MPUtil;
import com.utils.CommonUtil;
import java.io.IOException;

/**
 * 分配计划
 * 后端接口
 * 
 * @date 2025-03-26
 */
@RestController
@RequestMapping("/fenpeijihua")
public class FenpeijihuaController {
    @Autowired
    private FenpeijihuaService fenpeijihuaService;



    


    /**
     * 后端列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,FenpeijihuaEntity fenpeijihua, 
		HttpServletRequest request){

		String tableName = request.getSession().getAttribute("tableName").toString();
		if(tableName.equals("dpry")) {
			fenpeijihua.setDpryzhanghao((String)request.getSession().getAttribute("username"));
		}
        EntityWrapper<FenpeijihuaEntity> ew = new EntityWrapper<FenpeijihuaEntity>();

    	PageUtils page = fenpeijihuaService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, fenpeijihua), params), params));
		request.setAttribute("data", page);
        return R.ok().put("data", page);
    }
    
    /**
     * 前端列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,FenpeijihuaEntity fenpeijihua, 
		HttpServletRequest request){
        EntityWrapper<FenpeijihuaEntity> ew = new EntityWrapper<FenpeijihuaEntity>();

    	PageUtils page = fenpeijihuaService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, fenpeijihua), params), params));
		request.setAttribute("data", page);
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( FenpeijihuaEntity fenpeijihua){
       	EntityWrapper<FenpeijihuaEntity> ew = new EntityWrapper<FenpeijihuaEntity>();
      	ew.allEq(MPUtil.allEQMapPre( fenpeijihua, "fenpeijihua")); 
        return R.ok().put("data", fenpeijihuaService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(FenpeijihuaEntity fenpeijihua){
        EntityWrapper< FenpeijihuaEntity> ew = new EntityWrapper< FenpeijihuaEntity>();
 		ew.allEq(MPUtil.allEQMapPre( fenpeijihua, "fenpeijihua")); 
		FenpeijihuaView fenpeijihuaView =  fenpeijihuaService.selectView(ew);
		return R.ok("查询分配计划成功").put("data", fenpeijihuaView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        FenpeijihuaEntity fenpeijihua = fenpeijihuaService.selectById(id);
        return R.ok().put("data", fenpeijihua);
    }

    /**
     * 前端详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        FenpeijihuaEntity fenpeijihua = fenpeijihuaService.selectById(id);
        return R.ok().put("data", fenpeijihua);
    }
    



    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody FenpeijihuaEntity fenpeijihua, HttpServletRequest request){
    	fenpeijihua.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(fenpeijihua);

        fenpeijihuaService.insert(fenpeijihua);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody FenpeijihuaEntity fenpeijihua, HttpServletRequest request){
    	fenpeijihua.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(fenpeijihua);

        fenpeijihuaService.insert(fenpeijihua);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody FenpeijihuaEntity fenpeijihua, HttpServletRequest request){
        //ValidatorUtils.validateEntity(fenpeijihua);
        fenpeijihuaService.updateById(fenpeijihua);//全部更新
        return R.ok();
    }
    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        fenpeijihuaService.deleteBatchIds(Arrays.asList(ids));
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
		
		Wrapper<FenpeijihuaEntity> wrapper = new EntityWrapper<FenpeijihuaEntity>();
		if(map.get("remindstart")!=null) {
			wrapper.ge(columnName, map.get("remindstart"));
		}
		if(map.get("remindend")!=null) {
			wrapper.le(columnName, map.get("remindend"));
		}

		String tableName = request.getSession().getAttribute("tableName").toString();
		if(tableName.equals("dpry")) {
			wrapper.eq("dpryzhanghao", (String)request.getSession().getAttribute("username"));
		}

		int count = fenpeijihuaService.selectCount(wrapper);
		return R.ok().put("count", count);
	}
	
	





}
