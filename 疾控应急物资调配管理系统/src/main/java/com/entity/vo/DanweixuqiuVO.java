package com.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
 

/**
 * 单位需求
 * 手机端接口返回实体辅助类 
 * （主要作用去除一些不必要的字段）
 * 
 * @date 2025-03-26
 */
public class DanweixuqiuVO  implements Serializable {
	private static final long serialVersionUID = 1L;

	 			
	/**
	 * 手机号码
	 */
	
	private String shoujihaoma;
		
	/**
	 * 单位负责人姓名
	 */
	
	private String dwfzrxingming;
		
	/**
	 * 单位地址
	 */
	
	private String danweidizhi;
		
	/**
	 * 需求物资
	 */
	
	private String xuqiuwuzi;
		
	/**
	 * 需求数量
	 */
	
	private Integer xuqiushuliang;
		
	/**
	 * 申请时间
	 */
		
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat 
	private Date shenqingshijian;
		
	/**
	 * 备注
	 */
	
	private String beizhu;
		
	/**
	 * 是否审核
	 */
	
	private String sfsh;
		
	/**
	 * 审核回复
	 */
	
	private String shhf;
				
	
	/**
	 * 设置：手机号码
	 */
	 
	public void setShoujihaoma(String shoujihaoma) {
		this.shoujihaoma = shoujihaoma;
	}
	
	/**
	 * 获取：手机号码
	 */
	public String getShoujihaoma() {
		return shoujihaoma;
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
	 * 设置：单位地址
	 */
	 
	public void setDanweidizhi(String danweidizhi) {
		this.danweidizhi = danweidizhi;
	}
	
	/**
	 * 获取：单位地址
	 */
	public String getDanweidizhi() {
		return danweidizhi;
	}
				
	
	/**
	 * 设置：需求物资
	 */
	 
	public void setXuqiuwuzi(String xuqiuwuzi) {
		this.xuqiuwuzi = xuqiuwuzi;
	}
	
	/**
	 * 获取：需求物资
	 */
	public String getXuqiuwuzi() {
		return xuqiuwuzi;
	}
				
	
	/**
	 * 设置：需求数量
	 */
	 
	public void setXuqiushuliang(Integer xuqiushuliang) {
		this.xuqiushuliang = xuqiushuliang;
	}
	
	/**
	 * 获取：需求数量
	 */
	public Integer getXuqiushuliang() {
		return xuqiushuliang;
	}
				
	
	/**
	 * 设置：申请时间
	 */
	 
	public void setShenqingshijian(Date shenqingshijian) {
		this.shenqingshijian = shenqingshijian;
	}
	
	/**
	 * 获取：申请时间
	 */
	public Date getShenqingshijian() {
		return shenqingshijian;
	}
				
	
	/**
	 * 设置：备注
	 */
	 
	public void setBeizhu(String beizhu) {
		this.beizhu = beizhu;
	}
	
	/**
	 * 获取：备注
	 */
	public String getBeizhu() {
		return beizhu;
	}
				
	
	/**
	 * 设置：是否审核
	 */
	 
	public void setSfsh(String sfsh) {
		this.sfsh = sfsh;
	}
	
	/**
	 * 获取：是否审核
	 */
	public String getSfsh() {
		return sfsh;
	}
				
	
	/**
	 * 设置：审核回复
	 */
	 
	public void setShhf(String shhf) {
		this.shhf = shhf;
	}
	
	/**
	 * 获取：审核回复
	 */
	public String getShhf() {
		return shhf;
	}
			
}
