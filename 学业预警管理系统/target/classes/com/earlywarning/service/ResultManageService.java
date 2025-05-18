package com.earlywarning.service;

import com.earlywarning.common.ServerResponse;
import com.earlywarning.entity.system.Page;
import com.earlywarning.entity.system.PageData;
import com.earlywarning.mapper.ResultManageMapper;
import com.earlywarning.util.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ResultManageService extends BaseService {
    @Autowired
    private ResultManageMapper _mapper;

    /**
     * 添加成绩管理 重复数据不能添加
     */
    @Transactional
    public ServerResponse<String> addResultManageNo(PageData pd) {
        if (Tools.isObjEmpty(pd)) {
            return ServerResponse.createByErrorMessage("删除成绩管理参数错误");
        }
        List<PageData> list = _mapper.queryResultManageKey(pd);
        if (list.size() > 0) return ServerResponse.createByErrorMessage("成绩管理已存在");
        int rowCount = _mapper.addResultManage(pd);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("添加成绩管理成功");
        }
        return ServerResponse.createByErrorMessage("添加成绩管理失败");
    }

    /**
     * 添加成绩管理 重复数据可以添加
     */
    @Transactional
    public ServerResponse<String> addResultManageAll(PageData pd) {
        int rowCount = _mapper.addResultManage(pd);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("添加成绩管理成功");
        }
        return ServerResponse.createByErrorMessage("添加成绩管理失败");
    }

    /**
     * 根据id删除成绩管理数据
     */
    public ServerResponse<String> deleteResultManage(PageData pd) {
        if (Tools.isObjEmpty(pd.get("id"))) {
            return ServerResponse.createByErrorMessage("删除成绩管理参数错误");
        }
        int rowCount = _mapper.deleteResultManageId(pd);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("删除成绩管理成功");
        }
        return ServerResponse.createByErrorMessage("删除成绩管理失败");
    }

    /**
     * 根据id更新成绩管理数据
     */
    @Transactional
    public ServerResponse<String> updateResultManage(PageData pd) {
        if (Tools.isEmpty(pd.getString("id"))) {
            return ServerResponse.createByErrorMessage("修改成绩管理参数错误");
        }
        int rowCount = _mapper.updateResultManage(pd);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("修改成绩管理成功");
        }
        return ServerResponse.createByErrorMessage("修改成绩管理失败");
    }

    /**
     * 获取成绩管理数据(非分页,搜索功能)
     */
    public ServerResponse<List<PageData>> queryResultManageKey(PageData pd) {
        if (Tools.isObjEmpty(pd)) {
            return ServerResponse.badArgument();
        }
        List<PageData> list = _mapper.queryResultManageKey(pd);
        return ServerResponse.createBySuccess(list);
    }

    /**
     * 获取成绩管理列表数据 分页
     */
    public List<PageData> queryPageResultManageKeyList(Page pd) {
        List<PageData> list = _mapper.queryPageResultManageKeyList(pd);
        return list;
    }


    /**
     * 根据学年获取成绩列表
     */
    public List<PageData> queryResultManageBySessionManageId(String session_manage_id) {
        List<PageData> list = _mapper.queryResultManageBySessionManageId(session_manage_id);
        return list;
    }
}
