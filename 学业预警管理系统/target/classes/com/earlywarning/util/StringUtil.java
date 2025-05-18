package com.earlywarning.util;

import org.springframework.web.util.WebUtils;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字串工具类
 *
 * @author Allen
 */
@SuppressWarnings("rawtypes")
public abstract class StringUtil {

    private static final char SEPARATOR = '_';

    /**
     * 判断是否为email
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        if (email == null || !email.matches("^\\w+(\\.\\w+)*@\\w+(\\.\\w+)+$")) return false;
        return true;
    }

    /**
     * 判断是否为URI
     *
     * @param uri
     * @return
     */
    public static boolean isUri(String uri) {
        if (uri != null) return uri.matches("^(\\/[\\w]*)*$");
        return false;
    }

    /**
     * 判断字符串是否为0或1
     *
     * @param num
     * @return
     */
    public static boolean is1or0(String num) {
        if (num != null) {
            return num.matches("^[01]$");
        }
        return false;
    }

    /**
     * 判断数字是否为0或1
     *
     * @param num
     * @return
     */
    public static boolean is1or0(Integer num) {
        if (num != null && (num.equals(1) || num.equals(0))) return true;
        return false;
    }

    /**
     * 判断是否为手机号
     *
     * @param mobile
     * @return
     */
    public static boolean isMobile(String mobile) {
        if (mobile != null) {
            return mobile.matches("^1[3|4|5|7|8]\\d{9}$");
        }
        return false;
    }

    /**
     * 判断是否为身份证
     *
     * @param idCard
     * @return
     */
    public static boolean isIDCard(String idCard) {
        if (idCard != null) {
            return idCard.matches("(^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$)");
        }
        return false;
    }

    /**
     * 判断字符串是否为(数字1,数字2,数字3,...)的字符串(不能有空格)
     *
     * @param s
     * @return
     */
    public static boolean isInNumberBracket(String s) {
        if (s != null) {
            return s.matches("^\\(\\d+(?:,\\d+)*\\)$");
        }
        return false;
    }

    /**
     * 判断一个字符串是否为最简单的JSON格式
     *
     * @param json
     * @return
     */
    public static boolean isSimplestJsonObject(String json) {
        if (json == null) return false;
        return json.matches("^\\{(?:(?:\"[^\"]*\"):(?:\"[^\"]*\"|\\d+(?:\\.\\d)?|true|false))?(?: *, *(?:\"[^\"]*\"):(?:\"[^\"]*\"|\\d+(?:\\.\\d)?|true|false))*\\}$");
    }

    /**
     * 判断是否为整数
     *
     * @param integer 被判断的字符串
     * @return true: 是整数; false:不为整数或数字
     */
    public static boolean isNumber(String integer) {
        return isNumber(integer, false);
    }

    /**
     * 判断是否为整数
     *
     * @param integer  被判断的字符串
     * @param isUnsign 是否为正数
     * @return true: 是整数; false:不为整数或数字
     */
    public static boolean isNumber(String integer, boolean isUnsign) {
        if (integer == null) return false;
        return integer.matches("^" + (isUnsign ? "" : "[-+]?") + "[0-9]+$");
    }

    /**
     * 判断是否为正整数
     *
     * @param integer 被判断的字符串
     * @return true: 是整数; false:不为整数或数字
     */
    public static boolean isUnsignInteger(String integer) {
        return isNumber(integer, true);
    }

    /**
     * 将一个Double转为int的String，将省略小数点后面的值
     *
     * @param d
     * @return
     */
    public static String doubleToIntString(Double d) {
        if (d == null) return null;
        int value = ((Double) d).intValue();
        return String.valueOf(value);
    }

