package com.earlywarning.service;

import com.earlywarning.common.ServerResponse;
import com.earlywarning.entity.system.Page;
import com.earlywarning.entity.system.PageData;
import com.earlywarning.mapper.NewsManageMapper;
import com.earlywarning.util.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NewsManageService extends BaseService {
    @Autowired
    private NewsManageMapper _mapper;

    /**
     * 添加公告管理 重复数据不能添加
     */
    @Transactional
    public ServerResponse<String> addNewsManageNo(PageData pd) {
        if (Tools.isObjEmpty(pd)) {
            return ServerResponse.createByErrorMessage("删除公告管理参数错误");
        }
        List<PageData> list = _mapper.queryNewsManageKey(pd);
        if (list.size() > 0) return ServerResponse.createByErrorMessage("公告管理已存在");
        int rowCount = _mapper.addNewsManage(pd);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("添加公告管理成功");
        }
        return ServerResponse.createByErrorMessage("添加公告管理失败");
    }

    /**
     * 添加公告管理 重复数据可以添加
     */
    @Transactional
    public ServerResponse<String> addNewsManageAll(PageData pd) {
        int rowCount = _mapper.addNewsManage(pd);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("添加公告管理成功");
        }
        return ServerResponse.createByErrorMessage("添加公告管理失败");
    }

    /**
     * 根据id删除公告管理数据
     */
    public ServerResponse<String> deleteNewsManage(PageData pd) {
        if (Tools.isObjEmpty(pd.get("id"))) {
            return ServerResponse.createByErrorMessage("删除公告管理参数错误");
        }
        int rowCount = _mapper.deleteNewsManageId(pd);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("删除公告管理成功");
        }
        return ServerResponse.createByErrorMessage("删除公告管理失败");
    }

    /**
     * 根据id更新公告管理数据
     */
    @Transactional
    public ServerResponse<String> updateNewsManage(PageData pd) {
        if (Tools.isEmpty(pd.getString("id"))) {
            return ServerResponse.createByErrorMessage("修改公告管理参数错误");
        }
        int rowCount = _mapper.updateNewsManage(pd);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("修改公告管理成功");
        }
        return ServerResponse.createByErrorMessage("修改公告管理失败");
    }

    /**
     * 获取公告管理数据(非分页,搜索功能)
     */
    public ServerResponse<List<PageData>> queryNewsManageKey(PageData pd) {
        if (Tools.isObjEmpty(pd)) {
            return ServerResponse.badArgument();
        }
        List<PageData> list = _mapper.queryNewsManageKey(pd);
        return ServerResponse.createBySuccess(list);
    }

    /**
     * 获取公告管理列表数据 分页
     */
    public List<PageData> queryPageNewsManageKeyList(Page pd) {
        List<PageData> list = _mapper.queryPageNewsManageKeyList(pd);
        return list;
    }
}
