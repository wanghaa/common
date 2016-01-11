package com.xuliugen.common.constant;

/**
 * HTTP返回码
 * Created by xuliugen on 16/1/10.
 */
public interface ConstHttpCode {

    /**
     * SUCCESS,指示客服端的请求已经成功收到，解析，接受。
     */
    String HTTP_CODE_SUCCESS = "200";

    /**
     * ACCEPTED,请求已经被接受用来处理。但是处理并没有完成。
     */
    String HTTP_CODE_ACCEPTED = "202";

    /**
     * BAD_REQUEST,因为错误的语法导致服务器无法理解请求信息。
     */
    String HTTP_CODE_BAD_REQUEST = "400";

}
