package com.earlywarning.service;

import com.earlywarning.common.ServerResponse;
import com.earlywarning.entity.system.Page;
import com.earlywarning.entity.system.PageData;
import com.earlywarning.mapper.AppMenuMapper;
import com.earlywarning.util.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AppMenuService extends BaseService {
    @Autowired
    private AppMenuMapper _mapper;

    /**
     * 添加菜单管理 重复数据不能添加
     */
    @Transactional
    public ServerResponse<String> addAppMenuNo(PageData pd) {
        if (Tools.isObjEmpty(pd)) {
            return ServerResponse.createByErrorMessage("删除菜单管理参数错误");
        }
        List<PageData> list = _mapper.queryAppMenuKey(pd);
        if (list.size() > 0) return ServerResponse.createByErrorMessage("菜单管理已存在");
        int rowCount = _mapper.addAppMenu(pd);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("添加菜单管理成功");
        }
        return ServerResponse.createByErrorMessage("添加菜单管理失败");
    }

    /**
     * 添加菜单管理 重复数据可以添加
     */
    @Transactional
    public ServerResponse<String> addAppMenuAll(PageData pd) {
        int rowCount = _mapper.addAppMenu(pd);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("添加菜单管理成功");
        }
        return ServerResponse.createByErrorMessage("添加菜单管理失败");
    }

    /**
     * 根据id删除菜单管理数据
     */
    public ServerResponse<String> deleteAppMenu(PageData pd) {
        if (Tools.isObjEmpty(pd.get("id"))) {
            return ServerResponse.createByErrorMessage("删除菜单管理参数错误");
        }
        int rowCount = _mapper.deleteAppMenuId(pd);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("删除菜单管理成功");
        }
        return ServerResponse.createByErrorMessage("删除菜单管理失败");
    }

    /**
     * 根据id更新菜单管理数据
     */
    @Transactional
    public ServerResponse<String> updateAppMenu(PageData pd) {
        if (Tools.isEmpty(pd.getString("id"))) {
            return ServerResponse.createByErrorMessage("修改菜单管理参数错误");
        }
        int rowCount = _mapper.updateAppMenu(pd);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("修改菜单管理成功");
        }
        return ServerResponse.createByErrorMessage("修改菜单管理失败");
    }

    /**
     * 获取菜单管理数据(非分页,搜索功能)
     */
    public ServerResponse<List<PageData>> queryAppMenuKey(PageData pd) {
        if (Tools.isObjEmpty(pd)) {
            return ServerResponse.badArgument();
        }
        List<PageData> list = _mapper.queryAppMenuKey(pd);
        return ServerResponse.createBySuccess(list);
    }

    /**
     * 获取菜单管理列表数据 分页
     */
    public List<PageData> queryPageAppMenuKeyList(Page pd) {
        List<PageData> list = _mapper.queryPageAppMenuKeyList(pd);
        return list;
    }
}
