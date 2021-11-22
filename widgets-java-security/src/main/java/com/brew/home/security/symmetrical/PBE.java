package com.brew.home.security.symmetrical;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.security.*;
import java.security.spec.InvalidKeySpecException;

/**
 * 〈一句话功能简述〉:
 * 〈〉
 *
 * @author feiyi
 * @create 2020/12/5
 * @since 1.0.0
 */
public class PBE {

    private static final String originStr = "I'm sidalu";

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        //1⃣️初始化盐
        SecureRandom random = new SecureRandom();
        byte[] salt = random.generateSeed(8);

        //2⃣️加密 口令与密钥
        String password = "brewwww";
        PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray());
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBEWITHMD5andDES");
        Key key = factory.generateSecret(pbeKeySpec);

        //3⃣️加密
        PBEParameterSpec pbeParameterSpec = new PBEParameterSpec(salt,100);
        Cipher cipher = Cipher.getInstance("PBEWITHMD5andDES");
        cipher.init(Cipher.ENCRYPT_MODE, key, pbeParameterSpec);
        byte[] result = cipher.doFinal(originStr.getBytes());
        System.out.println("jdk PBE encrypt: " + new String(Base64.encodeBase64(result)));

        //4⃣️解密
        cipher.init(Cipher.DECRYPT_MODE, key,pbeParameterSpec);
        result = cipher.doFinal(result);
        System.out.println("jdk PBE decrypt: " + new String(result));
    }
}
