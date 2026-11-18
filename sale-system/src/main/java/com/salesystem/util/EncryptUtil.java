package com.salesystem.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 敏感数据加解密工具（加分项6）
 * - BCrypt：管理员/顾客密码加密
 * - AES：手机号等敏感字段加密存储
 */
@Component
public class EncryptUtil {

    private static final String AES_KEY = "Salesystem2024!!"; // 16字节密钥
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // ========== BCrypt 密码加密 ==========

    /** 加密密码 */
    public String encodePassword(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    /** 验证密码 */
    public boolean matchPassword(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }

    // ========== AES 手机号加密 ==========

    /** AES加密 */
    public String aesEncrypt(String plainText) {
        try {
            SecretKeySpec key = new SecretKeySpec(AES_KEY.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            return plainText; // 加密失败返回原文
        }
    }

    /** AES解密 */
    public String aesDecrypt(String cipherText) {
        try {
            SecretKeySpec key = new SecretKeySpec(AES_KEY.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(cipherText));
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return cipherText; // 解密失败返回原文
        }
    }

    // ========== 手机号脱敏 ==========

    /** 手机号脱敏显示 138****0001 */
    public String maskPhone(String phone) {
        if (phone == null || phone.length() < 7) return phone;
        return phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4);
    }

    /** 地址脱敏（只显示省市） */
    public String maskAddress(String address) {
        if (address == null || address.length() <= 6) return address;
        return address.substring(0, 6) + "****";
    }
}
