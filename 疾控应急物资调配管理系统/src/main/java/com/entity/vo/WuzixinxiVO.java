package com.entity.vo;

import com.entity.WuzixinxiEntity;

import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
 

/**
 * 物资信息
 * 手机端接口返回实体辅助类 
 * （主要作用去除一些不必要的字段）
 * 
 * @date 2025-03-26
 */
public class WuzixinxiVO  implements Serializable {
	private static final long serialVersionUID = 1L;

	 			
	/**
	 * 物资名称
	 */
	
	private String wuzimingcheng;
		
	/**
	 * 图片
	 */
	
	private String tupian;
		
	/**
	 * 物资规格
	 */
	
	private String wuziguige;
		
	/**
	 * 物资数量
	 */
	
	private Integer wuzishuliang;
		
	/**
	 * 物资详情
	 */
	
	private String wuzixiangqing;
				
	
	/**
	 * 设置：物资名称
	 */
	 
	public void setWuzimingcheng(String wuzimingcheng) {
		this.wuzimingcheng = wuzimingcheng;
	}
	
	/**
	 * 获取：物资名称
	 */
	public String getWuzimingcheng() {
		return wuzimingcheng;
	}
				
	
	/**
	 * 设置：图片
	 */
	 
	public void setTupian(String tupian) {
		this.tupian = tupian;
	}
	
	/**
	 * 获取：图片
	 */
	public String getTupian() {
		return tupian;
	}
				
	
	/**
	 * 设置：物资规格
	 */
	 
	public void setWuziguige(String wuziguige) {
		this.wuziguige = wuziguige;
	}
	
	/**
	 * 获取：物资规格
	 */
	public String getWuziguige() {
		return wuziguige;
	}
				
	
	/**
	 * 设置：物资数量
	 */
	 
	public void setWuzishuliang(Integer wuzishuliang) {
		this.wuzishuliang = wuzishuliang;
	}
	
	/**
	 * 获取：物资数量
	 */
	public Integer getWuzishuliang() {
		return wuzishuliang;
	}
				
	
	/**
	 * 设置：物资详情
	 */
	 
	public void setWuzixiangqing(String wuzixiangqing) {
		this.wuzixiangqing = wuzixiangqing;
	}
	
	/**
	 * 获取：物资详情
	 */
	public String getWuzixiangqing() {
		return wuzixiangqing;
	}
			
}
