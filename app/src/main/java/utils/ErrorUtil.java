package utils;

import com.alibaba.fastjson.JSON;

import bean.ErrorBean;

/**
 * Created by Jason on 2016/11/29.
 */

public class ErrorUtil {

    public static String getErrorMessage(String error){
        ErrorBean errorBean= JSON.parseObject(error,ErrorBean.class);
        return errorBean.getError();
    }
}
