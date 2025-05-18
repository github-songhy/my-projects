package com.earlywarning.mapper;

import com.earlywarning.entity.system.Page;
import com.earlywarning.entity.system.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StuViolationMapper {
    /**
     * 添加数据
     */
    int addStuViolation(PageData pd);

    /**
     * 根据id删除数据
     */
    int deleteStuViolationId(PageData pd);

    /**
     * 根据id更新数据
     */
    int updateStuViolation(PageData pd);

    /**
     * 获取用户列表数据 不分页
     */
    List<PageData> queryStuViolationKey(PageData pd);

    /**
     * 获取用户列表数据 分页
     */
    List<PageData> queryPageStuViolationKeyList(Page page);
}
