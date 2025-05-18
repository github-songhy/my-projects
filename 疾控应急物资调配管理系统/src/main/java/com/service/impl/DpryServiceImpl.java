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


import com.dao.DpryDao;
import com.entity.DpryEntity;
import com.service.DpryService;
import com.entity.vo.DpryVO;
import com.entity.view.DpryView;

@Service("dpryService")
public class DpryServiceImpl extends ServiceImpl<DpryDao, DpryEntity> implements DpryService {


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<DpryEntity> page = this.selectPage(
                new Query<DpryEntity>(params).getPage(),
                new EntityWrapper<DpryEntity>()
        );
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, Wrapper<DpryEntity> wrapper) {
		  Page<DpryView> page =new Query<DpryView>(params).getPage();
	        page.setRecords(baseMapper.selectListView(page,wrapper));
	    	PageUtils pageUtil = new PageUtils(page);
	    	return pageUtil;
 	}
    
    @Override
	public List<DpryVO> selectListVO(Wrapper<DpryEntity> wrapper) {
 		return baseMapper.selectListVO(wrapper);
	}
	
	@Override
	public DpryVO selectVO(Wrapper<DpryEntity> wrapper) {
 		return baseMapper.selectVO(wrapper);
	}
	
	@Override
	public List<DpryView> selectListView(Wrapper<DpryEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public DpryView selectView(Wrapper<DpryEntity> wrapper) {
		return baseMapper.selectView(wrapper);
	}


}
