package com.earlywarning.service;

import com.earlywarning.common.ServerResponse;
import com.earlywarning.entity.system.Page;
import com.earlywarning.entity.system.PageData;
import com.earlywarning.mapper.AppUserMapper;
import com.earlywarning.util.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AppUserService extends BaseService {
    @Autowired
    private AppUserMapper _mapper;

    /**
     * 添加学生管理 重复数据不能添加
     */
    @Transactional
    public ServerResponse<String> addAppUserNo(PageData pd) {
        if (Tools.isObjEmpty(pd)) {
            return ServerResponse.createByErrorMessage("删除学生管理参数错误");
        }
        List<PageData> list = _mapper.queryAppUserKey(pd);
        if (list.size() > 0) return ServerResponse.createByErrorMessage("学生管理已存在");
        int rowCount = _mapper.addAppUser(pd);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("添加学生管理成功");
        }
        return ServerResponse.createByErrorMessage("添加学生管理失败");
    }

    /**
     * 添加学生管理 重复数据可以添加
     */
    @Transactional
    public ServerResponse<String> addAppUserAll(PageData pd) {
        int rowCount = _mapper.addAppUser(pd);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("添加学生管理成功");
        }
        return ServerResponse.createByErrorMessage("添加学生管理失败");
    }

    /**
     * 根据id删除学生管理数据
     */
    public ServerResponse<String> deleteAppUser(PageData pd) {
        if (Tools.isObjEmpty(pd.get("id"))) {
            return ServerResponse.createByErrorMessage("删除学生管理参数错误");
        }
        int rowCount = _mapper.deleteAppUserId(pd);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("删除学生管理成功");
        }
        return ServerResponse.createByErrorMessage("删除学生管理失败");
    }

    /**
     * 根据id更新学生管理数据
     */
    @Transactional
    public ServerResponse<String> updateAppUser(PageData pd) {
        if (Tools.isEmpty(pd.getString("id"))) {
            return ServerResponse.createByErrorMessage("修改学生管理参数错误");
        }
        int rowCount = _mapper.updateAppUser(pd);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("修改学生管理成功");
        }
        return ServerResponse.createByErrorMessage("修改学生管理失败");
    }

    /**
     * 获取学生管理数据(非分页,搜索功能)
     */
    public ServerResponse<List<PageData>> queryAppUserKey(PageData pd) {
        if (Tools.isObjEmpty(pd)) {
            return ServerResponse.badArgument();
        }
        List<PageData> list = _mapper.queryAppUserKey(pd);
        return ServerResponse.createBySuccess(list);
    }

    /**
     * 获取学生管理列表数据 分页
     */
    public List<PageData> queryPageAppUserKeyList(Page pd) {
        List<PageData> list = _mapper.queryPageAppUserKeyList(pd);
        //System.out.println("*--------------------------------------------------*");
        //System.out.println(list.toString());
        return list;
    }
}
