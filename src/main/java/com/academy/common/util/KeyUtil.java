package com.academy.common.util;

public class KeyUtil {

    private static final String COLON = ":";

    public static String userRoleApiMapKey(String uri, String method){
        return uri + COLON + method;
    }

    public static String redisTokenKey(String appName, String username, String jti){
        return appName + COLON + username + COLON + jti;
    }

    public static String redisTokenKey(String appName, String username){
        return appName + COLON + username;
    }
}
