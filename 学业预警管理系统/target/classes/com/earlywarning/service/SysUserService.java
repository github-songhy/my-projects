package com.earlywarning.service;

import com.earlywarning.common.ServerResponse;
import com.earlywarning.entity.system.Page;
import com.earlywarning.entity.system.PageData;
import com.earlywarning.mapper.SysUserMapper;
import com.earlywarning.util.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SysUserService extends BaseService {
    @Autowired
    private SysUserMapper _mapper;

    /**
     * 添加用户管理 重复数据不能添加
     */
    @Transactional
    public ServerResponse<String> addSysUserNo(PageData pd) {
        if (Tools.isObjEmpty(pd)) {
            return ServerResponse.createByErrorMessage("删除用户管理参数错误");
        }
        List<PageData> list = _mapper.querySysUserKey(pd);
        if (list.size() > 0) return ServerResponse.createByErrorMessage("用户管理已存在");
        int rowCount = _mapper.addSysUser(pd);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("添加用户管理成功");
        }
        return ServerResponse.createByErrorMessage("添加用户管理失败");
    }

    /**
     * 添加用户管理 重复数据可以添加
     */
    @Transactional
    public ServerResponse<String> addSysUserAll(PageData pd) {
        int rowCount = _mapper.addSysUser(pd);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("添加用户管理成功");
        }
        return ServerResponse.createByErrorMessage("添加用户管理失败");
    }

    /**
     * 根据id删除用户管理数据
     */
    public ServerResponse<String> deleteSysUser(PageData pd) {
        if (Tools.isObjEmpty(pd.get("id"))) {
            return ServerResponse.createByErrorMessage("删除用户管理参数错误");
        }
        int rowCount = _mapper.deleteSysUserId(pd);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("删除用户管理成功");
        }
        return ServerResponse.createByErrorMessage("删除用户管理失败");
    }

    /**
     * 根据id更新用户管理数据
     */
    @Transactional
    public ServerResponse<String> updateSysUser(PageData pd) {
        if (Tools.isEmpty(pd.getString("id"))) {
            return ServerResponse.createByErrorMessage("修改用户管理参数错误");
        }
        int rowCount = _mapper.updateSysUser(pd);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("修改用户管理成功");
        }
        return ServerResponse.createByErrorMessage("修改用户管理失败");
    }

    /**
     * 获取用户管理数据(非分页,搜索功能)
     */
    public ServerResponse<List<PageData>> querySysUserKey(PageData pd) {
        if (Tools.isObjEmpty(pd)) {
            return ServerResponse.badArgument();
        }
        List<PageData> list = _mapper.querySysUserKey(pd);
        return ServerResponse.createBySuccess(list);
    }

    /**
     * 获取用户管理列表数据 分页
     */
    public List<PageData> queryPageSysUserKeyList(Page pd) {
        List<PageData> list = _mapper.queryPageSysUserKeyList(pd);
        return list;
    }
}
