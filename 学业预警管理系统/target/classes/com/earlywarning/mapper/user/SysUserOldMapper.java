package com.earlywarning.mapper.user;

import com.earlywarning.entity.system.PageData;

public interface SysUserOldMapper {

    /**
     * 验证用户账号及密码是否存在
     */
    PageData getLoginValidation(PageData pd);

    int addSystemUser(PageData pd);

    PageData getSystemUserByColumn(PageData pd);

}
