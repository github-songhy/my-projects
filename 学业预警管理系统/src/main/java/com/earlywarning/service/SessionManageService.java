package com.earlywarning.service;

import com.earlywarning.common.ServerResponse;
import com.earlywarning.entity.system.Page;
import com.earlywarning.entity.system.PageData;
import com.earlywarning.mapper.SessionManageMapper;
import com.earlywarning.util.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SessionManageService extends BaseService {
    @Autowired
    private SessionManageMapper _mapper;

    /**
     * 添加学年管理 重复数据不能添加
     */
    @Transactional
    public ServerResponse<String> addSessionManageNo(PageData pd) {
        if (Tools.isObjEmpty(pd)) {
            return ServerResponse.createByErrorMessage("删除学年管理参数错误");
        }
        List<PageData> list = _mapper.querySessionManageKey(pd);
        if (list.size() > 0) return ServerResponse.createByErrorMessage("学年管理已存在");
        int rowCount = _mapper.addSessionManage(pd);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("添加学年管理成功");
        }
        return ServerResponse.createByErrorMessage("添加学年管理失败");
    }

    /**
     * 添加学年管理 重复数据可以添加
     */
    @Transactional
    public ServerResponse<String> addSessionManageAll(PageData pd) {
        int rowCount = _mapper.addSessionManage(pd);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("添加学年管理成功");
        }
        return ServerResponse.createByErrorMessage("添加学年管理失败");
    }

    /**
     * 根据id删除学年管理数据
     */
    public ServerResponse<String> deleteSessionManage(PageData pd) {
        if (Tools.isObjEmpty(pd.get("id"))) {
            return ServerResponse.createByErrorMessage("删除学年管理参数错误");
        }
        int rowCount = _mapper.deleteSessionManageId(pd);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("删除学年管理成功");
        }
        return ServerResponse.createByErrorMessage("删除学年管理失败");
    }

    /**
     * 根据id更新学年管理数据
     */
    @Transactional
    public ServerResponse<String> updateSessionManage(PageData pd) {
        if (Tools.isEmpty(pd.getString("id"))) {
            return ServerResponse.createByErrorMessage("修改学年管理参数错误");
        }
        int rowCount = _mapper.updateSessionManage(pd);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("修改学年管理成功");
        }
        return ServerResponse.createByErrorMessage("修改学年管理失败");
    }

    /**
     * 获取学年管理数据(非分页,搜索功能)
     */
    public ServerResponse<List<PageData>> querySessionManageKey(PageData pd) {
        if (Tools.isObjEmpty(pd)) {
            return ServerResponse.badArgument();
        }
        List<PageData> list = _mapper.querySessionManageKey(pd);
        return ServerResponse.createBySuccess(list);
    }

    /**
     * 获取学年管理列表数据 分页
     */
    public List<PageData> queryPageSessionManageKeyList(Page pd) {
        List<PageData> list = _mapper.queryPageSessionManageKeyList(pd);
        return list;
    }
}
