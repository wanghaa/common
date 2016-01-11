package com.xuliugen.common.constant;

/**
 * 正则表达式常量类
 * Created by xuliugen on 16/1/10.
 */
public interface ConstRegex {

    /**
     * 邮件正则表达式
     */
    String REGEX_EMAIL = "[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?";

    //TODO 完成tel的正则表达
    /**
     * 点换号码正则表达式
     */
    String REGEX_TELPHONE = "";

    /**
     * QQ号正则表达式
     */
    String REGEX_QQ = "";
}
