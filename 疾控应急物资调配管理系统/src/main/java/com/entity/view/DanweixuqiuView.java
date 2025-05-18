package com.entity.view;

import com.entity.DanweixuqiuEntity;

import com.baomidou.mybatisplus.annotations.TableName;
import org.apache.commons.beanutils.BeanUtils;
import java.lang.reflect.InvocationTargetException;

import java.io.Serializable;
 

/**
 * 单位需求
 * 后端返回视图实体辅助类   
 * （通常后端关联的表或者自定义的字段需要返回使用）
 * 
 * @date 2025-03-26
 */
@TableName("danweixuqiu")
public class DanweixuqiuView  extends DanweixuqiuEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	public DanweixuqiuView(){
	}
 
 	public DanweixuqiuView(DanweixuqiuEntity danweixuqiuEntity){
 	try {
			BeanUtils.copyProperties(this, danweixuqiuEntity);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		
	}
}
