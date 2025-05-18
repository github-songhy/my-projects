package com.earlywarning.service;

import com.earlywarning.common.ServerResponse;
import com.earlywarning.entity.system.Page;
import com.earlywarning.entity.system.PageData;
import com.earlywarning.mapper.RoleManageMapper;
import com.earlywarning.util.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleManageService extends BaseService {
    @Autowired
    private RoleManageMapper _mapper;

    /**
     * 添加角色管理 重复数据不能添加
     */
    @Transactional
    public ServerResponse<String> addRoleManageNo(PageData pd) {
        if (Tools.isObjEmpty(pd)) {
            return ServerResponse.createByErrorMessage("删除角色管理参数错误");
        }
        List<PageData> list = _mapper.queryRoleManageKey(pd);
        if (list.size() > 0) return ServerResponse.createByErrorMessage("角色管理已存在");
        int rowCount = _mapper.addRoleManage(pd);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("添加角色管理成功");
        }
        return ServerResponse.createByErrorMessage("添加角色管理失败");
    }

    /**
     * 添加角色管理 重复数据可以添加
     */
    @Transactional
    public ServerResponse<String> addRoleManageAll(PageData pd) {
        int rowCount = _mapper.addRoleManage(pd);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("添加角色管理成功");
        }
        return ServerResponse.createByErrorMessage("添加角色管理失败");
    }

    /**
     * 根据id删除角色管理数据
     */
    public ServerResponse<String> deleteRoleManage(PageData pd) {
        if (Tools.isObjEmpty(pd.get("id"))) {
            return ServerResponse.createByErrorMessage("删除角色管理参数错误");
        }
        int rowCount = _mapper.deleteRoleManageId(pd);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("删除角色管理成功");
        }
        return ServerResponse.createByErrorMessage("删除角色管理失败");
    }

    /**
     * 根据id更新角色管理数据
     */
    @Transactional
    public ServerResponse<String> updateRoleManage(PageData pd) {
        if (Tools.isEmpty(pd.getString("id"))) {
            return ServerResponse.createByErrorMessage("修改角色管理参数错误");
        }
        int rowCount = _mapper.updateRoleManage(pd);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("修改角色管理成功");
        }
        return ServerResponse.createByErrorMessage("修改角色管理失败");
    }

    /**
     * 获取角色管理数据(非分页,搜索功能)
     */
    public ServerResponse<List<PageData>> queryRoleManageKey(PageData pd) {
        if (Tools.isObjEmpty(pd)) {
            return ServerResponse.badArgument();
        }
        List<PageData> list = _mapper.queryRoleManageKey(pd);
        return ServerResponse.createBySuccess(list);
    }

    /**
     * 获取角色管理列表数据 分页
     */
    public List<PageData> queryPageRoleManageKeyList(Page pd) {
        List<PageData> list = _mapper.queryPageRoleManageKeyList(pd);
        return list;
    }
}
