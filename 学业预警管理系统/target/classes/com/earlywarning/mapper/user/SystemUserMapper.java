package com.earlywarning.mapper.user;

import com.earlywarning.entity.system.Page;
import com.earlywarning.entity.system.PageData;

import java.util.List;

public interface SystemUserMapper {

    int deleteSystemUserByColumn(PageData pd);

    int addSystemUser(PageData pd);

    int addSystemUserSelective(PageData pd);

    int addSystemUserBatch(PageData pd);

    PageData getSystemUserByColumn(PageData pd);

    int updateSystemUserByid(PageData pd);

    int updateSystemUserByColumn(PageData pd);

    //int updateSystemUserBatch(PageData pd);

    List<PageData> queryPageSystemUserList(Page page);

    List<PageData> getSystemUserList(PageData pd);


}
