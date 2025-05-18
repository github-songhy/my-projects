package com.entity.view;

import com.entity.FenpeijihuaEntity;

import com.baomidou.mybatisplus.annotations.TableName;
import org.apache.commons.beanutils.BeanUtils;
import java.lang.reflect.InvocationTargetException;

import java.io.Serializable;
 

/**
 * 分配计划
 * 后端返回视图实体辅助类   
 * （通常后端关联的表或者自定义的字段需要返回使用）
 * 
 * @date 2025-03-26
 */
@TableName("fenpeijihua")
public class FenpeijihuaView  extends FenpeijihuaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	public FenpeijihuaView(){
	}
 
 	public FenpeijihuaView(FenpeijihuaEntity fenpeijihuaEntity){
 	try {
			BeanUtils.copyProperties(this, fenpeijihuaEntity);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		
	}
}
