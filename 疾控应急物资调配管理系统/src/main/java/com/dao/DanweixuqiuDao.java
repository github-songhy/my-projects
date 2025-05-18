package com.dao;

import com.entity.DanweixuqiuEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import org.apache.ibatis.annotations.Param;
import com.entity.vo.DanweixuqiuVO;
import com.entity.view.DanweixuqiuView;


/**
 * 单位需求
 * 
 * 
 * @date 2025-03-26
 */
public interface DanweixuqiuDao extends BaseMapper<DanweixuqiuEntity> {
	
	List<DanweixuqiuVO> selectListVO(@Param("ew") Wrapper<DanweixuqiuEntity> wrapper);
	
	DanweixuqiuVO selectVO(@Param("ew") Wrapper<DanweixuqiuEntity> wrapper);
	
	List<DanweixuqiuView> selectListView(@Param("ew") Wrapper<DanweixuqiuEntity> wrapper);

	List<DanweixuqiuView> selectListView(Pagination page,@Param("ew") Wrapper<DanweixuqiuEntity> wrapper);
	
	DanweixuqiuView selectView(@Param("ew") Wrapper<DanweixuqiuEntity> wrapper);
	

}
