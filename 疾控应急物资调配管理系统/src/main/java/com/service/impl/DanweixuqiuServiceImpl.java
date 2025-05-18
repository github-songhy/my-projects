package com.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.List;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.utils.PageUtils;
import com.utils.Query;


import com.dao.DanweixuqiuDao;
import com.entity.DanweixuqiuEntity;
import com.service.DanweixuqiuService;
import com.entity.vo.DanweixuqiuVO;
import com.entity.view.DanweixuqiuView;

@Service("danweixuqiuService")
public class DanweixuqiuServiceImpl extends ServiceImpl<DanweixuqiuDao, DanweixuqiuEntity> implements DanweixuqiuService {


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<DanweixuqiuEntity> page = this.selectPage(
                new Query<DanweixuqiuEntity>(params).getPage(),
                new EntityWrapper<DanweixuqiuEntity>()
        );
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, Wrapper<DanweixuqiuEntity> wrapper) {
		  Page<DanweixuqiuView> page =new Query<DanweixuqiuView>(params).getPage();
	        page.setRecords(baseMapper.selectListView(page,wrapper));
	    	PageUtils pageUtil = new PageUtils(page);
	    	return pageUtil;
 	}
    
    @Override
	public List<DanweixuqiuVO> selectListVO(Wrapper<DanweixuqiuEntity> wrapper) {
 		return baseMapper.selectListVO(wrapper);
	}
	
	@Override
	public DanweixuqiuVO selectVO(Wrapper<DanweixuqiuEntity> wrapper) {
 		return baseMapper.selectVO(wrapper);
	}
	
	@Override
	public List<DanweixuqiuView> selectListView(Wrapper<DanweixuqiuEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public DanweixuqiuView selectView(Wrapper<DanweixuqiuEntity> wrapper) {
		return baseMapper.selectView(wrapper);
	}


}
