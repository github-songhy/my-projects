package com.earlywarning.service;

import com.earlywarning.common.ServerResponse;
import com.earlywarning.entity.system.Page;
import com.earlywarning.entity.system.PageData;
import com.earlywarning.mapper.JurisdictionManageMapper;
import com.earlywarning.util.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class JurisdictionManageService extends BaseService {
    @Autowired
    private JurisdictionManageMapper _mapper;

    /**
     * 添加权限管理 重复数据不能添加
     */
    @Transactional
    public ServerResponse<String> addJurisdictionManageNo(PageData pd) {
        if (Tools.isObjEmpty(pd)) {
            return ServerResponse.createByErrorMessage("删除权限管理参数错误");
        }
        List<PageData> list = _mapper.queryJurisdictionManageKey(pd);
        if (list.size() > 0) return ServerResponse.createByErrorMessage("权限管理已存在");
        int rowCount = _mapper.addJurisdictionManage(pd);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("添加权限管理成功");
        }
        return ServerResponse.createByErrorMessage("添加权限管理失败");
    }

    /**
     * 添加权限管理 重复数据可以添加
     */
    @Transactional
    public ServerResponse<String> addJurisdictionManageAll(PageData pd) {
        int rowCount = _mapper.addJurisdictionManage(pd);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("添加权限管理成功");
        }
        return ServerResponse.createByErrorMessage("添加权限管理失败");
    }

    /**
     * 根据id删除权限管理数据
     */
    public ServerResponse<String> deleteJurisdictionManage(PageData pd) {
        if (Tools.isObjEmpty(pd.get("id"))) {
            return ServerResponse.createByErrorMessage("删除权限管理参数错误");
        }
        int rowCount = _mapper.deleteJurisdictionManageId(pd);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("删除权限管理成功");
        }
        return ServerResponse.createByErrorMessage("删除权限管理失败");
    }

    /**
     * 根据id更新权限管理数据
     */
    @Transactional
    public ServerResponse<String> updateJurisdictionManage(PageData pd) {
        if (Tools.isEmpty(pd.getString("id"))) {
            return ServerResponse.createByErrorMessage("修改权限管理参数错误");
        }
        int rowCount = _mapper.updateJurisdictionManage(pd);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("修改权限管理成功");
        }
        return ServerResponse.createByErrorMessage("修改权限管理失败");
    }

    /**
     * 获取权限管理数据(非分页,搜索功能)
     */
    public ServerResponse<List<PageData>> queryJurisdictionManageKey(PageData pd) {
        if (Tools.isObjEmpty(pd)) {
            return ServerResponse.badArgument();
        }
        List<PageData> list = _mapper.queryJurisdictionManageKey(pd);
        return ServerResponse.createBySuccess(list);
    }

    /**
     * 获取权限管理列表数据 分页
     */
    public List<PageData> queryPageJurisdictionManageKeyList(Page pd) {
        List<PageData> list = _mapper.queryPageJurisdictionManageKeyList(pd);
        return list;
    }
}
