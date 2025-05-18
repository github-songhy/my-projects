package com.entity.vo;

import com.entity.FenpeijihuaEntity;

import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
 

/**
 * 分配计划
 * 手机端接口返回实体辅助类 
 * （主要作用去除一些不必要的字段）
 * 
 * @date 2025-03-26
 */
public class FenpeijihuaVO  implements Serializable {
	private static final long serialVersionUID = 1L;

	 			
	/**
	 * 物资名称
	 */
	
	private String wuzimingcheng;
		
	/**
	 * 物资规格
	 */
	
	private String wuziguige;
		
	/**
	 * 分配任务
	 */
	
	private String fenpeirenwu;
		
	/**
	 * 发布时间
	 */
		
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat 
	private Date fabushijian;
		
	/**
	 * 调配人员账号
	 */
	
	private String dpryzhanghao;
		
	/**
	 * 调配人员姓名
	 */
	
	private String dpryxingming;
				
	
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
	 * 设置：分配任务
	 */
	 
	public void setFenpeirenwu(String fenpeirenwu) {
		this.fenpeirenwu = fenpeirenwu;
	}
	
	/**
	 * 获取：分配任务
	 */
	public String getFenpeirenwu() {
		return fenpeirenwu;
	}
				
	
	/**
	 * 设置：发布时间
	 */
	 
	public void setFabushijian(Date fabushijian) {
		this.fabushijian = fabushijian;
	}
	
	/**
	 * 获取：发布时间
	 */
	public Date getFabushijian() {
		return fabushijian;
	}
				
	
	/**
	 * 设置：调配人员账号
	 */
	 
	public void setDpryzhanghao(String dpryzhanghao) {
		this.dpryzhanghao = dpryzhanghao;
	}
	
	/**
	 * 获取：调配人员账号
	 */
	public String getDpryzhanghao() {
		return dpryzhanghao;
	}
				
	
	/**
	 * 设置：调配人员姓名
	 */
	 
	public void setDpryxingming(String dpryxingming) {
		this.dpryxingming = dpryxingming;
	}
	
	/**
	 * 获取：调配人员姓名
	 */
	public String getDpryxingming() {
		return dpryxingming;
	}
			
}
