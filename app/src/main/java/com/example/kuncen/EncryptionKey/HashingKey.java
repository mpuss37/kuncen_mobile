package com.example.kuncen.EncryptionKey;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;

public class HashingKey {
    public static SecretKey generateAESKeyFromHexString(String hexString) {
        byte[] keyBytes = hexStringToBytes(hexString);
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, "AES");
    }

    public static String encrypt(String plaintext, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String encryptedText, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }


    public static byte[] hexStringToBytes(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) +
                    Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }

    public static SecretKey keyFromHexString(String hexString) {
        byte[] keyBytes = hexStringToBytes(hexString);
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, "AES");
    }


    public static void main(String[] args) {
        try {
            String key = "a3271802a5318fb310c94d6f28943212";
//            SecretKey secretKey = generateAESKeyFromHexString(key);
            SecretKey secretKey = keyFromHexString(key);
            String keyString = bytesToHex(secretKey.getEncoded());
            System.out.println(secretKey);
            System.out.println(keyString);
//            String enkrip = encrypt("herdi", secretKey);
//            System.out.println("ini enkrip " + enkrip);
//            System.out.println("ini dekrip " + decrypt("LA40AD4JPVLDHwp0f64nag==", secretKey));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
