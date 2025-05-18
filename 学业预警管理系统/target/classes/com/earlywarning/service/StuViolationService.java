package com.earlywarning.service;

import com.earlywarning.common.ServerResponse;
import com.earlywarning.entity.system.Page;
import com.earlywarning.entity.system.PageData;
import com.earlywarning.mapper.StuViolationMapper;
import com.earlywarning.util.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StuViolationService extends BaseService {
    @Autowired
    private StuViolationMapper _mapper;

    /**
     * 添加违纪处分管理 重复数据不能添加
     */
    @Transactional
    public ServerResponse<String> addStuViolationNo(PageData pd) {
        if (Tools.isObjEmpty(pd)) {
            return ServerResponse.createByErrorMessage("删除违纪处分管理参数错误");
        }
        List<PageData> list = _mapper.queryStuViolationKey(pd);
        if (list.size() > 0) return ServerResponse.createByErrorMessage("违纪处分管理已存在");
        int rowCount = _mapper.addStuViolation(pd);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("添加违纪处分管理成功");
        }
        return ServerResponse.createByErrorMessage("添加违纪处分管理失败");
    }

    /**
     * 添加违纪处分管理 重复数据可以添加
     */
    @Transactional
    public ServerResponse<String> addStuViolationAll(PageData pd) {
        int rowCount = _mapper.addStuViolation(pd);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("添加违纪处分管理成功");
        }
        return ServerResponse.createByErrorMessage("添加违纪处分管理失败");
    }

    /**
     * 根据id删除违纪处分管理数据
     */
    public ServerResponse<String> deleteStuViolation(PageData pd) {
        if (Tools.isObjEmpty(pd.get("id"))) {
            return ServerResponse.createByErrorMessage("删除违纪处分管理参数错误");
        }
        int rowCount = _mapper.deleteStuViolationId(pd);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("删除违纪处分管理成功");
        }
        return ServerResponse.createByErrorMessage("删除违纪处分管理失败");
    }

    /**
     * 根据id更新违纪处分管理数据
     */
    @Transactional
    public ServerResponse<String> updateStuViolation(PageData pd) {
        if (Tools.isEmpty(pd.getString("id"))) {
            return ServerResponse.createByErrorMessage("修改违纪处分管理参数错误");
        }
        int rowCount = _mapper.updateStuViolation(pd);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("修改违纪处分管理成功");
        }
        return ServerResponse.createByErrorMessage("修改违纪处分管理失败");
    }

    /**
     * 获取违纪处分管理数据(非分页,搜索功能)
     */
    public ServerResponse<List<PageData>> queryStuViolationKey(PageData pd) {
        if (Tools.isObjEmpty(pd)) {
            return ServerResponse.badArgument();
        }
        List<PageData> list = _mapper.queryStuViolationKey(pd);
        return ServerResponse.createBySuccess(list);
    }

    /**
     * 获取违纪处分管理列表数据 分页
     */
    public List<PageData> queryPageStuViolationKeyList(Page pd) {
        List<PageData> list = _mapper.queryPageStuViolationKeyList(pd);
        return list;
    }
}
