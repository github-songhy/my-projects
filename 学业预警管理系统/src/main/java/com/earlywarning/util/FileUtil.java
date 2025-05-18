package com.earlywarning.util;

import org.apache.commons.io.FileUtils;
import sun.misc.BASE64Encoder;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.security.MessageDigest;
import java.util.List;

/**
 * 文件工具类
 */
@SuppressWarnings("restriction")
public abstract class FileUtil {

    /**
     * 将image对象转换为BufferedImage
     *
     * @param image image对象
     * @return BufferedImage实例
     */
    public static BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }

        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();

        // Determine if the image has transparent pixels; for this method's
        // implementation, see e661 Determining If an Image Has Transparent
        // Pixels
        // boolean hasAlpha = hasAlpha(image);

        // Create a buffered image with a format that's compatible with the
        // screen
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            // Determine the type of transparency of the new buffered image
            int transparency = Transparency.OPAQUE;
            /*
             * if (hasAlpha) { transparency = Transparency.BITMASK; }
             */

            // Create the buffered image
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), transparency);
        } catch (HeadlessException e) {
            // The system does not have a screen
        }

        if (bimage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;
            // int type = BufferedImage.TYPE_3BYTE_BGR;//by wang
            /*
             * if (hasAlpha) { type = BufferedImage.TYPE_INT_ARGB; }
             */
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }

        // Copy image to buffered image
        Graphics g = bimage.createGraphics();

        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose();

        return bimage;
    }

    /**
     * 根据InputStream在制定的路径下创建文件, 不考虑父文件夹是否存在
     *
     * @param in       要写入磁盘的数据
     * @param filePath 给定的路径
     */
    public static void createFile(InputStream in, String filePath) {
        if (in == null)
            throw new RuntimeException("create file error: inputstream is null");
        int potPos = filePath.lastIndexOf('/') + 1;
        String folderPath = filePath.substring(0, potPos);
        createFolder(folderPath);
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(filePath);
            byte[] by = new byte[1024];
            int c;
            while ((c = in.read(by)) != -1) {
                outputStream.write(by, 0, c);
            }
        } catch (IOException e) {
            e.getStackTrace().toString();
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否是允许上传文件
     *
     * @param ex
     * @return
     */
    public static boolean isAllowUp(String logoFileName) {
        logoFileName = logoFileName.toLowerCase();
        String allowTYpe = "gif,jpg,bmp,swf,png,rar,doc,docx,xls,xlsx,pdf,zip,ico,txt";
        if (!logoFileName.trim().equals("") && logoFileName.length() > 0) {
            String ex = logoFileName.substring(logoFileName.lastIndexOf(".") + 1, logoFileName.length());
            return allowTYpe.toUpperCase().indexOf(ex.toUpperCase()) >= 0;
        } else {
            return false;
        }
    }

    /**
     * 把内容写入文件
     *
     * @param filePath
     * @param fileContent
     */
    public static void write(String filePath, String fileContent) {

        try {
            FileOutputStream fo = new FileOutputStream(filePath);
            OutputStreamWriter out = new OutputStreamWriter(fo, "UTF-8");

            out.write(fileContent);

            out.close();
        } catch (IOException ex) {

            System.err.println("Create File Error!");
            ex.printStackTrace();
        }
    }

    /**
     * 读取文件内容 默认是UTF-8编码
     *
     * @param filePath
     * @return
     */
    public static String read(String filePath, String code) {
        if (code == null || code.equals("")) {
            code = "UTF-8";
        }
        String fileContent = "";
        File file = new File(filePath);
        try {
            InputStreamReader read = new InputStreamReader(new FileInputStream(file), code);
            BufferedReader reader = new BufferedReader(read);
            String line;
            while ((line = reader.readLine()) != null) {
                fileContent = fileContent + line + "\n";
            }
            read.close();
            read = null;
            reader.close();
            read = null;
        } catch (Exception ex) {
            ex.printStackTrace();
            fileContent = "";
        }
        return fileContent;
    }

    /**
     * 删除文件或文件夹
     *
     * @param filePath
     */
    public static void delete(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                if (file.isDirectory()) {
                    FileUtils.deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 判断该路径是否存在
     *
     * @param filepath 路径
     * @return 存在true, 反之则false
     */
    public static boolean exist(String filepath) {
        File file = new File(filepath);

        return file.exists();
    }

    /**
     * 创建文件夹
     *
     * @param filePath
     */
    public static void createFolder(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
        } catch (Exception ex) {
            System.err.println("Make Folder Error:" + ex.getMessage());
        }
    }

    /**
     * 重命名文件、文件夹
     *
     * @param from
     * @param to
     */
    public static void renameFile(String from, String to) {
        try {
            File file = new File(from);
            if (file.exists()) {
                file.renameTo(new File(to));
            }
        } catch (Exception ex) {
            System.err.println("Rename File/Folder Error:" + ex.getMessage());
        }
    }

    /**
     * 得到文件的扩展名
     *
     * @param fileName
     * @return
     */
    public static String getFileExt(String fileName) {

        int potPos = fileName.lastIndexOf('.') + 1;
        String type = fileName.substring(potPos, fileName.length());
        return type;
    }

    /**
     * 通过File对象创建文件
     *
     * @param file
     * @param filePath
     */
    public static void createFile(File file, String filePath) {
        int potPos = filePath.lastIndexOf('/') + 1;
        String folderPath = filePath.substring(0, potPos);
        createFolder(folderPath);
        FileOutputStream outputStream = null;
        FileInputStream fileInputStream = null;
        try {
            outputStream = new FileOutputStream(filePath);
            fileInputStream = new FileInputStream(file);
            byte[] by = new byte[1024];
            int c;
            while ((c = fileInputStream.read(by)) != -1) {
                outputStream.write(by, 0, c);
            }
            outputStream.close();
            fileInputStream.close();
        } catch (IOException e) {
            e.getStackTrace().toString();
        }
    }

    /**
     * 获取路径文件内的所有内容
     *
     * @param resource 路径
     * @return 文件内的所有内容
     */
    public static String readFile(String resource) {
        InputStream stream = getResourceAsStream(resource);
        String content = readStreamToString(stream);

        return content;

    }

    /**
     * 根据文件路径获取InputStream
     *
     * @param resource
     * @return
     */
    public static InputStream getResourceAsStream(String resource) {
        String stripped = resource.startsWith("/") ? resource.substring(1) : resource;

        InputStream stream = null;
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader != null) {
            stream = classLoader.getResourceAsStream(stripped);

        }

        return stream;
    }

    /**
     * 根据路径获取FileInputStream
     *
     * @param path 文件路径
     * @return
     * @throws IOException
     */
    public static InputStream getFileInputStream(String path) throws IOException {
        return new FileInputStream(path);
    }

    /**
     * 根据InputStream读取所有内容
     *
     * @param stream InputStream实例
     * @return 读取的所有内容
     */
    public static String readStreamToString(InputStream stream) {
        String fileContent = "";

        try {
            InputStreamReader read = new InputStreamReader(stream, "utf-8");
            BufferedReader reader = new BufferedReader(read);
            String line;
            while ((line = reader.readLine()) != null) {
                fileContent = fileContent + line + "\n";
            }
            read.close();
            read = null;
            reader.close();
            read = null;
        } catch (Exception ex) {
            fileContent = "";
        }
        return fileContent;
    }

    /**
     * delete file folder
     *
     * @param path
     */
    public static void removeFile(File path) {

        if (path.isDirectory()) {
            try {
                FileUtils.deleteDirectory(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 复制文件
     *
     * @param srcFile  源文件路径
     * @param destFile 目标文件路径
     */
    public static void copyFile(String srcFile, String destFile) {
        try {
            if (FileUtil.exist(srcFile)) {
                FileUtils.copyFile(new File(srcFile), new File(destFile));
            }
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    /**
     * 复制文件夹
     *
     * @param sourceFolder      源文件夹路径
     * @param destinationFolder 目标文件夹路径
     */
    public static void copyFolder(String sourceFolder, String destinationFolder) {
        // ////System.out.println("copy " + sourceFolder + " to " +
        // destinationFolder);
        try {
            File sourceF = new File(sourceFolder);
            if (sourceF.exists())
                FileUtils.copyDirectory(new File(sourceFolder), new File(destinationFolder));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("copy file error");
        }

    }

    /**
     * 拷贝源文件夹至目标文件夹 只拷贝新文件
     *
     * @param sourceFolder
     * @param targetFolder
     */
    public static void copyNewFile(String sourceFolder, String targetFolder) {
        try {
            File sourceF = new File(sourceFolder);

            if (!targetFolder.endsWith("/"))
                targetFolder = targetFolder + "/";

            if (sourceF.exists()) {
                File[] filelist = sourceF.listFiles();
                for (File f : filelist) {
                    File targetFile = new File(targetFolder + f.getName());

                    if (f.isFile()) {
                        // 如果目标文件比较新，或源文件比较新，则拷贝，否则跳过
                        if (!targetFile.exists() || FileUtils.isFileNewer(f, targetFile)) {
                            FileUtils.copyFileToDirectory(f, new File(targetFolder));
                            // ////System.out.println("copy "+ f.getName());
                        } else {
                            // ////System.out.println("skip "+ f.getName());
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("copy file error");
        }
    }

    /**
     * 用base64加密文件流
     *
     * @param stream
     * @return
     */
    public static String base64Stream(InputStream stream) {
        if (stream == null) {
            return null;
        }
        byte[] data = null;
        try {
            data = new byte[stream.available()];
            stream.read(data);
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new BASE64Encoder().encode(data);
    }

    /**
     * 获取文件的MD5值
     *
     * @param file
     * @return
     */
    public static String md5(File file) {
        final char[] hexChar = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            double filesize = file.length();
            double byteSize = 8192.0D;
            InputStream ins = new FileInputStream(file);
            byte[] buffer = new byte[(int) byteSize];
            MessageDigest md5 = MessageDigest.getInstance("MD5");

            double each = filesize / byteSize;
            int loops = (int) (each / 50.0D);
            int loopCount = 0;
            System.out.print("\n-------------------------------------------------- : 100%" + "\n");
            int len;
            while ((len = ins.read(buffer)) != -1) {
                md5.update(buffer, 0, len);
                if (loops != 0) {
                    if (loopCount == loops) {
                        System.out.print("-");
                        loopCount = 0;
                    } else {
                        loopCount++;
                    }
                }
            }
            if (loops != 0) {
                //System.out.println("- : MD5ed");
            } else {
                //System.out.println("-------------------------------------------------- : MD5ed");
            }
            ins.close();

            byte[] result = md5.digest();

            StringBuilder sb = new StringBuilder(result.length * 2);
            for (int i = 0; i < result.length; i++) {
                sb.append(hexChar[((result[i] & 0xF0) >>> 4)]);
                sb.append(hexChar[(result[i] & 0xF)]);
            }
            return sb.toString().toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取一个可视化的文件结构列表, 后面三个参数是递归时要使用的, 可以直接填写默认值即可
     *
     * @param folder              要被扫描的文件夹
     * @param name                文件名称, 当tabCount为0时, 会被忽略, 显示folder
     * @param tabCount            直接给0, 这个就是每一行前面的tab
     * @param ignoredDashPosition 直接给""或null, 被忽略的特定位置的|
     * @param osw                 输出文件地址
     * @return
     */
    public static String getVisibleFileList(String folder, String name, int tabCount, String ignoredDashPosition, OutputStreamWriter osw) {
        // 检查数据
        if (folder == null || folder.isEmpty()) return null;
        OutputStream os = null;
        if (osw == null) {
            File displayFile = new File(folder + "/visibleFileList.txt");
            try {
                if (!displayFile.exists()) displayFile.createNewFile();
                os = new FileOutputStream(displayFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
            osw = new OutputStreamWriter(os);
        }
        // 每行要被换行的数量
        String tabs = "";
        for (int i = 0; i < tabCount; i++) {
            // 检查是否有特定位置的|被忽略
            boolean isIgnored = (ignoredDashPosition == null || ignoredDashPosition.isEmpty() || !(i + "").matches(ignoredDashPosition));
            // 按条件生成该文件夹前面的tab
            tabs += (isIgnored ? "│" : "") + "\t";
        }
        // 结果参数
        /**String result = (tabCount == 0?folder:name);*/
        try {
            osw.write((tabCount == 0 ? folder : name));
            // 获取这个路径的文件对象
            File files = new File(folder);
            // 判断是否存在, 且判断是否为文件夹, 因为担心使用者给出的参数是一个文件
            if (files.exists() && files.isDirectory()) {
                // 获取该文件夹下面的所有文件和文件夹
                String[] fileList = files.list();
                if (fileList == null) throw new IllegalArgumentException("空list");
                // 换行父文件夹名称
                if (fileList.length > 0) {
                    /**result += "\r\n";*/
                    osw.write("\r\n");
                }
                // 遍历文件/文件夹
                for (int i = 0; i < fileList.length; i++) {
                    // 是否最后一个文件/文件夹
                    boolean isLastFile = (i == fileList.length - 1);
                    // 获取这个子文件/子文件夹
                    File subFile = new File(folder, fileList[i]);
                    // 如果不是最后一个文件/子文件夹, 就显示 ├
                    String prefix = "├";
                    // 是最后一个就显示└
                    if (isLastFile) prefix = "└";
                    // 将前面的tab拼接到这行中
                    prefix = tabs + prefix;
                    // 判断子文件/子文件夹是否存在, 怕在扫描的时候用户删除了该文件/文件夹, 防止抛错
                    if (subFile.exists()) {
                        // 如果是文件夹就用递归处理
                        if (subFile.isDirectory()) {
                            // 判断当前这个文件夹是否为最后一个, 是就让这个位置的tab不输出|, 为了美观而已
                            if (isLastFile)
                                // 拼接一个正则表达式, 拼接前先检查了一次数据, 避免NullPointException
                                ignoredDashPosition = ignoredDashPosition == null || ignoredDashPosition.isEmpty() ? (tabCount + "|") : (ignoredDashPosition + "|" + tabCount);
                            // 递归
                            osw.write(prefix + " ");
                            /**result += prefix+" "+*/
                            getVisibleFileList(subFile.getAbsolutePath(), fileList[i], tabCount + 1, ignoredDashPosition, osw);
                            // 如果是文件就将文件名称拼接到这行数据后面
                        } else if (subFile.isFile()) {
                            /**result += prefix+" "+subFile.getName();*/
                            osw.write(prefix + " " + subFile.getName());
                        } else {
                            continue;
                        }
                        // 换行
                        if (!isLastFile) {
                            /**result += "\r\n";*/
                            osw.write("\r\n");
                        }
                        // 关闭流
                        if (isLastFile && tabCount == 0) {
                            try {
                                if (osw != null) osw.close();
                                if (os != null) os.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            } else if (files.isFile()) {
                throw new IllegalArgumentException("给出的路径应该是一个文件夹而不是一个文件!");
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return /**result*/null;
    }

    /**
     * 根据给出的文件夹去找该文件夹下面的所有.java或.class或.jar文件
     *
     * @param results     接收找到的文件的list
     * @param folder      文件夹
     * @param classPrefix 包名
     */
    public static void findClassFile(List<String> results, File folder, String classPrefix) {
        String[] filenames = folder.list();
        for (String filename : filenames) {
            String suffix = FileUtil.getFileExt(filename).toLowerCase();
            if (suffix.equals("java") || suffix.equals("class") || suffix.equals("jar")) {
                results.add(classPrefix + "." + (filename.split("\\.")[0]));
            }
        }
    }

}