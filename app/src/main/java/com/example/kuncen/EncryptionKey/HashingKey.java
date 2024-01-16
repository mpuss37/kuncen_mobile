package com.example.kuncen.EncryptionKey;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class HashingKey {
    private StringBuilder stringBuilder;
    private String message, pass5Char;

    private static SecretKey generateAESKeyFromHexString(String hexString) {
        byte[] keyBytes = hexStringToBytes(hexString);
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, "AES");
    }

    public String passToHash1(String input) {
        try {
            stringBuilder = new StringBuilder();
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            byte[] bytes = input.getBytes();
            sha1.update(bytes);
            byte[] sha1Hash = sha1.digest();

            for (byte b : sha1Hash) {
                stringBuilder.append(String.format("%02x", b));
            }
        } catch (NoSuchAlgorithmException e) {
            System.err.println("error class hashing key");
        }

        return stringBuilder.toString().toUpperCase();
    }

    public String checkerPass(String pass) {
        try {
            stringBuilder = new StringBuilder();
            pass5Char = pass.substring(0, 5);
            String apiUrl = "https://api.pwnedpasswords.com/range/" + pass5Char;
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            reader.close();

            if (stringBuilder.toString().contains(pass.substring(5))) {
                message = "your account not safe, Change yours password";
            } else {
                message = "your account its safe";
            }
            connection.disconnect();

        } catch (IOException e) {
            System.err.println(e);
        }

        return message;
    }

    public String encrypt(String plaintext, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public String decrypt(String encryptedText, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    public String bytesToHex(byte[] bytes) {
        for (byte b : bytes) {
            stringBuilder.append(String.format("%02x", b));
        }
        return stringBuilder.toString();
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

    public SecretKey keyFromHexString(String hexString) {
        byte[] keyBytes = hexStringToBytes(hexString);
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, "AES");
    }


//    public void main(String[] args) {
//        try {
//            String key = "a3271802a5318fb310c94d6f28943212";
////            SecretKey secretKey = generateAESKeyFromHexString(key);
//            SecretKey secretKey = keyFromHexString(key);
////            String keyString = bytesToHex(secretKey.getEncoded());
//            System.out.println(secretKey);
////            System.out.println(keyString);
////            String enkrip = encrypt("herdi", secretKey);
////            System.out.println("ini enkrip " + enkrip);
////            System.out.println("ini dekrip " + decrypt("LA40AD4JPVLDHwp0f64nag==", secretKey));
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
}
