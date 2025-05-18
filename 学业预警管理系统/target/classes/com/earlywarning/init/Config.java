package com.earlywarning.init;

import com.earlywarning.util.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * 全局配置
 */
@Component
public class Config {


    /**
     * 获取当前类在当前系统中的绝对路径
     */
    public static final String CLASS_REAL_PATH = Config.class.getClassLoader().getResource("").toString();

    /**
     * classes的绝对路径
     */
    public static final String REAL_PATH_OF_CLASSES = CLASS_REAL_PATH.substring(6, CLASS_REAL_PATH.length() - 1);

    /**
     * webcontent的绝对路径
     */
    public static final String REAL_PATH_OF_WEBCONTENT = REAL_PATH_OF_CLASSES.substring(0, REAL_PATH_OF_CLASSES.length() - 16);

    /**
     * 图片等用户上传的静态文件绝对路径
     */
    public static final String REAL_PATH_OF_STATIC_FILE = REAL_PATH_OF_WEBCONTENT + "/static";

    /**
     * 静态文件路径的压缩URL, 最后一位不能是斜杠, 反斜杠
     */
    public static final String STATIC_FILES_RELATIVE_URL_PREFIX = "fs:";

    /**
     * 管理员访问权限拦截的URL, 正则表达式
     */
    public static final String ADMIN_HIJACK_PREFIX = "^/admin(/[\\w]*)*$";


    /************* 可配置变量 *************/

    /**
     * 网站访问URL, 域名或IP地址
     */
    public static String WEBSITE_HOST;
    /**
     * 静态文件访问前缀, 基于WEBSITE_HOST
     */
    public static String STATIC_FILES_ACCESS_URL;
    /**
     * 静态文件存储文件夹, 如果为null或空时则使用 <code>REAL_PATH_OF_STATIC_FILE</code>
     */
    public static String STATIC_FILES_SAVE_PATH = null;
    /**
     * 图片存放的路径
     */
    public static String STATIC_IMAGES_FOLDER = "images";
    /**
     * 视频存放的路径
     */
    public static String STATIC_VIDEOS_FOLDER = "videos";
    /**
     * 音频存放的路径
     */
    public static String STATIC_AUDIOS_FOLDER = "audios";
    /**
     * 其他文件存放的路径
     */
    public static String STATIC_FILES_FOLDER = "files";
    /**
     * 数据库表的统一前缀
     */
    public static String DATABASE_TABLE_UNITIVE_PREFIX = "";
    /**
     * 图片后缀范围, 正则表达式, 必须小写
     */
    public static String IMAGE_SUFFIXES_SCOPE = "jpg|gif|png|jpeg";
    /**
     * 图片上传大小限制
     */
    public static Long IMAGE_SIZE_LIMITATION = 1024 * 1024 * 10l;
    /**
     * 视频后缀范围, 正则表达式, 必须小写
     */
    public static String VIDEO_SUFFIXES_SCOPE = "mp4|flv|f4v|webm|m3u8";
    /**
     * 视频上传大小限制
     */
    public static Long VIDEO_SIZE_LIMITATION = 1024 * 1024 * 100l;
    /**
     * 视频后缀范围, 正则表达式, 必须小写
     */
    public static String AUDIO_SUFFIXES_SCOPE = "mp3|wav|ogg";
    /**
     * 视频上传大小限制
     */
    public static Long AUDIO_SIZE_LIMITATION = 1024 * 1024 * 20l;
    /**
     * 其他文件后缀范围, 正则表达式, 必须小写
     */
    public static String OTHER_FILE_SUFFIXES_SCOPE = "mp3";
    /**
     * 其他文件上传大小限制
     */
    public static Long OTHER_FILE_SIZE_LIMITATION = 1024 * 1024 * 100l;
    /**
     * 跨域访问设置
     */
    public static String ACCESS_CONTROL_ALLOW_ORIGIN = "";
    /**
     * 默认根目录访问URI
     */
    public static String DEFAULT_REQUEST_URI = "/";

    /**
     * 获取文件保存路径(基于当前系统的绝对路径)
     *
     * @param subFolder 父文件夹名称
     * @return 文件保存路径
     */
    public static String getStaticFilesAbsoluteSavePath(String subFolder) {
        if (subFolder == null) throw new IllegalArgumentException("父文件夹不能为空!");

        String filePath = STATIC_FILES_SAVE_PATH;

        if (filePath == null || filePath.isEmpty()) filePath = REAL_PATH_OF_STATIC_FILE;

        filePath += "/" + subFolder + "/";

        return filePath;
    }

