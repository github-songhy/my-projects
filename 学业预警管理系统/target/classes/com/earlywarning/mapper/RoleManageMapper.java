package com.earlywarning.mapper;

import com.earlywarning.entity.system.Page;
import com.earlywarning.entity.system.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleManageMapper {
    /**
     * 添加数据
     */
    int addRoleManage(PageData pd);

    /**
     * 根据id删除数据
     */
    int deleteRoleManageId(PageData pd);

    /**
     * 根据id更新数据
     */
    int updateRoleManage(PageData pd);

    /**
     * 获取用户列表数据 不分页
     */
    List<PageData> queryRoleManageKey(PageData pd);

    /**
     * 获取用户列表数据 分页
     */
    List<PageData> queryPageRoleManageKeyList(Page page);
}
