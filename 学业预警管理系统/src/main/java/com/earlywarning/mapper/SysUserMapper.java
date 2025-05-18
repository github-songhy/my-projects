package com.earlywarning.mapper;

import com.earlywarning.entity.system.Page;
import com.earlywarning.entity.system.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysUserMapper {
    /**
     * 添加数据
     */
    int addSysUser(PageData pd);

    /**
     * 根据id删除数据
     */
    int deleteSysUserId(PageData pd);

    /**
     * 根据id更新数据
     */
    int updateSysUser(PageData pd);

    /**
     * 获取用户列表数据 不分页
     */
    List<PageData> querySysUserKey(PageData pd);

    /**
     * 获取用户列表数据 分页
     */
    List<PageData> queryPageSysUserKeyList(Page page);
}
