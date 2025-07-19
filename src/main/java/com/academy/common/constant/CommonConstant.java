package com.academy.common.constant;

import java.util.regex.Pattern;

public interface CommonConstant {

    String X_TRACKING_ID = "X-Tracking-Id";
    String X_USER_ROLE = "X-USER-ROLE";
    String X_CLIENT_IP = "X-CLIENT-IP";
    String X_USER_EMAIL = "X-USER-EMAIL";
    String X_USER_NAME = "X-USER-NAME";
    String CONTENT_TYPE = "Content-Type";
    String APPLICATION_JSON = "application/json";
    String ACCEPT = "Accept";

    String X_FORWARDED_FOR = "X-Forwarded-For";
    String PROXY_CLIENT_IP = "Proxy-Client-IP";
    String WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";
    String HTTP_X_FORWARDED_FOR = "HTTP_X_FORWARDED_FOR";
    String UNKNOWN = "unknown";

    String EMAIL_VALIDATION_REGEX = "^[a-zA-Z][0-9]?[a-zA-Z]?[0-9]?\\.@[a-zA-Z]+\\.[a-zA-Z]+$";
    Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_VALIDATION_REGEX);
    String PASSWORD_VALIDATION_REGEX = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+=\\-{}\\[\\]:;\"'<>,.?/]).{8,12}$";
    Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_VALIDATION_REGEX);

    String ENCRYPTION_KEY = "";
    String ENCRYPTION_KEY_ID = "encryption.key";


}
