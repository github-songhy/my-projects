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


import com.dao.FenpeijihuaDao;
import com.entity.FenpeijihuaEntity;
import com.service.FenpeijihuaService;
import com.entity.vo.FenpeijihuaVO;
import com.entity.view.FenpeijihuaView;

@Service("fenpeijihuaService")
public class FenpeijihuaServiceImpl extends ServiceImpl<FenpeijihuaDao, FenpeijihuaEntity> implements FenpeijihuaService {


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<FenpeijihuaEntity> page = this.selectPage(
                new Query<FenpeijihuaEntity>(params).getPage(),
                new EntityWrapper<FenpeijihuaEntity>()
        );
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, Wrapper<FenpeijihuaEntity> wrapper) {
		  Page<FenpeijihuaView> page =new Query<FenpeijihuaView>(params).getPage();
	        page.setRecords(baseMapper.selectListView(page,wrapper));
	    	PageUtils pageUtil = new PageUtils(page);
	    	return pageUtil;
 	}
    
    @Override
	public List<FenpeijihuaVO> selectListVO(Wrapper<FenpeijihuaEntity> wrapper) {
 		return baseMapper.selectListVO(wrapper);
	}
	
	@Override
	public FenpeijihuaVO selectVO(Wrapper<FenpeijihuaEntity> wrapper) {
 		return baseMapper.selectVO(wrapper);
	}
	
	@Override
	public List<FenpeijihuaView> selectListView(Wrapper<FenpeijihuaEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public FenpeijihuaView selectView(Wrapper<FenpeijihuaEntity> wrapper) {
		return baseMapper.selectView(wrapper);
	}


}
