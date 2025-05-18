package com.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.utils.PageUtils;
import com.entity.DanweixuqiuEntity;
import java.util.List;
import java.util.Map;
import com.entity.vo.DanweixuqiuVO;
import org.apache.ibatis.annotations.Param;
import com.entity.view.DanweixuqiuView;


/**
 * 单位需求
 *
 * 
 * @date 2025-03-26
 */
public interface DanweixuqiuService extends IService<DanweixuqiuEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	List<DanweixuqiuVO> selectListVO(Wrapper<DanweixuqiuEntity> wrapper);
   	
   	DanweixuqiuVO selectVO(@Param("ew") Wrapper<DanweixuqiuEntity> wrapper);
   	
   	List<DanweixuqiuView> selectListView(Wrapper<DanweixuqiuEntity> wrapper);
   	
   	DanweixuqiuView selectView(@Param("ew") Wrapper<DanweixuqiuEntity> wrapper);
   	
   	PageUtils queryPage(Map<String, Object> params,Wrapper<DanweixuqiuEntity> wrapper);
   	

}

