package com.xuliugen.common.exception.extend;

/**
 * 类    名：org.openkoala.exception.BizException<br />
 * <p/>
 * 功能描述：业务异常<br />
 * <p/>
 * 创建日期：2013-1-18  <br />
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
public class BizException extends ApplicationException {

    private static final long serialVersionUID = -140076445751602150L;

    public BizException(String errorCode, String defaultMessage) {
        super(errorCode, defaultMessage);
    }

    public BizException(Throwable cause, String errorCode, String defaultMessage) {
        super(cause, errorCode, defaultMessage);
    }

    public BizException(Throwable cause, String errorCode) {
        super(cause, errorCode);
    }

}
