package com.earlywarning.service.user;

import com.earlywarning.common.ServerResponse;
import com.earlywarning.entity.system.PageData;
import com.earlywarning.mapper.user.SysUserOldMapper;
import com.earlywarning.util.Tools;
import org.springframework.stereotype.Service;

@Service
public class SysUserOldService {


    private final SysUserOldMapper sysuserOldMapper;

    public SysUserOldService(SysUserOldMapper sysuserOldMapper) {
        this.sysuserOldMapper = sysuserOldMapper;
    }

    /**
     * 验证用户账号及密码是否存在
     */
    public PageData getLoginValidation(PageData pd) {
        return sysuserOldMapper.getLoginValidation(pd);
    }


    public ServerResponse registerUser(PageData pd) {

        if (Tools.isEmpty(pd.getString("USERNAME")) || Tools.isEmpty(pd.getString("PASSWORD"))) {
            return ServerResponse.badArgument();
        }

        final PageData pageData = new PageData();
        pageData.put("USERNAME", pd.getString("USERNAME"));
        pageData.put("luck", true);
        final PageData systemUserByColumn = sysuserOldMapper.getSystemUserByColumn(pageData);

        if (!Tools.isObjEmpty(systemUserByColumn)) {
            return ServerResponse.createByErrorMessage("该用户已存在");
        }

        final int i = sysuserOldMapper.addSystemUser(pd);

        if (i > 0) return ServerResponse.createBySuccessMessage("注册成功");

        return ServerResponse.createByErrorMessage("注册失败");
    }


}
