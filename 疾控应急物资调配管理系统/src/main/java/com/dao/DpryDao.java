package com.dao;

import com.entity.DpryEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import org.apache.ibatis.annotations.Param;
import com.entity.vo.DpryVO;
import com.entity.view.DpryView;


/**
 * 调配人员
 * 
 * 
 * @date 2025-03-26
 */
public interface DpryDao extends BaseMapper<DpryEntity> {
	
	List<DpryVO> selectListVO(@Param("ew") Wrapper<DpryEntity> wrapper);
	
	DpryVO selectVO(@Param("ew") Wrapper<DpryEntity> wrapper);
	
	List<DpryView> selectListView(@Param("ew") Wrapper<DpryEntity> wrapper);

	List<DpryView> selectListView(Pagination page,@Param("ew") Wrapper<DpryEntity> wrapper);
	
	DpryView selectView(@Param("ew") Wrapper<DpryEntity> wrapper);
	

}
