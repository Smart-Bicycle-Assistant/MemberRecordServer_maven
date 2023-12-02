package com.sba.recordingserver.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCrypt;

//@Configuration
public class PasswordEncoder implements org.springframework.security.crypto.password.PasswordEncoder {

    public String encode(CharSequence rawPassword) {
        if (rawPassword == null) {
            throw new IllegalArgumentException("rawPassword cannot be null");
        }

        String salt;
//        if (random != null) {
//            salt = BCrypt.gensalt(version.getVersion(), strength, random);
//        } else {
//            salt = BCrypt.gensalt(version.getVersion(), strength);
//        }
        salt = BCrypt.gensalt();
        return BCrypt.hashpw(rawPassword.toString(), salt);
    }

    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (rawPassword == null) {
            throw new IllegalArgumentException("rawPassword cannot be null");
        }

//        if (encodedPassword == null || encodedPassword.length() == 0) {
//            logger.warn("Empty encoded password");
//            return false;
//        }
//
//        if (!BCRYPT_PATTERN.matcher(encodedPassword).matches()) {
//            logger.warn("Encoded password does not look like BCrypt");
//            return false;
//        }

        return BCrypt.checkpw(rawPassword.toString(), encodedPassword);
    }


}
