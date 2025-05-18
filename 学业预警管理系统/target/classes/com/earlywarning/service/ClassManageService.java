package com.earlywarning.service;

import com.earlywarning.common.ServerResponse;
import com.earlywarning.entity.system.Page;
import com.earlywarning.entity.system.PageData;
import com.earlywarning.mapper.ClassManageMapper;
import com.earlywarning.util.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClassManageService extends BaseService {
    @Autowired
    private ClassManageMapper _mapper;

    /**
     * 添加班级管理 重复数据不能添加
     */
    @Transactional
    public ServerResponse<String> addClassManageNo(PageData pd) {
        if (Tools.isObjEmpty(pd)) {
            return ServerResponse.createByErrorMessage("删除班级管理参数错误");
        }
        List<PageData> list = _mapper.queryClassManageKey(pd);
        if (list.size() > 0) return ServerResponse.createByErrorMessage("班级管理已存在");
        int rowCount = _mapper.addClassManage(pd);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("添加班级管理成功");
        }
        return ServerResponse.createByErrorMessage("添加班级管理失败");
    }

    /**
     * 添加班级管理 重复数据可以添加
     */
    @Transactional
    public ServerResponse<String> addClassManageAll(PageData pd) {
        int rowCount = _mapper.addClassManage(pd);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("添加班级管理成功");
        }
        return ServerResponse.createByErrorMessage("添加班级管理失败");
    }

    /**
     * 根据id删除班级管理数据
     */
    public ServerResponse<String> deleteClassManage(PageData pd) {
        if (Tools.isObjEmpty(pd.get("id"))) {
            return ServerResponse.createByErrorMessage("删除班级管理参数错误");
        }
        int rowCount = _mapper.deleteClassManageId(pd);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("删除班级管理成功");
        }
        return ServerResponse.createByErrorMessage("删除班级管理失败");
    }

    /**
     * 根据id更新班级管理数据
     */
    @Transactional
    public ServerResponse<String> updateClassManage(PageData pd) {
        if (Tools.isEmpty(pd.getString("id"))) {
            return ServerResponse.createByErrorMessage("修改班级管理参数错误");
        }
        int rowCount = _mapper.updateClassManage(pd);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("修改班级管理成功");
        }
        return ServerResponse.createByErrorMessage("修改班级管理失败");
    }

    /**
     * 获取班级管理数据(非分页,搜索功能)
     */
    public ServerResponse<List<PageData>> queryClassManageKey(PageData pd) {
        if (Tools.isObjEmpty(pd)) {
            return ServerResponse.badArgument();
        }
        List<PageData> list = _mapper.queryClassManageKey(pd);
        return ServerResponse.createBySuccess(list);
    }

    /**
     * 获取班级管理列表数据 分页
     */
    public List<PageData> queryPageClassManageKeyList(Page pd) {
        List<PageData> list = _mapper.queryPageClassManageKeyList(pd);
        return list;
    }
}
