package com.xuliugen.common.enums;

/**
 * Response返回码
 * Created by xuliugen on 16/1/11.
 */
public enum EResponseState {

    SUCCESS(0, "成功"),
    PARAM_INVALID(10000001, "参数不合法"),
    UNKNOWN_ERROR(99999999, "未知错误");

    public Integer value;
    public String desc;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    EResponseState(Integer code, String msg) {
        this.value = code;
        this.desc = msg;
    }

    public static EResponseState getTypeByVal(Integer value) {
        EResponseState defaultState = SUCCESS;
        for (EResponseState eRecordState : EResponseState.values()) {
            if (eRecordState.value.equals(value)) {
                return eRecordState;
            }
        }
        return defaultState;
    }

    public static String getDescByVal(Integer value) {
        return getTypeByVal(value).desc;
    }
}
