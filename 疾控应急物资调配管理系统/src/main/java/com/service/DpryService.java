package com.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.utils.PageUtils;
import com.entity.DpryEntity;
import java.util.List;
import java.util.Map;
import com.entity.vo.DpryVO;
import org.apache.ibatis.annotations.Param;
import com.entity.view.DpryView;


/**
 * 调配人员
 *
 * 
 * @date 2025-03-26
 */
public interface DpryService extends IService<DpryEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	List<DpryVO> selectListVO(Wrapper<DpryEntity> wrapper);
   	
   	DpryVO selectVO(@Param("ew") Wrapper<DpryEntity> wrapper);
   	
   	List<DpryView> selectListView(Wrapper<DpryEntity> wrapper);
   	
   	DpryView selectView(@Param("ew") Wrapper<DpryEntity> wrapper);
   	
   	PageUtils queryPage(Map<String, Object> params,Wrapper<DpryEntity> wrapper);
   	

}

