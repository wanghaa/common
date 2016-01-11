package com.xuliugen.common.exception.base.utils;

import com.xuliugen.common.exception.base.BaseException;
import com.xuliugen.common.i18n.I18NManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 类    名：org.openkoala.exception.base.org.openkoala.businesslog.utils.WebErrUtils<br />
 * <p/>
 * 功能描述：web错误工具<br />
 * <p/>
 * 创建日期：2013-1-21  <br />
 * <p/>
 * 版本信息：v 1.0<br />
 * <p/>
 * 版权信息：Copyright (c) 2011 openkoala All Rights Reserved<br />
 * <p/>
 * 作    者：<a href="mailto:jiangwei@openkoala.com">vakin jiang</a><br />
 * <p/>
 * 修改记录： <br />
 * 修 改 者    修改日期     文件版本   修改说明
 */
public class WebErrUtils {

    public static final String DEFAULT_ERROR_MSG = "系统繁忙，请稍后再试";

    public static final String ERROR_KEY = "errorMsg";

    /**
     *
     */
    public static final String RESULT_BUSS_EXCEPTION = "buss_exception";

    /**
     * 输出json数据到web页面
     * @param json
     * @throws IOException
     */
    public static void writeJSON(String json) {
        try {
            writeObject(json, "text/x-json;charset=UTF-8");
        } catch (Exception e) {
        }

    }

    public static void writeJSON(String key, String value) {

        try {
            String template = "{\"{key}\":\"{value}\"}";
            writeObject(template.replace("{key}", key).replace("{value}", value), "text/x-json;charset=UTF-8");
        } catch (Exception e) {
        }
    }


    /**
     * 直接输出HTML.
     * @param text
     * @throws IOException
     */
    public static void writeHTML(String text) {
        try {
            writeObject(text == null ? "" : text, "text/html;charset=UTF-8");
        } catch (Exception e) {
        }
    }

    /**
     * 输出.
     * @param contentType 内容的类型.html,text,xml的值见后，json为"text/x-json;charset=UTF-8"
     * @throws IOException
     */
    private static void writeObject(String text,
                                    String contentType) throws IOException {
        PrintWriter writer = null;
        try {
            HttpServletResponse response = ServletActionContext.getResponse();
            response.setContentType(contentType);
            writer = response.getWriter();
            writer.write(text);
        } finally {
            try {
                writer.close();
            } catch (Exception e2) {
            }
        }
    }

    /**
     * 格式化异常信息，用于友好响应用户
     * @param e
     * @return
     */
    public static String formatException(Exception e) {
        String message = null;
        Throwable ourCause = e;
        while ((ourCause = e.getCause()) != null) {
            e = (Exception) ourCause;
        }
        String eClassName = e.getClass().getName();
        //一些常见异常提示
        if ("java.lang.NumberFormatException".equals(eClassName)) {
            message = WebErrUtils.getI18nMessage("NumberFormatException", "请输入正确的数字");
        } else if (e instanceof BaseException || e instanceof RuntimeException) {
            message = e.getMessage();
            if (StringUtils.isBlank(message)) message = e.toString();
        }

        //获取默认异常提示
        if (StringUtils.isBlank(message)) {
            message = WebErrUtils.getDefaultMessage();
        }
        //替换特殊字符
        message = message.replaceAll("\"", "'");
        return message;
    }

    /**
     * 获取国际化异常信息
     * @param errorCode  错误码 对于i18n配置
     * @param defaultMsg 默认错误
     * @return
     */
    public static String getI18nMessage(String errorCode, String defaultMsg) {
        String message = null;
        if (StringUtils.isNotBlank(errorCode)) {
            message = I18NManager.getMessage(errorCode);
            if (errorCode.equals(message)) message = null;
        }
        if (StringUtils.isBlank(message)) {
            message = StringUtils.isBlank(defaultMsg) ? getDefaultMessage() : defaultMsg;
        }
        return message + ",错误码[" + errorCode + "]";
    }

    /**
     * 获取默认异常
     * @return
     */
    public static String getDefaultMessage() {
        String defaultMessage = I18NManager.getMessage("default.exception.message");
        if (StringUtils.isBlank(defaultMessage)) {
            defaultMessage = WebErrUtils.DEFAULT_ERROR_MSG;
        }
        return defaultMessage;
    }

//    public static void main(String[] args) {
//        SystemException exception = new SystemException("errCode.sys", "系统模拟异常");
//        System.out.println(formatException(exception));
//    }
}
