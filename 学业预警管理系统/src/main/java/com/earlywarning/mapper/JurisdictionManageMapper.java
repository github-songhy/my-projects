package com.earlywarning.mapper;

import com.earlywarning.entity.system.Page;
import com.earlywarning.entity.system.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JurisdictionManageMapper {
    /**
     * 添加数据
     */
    int addJurisdictionManage(PageData pd);

    /**
     * 根据id删除数据
     */
    int deleteJurisdictionManageId(PageData pd);

    /**
     * 根据id更新数据
     */
    int updateJurisdictionManage(PageData pd);

    /**
     * 获取用户列表数据 不分页
     */
    List<PageData> queryJurisdictionManageKey(PageData pd);

    /**
     * 获取用户列表数据 分页
     */
    List<PageData> queryPageJurisdictionManageKeyList(Page page);
}
