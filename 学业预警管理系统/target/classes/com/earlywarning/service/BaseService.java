package com.earlywarning.service;

import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class BaseService {

    /**
     * 获取操作失败返回的json
     *
     * @return
     */
    public JSONObject getFalJson() {
        JSONObject json = new JSONObject();
        json.put("msg", "500");
        json.put("result", "filed");
        return json;
    }

}
