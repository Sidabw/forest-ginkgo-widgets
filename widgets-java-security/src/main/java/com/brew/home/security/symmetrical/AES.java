package com.brew.home.security.symmetrical;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

/**
 * 〈一句话功能简述〉:
 * 〈〉
 *
 * @author feiyi
 * @create 2020/12/5
 * @since 1.0.0
 */
public class AES {

    private static final String originStr = "I'm sidalu";

    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, DecoderException {
        //1⃣️初始化密钥
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        //128 or 192 or 256
        //256好像额外需要一个无政策限制权限文件... 不懂
        keyGenerator.init(128);
        SecretKey secretKey = keyGenerator.generateKey();
        byte[] keyBytes = secretKey.getEncoded();
        String hexStr = new String(Hex.encodeHex(keyBytes));
        //只是输出看一下
        System.out.println("十六进制密钥：" + hexStr);

        //2⃣️密钥key转换
        byte[] fromHexBytes = Hex.decodeHex(hexStr.toCharArray());
        Key key = new SecretKeySpec(fromHexBytes,"AES");

        //3⃣️加密
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] result = cipher.doFinal(originStr.getBytes());
        System.out.println("jdk AES encrypt: " +  new String(Base64.encodeBase64(result)));

        //4⃣️解密
        Key key2 = new SecretKeySpec(fromHexBytes,"AES");
        Cipher cipher2 = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher2.init(Cipher.DECRYPT_MODE, key2);
        result = cipher2.doFinal(result);
        System.out.println("jdk aes decrypt: " + new String(result));

    }
}
