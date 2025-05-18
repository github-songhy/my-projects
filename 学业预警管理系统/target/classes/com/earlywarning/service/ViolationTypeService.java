package com.earlywarning.service;

import com.earlywarning.common.Chars;
import com.earlywarning.common.ServerResponse;
import com.earlywarning.entity.system.Page;
import com.earlywarning.entity.system.PageData;
import com.earlywarning.mapper.ViolationTypeMapper;
import com.earlywarning.util.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ViolationTypeService extends BaseService {
    @Autowired
    private ViolationTypeMapper _mapper;

    /**
     * 添加违纪处分类型 重复数据不能添加
     */
    @Transactional
    public ServerResponse<String> addViolationTypeNo(PageData pd) {
        if (Tools.isObjEmpty(pd)) {
            return ServerResponse.createByErrorMessage("删除违纪处分类型参数错误");
        }
        List<PageData> list = _mapper.queryViolationTypeKey(pd);
        if (list.size() > 0) return ServerResponse.createByErrorMessage("违纪处分类型已存在");
        int rowCount = _mapper.addViolationType(pd);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("添加违纪处分类型成功");
        }
        return ServerResponse.createByErrorMessage("添加违纪处分类型失败");
    }

    /**
     * 添加违纪处分类型 重复数据可以添加
     */
    @Transactional
    public ServerResponse<String> addViolationTypeAll(PageData pd) {
        int rowCount = _mapper.addViolationType(pd);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("添加违纪处分类型成功");
        }
        return ServerResponse.createByErrorMessage("添加违纪处分类型失败");
    }

    /**
     * 根据id删除违纪处分类型数据
     */
    public ServerResponse<String> deleteViolationType(PageData pd) {
        if (Tools.isObjEmpty(pd.get("id"))) {
            return ServerResponse.createByErrorMessage("删除违纪处分类型参数错误");
        }
        int rowCount = _mapper.deleteViolationTypeId(pd);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("删除违纪处分类型成功");
        }
        return ServerResponse.createByErrorMessage("删除违纪处分类型失败");
    }

    /**
     * 根据id更新违纪处分类型数据
     */
    @Transactional
    public ServerResponse<String> updateViolationType(PageData pd) {
        if (Tools.isEmpty(pd.getString("id"))) {
            return ServerResponse.createByErrorMessage("修改违纪处分类型参数错误");
        }
        int rowCount = _mapper.updateViolationType(pd);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("修改违纪处分类型成功");
        }
        return ServerResponse.createByErrorMessage("修改违纪处分类型失败");
    }

    /**
     * 获取违纪处分类型数据(非分页,搜索功能)
     */
    public ServerResponse<List<PageData>> queryViolationTypeKey(PageData pd) {
        if (Tools.isObjEmpty(pd)) {
            return ServerResponse.badArgument();
        }
        List<PageData> list = _mapper.queryViolationTypeKey(pd);
        return ServerResponse.createBySuccess(list);
    }

    /**
     * 获取违纪处分类型列表数据 分页
     */
    public List<PageData> queryPageViolationTypeKeyList(Page pd) {
        List<PageData> list = _mapper.queryPageViolationTypeKeyList(pd);
        return list;
    }


    public List<Chars> queryViolationPie() {
        return _mapper.queryViolationPie();
    }

    ;

}
