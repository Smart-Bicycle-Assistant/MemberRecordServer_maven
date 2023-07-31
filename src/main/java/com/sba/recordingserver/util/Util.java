package com.sba.recordingserver.util;

import java.util.concurrent.ThreadLocalRandom;

public class Util {
    public static String createPassword() {
        String newPassword = new String(String.valueOf(ThreadLocalRandom.current().nextInt(100000,999999)));
        return newPassword;
    }
}
