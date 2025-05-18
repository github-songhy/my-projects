package com.earlywarning.controller;


import com.baidu.ueditor.ActionEnter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("ueditor")
@Configuration
@PropertySource("classpath:application.yml")
public class UeditorController {

    // 读取配置文件中配置的上传文件保存地址
    @Value("${file.save.location}")
    private String ueditorUrl;

    /**
     * 接收和获取百度编辑插件的文件，需要注意以下几点
     * 1，需要在 ueditorUrl 目录下创建 ueditor 目录，将 config.json 拷贝到该路径下
     * 2，必须将 ueditorUrl 目录设置为静态资源路径，否则上传的文件可能无法访问
     * 3，注意访问配置文件的方式 ueditor.config.js 请求的路径就是 config.json 放置的同级路径
     */
    @RequestMapping("upload")
    public String upload(HttpServletRequest request, String action) {
        String result = new ActionEnter(request, ueditorUrl).exec();
        //System.out.println("-------------------");
        //System.out.println(action);
        //System.out.println(ueditorUrl);
        //System.out.println(result);
        ueditorUrl = ueditorUrl.replaceAll("\\\\", "/");
        if (action != null && (action.equals("listfile") || action.equals("listimage"))) {
            return result.replaceAll(ueditorUrl, "");
        }
        return result;
    }

}
