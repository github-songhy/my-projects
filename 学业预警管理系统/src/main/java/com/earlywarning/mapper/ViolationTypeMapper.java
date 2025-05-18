package com.earlywarning.mapper;

import com.earlywarning.common.Chars;
import com.earlywarning.entity.system.Page;
import com.earlywarning.entity.system.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViolationTypeMapper {
    /**
     * 添加数据
     */
    int addViolationType(PageData pd);

    /**
     * 根据id删除数据
     */
    int deleteViolationTypeId(PageData pd);

    /**
     * 根据id更新数据
     */
    int updateViolationType(PageData pd);

    /**
     * 获取用户列表数据 不分页
     */
    List<PageData> queryViolationTypeKey(PageData pd);

    /**
     * 获取用户列表数据 分页
     */
    List<PageData> queryPageViolationTypeKeyList(Page page);

    List<Chars> queryViolationPie();

}

