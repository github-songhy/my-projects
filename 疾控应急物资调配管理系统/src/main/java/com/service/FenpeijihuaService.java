package com.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.utils.PageUtils;
import com.entity.FenpeijihuaEntity;
import java.util.List;
import java.util.Map;
import com.entity.vo.FenpeijihuaVO;
import org.apache.ibatis.annotations.Param;
import com.entity.view.FenpeijihuaView;


/**
 * 分配计划
 *
 * 
 * @date 2025-03-26
 */
public interface FenpeijihuaService extends IService<FenpeijihuaEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	List<FenpeijihuaVO> selectListVO(Wrapper<FenpeijihuaEntity> wrapper);
   	
   	FenpeijihuaVO selectVO(@Param("ew") Wrapper<FenpeijihuaEntity> wrapper);
   	
   	List<FenpeijihuaView> selectListView(Wrapper<FenpeijihuaEntity> wrapper);
   	
   	FenpeijihuaView selectView(@Param("ew") Wrapper<FenpeijihuaEntity> wrapper);
   	
   	PageUtils queryPage(Map<String, Object> params,Wrapper<FenpeijihuaEntity> wrapper);
   	

}

