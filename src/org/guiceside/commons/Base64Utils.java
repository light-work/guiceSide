package org.guiceside.commons;

import org.guiceside.commons.lang.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * Created with IntelliJ IDEA.
 * User: zhenjiaWang
 * Date: 14-2-4
 * Time: 下午11:41
 * To change this template use File | Settings | File Templates.
 */
public class Base64Utils {

    public static String encode(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(str.getBytes());
    }

    public static String decode(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            byte[] b = decoder.decodeBuffer(str);
            return new String(b, "UTF-8");
        } catch (Exception e) {
            return null;
        }
    }
}
