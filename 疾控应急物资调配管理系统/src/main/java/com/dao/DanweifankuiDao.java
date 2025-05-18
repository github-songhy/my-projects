package com.dao;

import com.entity.DanweifankuiEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import org.apache.ibatis.annotations.Param;
import com.entity.vo.DanweifankuiVO;
import com.entity.view.DanweifankuiView;


/**
 * 单位反馈
 * 
 * 
 * @date 2025-03-26
 */
public interface DanweifankuiDao extends BaseMapper<DanweifankuiEntity> {
	
	List<DanweifankuiVO> selectListVO(@Param("ew") Wrapper<DanweifankuiEntity> wrapper);
	
	DanweifankuiVO selectVO(@Param("ew") Wrapper<DanweifankuiEntity> wrapper);
	
	List<DanweifankuiView> selectListView(@Param("ew") Wrapper<DanweifankuiEntity> wrapper);

	List<DanweifankuiView> selectListView(Pagination page,@Param("ew") Wrapper<DanweifankuiEntity> wrapper);
	
	DanweifankuiView selectView(@Param("ew") Wrapper<DanweifankuiEntity> wrapper);
	

}