    /**
     * 获取文件基于 STATIC_FILES_PATH_PREFIX 的相对路径, 使用时需要将 STATIC_FILES_PATH_PREFIX 替换成 STATIC_FILES_ACCESS_PATH
     *
     * @param fileName  文件名称
     * @param subFolder 文件的父文件夹名称
     * @return
     */
    public static String getStaticFilesRelativeAccessURL(String subFolder, String fileName) {
        if (subFolder == null) throw new IllegalArgumentException("父文件夹不能为空!");
        return STATIC_FILES_RELATIVE_URL_PREFIX + fileName;
    }

    /************* 跟配置文件有关的方法 *************/

    /**
     * 根据getStaticFilesRelativeAccessPath获取的相对路径, 结合配置文件的静态文件访问路径, 生成一个静态文件访问URL
     *
     * @param fs
     * @param isWithHost URL前面是否加上主机host
     * @return 可访问的URL, 如果该字符串内不包含STATIC_FILES_PATH_PREFIX则返回null
     */
    public static String getStaticFilesAccessURL(String fs, boolean isWithHost) {
        //检查数据
        if (fs == null || fs.isEmpty()) {
            return null;
        }
        //检查是否包含关键字
        if (!fs.contains(STATIC_FILES_RELATIVE_URL_PREFIX)) {
            return null;
        }
        //准备工作
        int urlLength = STATIC_FILES_ACCESS_URL.length() - 1;
        fs.replaceAll("\\\\", "/");

        String prefix = STATIC_FILES_RELATIVE_URL_PREFIX;
        //避免出现重复的斜杠
        if ((fs.charAt(3) == '/' || fs.charAt(3) == '\\') &&
                STATIC_FILES_ACCESS_URL.lastIndexOf('/') == urlLength
                || STATIC_FILES_ACCESS_URL.lastIndexOf('\\') == urlLength) {
            prefix = prefix + "/";
        }

        return fs.replaceFirst(prefix, (isWithHost ? WEBSITE_HOST : "") + STATIC_FILES_ACCESS_URL);
    }

    /**
     * 根据getStaticFilesRelativeAccessPath获取的相对路径, 结合配置文件的静态文件访问路径, 生成一个静态文件访问URL, 默认加上host
     *
     * @param fs
     * @return 可访问的URL, 如果该字符串内不包含STATIC_FILES_PATH_PREFIX则返回null
     */
    public static String getStaticFilesAccessURL(String fs) {
        return getStaticFilesAccessURL(fs, false);
        //return getStaticFilesAccessURL(fs, true);
    }

    /**
     * 获取静态文件在当前文件系统的绝对路径
     *
     * @param fs 相对URL
     * @return 在当前文件系统的绝对路径
     */
    public static String getStaticFilesAbsolutePath(String fs) {
        //检查数据
        if (fs == null || fs.isEmpty()) {
            return null;
        }
        //检查是否包含关键字
        if (!fs.contains(STATIC_FILES_RELATIVE_URL_PREFIX)) {
            return null;
        }
        //准备工作
        int urlLength = REAL_PATH_OF_STATIC_FILE.length() - 1;
        fs.replaceAll("\\\\", "/");
        //避免出现重复的斜杠
        if ((fs.charAt(3) == '/' || fs.charAt(3) == '\\') &&
                REAL_PATH_OF_STATIC_FILE.lastIndexOf('/') == urlLength
                || REAL_PATH_OF_STATIC_FILE.lastIndexOf('\\') == urlLength) {
            return fs.replaceFirst(STATIC_FILES_RELATIVE_URL_PREFIX + "/", REAL_PATH_OF_STATIC_FILE);
        } else {
            return fs.replaceFirst(STATIC_FILES_RELATIVE_URL_PREFIX, REAL_PATH_OF_STATIC_FILE);
        }
    }

    /**
     * 如果表有统一前缀, 通过此方法获取正确的表名
     *
     * @param tablename 表名
     * @return 添加前缀后的表名
     */
    public static String getTable(String tablename) {
        if (tablename == null || tablename.isEmpty()) {
            return null;
        }
        return " " + StringUtil.addPrefix(tablename, DATABASE_TABLE_UNITIVE_PREFIX) + " ";
    }

    @Value("${config.url.website_host}")
    public void setWebsiteHost(String websiteHost) {
        WEBSITE_HOST = websiteHost;
    }

    @Value("${config.url.static_files_access_url}")
    public void setStaticFilesAccessUrl(String staticFilesAccessUrl) {
        STATIC_FILES_ACCESS_URL = staticFilesAccessUrl;
    }
}