    /**
     * 判断是否浮点数或数字
     *
     * @param decimal 被检查的字符串
     * @param type    "0+":非负浮点数; "+":正浮点数; "-0":非正浮点数; "-":负浮点数; "":浮点数;
     * @return true: 是浮点数或数字; false: 不为浮点数或数字;
     */
    public static boolean isDecimal(String decimal, String type) {
        if (decimal == null) return false;
        String eL = "";
        if (type.equals("0+")) eL = "^\\d+(\\.\\d+)?$";// 非负浮点数
        else if (type.equals("+")) eL = "^((\\d+\\.\\d*[1-9]\\d*)|(\\d*[1-9]\\d*\\.\\d+)|(\\d*[1-9]\\d*))$";// 正浮点数
        else if (type.equals("-0")) eL = "^((-\\d+(\\.\\d+)?)|(0+(\\.0+)?))$";// 非正浮点数
        else if (type.equals("-")) eL = "^(-((\\d+\\.\\d*[1-9]\\d*)|(\\d*[1-9]\\d*\\.\\d+)|(\\d*[1-9]\\d*)))$";// 负浮点数
        else eL = "^(-?\\d+)(\\.\\d+)?$";// 浮点数
        return decimal.matches(eL);
    }

    /**
     * 判断是否浮点数或数字
     *
     * @param decimal
     * @return true: 是浮点数或数字; false: 不为浮点数或数字;
     */
    public static boolean isDecimal(String decimal) {
        return isDecimal(decimal, "");
    }

    /**
     * 判断是否为布尔型
     *
     * @param bool
     * @return
     */
    public static boolean isBoolean(String bool) {
        if (bool == null) return false;
        return bool.matches("^(true|false)$");
    }

    /**
     * 检测对象是否存在某数组中
     *
     * @param value
     * @param array
     * @return 存在返回真，不存在返回假
     */
    public static boolean isInArray(Object value, Object[] array) {
        if (array == null) return false;
        for (Object v : array) {
            if (v.equals(value)) return true;
        }
        return false;
    }

    /**
     * MD5加密方法, 大写方式输出
     *
     * @param str 要被加密的字符串
     * @return 32位大写的字符串
     */
    public static String md5uper(String str) {
        return md5(str).toUpperCase();
    }

    /**
     * MD5加密方法
     *
     * @param str 要被加密的字符串
     * @returnn 32位的字符串
     */
    public static String md5(String str) {
        return md5(str, true);
    }

