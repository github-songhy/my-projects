package com.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.lang.reflect.InvocationTargetException;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.beanutils.BeanUtils;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;


/**
 * 分配计划
 * 数据库通用操作实体类（普通增删改查）
 * 
 * @date 2025-03-26
 */
@TableName("fenpeijihua")
public class FenpeijihuaEntity<T> implements Serializable {
	private static final long serialVersionUID = 1L;


	public FenpeijihuaEntity() {
		
	}
	
	public FenpeijihuaEntity(T t) {
		try {
			BeanUtils.copyProperties(this, t);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 主键id
	 */
	@TableId
	private Long id;
	/**
	 * 物资编号
	 */
					
	private String wuzibianhao;
	
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
	
	
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
	private Date addtime;

	public Date getAddtime() {
		return addtime;
	}
	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 设置：物资编号
	 */
	public void setWuzibianhao(String wuzibianhao) {
		this.wuzibianhao = wuzibianhao;
	}
	/**
	 * 获取：物资编号
	 */
	public String getWuzibianhao() {
		return wuzibianhao;
	}
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
