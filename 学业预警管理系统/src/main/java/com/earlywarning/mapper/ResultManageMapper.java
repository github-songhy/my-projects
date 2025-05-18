package com.earlywarning.mapper;

import com.earlywarning.entity.system.Page;
import com.earlywarning.entity.system.PageData;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultManageMapper {
    /**
     * 添加数据
     */
    int addResultManage(PageData pd);

    /**
     * 根据id删除数据
     */
    int deleteResultManageId(PageData pd);

    /**
     * 根据id更新数据
     */
    int updateResultManage(PageData pd);

    /**
     * 获取用户列表数据 不分页
     */
    List<PageData> queryResultManageKey(PageData pd);

    /**
     * 获取用户列表数据 分页
     */
    List<PageData> queryPageResultManageKeyList(Page page);

    /**
     * 根据学年获取成绩列表
     */
    List<PageData> queryResultManageBySessionManageId(@Param("session_manage_id") String session_manage_id);
}


