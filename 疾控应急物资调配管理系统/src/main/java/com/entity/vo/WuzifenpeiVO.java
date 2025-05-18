package com.entity.vo;

import com.entity.WuzifenpeiEntity;

import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
 

/**
 * 物资分配
 * 手机端接口返回实体辅助类 
 * （主要作用去除一些不必要的字段）
 * 
 * @date 2025-03-26
 */
public class WuzifenpeiVO  implements Serializable {
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
	 * 物资数量
	 */
	
	private Integer wuzishuliang;
		
	/**
	 * 分配时间
	 */
		
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat 
	private Date fenpeishijian;
		
	/**
	 * 单位账号
	 */
	
	private String danweizhanghao;
		
	/**
	 * 单位负责人姓名
	 */
	
	private String dwfzrxingming;
		
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
	 * 设置：分配时间
	 */
	 
	public void setFenpeishijian(Date fenpeishijian) {
		this.fenpeishijian = fenpeishijian;
	}
	
	/**
	 * 获取：分配时间
	 */
	public Date getFenpeishijian() {
		return fenpeishijian;
	}
				
	
	/**
	 * 设置：单位账号
	 */
	 
	public void setDanweizhanghao(String danweizhanghao) {
		this.danweizhanghao = danweizhanghao;
	}
	
	/**
	 * 获取：单位账号
	 */
	public String getDanweizhanghao() {
		return danweizhanghao;
	}
				
	
	/**
	 * 设置：单位负责人姓名
	 */
	 
	public void setDwfzrxingming(String dwfzrxingming) {
		this.dwfzrxingming = dwfzrxingming;
	}
	
	/**
	 * 获取：单位负责人姓名
	 */
	public String getDwfzrxingming() {
		return dwfzrxingming;
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
