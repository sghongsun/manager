package me.sghong.manager.util;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.MessageDigest;

public class SHA512 implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        if (rawPassword == null) {
            throw new IllegalArgumentException("rawPassword cannot be null");
        }
        return this.getSHA512(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (rawPassword == null) {
            throw new IllegalArgumentException("rawPassword cannot be null");
        }
        if (encodedPassword == null || encodedPassword.isEmpty()) {
            return false;
        }

        String encodedRawPw = this.getSHA512(rawPassword);
        return encodedRawPw.equals(encodedPassword);
    }

    private String getSHA512(CharSequence val) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-512");
            md.update(val.toString().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

        byte[] msgb = md.digest();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < msgb.length; i++) {
            String tmp = Integer.toHexString(msgb[i] & 0xFF);
            while (tmp.length() < 2)
                tmp = "0" + tmp;
            sb.append(tmp.substring(tmp.length() - 2));
        }
        return sb.toString();
    }
}
