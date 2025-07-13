package com.academy.common.util;

import com.academy.common.constant.CommonConstant;
import org.apache.commons.lang3.StringUtils;

public class ValidationUtil {


    public static boolean isEmailValid(String email){
        if(StringUtils.isBlank(email)){
            return false;
        }
        return CommonConstant.EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isPasswordValid(String email){
        if(StringUtils.isBlank(email)){
            return false;
        }
        return CommonConstant.PASSWORD_PATTERN.matcher(email).matches();
    }
}
