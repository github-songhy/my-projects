package com.earlywarning.service;

import com.earlywarning.common.ServerResponse;
import com.earlywarning.entity.system.Page;
import com.earlywarning.entity.system.PageData;
import com.earlywarning.mapper.SpecialSubjectMapper;
import com.earlywarning.util.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SpecialSubjectService extends BaseService {
    @Autowired
    private SpecialSubjectMapper _mapper;

    /**
     * 添加专业管理 重复数据不能添加
     */
    @Transactional
    public ServerResponse<String> addSpecialSubjectNo(PageData pd) {
        if (Tools.isObjEmpty(pd)) {
            return ServerResponse.createByErrorMessage("删除专业管理参数错误");
        }
        List<PageData> list = _mapper.querySpecialSubjectKey(pd);
        if (list.size() > 0) return ServerResponse.createByErrorMessage("专业管理已存在");
        int rowCount = _mapper.addSpecialSubject(pd);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("添加专业管理成功");
        }
        return ServerResponse.createByErrorMessage("添加专业管理失败");
    }

    /**
     * 添加专业管理 重复数据可以添加
     */
    @Transactional
    public ServerResponse<String> addSpecialSubjectAll(PageData pd) {
        int rowCount = _mapper.addSpecialSubject(pd);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("添加专业管理成功");
        }
        return ServerResponse.createByErrorMessage("添加专业管理失败");
    }

    /**
     * 根据id删除专业管理数据
     */
    public ServerResponse<String> deleteSpecialSubject(PageData pd) {
        if (Tools.isObjEmpty(pd.get("id"))) {
            return ServerResponse.createByErrorMessage("删除专业管理参数错误");
        }
        int rowCount = _mapper.deleteSpecialSubjectId(pd);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("删除专业管理成功");
        }
        return ServerResponse.createByErrorMessage("删除专业管理失败");
    }

    /**
     * 根据id更新专业管理数据
     */
    @Transactional
    public ServerResponse<String> updateSpecialSubject(PageData pd) {
        if (Tools.isEmpty(pd.getString("id"))) {
            return ServerResponse.createByErrorMessage("修改专业管理参数错误");
        }
        int rowCount = _mapper.updateSpecialSubject(pd);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("修改专业管理成功");
        }
        return ServerResponse.createByErrorMessage("修改专业管理失败");
    }

    /**
     * 获取专业管理数据(非分页,搜索功能)
     */
    public ServerResponse<List<PageData>> querySpecialSubjectKey(PageData pd) {
        if (Tools.isObjEmpty(pd)) {
            return ServerResponse.badArgument();
        }
        List<PageData> list = _mapper.querySpecialSubjectKey(pd);
        return ServerResponse.createBySuccess(list);
    }

    /**
     * 获取专业管理列表数据 分页
     */
    public List<PageData> queryPageSpecialSubjectKeyList(Page pd) {
        List<PageData> list = _mapper.queryPageSpecialSubjectKeyList(pd);
        return list;
    }
}
