package org.guiceside.commons.lang;

import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeUtility;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.util.Date;

/**
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @version 1.0 2008-10-14
 * @since JDK1.5
 */
public class StringUtils extends org.apache.commons.lang.StringUtils {
    private final static String nullStr = "";

    private StringUtils() {

    }

    /**
     * 将连续的多余空格清除
     *
     * @param str
     * @return
     */
    public static String clearSpilthBank(String str) {
        if (isBlank(str)) {
            return null;
        }
        char[] chars = str.toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        boolean bankExist = false;
        if (chars != null && chars.length > 0) {
            for (char c : chars) {
                int asci = (int) c;
                if (asci == 32) {
                    if (!bankExist) {
                        bankExist = true;
                        stringBuilder.append(c);
                    } else {
                        continue;
                    }
                } else {
                    bankExist = false;
                    stringBuilder.append(c);
                }
            }
        }
        return stringBuilder.toString();
    }

    public static boolean getYesNo(String str) {
        if (StringUtils.isNotBlank(str)) {
            if (str.toLowerCase().equals("true")) {
                return true;
            } else if (str.toLowerCase().equals("false")) {
                return false;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static String toUtf8String(String s) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= 0 && c <= 255) {
                sb.append(c);
            } else {
                byte[] b;
                try {
                    b = Character.toString(c).getBytes("utf-8");
                } catch (Exception ex) {
                    b = new byte[0];
                }
                for (int j = 0; j < b.length; j++) {
                    int k = b[j];
                    if (k < 0)
                        k += 256;
                    sb.append("%" + Integer.toHexString(k).toUpperCase());
                }
            }
        }
        String s_utf8 = sb.toString();
        sb.delete(0, sb.length());
        sb.setLength(0);
        sb = null;
        return s_utf8;
    }

    public static String defaultIfEmptyByDate(Date source, String pattern) {
        if (source == null) {
            return nullStr;
        }
        return DateFormatUtil.format(source, pattern);
    }

    public static String defaultIfEmpty(String source) {
        return org.apache.commons.lang.StringUtils.defaultIfEmpty(source,
                nullStr);
    }

    public static String defaultIfEmpty(String source, String nullSTR) {
        return org.apache.commons.lang.StringUtils.defaultIfEmpty(source,
                nullSTR);
    }

    public static String defaultIfEmpty(String source, String nullSTR, String appendSTR) {
        return org.apache.commons.lang.StringUtils.defaultIfEmpty(source,
                nullSTR) + appendSTR;
    }

    public static String defaultIfEmpty(Object source) {
        if (source == null) {
            return nullStr;
        }
        return org.apache.commons.lang.StringUtils.defaultIfEmpty(source
                .toString(), nullStr);
    }

    public static String defaultIfEmpty(Object source, String nullSTR) {
        if (source == null) {
            return nullSTR;
        }
        return org.apache.commons.lang.StringUtils.defaultIfEmpty(source
                .toString(), nullSTR);
    }

    public static String defaultIfEmpty(Object source, String nullSTR, String appendSTR) {
        if (source == null) {
            return nullSTR;
        }
        return org.apache.commons.lang.StringUtils.defaultIfEmpty(source
                .toString(), nullSTR) + appendSTR;
    }

    public static String limitLength(String content, int length) {
        if (StringUtils.isBlank(content)) {
            return "";
        }
        if (content.length() >= length) {
            content = content.substring(0, length) + "..";
        }
        return content;
    }

    private static String utf8Togb2312(String str) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            switch (c) {
                case '+':
                    sb.append(' ');
                    break;
                case '%':
                    try {
                        sb.append((char) Integer.parseInt(
                                str.substring(i + 1, i + 3), 16));
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException();
                    }
                    i += 2;
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        // Undo conversion to external encoding
        String result = sb.toString();
        String res = null;
        try {
            byte[] inputBytes = result.getBytes("8859_1");
            res = new String(inputBytes, "UTF-8");
        } catch (Exception e) {
        }
        return res;
    }

    public static String encodeFileName(String filename, HttpServletRequest request) {
        /**
         * 获取客户端浏览器和操作系统信息
         * 在IE浏览器中得到的是：User-Agent=Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; Maxthon; Alexa Toolbar)
         * 在Firefox中得到的是：User-Agent=Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.7.10) Gecko/20050717 Firefox/1.0.6
         */
        String agent = request.getHeader("USER-AGENT");
        try {
            if ((agent != null) && (-1 != agent.indexOf("MSIE"))) {
                String newFileName = URLEncoder.encode(filename, "UTF-8");
                newFileName = StringUtils.replace(newFileName, "+", "%20");
                if (newFileName.length() > 150) {
                    newFileName = new String(filename.getBytes("GB2312"), "ISO8859-1");
                    newFileName = StringUtils.replace(newFileName, " ", "%20");
                }
                return newFileName;
            }
            if ((agent != null) && (-1 != agent.indexOf("Mozilla")))
                return MimeUtility.encodeText(filename, "UTF-8", "B");

            return filename;
        } catch (Exception ex) {
            return filename;
        }
    }

}
