package coloryr.allmusic.core.music.api;

import coloryr.allmusic.core.AllMusic;
import coloryr.allmusic.core.objs.api.EncResObj;
import com.google.gson.JsonObject;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class CryptoUtil {

    private static final String presetKey = "0CoJUm6Qyw8W8jud";
    private static final String iv = "0102030405060708";
    private static final String base62 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    private static final String eapiKey = "e82ckenh8dichen8";

    private static String createSecretKey(int size) {
        StringBuilder key = new StringBuilder();
        for (int i = 0; i < size; i++) {
            double index = Math.floor(Math.random() * base62.length());
            key.append(base62.charAt((int) index));
        }
        return key.toString();
    }

    public static String byteArrToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    private static String aesEncrypt(String content, String key, String iv) {
        String result = null;
        if (content == null || key == null)
            return result;
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
            byte[] keys = key.getBytes(StandardCharsets.UTF_8);
            byte[] ivs = iv.getBytes(StandardCharsets.UTF_8);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(keys, "AES"),
                    new IvParameterSpec(ivs));
            bytes = cipher.doFinal(bytes);
            result = Base64.getEncoder().encodeToString(bytes);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String aesEncrypt(String content, String key) {
        String result = null;
        if (content == null || key == null)
            return result;
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
            byte[] keys = key.getBytes(StandardCharsets.UTF_8);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(keys, "AES"));
            bytes = cipher.doFinal(bytes);
            result = byteArrToHex(bytes).toUpperCase();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String zFill(String str) {
        StringBuilder strBuilder = new StringBuilder(str);
        while (strBuilder.length() < 256) {
            strBuilder.insert(0, "0");
        }
        str = strBuilder.toString();
        return str;
    }

    private static String rsaEncrypt(String text) {
        text = new StringBuffer(text).reverse().toString();

        BigInteger biText = new BigInteger(strToHex(text), 16);
        BigInteger biEx = new BigInteger("010001", 16);
        BigInteger biMod = new BigInteger("00e0b509f6259df8642dbc35662901477df22677ec152b5ff68ace615bb7b725152b3ab17a876aea8a5aa76d2e417629ec4ee341f56135fccf695280104e0312ecbda92557c93870114af6c9d05c4f7f0c3685b7a46bee255932575cce10b424d813cfe4875d3e82047b97ddef52741d546b8e289dc6935b3ece0462db0a22b8e7", 16);
        BigInteger biRet = biText.modPow(biEx, biMod);

        return zFill(biRet.toString(16));
    }

    private static String strToHex(String s) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            int ch = s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str.append(s4);
        }
        return str.toString();
    }

    public static EncResObj weapiEncrypt(String content) {
        String key = createSecretKey(16);
        String encText = aesEncrypt(aesEncrypt(content, presetKey, iv), key, iv);
        String encSecKey = rsaEncrypt(key);
        return new EncResObj(encText, encSecKey);
    }

    public static EncResObj eapi(String url, JsonObject object) {
        String text = AllMusic.gson.toJson(object);
        String message = "nobody" + url + "use" + text + "md5forencrypt";
        String digest = getMd5(message);
        String data = url + "-36cd479b6b5-" + text + "-36cd479b6b5-" + digest;
        return new EncResObj(aesEncrypt(data, eapiKey), "");
    }

    public static String getMd5(String plainText) {
        byte[] secretBytes;
        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            digest.update(plainText.getBytes(StandardCharsets.UTF_8));
            secretBytes = digest.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("没有这个md5算法！");
        }
        return byteArrToHex(secretBytes);
    }
}