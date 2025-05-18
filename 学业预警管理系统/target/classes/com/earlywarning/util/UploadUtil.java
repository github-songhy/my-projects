package com.earlywarning.util;

import com.earlywarning.init.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class UploadUtil {

    /**
     * 默认上传类型, 接收所有类型的文件
     */
    public static final int DEFAULT = 0;
    /**
     * 上传的是图片
     */
    public static final int IMAGE = 1;
    private static final Logger log = LoggerFactory.getLogger(UploadUtil.class);
    /**
     * 文件上传地址
     */
    public static String imgUrl;

    /**
     * 上传文件
     *
     * @param file         文件流
     * @param fileFileName 文件名
     * @param subFolder    该文件的父文件夹
     * @return 文件的路径, 如fs:/attachment/goods/2001010101030.jpg, fs:是根据Settings中设置, 在使用的时候会替换成静态文件访问路径
     */
    public static String uploadFile(File file, String fileFileName, String subFolder) {
        //检查内容
        if (file == null || fileFileName == null) throw new IllegalArgumentException("文件或文件名为空!");
        if (subFolder == null) throw new IllegalArgumentException("父文件夹不能为空!");

        //添加一个yyyyMM的文件夹, 为了让管理员更好的浏览
        subFolder = subFolder + "/" + new SimpleDateFormat("yyyyMM").format(new Date());

        //获取文件后缀
        String ext = FileUtil.getFileExt(fileFileName);
        //生成时间关联的文件名
        String fileName = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + "_" + new Date().getTime() + "." + ext;
        //根据静态文件生成在系统中的绝对路径
        String filePath = Config.getStaticFilesAbsoluteSavePath(subFolder);
        //拼接绝对路径
        filePath += fileName;
        //写入文件
        FileUtil.createFile(file, filePath);

        //拼接一个相对路径
        String path = Config.getStaticFilesRelativeAccessURL(subFolder, fileName);

        return path;
    }

    /**
     * 上传文件
     *
     * @param file      MultipartFile类型的实例
     * @param subFolder 父文件夹名称
     * @param type      上传文件的类型
     * @return 该文件的相对路径
     * @throws Exception
     */
    public static String uploadFile(MultipartFile file, String subFolder, int type) throws Exception {
        //检查内容
        if (file == null || file.getSize() == 0) throw new IllegalArgumentException("文件为空!");
        if (subFolder == null || subFolder.isEmpty()) throw new IllegalArgumentException("父文件夹不能为空!");

        //添加一个yyyyMM的文件夹, 为了让管理员更好的浏览
        subFolder = subFolder + "/" + new SimpleDateFormat("yyyyMM").format(new Date());

        //文件原名称
        String fileOriginalName = file.getOriginalFilename();
        //检查文件
        switch (type) {
            case 0:
                break;
            case 1:
                if (!isImage(fileOriginalName)) {
                    throw new IllegalArgumentException("文件类型不匹配!");
                }
                ;
                break;
            default:
                throw new IllegalArgumentException("未指定正确的文件类型!");
        }
        //生成时间关联的文件名
        String fileName = getDateFileName(fileOriginalName);
        //根据静态文件生成在系统中的绝对路径
        String filePath = Config.getStaticFilesAbsoluteSavePath(subFolder);
        //先生成路径

        //System.out.println(imgUrl);

        FileUtil.createFolder(imgUrl);
        //拼接绝对路径
        filePath = imgUrl + "/" + fileName;
        //写入文件
        file.transferTo(new File(filePath));

        //拼接一个相对路径
        String path = Config.getStaticFilesRelativeAccessURL(subFolder, fileName);

        log.info("保存文件 " + file.getOriginalFilename() + " 至 " + filePath + ", 文件大小: " + StringUtil.getFileSize(file.getSize()) + ", 相对访问路径: " + path);

        return path;
    }

    /**
     * 上传文件
     *
     * @param file      MultipartFile类型的实例
     * @param subFolder 父文件夹名称
     * @param type      上传文件的类型
     * @return 该文件的相对路径
     * @throws Exception
     */
    public static String uploadFile(MultipartFile file, String subFolder, int type, String uploadPath) throws Exception {
        //检查内容
        if (file == null || file.getSize() == 0) throw new IllegalArgumentException("文件为空!");
        if (subFolder == null || subFolder.isEmpty()) throw new IllegalArgumentException("父文件夹不能为空!");

        //添加一个yyyyMM的文件夹, 为了让管理员更好的浏览
        subFolder = subFolder + "/" + new SimpleDateFormat("yyyyMM").format(new Date());

        //文件原名称
        String fileOriginalName = file.getOriginalFilename();
        //检查文件
        switch (type) {
            case 0:
                break;
            case 1:
                if (!isImage(fileOriginalName)) {
                    throw new IllegalArgumentException("文件类型不匹配!");
                }
                ;
                break;
            default:
                throw new IllegalArgumentException("未指定正确的文件类型!");
        }
        //生成时间关联的文件名
        String fileName = getDateFileName(fileOriginalName);
        //根据静态文件生成在系统中的绝对路径
        String filePath = Config.getStaticFilesAbsoluteSavePath(subFolder);
        //先生成路径
        FileUtil.createFolder(uploadPath);
        //拼接绝对路径
        filePath = uploadPath + fileName;
        //写入文件
        file.transferTo(new File(filePath));

        //拼接一个相对路径
        String path = fileName;

        log.info("保存文件 " + file.getOriginalFilename() + " 至 " + filePath + ", 文件大小: " + StringUtil.getFileSize(file.getSize()) + ", 相对访问路径: " + path);

        return path;
    }

    /**
     * 上传文件
     *
     * @param file      MultipartFile类型的实例
     * @param subFolder 父文件夹名称
     * @return 该文件的相对路径
     * @throws Exception
     */
    public static String uploadFile(MultipartFile file, String subFolder, String uploadPath) throws Exception {
        return uploadFile(file, subFolder, DEFAULT, uploadPath);
    }

    /**
     * 上传文件
     *
     * @param file      MultipartFile类型的实例
     * @param subFolder 父文件夹名称
     * @return 该文件的相对路径
     * @throws Exception
     */
    public static String uploadFile(MultipartFile file, String subFolder) throws Exception {
        return uploadFile(file, subFolder, DEFAULT);
    }

    /**
     * 获取根据日期生成的文件名称
     *
     * @param originalFilename 原本的文件名称
     * @return 根据日期生成的文件名称
     */
    synchronized private static String getDateFileName(String originalFilename) {
        //获取文件后缀
        String ext = FileUtil.getFileExt(originalFilename);
        //生成时间关联的文件名
        return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + "_" + new Date().getTime() + "." + ext;
    }

    /**
     * 检查文件名称是否为指定格式的图片
     *
     * @param filename 文件名称
     * @return
     */
    public static boolean isImage(String filename) {
        return FileUtil.getFileExt(filename).toLowerCase().matches(Config.IMAGE_SUFFIXES_SCOPE);
    }

    /**
     * 检查文件名称是否为指定格式的图片
     *
     * @param file MultipartFile类型的对象
     * @return
     */
    public static boolean isImage(MultipartFile file) {
        if (file == null || file.getSize() == 0) {
            return false;
        }

        return isImage(file.getOriginalFilename());
    }

    /**
     * 检查文件名称是否为指定格式的图片
     *
     * @param file File类型的对象
     * @return
     */
    public static boolean isImage(File file) {
        if (file == null || !file.exists()) {
            return false;
        }

        return isImage(file.getName());
    }

    /**
     * 检查文件名称是否为指定格式的视频
     *
     * @param filename 文件名称
     * @return
     */
    public static boolean isVideo(String filename) {
        return FileUtil.getFileExt(filename).toLowerCase().matches(Config.VIDEO_SUFFIXES_SCOPE);
    }

    /**
     * 检查文件名称是否为指定格式的视频
     *
     * @param file MultipartFile类型的对象
     * @return
     */
    public static boolean isVideo(MultipartFile file) {
        if (file == null || file.getSize() == 0) {
            return false;
        }

        return isVideo(file.getOriginalFilename());
    }

    /**
     * 检查文件名称是否为指定格式的视频
     *
     * @param file File类型的对象
     * @return
     */
    public static boolean isVideo(File file) {
        if (file == null || !file.exists()) {
            return false;
        }

        return isVideo(file.getName());
    }

    /**
     * 检查文件名称是否为指定格式的音频
     *
     * @param filename 文件名称
     * @return
     */
    public static boolean isAudio(String filename) {
        return FileUtil.getFileExt(filename).toLowerCase().matches(Config.AUDIO_SUFFIXES_SCOPE);
    }

    /**
     * 检查文件名称是否为指定格式的音频
     *
     * @param file MultipartFile类型的对象
     * @return
     */
    public static boolean isAudio(MultipartFile file) {
        if (file == null || file.getSize() == 0) {
            return false;
        }

        return isAudio(file.getOriginalFilename());
    }

    /**
     * 检查文件名称是否为指定格式的音频
     *
     * @param file File类型的对象
     * @return
     */
    public static boolean isAudio(File file) {
        if (file == null || !file.exists()) {
            return false;
        }

        return isAudio(file.getName());
    }

    /**
     * 检查文件名称是否为指定格式
     *
     * @param filename 文件名称
     * @return
     */
    public static boolean isSuffixInScope(String filename) {
        return FileUtil.getFileExt(filename).toLowerCase().matches(Config.OTHER_FILE_SUFFIXES_SCOPE);
    }

    /**
     * 检查文件名称是否为指定格式
     *
     * @param file MultipartFile类型的对象
     * @return
     */
    public static boolean isSuffixInScope(MultipartFile file) {
        if (file == null || file.getSize() == 0) {
            return false;
        }

        return isSuffixInScope(file.getOriginalFilename());
    }

    /**
     * 检查文件名称是否为指定格式
     *
     * @param file File类型的对象
     * @return
     */
    public static boolean isSuffixInScope(File file) {
        if (file == null || !file.exists()) {
            return false;
        }

        return isSuffixInScope(file.getName());
    }

    /**
     * 判断这个MultipartFile是否存在
     *
     * @param file
     * @return
     */
    public static boolean isMultipartFile(MultipartFile file) {
        if (file == null || file.getSize() == 0) {
            return false;
        }
        return true;
    }

    /**
     * 判断文件的后缀名是否匹配第二个参数给出的正则表达式
     *
     * @param filename  文件名
     * @param suffixReg 正则表达式
     */
    public static boolean regSuffix(String filename, String suffixReg) {
        String suffix = FileUtil.getFileExt(filename);
        return suffix.matches(suffixReg);
    }

    /**
     * 判断文件的后缀名是否匹配第二个参数给出的正则表达式
     */
    public static boolean regSuffix(MultipartFile file, String suffixReg) {
        if (!isMultipartFile(file)) return false;
        return regSuffix(file.getOriginalFilename(), suffixReg);
    }

    //	@Value("${img.upload.url}")
    @Value("${file.save.location}")
    public void setImgUrl(String imgurl) {
        UploadUtil.imgUrl = imgurl;
    }

}
