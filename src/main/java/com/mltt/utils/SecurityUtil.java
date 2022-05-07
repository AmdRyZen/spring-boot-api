package com.mltt.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

public class SecurityUtil {
    public static final String KEY_ALGORITHM = "RSA";
    private static final String PUBLIC_KEY = "RSAPublicKey";
    private static final String PRIVATE_KEY = "RSAPrivateKey";
    static private final int     BASELENGTH           = 128;
    static private final int     LOOKUPLENGTH         = 64;
    static private final int     TWENTYFOURBITGROUP   = 24;
    static private final int     EIGHTBIT             = 8;
    static private final int     SIXTEENBIT           = 16;
    static private final int     FOURBYTE             = 4;
    static private final int     SIGN                 = -128;
    static private final char    PAD                  = '=';
    static private final boolean fDebug               = false;
    static final private byte[]  base64Alphabet       = new byte[BASELENGTH];
    static final private char[]  lookUpBase64Alphabet = new char[LOOKUPLENGTH];
    private final static Log log = LogFactory.getLog(SecurityUtil.class);

    /**
     * md5签名，先用md5算法转化为byte数组，然后转化为hexString
     *
     * @param str
     * @return
     */
    public static String md5(String str) {
        return md5(str, "utf-8");
    }

    /**
     * MD5加密函数
     *
     * @param
     * @return
     */
    public static String md5(String str, String encode) {
        if (str == null) {
            return null;
        }
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(str.getBytes(encode));
            byte[] digest = md5.digest();

            StringBuffer hexString = new StringBuffer();
            String strTemp;
            for (int i = 0; i < digest.length; i++) {
                // byteVar &
                // 0x000000FF的作用是，如果digest[i]是负数，则会清除前面24个零，正的byte整型不受影响。
                // (...) | 0xFFFFFF00的作用是，如果digest[i]是正数，则置前24位为一，
                // 这样toHexString输出一个小于等于15的byte整型的十六进制时，倒数第二位为零且不会被丢弃，这样可以通过substring方法进行截取最后两位即可。
                strTemp = Integer.toHexString(
                        (digest[i] & 0x000000FF) | 0xFFFFFF00).substring(6);
                hexString.append(strTemp);
            }

            return hexString.toString();
        } catch (Exception e) {
            log.error("MD5加密发生错误" + e.getMessage());
        }
        return encode;
    }

    /**
     * aes解密-128位 key长度为16 AES/CBC/NoPadding
     *
     * @param encryptContent
     *            (16进制) 密文
     * @param password
     *            密钥
     * @return 原文
     */
    public static String aesDecryptKey16(String encryptContent, String password) {

        if (StringUtils.isEmpty(password) || password.length() != 16) {
            log.error("密钥长度为16位");
        }
        if (StringUtils.isEmpty(encryptContent)) {
            log.error("密文为空");
        }

        try {
            String key = password;
            String iv = password;
            byte[] encrypted1 = Base64.getDecoder().decode(encryptContent.getBytes(StandardCharsets.UTF_8));
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
            byte[] original = cipher.doFinal(encrypted1);
            return new String(original).trim();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("aes解密发生错误" + e.getMessage());
        }
        return password;

    }

    /**
     * aes加密-128位 key长度为16 AES/CBC/NoPadding
     *
     * @param content
     *            原文
     * @param password
     *            密钥
     * @return 16进制密文
     */
    public static String aesEncryptKey16(String content, String password) {

        if (StringUtils.isEmpty(password) || password.length() != 16) {
            log.error("密钥长度为16位");
        }

        try {
            String key = password;
            String iv = password;
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            int blockSize = cipher.getBlockSize();
            byte[] dataBytes = content.trim().getBytes("utf-8");
            int plaintextLength = dataBytes.length;

            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength
                        + (blockSize - (plaintextLength % blockSize));
            }
            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            byte[] encrypted = cipher.doFinal(plaintext);
            return Base64.getEncoder().encode(encrypted).toString();
        } catch (Exception e) {
            log.error("aes加密发生错误" + e.getMessage());
        }
        return password;
    }

    /**
     * DES加密
     *
     * @param source
     * @param desKey
     * @return
     */
    public static String desEncrypt(String source, String desKey) {
        try {
            // 从原始密匙数据创建DESKeySpec对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(new DESKeySpec(
                    desKey.getBytes()));
            // Cipher对象实际完成加密操作
            Cipher cipher = Cipher.getInstance("DES");
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, securekey);
            // 现在，获取数据并加密
            byte[] destBytes = cipher.doFinal(source.getBytes());
            StringBuilder hexRetSB = new StringBuilder();
            for (byte b : destBytes) {
                String hexString = Integer.toHexString(0x00ff & b);
                hexRetSB.append(hexString.length() == 1 ? 0 : "").append(
                        hexString);
            }
            return hexRetSB.toString();
        } catch (Exception e) {
            log.error("DES加密发生错误" + e.getMessage());
        }
        return desKey;
    }

    /**
     * DES解密
     *
     * @param source
     * @param desKey
     * @return
     */
    public static String desDecrypt(String source, String desKey) {
        // 解密数据
        byte[] sourceBytes = new byte[source.length() / 2];
        for (int i = 0; i < sourceBytes.length; i++) {
            sourceBytes[i] = (byte) Integer.parseInt(
                    source.substring(i * 2, i * 2 + 2), 16);
        }
        return desDecrypt(sourceBytes, desKey);
    }

    /**
     * DES解密
     *
     * @param source
     * @param desKey
     * @return
     */
    public static String desDecrypt(byte[] source, String desKey) {
        try {
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(new DESKeySpec(
                    desKey.getBytes()));
            Cipher cipher = Cipher.getInstance("DES");
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.DECRYPT_MODE, securekey);
            // 现在，获取数据并解密
            byte[] destBytes = cipher.doFinal(source);
            return new String(destBytes);
        } catch (Exception e) {
            log.error("DES解密发生错误" + e.getMessage());
        }
        return desKey;
    }

    /**
     * 3DES加密
     *
     * @param keybyte
     * @param src
     * @return
     */
    public static byte[] threeDesEncrypt(byte[] keybyte, byte[] src) {
        try {
            // 生成密钥
            byte[] key = new byte[24];
            if (keybyte.length < key.length) {
                System.arraycopy(keybyte, 0, key, 0, keybyte.length);
            } else {
                System.arraycopy(keybyte, 0, key, 0, key.length);
            }
            SecretKey deskey = new SecretKeySpec(key, "DESede");
            // 加密
            Cipher c1 = Cipher.getInstance("DESede/ECB/PKCS5Padding");
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (Exception e) {
            log.error("3DES加密发生错误" + e.getMessage());
        }
        return src;
    }

    /**
     * 3DES解密
     *
     * @param keybyte
     *            加密密钥，长度为24字节
     * @param src
     *            加密后的缓冲区
     * @return
     */
    public static byte[] threeDesDecrypt(byte[] keybyte, byte[] src) {
        try {
            // 生成密钥
            byte[] key = new byte[24];
            if (keybyte.length < key.length) {
                System.arraycopy(keybyte, 0, key, 0, keybyte.length);
            } else {
                System.arraycopy(keybyte, 0, key, 0, key.length);
            }
            SecretKey deskey = new SecretKeySpec(key, "DESede");
            // 解密
            Cipher c1 = Cipher.getInstance("DESede/ECB/PKCS5Padding");
            c1.init(Cipher.DECRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (Exception e) {
            log.error("3DES解密发生错误" + e.getMessage());
        }
        return src;
    }

    /**
     * 生成密钥对(公钥和私钥)
     * @return
     * @throws Exception
     */
    public static Map<String, Object> genKeyPair() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, Object> keyMap = new HashMap<String, Object>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

    /**
     * Encodes hex octects into Base64
     *
     * @param binaryData Array containing binaryData
     * @return Encoded Base64 array
     */
    public static String encode(byte[] binaryData) {

        if (binaryData == null) {
            return null;
        }

        int lengthDataBits = binaryData.length * EIGHTBIT;
        if (lengthDataBits == 0) {
            return "";
        }

        int fewerThan24bits = lengthDataBits % TWENTYFOURBITGROUP;
        int numberTriplets = lengthDataBits / TWENTYFOURBITGROUP;
        int numberQuartet = fewerThan24bits != 0 ? numberTriplets + 1 : numberTriplets;
        char encodedData[] = null;

        encodedData = new char[numberQuartet * 4];

        byte k = 0, l = 0, b1 = 0, b2 = 0, b3 = 0;

        int encodedIndex = 0;
        int dataIndex = 0;
        if (fDebug) {
            System.out.println("number of triplets = " + numberTriplets);
        }

        for (int i = 0; i < numberTriplets; i++) {
            b1 = binaryData[dataIndex++];
            b2 = binaryData[dataIndex++];
            b3 = binaryData[dataIndex++];

            if (fDebug) {
                System.out.println("b1= " + b1 + ", b2= " + b2 + ", b3= " + b3);
            }

            l = (byte) (b2 & 0x0f);
            k = (byte) (b1 & 0x03);

            byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2) : (byte) ((b1) >> 2 ^ 0xc0);
            byte val2 = ((b2 & SIGN) == 0) ? (byte) (b2 >> 4) : (byte) ((b2) >> 4 ^ 0xf0);
            byte val3 = ((b3 & SIGN) == 0) ? (byte) (b3 >> 6) : (byte) ((b3) >> 6 ^ 0xfc);

            if (fDebug) {
                System.out.println("val2 = " + val2);
                System.out.println("k4   = " + (k << 4));
                System.out.println("vak  = " + (val2 | (k << 4)));
            }

            encodedData[encodedIndex++] = lookUpBase64Alphabet[val1];
            encodedData[encodedIndex++] = lookUpBase64Alphabet[val2 | (k << 4)];
            encodedData[encodedIndex++] = lookUpBase64Alphabet[(l << 2) | val3];
            encodedData[encodedIndex++] = lookUpBase64Alphabet[b3 & 0x3f];
        }

        // form integral number of 6-bit groups
        if (fewerThan24bits == EIGHTBIT) {
            b1 = binaryData[dataIndex];
            k = (byte) (b1 & 0x03);
            if (fDebug) {
                System.out.println("b1=" + b1);
                System.out.println("b1<<2 = " + (b1 >> 2));
            }
            byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2) : (byte) ((b1) >> 2 ^ 0xc0);
            encodedData[encodedIndex++] = lookUpBase64Alphabet[val1];
            encodedData[encodedIndex++] = lookUpBase64Alphabet[k << 4];
            encodedData[encodedIndex++] = PAD;
            encodedData[encodedIndex++] = PAD;
        } else if (fewerThan24bits == SIXTEENBIT) {
            b1 = binaryData[dataIndex];
            b2 = binaryData[dataIndex + 1];
            l = (byte) (b2 & 0x0f);
            k = (byte) (b1 & 0x03);

            byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2) : (byte) ((b1) >> 2 ^ 0xc0);
            byte val2 = ((b2 & SIGN) == 0) ? (byte) (b2 >> 4) : (byte) ((b2) >> 4 ^ 0xf0);

            encodedData[encodedIndex++] = lookUpBase64Alphabet[val1];
            encodedData[encodedIndex++] = lookUpBase64Alphabet[val2 | (k << 4)];
            encodedData[encodedIndex++] = lookUpBase64Alphabet[l << 2];
            encodedData[encodedIndex++] = PAD;
        }

        return new String(encodedData);
    }

    /**
     * Decodes Base64 data into octects
     *
     * @param encoded string containing Base64 data
     * @return Array containind decoded data.
     */
    public static byte[] decode(String encoded) {

        if (encoded == null) {
            return null;
        }

        char[] base64Data = encoded.toCharArray();
        // remove white spaces
        int len = removeWhiteSpace(base64Data);

        if (len % FOURBYTE != 0) {
            return null;//should be divisible by four
        }

        int numberQuadruple = (len / FOURBYTE);

        if (numberQuadruple == 0) {
            return new byte[0];
        }

        byte decodedData[] = null;
        byte b1 = 0, b2 = 0, b3 = 0, b4 = 0;
        char d1 = 0, d2 = 0, d3 = 0, d4 = 0;

        int i = 0;
        int encodedIndex = 0;
        int dataIndex = 0;
        decodedData = new byte[(numberQuadruple) * 3];

        for (; i < numberQuadruple - 1; i++) {

            if (!isData((d1 = base64Data[dataIndex++])) || !isData((d2 = base64Data[dataIndex++]))
                    || !isData((d3 = base64Data[dataIndex++]))
                    || !isData((d4 = base64Data[dataIndex++]))) {
                return null;
            }//if found "no data" just return null

            b1 = base64Alphabet[d1];
            b2 = base64Alphabet[d2];
            b3 = base64Alphabet[d3];
            b4 = base64Alphabet[d4];

            decodedData[encodedIndex++] = (byte) (b1 << 2 | b2 >> 4);
            decodedData[encodedIndex++] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
            decodedData[encodedIndex++] = (byte) (b3 << 6 | b4);
        }

        if (!isData((d1 = base64Data[dataIndex++])) || !isData((d2 = base64Data[dataIndex++]))) {
            return null;//if found "no data" just return null
        }

        b1 = base64Alphabet[d1];
        b2 = base64Alphabet[d2];

        d3 = base64Data[dataIndex++];
        d4 = base64Data[dataIndex++];
        if (!isData((d3)) || !isData((d4))) {//Check if they are PAD characters
            if (isPad(d3) && isPad(d4)) {
                if ((b2 & 0xf) != 0)//last 4 bits should be zero
                {
                    return null;
                }
                byte[] tmp = new byte[i * 3 + 1];
                System.arraycopy(decodedData, 0, tmp, 0, i * 3);
                tmp[encodedIndex] = (byte) (b1 << 2 | b2 >> 4);
                return tmp;
            } else if (!isPad(d3) && isPad(d4)) {
                b3 = base64Alphabet[d3];
                if ((b3 & 0x3) != 0)//last 2 bits should be zero
                {
                    return null;
                }
                byte[] tmp = new byte[i * 3 + 2];
                System.arraycopy(decodedData, 0, tmp, 0, i * 3);
                tmp[encodedIndex++] = (byte) (b1 << 2 | b2 >> 4);
                tmp[encodedIndex] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
                return tmp;
            } else {
                return null;
            }
        } else { //No PAD e.g 3cQl
            b3 = base64Alphabet[d3];
            b4 = base64Alphabet[d4];
            decodedData[encodedIndex++] = (byte) (b1 << 2 | b2 >> 4);
            decodedData[encodedIndex++] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
            decodedData[encodedIndex++] = (byte) (b3 << 6 | b4);

        }

        return decodedData;
    }

    /**
     * remove WhiteSpace from MIME containing encoded Base64 data.
     *
     * @param data  the byte array of base64 data (with WS)
     * @return      the new length
     */
    private static int removeWhiteSpace(char[] data) {
        if (data == null) {
            return 0;
        }

        // count characters that's not whitespace
        int newSize = 0;
        int len = data.length;
        for (int i = 0; i < len; i++) {
            if (!isWhiteSpace(data[i])) {
                data[newSize++] = data[i];
            }
        }
        return newSize;
    }
    static {
        for (int i = 0; i < BASELENGTH; ++i) {
            base64Alphabet[i] = -1;
        }
        for (int i = 'Z'; i >= 'A'; i--) {
            base64Alphabet[i] = (byte) (i - 'A');
        }
        for (int i = 'z'; i >= 'a'; i--) {
            base64Alphabet[i] = (byte) (i - 'a' + 26);
        }

        for (int i = '9'; i >= '0'; i--) {
            base64Alphabet[i] = (byte) (i - '0' + 52);
        }

        base64Alphabet['+'] = 62;
        base64Alphabet['/'] = 63;

        for (int i = 0; i <= 25; i++) {
            lookUpBase64Alphabet[i] = (char) ('A' + i);
        }

        for (int i = 26, j = 0; i <= 51; i++, j++) {
            lookUpBase64Alphabet[i] = (char) ('a' + j);
        }

        for (int i = 52, j = 0; i <= 61; i++, j++) {
            lookUpBase64Alphabet[i] = (char) ('0' + j);
        }
        lookUpBase64Alphabet[62] = (char) '+';
        lookUpBase64Alphabet[63] = (char) '/';

    }

    private static boolean isWhiteSpace(char octect) {
        return (octect == 0x20 || octect == 0xd || octect == 0xa || octect == 0x9);
    }

    private static boolean isPad(char octect) {
        return (octect == PAD);
    }

    private static boolean isData(char octect) {
        return (octect < BASELENGTH && base64Alphabet[octect] != -1);
    }
}
