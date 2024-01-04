package com.hodbenor.project.eventsservice.security;

import com.hodbenor.project.eventsservice.dao.beans.User;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;

@Service
public class TokenService {

    public String generateToken() {
        long expirationDate = LocalDateTime.now().plusMinutes(10).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        byte[] randomBytes = new byte[4];
        new SecureRandom().nextBytes(randomBytes);

        return Base64.getUrlEncoder().encodeToString(randomBytes) + "|" + expirationDate;
    }

    public boolean validToken(String token) {
        try {
            String[] parts = token.split("\\|");

            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid token format");
            }

            Instant expiration = Instant.ofEpochMilli(Long.parseLong(parts[1]));

            return Instant.now().isBefore(expiration);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error parsing token");
        }
    }

    private static String hashString(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));

            // Convert byte array to hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
