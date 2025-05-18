package com.earlywarning.service;

import com.earlywarning.common.ServerResponse;
import com.earlywarning.entity.system.Page;
import com.earlywarning.entity.system.PageData;
import com.earlywarning.mapper.CourseManageMapper;
import com.earlywarning.util.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseManageService extends BaseService {
    @Autowired
    private CourseManageMapper _mapper;

    /**
     * 添加课程管理 重复数据不能添加
     */
    @Transactional
    public ServerResponse<String> addCourseManageNo(PageData pd) {
        if (Tools.isObjEmpty(pd)) {
            return ServerResponse.createByErrorMessage("删除课程管理参数错误");
        }
        List<PageData> list = _mapper.queryCourseManageKey(pd);
        if (list.size() > 0) return ServerResponse.createByErrorMessage("课程管理已存在");
        int rowCount = _mapper.addCourseManage(pd);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("添加课程管理成功");
        }
        return ServerResponse.createByErrorMessage("添加课程管理失败");
    }

    /**
     * 添加课程管理 重复数据可以添加
     */
    @Transactional
    public ServerResponse<String> addCourseManageAll(PageData pd) {
        int rowCount = _mapper.addCourseManage(pd);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("添加课程管理成功");
        }
        return ServerResponse.createByErrorMessage("添加课程管理失败");
    }

    /**
     * 根据id删除课程管理数据
     */
    public ServerResponse<String> deleteCourseManage(PageData pd) {
        if (Tools.isObjEmpty(pd.get("id"))) {
            return ServerResponse.createByErrorMessage("删除课程管理参数错误");
        }
        int rowCount = _mapper.deleteCourseManageId(pd);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("删除课程管理成功");
        }
        return ServerResponse.createByErrorMessage("删除课程管理失败");
    }

    /**
     * 根据id更新课程管理数据
     */
    @Transactional
    public ServerResponse<String> updateCourseManage(PageData pd) {
        if (Tools.isEmpty(pd.getString("id"))) {
            return ServerResponse.createByErrorMessage("修改课程管理参数错误");
        }
        int rowCount = _mapper.updateCourseManage(pd);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("修改课程管理成功");
        }
        return ServerResponse.createByErrorMessage("修改课程管理失败");
    }

    /**
     * 获取课程管理数据(非分页,搜索功能)
     */
    public ServerResponse<List<PageData>> queryCourseManageKey(PageData pd) {
        if (Tools.isObjEmpty(pd)) {
            return ServerResponse.badArgument();
        }
        List<PageData> list = _mapper.queryCourseManageKey(pd);
        return ServerResponse.createBySuccess(list);
    }

    /**
     * 获取课程管理列表数据 分页
     */
    public List<PageData> queryPageCourseManageKeyList(Page pd) {
        List<PageData> list = _mapper.queryPageCourseManageKeyList(pd);
        return list;
    }
}
