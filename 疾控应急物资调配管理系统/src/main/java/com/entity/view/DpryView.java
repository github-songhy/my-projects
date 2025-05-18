package com.entity.view;

import com.entity.DpryEntity;

import com.baomidou.mybatisplus.annotations.TableName;
import org.apache.commons.beanutils.BeanUtils;
import java.lang.reflect.InvocationTargetException;

import java.io.Serializable;
 

/**
 * 调配人员
 * 后端返回视图实体辅助类   
 * （通常后端关联的表或者自定义的字段需要返回使用）
 * 
 * @date 2025-03-26
 */
@TableName("dpry")
public class DpryView  extends DpryEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	public DpryView(){
	}
 
 	public DpryView(DpryEntity dpryEntity){
 	try {
			BeanUtils.copyProperties(this, dpryEntity);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		
	}
}
