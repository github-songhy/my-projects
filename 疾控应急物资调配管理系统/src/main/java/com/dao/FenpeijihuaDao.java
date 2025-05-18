package com.dao;

import com.entity.FenpeijihuaEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import org.apache.ibatis.annotations.Param;
import com.entity.vo.FenpeijihuaVO;
import com.entity.view.FenpeijihuaView;


/**
 * 分配计划
 * 
 * 
 * @date 2025-03-26
 */
public interface FenpeijihuaDao extends BaseMapper<FenpeijihuaEntity> {
	
	List<FenpeijihuaVO> selectListVO(@Param("ew") Wrapper<FenpeijihuaEntity> wrapper);
	
	FenpeijihuaVO selectVO(@Param("ew") Wrapper<FenpeijihuaEntity> wrapper);
	
	List<FenpeijihuaView> selectListView(@Param("ew") Wrapper<FenpeijihuaEntity> wrapper);

	List<FenpeijihuaView> selectListView(Pagination page,@Param("ew") Wrapper<FenpeijihuaEntity> wrapper);
	
	FenpeijihuaView selectView(@Param("ew") Wrapper<FenpeijihuaEntity> wrapper);
	

}
