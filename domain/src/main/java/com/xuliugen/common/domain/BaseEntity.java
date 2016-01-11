package com.xuliugen.common.domain;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * 符号常量类
 * Created by xuliugen on 16/1/10.
 */
public abstract class BaseEntity implements Entity {

    private Long id;

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    /**
     * 依据业务主键判断两个实体是否等价。
     * @return 如果本实体和other等价则返回true, 否则返回false
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this, SerializerFeature.WriteMapNullValue);
    }

    /**
     * 判断该实体是否不存在于数据库中。
     * @return 如果数据库中已经存在拥有该id的实体则返回false，否则返回true。
     */
    public boolean notExisted() {
        return !existed();
    }

    public abstract String[] businessKeys();

    public void setId(Long id) {
        this.id = id;
    }
}
