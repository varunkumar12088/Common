package com.academy.common.util;

public class KeyUtil {

    private static final String COLON = ":";
    private static final String EMPTY = "";

    public static String userRoleApiMapKey(String uri, String method){
        return uri + COLON + method;
    }

    public static String redisTokenKey(String appName, String username, String jti){
        return String.join(COLON,
                appName == null ? EMPTY : appName,
                username == null ? EMPTY : username,
                jti == null ? EMPTY : jti
        );
    }

    public static String redisTokenKey(String appName, String username, String jti, String type){
        return String.join(COLON,
                appName == null ? EMPTY : appName,
                username == null ? EMPTY : username,
                jti == null ? EMPTY : jti,
                type == null ? EMPTY : type
        );
    }

    public static String redisTokenKey(String appName, String username){
        return String.join(COLON,
                appName == null ? EMPTY : appName,
                username == null ? EMPTY : username
        );
    }
}
