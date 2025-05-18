package com.earlywarning.mapper;

import com.earlywarning.entity.system.Page;
import com.earlywarning.entity.system.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassManageMapper {
    /**
     * 添加数据
     */
    int addClassManage(PageData pd);

    /**
     * 根据id删除数据
     */
    int deleteClassManageId(PageData pd);

    /**
     * 根据id更新数据
     */
    int updateClassManage(PageData pd);

    /**
     * 获取用户列表数据 不分页
     */
    List<PageData> queryClassManageKey(PageData pd);

    /**
     * 获取用户列表数据 分页
     */
    List<PageData> queryPageClassManageKeyList(Page page);
}