    /**
     * SHA1加密
     *
     * @param decript
     * @return
     */
    public static String SHA1(String decript) {
        try {
            MessageDigest digest = MessageDigest
                    .getInstance("SHA-1");
            digest.update(decript.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * SHA加密
     *
     * @param decript
     * @return
     */
    public static String SHA(String decript) {
        try {
            MessageDigest digest = MessageDigest
                    .getInstance("SHA");
            digest.update(decript.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 加密AES
     *
     * @param content
     * @param password
     * @return
     */
    public static byte[] encryptAES(String content, String password) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, new SecureRandom(password.getBytes()));
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(byteContent);
            return result; // 加密
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密AES
     *
     * @param content  待解密内容
     * @param password 解密密钥
     * @return
     */
    public static byte[] decryptAES(byte[] content, String password) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, new SecureRandom(password.getBytes()));
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(content);
            return result; // 加密
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * MD5加密
     *
     * @param str  要被加密的字符串
     * @param zero 是否用0进行补位
     * @return 32位的字符串
     */
    public static String md5(String str, boolean zero) {
        if (str == null) return null;
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        byte[] resultByte = messageDigest.digest(str.getBytes());
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < resultByte.length; ++i) {
            int v = 0xFF & resultByte[i];
            if (v < 16 && zero) result.append("0");
            result.append(Integer.toHexString(v));
        }
        return result.toString();
    }

    /**
     * 验证Email地址是否有效
     *
     * @param sEmail
     * @return true:有效; false: 无效
     */
    public static boolean validEmail(String email) {
        if (email == null) return false;
        return email.matches("^([a-z0-9A-Z]+[-|\\.|_]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
    }

    /**
     * 验证两个字符串是否相等且不能为空
     *
     * @param str1
     * @param str2
     * @return true: 相等;
     */
    public static boolean equals(String str1, String str2) {
        if (str1 == null || str1.equals("") || str2 == null || str2.equals("")) {
            return false;
        }
        return str1.equals(str2);
    }

    /**
     * 判断字符串是否为空, 为空则返回默认值
     *
     * @param str1 被判断字符串
     * @return true:有效; false: 无效
     */
    public static boolean isEmpty(String str1) {
        if (str1 == null || str1 == "") {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断字符串是否为空, 为空则返回
     *
     * @param string       被判断字符串
     * @param defaultValue 补位字符串
     * @return 判断后的字符串
     */
    public static String toString(String string, String defaultValue) {
        defaultValue = defaultValue == null ? "" : defaultValue;
        if (string == null) return defaultValue;
        return string;
    }

    /**
     * 过滤字符串所有非字母或数字的字符
     *
     * @param wrods 要被过滤的字符串
     * @return 过滤后的字符串
     */
    public static String toWords(String words) {
        if (words == null) return null;
        return words.replaceAll("[^a-zA-Z0-9]", "");
    }

    /**
     * 过滤字符串所有非数字或小数点或+-号的字符
     *
     * @param wrods 要被过滤的字符串
     * @return 过滤后的字符串
     */
    public static String toNumberString(String number) {
        if (number == null) return null;
        number = number.replaceAll("[^\\-\\+\\d\\.]", "");
        if (!isDecimal(number)) return null;
        return number;
    }

    /**
     * 将一个字串转为int，如果无空，则返回默认值
     *
     * @param integer      要转换的数字字符串
     * @param defaultValue 当转换出错时默认值
     * @return Integer
     */
    public static Integer toInt(String integer, Integer defaultValue) {
        if (isNumber(toNumberString(integer)))
            try {
                defaultValue = Integer.parseInt(integer);
            } catch (Exception ex) {
            }
        return defaultValue;
    }

    /**
     * 将字符型转为Int型
     *
     * @param integer 要转换的数字字符串
     * @return 不为整数则返回null
     */
    public static Integer toInt(String integer) {
        return toInt(integer, null);
    }

    /**
     * 转换为double, 如果转换失败则用defaultValue替代
     *
     * @param decimal      要转换的数字字符串
     * @param defaultValue 当转换出错时默认值
     * @return Double
     */
    public static Double toDouble(String decimal, Double defaultValue) {
        if (toNumberString(decimal) != null)
            try {
                defaultValue = Double.valueOf(decimal);
            } catch (Exception ex) {
            }
        return defaultValue;
    }

    /**
     * 字符串转换为double
     *
     * @param decimal 要转换的数字字符串
     * @return 不能转换为double时返回null
     */
    public static Double toDouble(String decimal) {
        return toDouble(decimal, null);
    }

    /**
     * 转换为boolean, 如果转换失败则用defaultValue替代
     *
     * @param bool         要转换的字符串
     * @param defaultValue 当转换出错时默认值
     * @return Boolean
     */
    public static Boolean toBoolean(String bool, Boolean defaultValue) {
        if (isBoolean(bool))
            try {
                defaultValue = Boolean.valueOf(bool);
            } catch (Exception e) {
            }
        return defaultValue;
    }

    /**
     * 字符串转换为boolean
     *
     * @param bool 要转换的字符串
     * @return 不能转换为boolean时返回null
     */
    public static Boolean toBoolean(String bool) {
        return toBoolean(bool, null);
    }

    /**
     * 转换为long, 如果转换失败则用defaultValue替代
     *
     * @param l            要转换的数字字符串
     * @param defaultValue 当转换出错时默认值
     * @return Long
     */
    public static Long toLong(String l, Long defaultValue) {
        if (isNumber(l))
            try {
                defaultValue = Long.valueOf(l);
            } catch (Exception e) {
            }
        return defaultValue;
    }

    /**
     * 字符串转换为long
     *
     * @param l 要转换的数字字符串
     * @return 不能转换为boolean时返回null
     */
    public static Long toLong(String l) {
        return toLong(l, null);
    }

    /**
     * 把数组转换成String
     *
     * @param array 数组
     * @param split 分隔符
     * @return
     */
    public static String arrayToString(Object[] array, String split) {
        if (array == null) {
            return "";
        }
        String str = "";
        for (int i = 0; i < array.length; i++) {
            if (i != array.length - 1) {
                str += array[i].toString() + split;
            } else {
                str += array[i].toString();
            }
        }
        return str;
    }

    /**
     * 将一个list转为以split分隔的string
     *
     * @param list  数组
     * @param split 分隔符
     * @return
     */
    public static String listToString(List list, String split) {
        if (list == null || list.isEmpty())
            return "";
        StringBuffer sb = new StringBuffer();
        for (Object obj : list) {
            if (sb.length() != 0) {
                sb.append(split);
            }
            sb.append(obj.toString());
        }
        return sb.toString();
    }

    /**
     * 得到WEB-INF的绝对路径
     *
     * @return
     */
    public static String getWebInfPath() {
        String filePath = Thread.currentThread().getContextClassLoader().getResource("").toString();
        if (filePath.toLowerCase().indexOf("file:") > -1) {
            filePath = filePath.substring(6, filePath.length());
        }
        if (filePath.toLowerCase().indexOf("classes") > -1) {
            filePath = filePath.replaceAll("/classes", "");
        }
        if (System.getProperty("os.name").toLowerCase().indexOf("window") < 0) {
            filePath = "/" + filePath;
        }
        if (!filePath.endsWith("/"))
            filePath += "/";
        return filePath;
    }

    /**
     * 得到根目录绝对路径(不包含WEB-INF)
     *
     * @return
     */
    public static String getRootPath() {
        String filePath = StringUtil.class.getResource("").toString();

        int index = filePath.indexOf("WEB-INF");
        if (index == -1) {
            index = filePath.indexOf("build");
        }

        if (index == -1) {
            index = filePath.indexOf("bin");
        }

        filePath = filePath.substring(0, index);
        if (filePath.startsWith("jar")) {
            // 当class文件在jar文件中时，返回”jar:file:/F:/ …”样的路径
            filePath = filePath.substring(10);
        } else if (filePath.startsWith("file")) {
            // 当class文件在jar文件中时，返回”file:/F:/ …”样的路径
            filePath = filePath.substring(6);
        }

        if (System.getProperty("os.name").toLowerCase().indexOf("window") < 0) {
            filePath = "/" + filePath;
        }

        if (filePath.endsWith("/")) filePath = filePath.substring(0, filePath.length() - 1);
        ////System.out.println("getRoot path is "+filePath );
        return filePath;
    }

    /**
     * 获取得到根目录绝对路径(不包含WEB-INF)+resource
     *
     * @param resource
     * @param request
     * @return
     */
    public static String getRootPath(String resource, HttpServletRequest request) {
        try {
            return WebUtils.getRealPath(request.getSession()
                    .getServletContext(), resource);
        } catch (FileNotFoundException e) {

            e.printStackTrace();
            return "";
        }

    }

    /**
     * 将计量单位字节转换为相应单位
     *
     * @param size
     * @return
     */
    public static String getFileSize(Object fileSize) {
        return getFileSize(toLong(fileSize.toString(), 0l));
    }

    /**
     * 将计量单位字节转换为相应单位
     *
     * @param size
     * @return
     */
    public static String getFileSize(long filesize) {
        String temp = "";
        Double fileSize = (double) filesize;
        DecimalFormat df = new DecimalFormat("0.00");
        if (fileSize >= 1024) {
            if (fileSize >= 1048576) {
                if (fileSize >= 1073741824) {
                    temp = df.format(fileSize / 1024 / 1024 / 1024) + " GB";
                } else {
                    temp = df.format(fileSize / 1024 / 1024) + " MB";
                }
            } else {
                temp = df.format(fileSize / 1024) + " KB";
            }
        } else {
            temp = df.format(fileSize / 1024) + " KB";
        }
        return temp;
    }

    /**
     * 得到一个32位随机字符
     *
     * @return
     */
    public static String getEntry() {
        Random random = new Random(100);
        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(new String(
                "yyyyMMddHHmmssS"));
        return md5(formatter.format(now) + random.nextDouble());
    }

    /**
     * 将字符串由本地编码转换至给定的编码格式
     *
     * @param str     被转换的字符串
     * @param charset 转换至的编码格式
     * @return
     */
    public static String to(String str, String charset) {
        return to(str, System.getProperty("file.encoding"), charset);
    }

    /**
     * 将str给的编码转换至给定的编码格式
     *
     * @param str        被转换的字符串
     * @param srtCharset str的编码格式
     * @param charset    转换至的编码格式
     * @return
     */
    public static String to(String str, String srtCharset, String charset) {
        if (str == null || str.equals("")) {
            return "";
        }
        try {
            return new String(str.getBytes(srtCharset), charset);
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    /**
     * 获取指定长度的随机数字字符串
     *
     * @param n 长度
     * @return 指定长度的随机数字字符串
     */
    public static String getRandStr(int n) {
        Random random = new Random();
        String sRand = "";
        for (int i = 0; i < n; i++) {
            String rand = String.valueOf(random.nextInt(10));
            sRand += rand;
        }
        return sRand;
    }

    /**
     * 获取指定长度的随机字符串
     *
     * @param length 长度
     * @return 指定长度的随机字符串
     */
    public static String getRandCode(int length) {
        String code = "";
        for (int i = 0; i < length; i++) {
            int typeCode = (int) (Math.random() * 10);
            int lchar = 0;
            switch (typeCode) {
                case 0:
                case 1:
                case 2:
                    lchar = 65 + (int) (Math.random() * 26);
                    code += ((char) lchar);
                    break;
                case 3:
                case 4:
                case 5:
                    lchar = 97 + (int) (Math.random() * 26);
                    code += ((char) lchar);
                    break;
                case 6:
                case 7:
                case 8:
                    code += ((int) (Math.random() * 10));
                    break;
                case 9:
                    i--;
                    break;
            }
        }
        return code;
    }

    /**
     * 去除HTML 元素
     *
     * @param element
     * @return
     */
    public static String getTxtWithoutHTMLElement(String element) {
        if (null == element || "".equals(element.trim())) {
            return element;
        }

        Pattern pattern = Pattern.compile("<[^<|^>]*>");
        Matcher matcher = pattern.matcher(element);
        StringBuffer txt = new StringBuffer();
        while (matcher.find()) {
            String group = matcher.group();
            if (group.matches("<[\\s]*>")) {
                matcher.appendReplacement(txt, group);
            } else {
                matcher.appendReplacement(txt, "");
            }
        }
        matcher.appendTail(txt);
        String temp = txt.toString().replaceAll("\n", "");
        temp = temp.replaceAll(" ", "");
        return temp;
    }

    /**
     * clear trim to String
     *
     * @return
     */
    public static String toTrim(String strtrim) {
        if (null != strtrim && !strtrim.equals("")) {
            return strtrim.trim();
        }
        return "";
    }

    /**
     * 转义字串的$
     *
     * @param str
     * @return
     */
    public static String filterDollarStr(String str) {
        String sReturn = "";
        if (!toTrim(str).equals("")) {
            if (str.indexOf('$', 0) > -1) {
                while (str.length() > 0) {
                    if (str.indexOf('$', 0) > -1) {
                        sReturn += str.subSequence(0, str.indexOf('$', 0));
                        sReturn += "\\$";
                        str = str.substring(str.indexOf('$', 0) + 1,
                                str.length());
                    } else {
                        sReturn += str;
                        str = "";
                    }
                }

            } else {
                sReturn = str;
            }
        }
        return sReturn;
    }

    /**
     * 压缩HTML格式的字符串
     *
     * @param html
     * @return
     */
    public static String compressHtml(String html) {
        if (html == null)
            return null;

        html = html.replaceAll("[\\t\\n\\f\\r]", "");
        return html;
    }

    public static String toCurrency(Double d) {
        if (d != null) {
            DecimalFormat df = new DecimalFormat("￥#,###.00");
            return df.format(d);
        }
        return "";
    }

    public static String toString(Integer i) {
        if (i != null) {
            return String.valueOf(i);
        }
        return "";
    }

    public static String toString(Object i) {
        if (i != null) {
            return String.valueOf(i);
        }
        return "";
    }

    public static String toString(Long i) {
        if (i != null) {
            return String.valueOf(i);
        }
        return "";
    }

    public static String toString(Double d) {
        if (null != d) {
            return String.valueOf(d);
        }
        return "";
    }

    public static String getRandom() {
        int[] array = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        Random rand = new Random();
        for (int i = 10; i > 1; i--) {
            int index = rand.nextInt(i);
            int tmp = array[index];
            array[index] = array[i - 1];
            array[i - 1] = tmp;
        }
        int result = 0;
        for (int i = 0; i < 6; i++)
            result = result * 10 + array[i];

        return "" + result;
    }

    /**
     * 处理树型码 获取本级别最大的code 如:301000 返回301999
     *
     * @param code
     * @return
     */
    public static int getMaxLevelCode(int code) {
        String codeStr = "" + code;
        StringBuffer str = new StringBuffer();
        boolean flag = true;
        for (int i = codeStr.length() - 1; i >= 0; i--) {
            char c = codeStr.charAt(i);
            if (c == '0' && flag) {
                str.insert(0, '9');
            } else {
                str.insert(0, c);
                flag = false;
            }
        }
        return Integer.valueOf(str.toString());
    }

    /**
     * 去掉sql的注释
     */
    public static String delSqlComment(String content) {
        String pattern = "/\\*(.|[\r\n])*?\\*/";
        Pattern p = Pattern.compile(pattern, 2 | Pattern.DOTALL);
        Matcher m = p.matcher(content);
        if (m.find()) {
            content = m.replaceAll("");
        }
        return content;
    }

    /**
     * 将InputStream转换为字符串
     *
     * @param is
     * @return
     */
    public static String inputStream2String(InputStream is) {
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        StringBuffer buffer = new StringBuffer();
        String line = "";
        try {
            while ((line = in.readLine()) != null) {
                buffer.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    /**
     * 将字符串转换为URL通用格式
     *
     * @param keyword
     * @return
     */
    public static String decodeURL(String keyword) {
        return decodeURL(keyword, "UTF-8");
    }

    /**
     * 将字符串转换为给定编码格式的URL通用格式
     *
     * @param keyword
     * @param charset
     * @return
     */
    public static String decodeURL(String keyword, String charset) {
        try {
            keyword = URLDecoder.decode(keyword, charset);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return keyword;
    }

    /**
     * 默认以UTF-8编码转义
     *
     * @param keyword
     * @return
     */
    public static String encodeURL(String keyword) {
        return encodeURL(keyword, "UTF-8");
    }

    /**
     * 将字符串转义为URI
     *
     * @param keyword 被转义的字符串
     * @param charset 转义用的编码
     * @return
     */
    public static String encodeURL(String keyword, String charset) {
        try {
            keyword = URLEncoder.encode(keyword, charset);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return keyword;
    }

    /**
     * 进行解析
     *
     * @param regex
     * @param rpstr
     * @param source
     * @return
     */
    public static String doFilter(String regex, String rpstr, String source) {
        Pattern p = Pattern.compile(regex, 2 | Pattern.DOTALL);
        Matcher m = p.matcher(source);
        return m.replaceAll(rpstr);
    }

    /**
     * 脚本过滤
     *
     * @param source
     * @return
     */
    public static String formatScript(String source) {
        source = source.replaceAll("javascript", "&#106avascript");
        source = source.replaceAll("jscript:", "&#106script:");
        source = source.replaceAll("js:", "&#106s:");
        source = source.replaceAll("value", "&#118alue");
        source = source.replaceAll("about:", "about&#58");
        source = source.replaceAll("file:", "file&#58");
        source = source.replaceAll("document.cookie", "documents&#46cookie");
        source = source.replaceAll("vbscript:", "&#118bscript:");
        source = source.replaceAll("vbs:", "&#118bs:");
        source = doFilter("(on(mouse|exit|error|click|key))", "&#111n$2",
                source);
        return source;
    }

    /**
     * 格式化HTML代码
     *
     * @param htmlContent
     * @return
     */
    public static String htmlDecode(String htmlContent) {
        htmlContent = formatScript(htmlContent);
        htmlContent = htmlContent.replaceAll(" ", "&nbsp;")
                .replaceAll("<", "&lt;").replaceAll(">", "&gt;")
                .replaceAll("\n\r", "<br>").replaceAll("\r\n", "<br>")
                .replaceAll("\r", "<br>");
        return htmlContent;
    }

    /**
     * 动态添加表前缀，对没有前缀的表增加前缀
     *
     * @param table
     * @param prefix
     * @return
     */
    public static String addPrefix(String table, String prefix) {
        String result = "";
        if (table.length() > prefix.length()) {
            if (table.substring(0, prefix.length()).toLowerCase()
                    .equals(prefix.toLowerCase()))
                result = table;
            else
                result = prefix + table;
        } else
            result = prefix + table;

        return result;
    }

    /**
     * 添加后缀
     *
     * @param table
     * @param suffix
     * @return
     */
    public static String addSuffix(String table, String suffix) {
        String result = "";
        if (table.length() > suffix.length()) {
            int start = table.length() - suffix.length();
            int end = start + suffix.length();
            if (table.substring(start, end).toLowerCase()
                    .equals(suffix.toLowerCase()))
                result = table;
            else
                result = table + suffix;
        } else
            result = table + suffix;

        return result;
    }

    /**
     * 得到异常的字串
     *
     * @param aThrowable
     * @return
     */
    public static String getStackTrace(Throwable aThrowable) {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        aThrowable.printStackTrace(printWriter);
        return result.toString();

    }

    /**
     * 驼峰命名法工具
     *
     * @return toCamelCase(" hello_world ") == "helloWorld"
     * toCapitalizeCamelCase("hello_world") == "HelloWorld"
     * toUnderScoreCase("helloWorld") = "hello_world"
     */
    public static String toCamelCase(String s) {
        if (s == null) {
            return null;
        }

        s = s.toLowerCase();

        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == SEPARATOR) {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    /**
     * 驼峰命名法工具
     *
     * @return toCamelCase(" hello_world ") == "helloWorld"
     * toCapitalizeCamelCase("hello_world") == "HelloWorld"
     * toUnderScoreCase("helloWorld") = "hello_world"
     */
    public static String toCapitalizeCamelCase(String s) {
        if (s == null) {
            return null;
        }
        s = toCamelCase(s);
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    /**
     * 驼峰命名法工具
     *
     * @return toCamelCase(" hello_world ") == "helloWorld"
     * toCapitalizeCamelCase("hello_world") == "HelloWorld"
     * toUnderScoreCase("helloWorld") = "hello_world"
     */
    public static String toUnderScoreCase(String s) {
        if (s == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            boolean nextUpperCase = true;

            if (i < (s.length() - 1)) {
                nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
            }

            if ((i > 0) && Character.isUpperCase(c)) {
                if (!upperCase || !nextUpperCase) {
                    sb.append(SEPARATOR);
                }
                upperCase = true;
            } else {
                upperCase = false;
            }

            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }

}
