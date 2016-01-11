package com.xuliugen.common.valueobject;

/**
 * 实体状态枚举（对应数据库中记录）
 * Created by xuliugen on 16/1/11.
 */
public enum EEntityState {

    VALID(0, "有效"),
    INVALID(1, "无效");

    public Integer value;
    public String desc;

    EEntityState(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static EEntityState getTypeByVal(Integer value) {
        EEntityState defaultState = VALID;
        for (EEntityState eEntityState : EEntityState.values()) {
            if (eEntityState.value.equals(value)) {
                return eEntityState;
            }
        }
        return defaultState;
    }

    public static String getDescByVal(Integer value) {
        return getTypeByVal(value).desc;
    }

}
