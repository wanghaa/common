package com.xuliugen.common.enums;

/**
 * 数据库中记录状态的枚举
 * Created by xuliugen on 16/1/10.
 */
public enum ERecordState {

    VALID(0, "有效"),
    INVALID(1, "无效");

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

    ERecordState(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static ERecordState getTypeByVal(Integer value) {
        ERecordState defaultState = VALID;
        for (ERecordState eRecordState : ERecordState.values()) {
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
