package com.earlywarning.mapper;

import com.earlywarning.entity.system.Page;
import com.earlywarning.entity.system.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseManageMapper {
    /**
     * 添加数据
     */
    int addCourseManage(PageData pd);

    /**
     * 根据id删除数据
     */
    int deleteCourseManageId(PageData pd);

    /**
     * 根据id更新数据
     */
    int updateCourseManage(PageData pd);

    /**
     * 获取用户列表数据 不分页
     */
    List<PageData> queryCourseManageKey(PageData pd);

    /**
     * 获取用户列表数据 分页
     */
    List<PageData> queryPageCourseManageKeyList(Page page);
}
