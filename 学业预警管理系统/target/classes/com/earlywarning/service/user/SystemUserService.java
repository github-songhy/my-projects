package com.earlywarning.service.user;

import com.earlywarning.common.ServerResponse;
import com.earlywarning.entity.system.Page;
import com.earlywarning.entity.system.PageData;
import com.earlywarning.mapper.user.SystemUserMapper;
import com.earlywarning.service.BaseService;
import com.earlywarning.util.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 */
@Service
public class SystemUserService extends BaseService {

    @Autowired
    private SystemUserMapper systemUserMapper;

    public List<PageData> getPageSystemUserList(Page page) {
        return systemUserMapper.queryPageSystemUserList(page);
    }

    public ServerResponse<List<PageData>> getSystemUserList(PageData pd) {
        if (Tools.isObjEmpty("111")) {
            return ServerResponse.badArgument();
        }
        return ServerResponse.createBySuccess(systemUserMapper.getSystemUserList(pd));
    }

    /**
     * 添加
     *
     * @param pd
     * @return
     */
    @Transactional
    public ServerResponse<String> addSystemUser(PageData pd) {


        int rowCount = systemUserMapper.addSystemUser(pd);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("添加系统用户成功");
        }

        return ServerResponse.createByErrorMessage("添加系统用户失败");
    }

    /**
     * 修改系统用户
     *
     * @param pd
     * @return
     */
    @Transactional
    public ServerResponse<String> updateSystemUserByid(PageData pd) {

        if (Tools.isObjEmpty(pd.get("id"))) {
            return ServerResponse.createByErrorMessage("修改系统用户参数错误");
        }

        int rowCount = systemUserMapper.updateSystemUserByid(pd);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("修改系统用户成功");
        }

        return ServerResponse.createByErrorMessage("修改系统用户失败");
    }

    /**
     * 获取单条数据
     *
     * @param pd
     * @return
     */
    public ServerResponse<PageData> getSystemUserByColumn(PageData pd) {

        if (Tools.isObjEmpty(pd.get("id"))) {
            return ServerResponse.createByErrorMessage("获取数据参数错误");
        }

        return ServerResponse.createBySuccess(systemUserMapper.getSystemUserByColumn(pd));
    }

    /**
     * 删除系统用户
     *
     * @param pd
     * @return
     */
    public ServerResponse<String> deleteSystemUserByColumn(PageData pd) {

        if (Tools.isObjEmpty(pd.get("id"))) {
            return ServerResponse.createByErrorMessage("删除系统用户参数错误");
        }

        int rowCount = systemUserMapper.deleteSystemUserByColumn(pd);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("删除系统用户成功");
        }

        return ServerResponse.createByErrorMessage("删除系统用户失败");
    }
}
