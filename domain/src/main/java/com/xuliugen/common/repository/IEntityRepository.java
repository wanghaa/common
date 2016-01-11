package com.xuliugen.common.repository;

import com.xuliugen.common.domain.BaseEntity;
import com.xuliugen.common.domain.Entity;

import java.io.Serializable;
import java.util.List;

public interface IEntityRepository {

    /**
     * 将实体（无论是新的还是修改了的）保存到仓储中。
     * @param <T>    实体的类型
     * @param entity 要存储的实体实例。
     * @return 持久化后的当前实体
     */
    <T extends Entity> T save(T entity);

    /**
     * 将实体从仓储中删除。如果仓储中不存在此实例将抛出EntityNotExistedException异常。
     * @param entity 要删除的实体实例。
     */
    void remove(Entity entity);

    /**
     * 判断仓储中是否存在指定ID的实体实例。
     * @param <T>   实体类型
     * @param clazz 实体的类
     * @param id    实体标识
     * @return 如果实体实例存在，返回true，否则返回false
     */
    <T extends Entity> boolean exists(Class<T> clazz, Serializable id);

    /**
     * 从仓储中获取指定类型、指定ID的实体
     * @param <T>   实体类型
     * @param clazz 实体的类
     * @param id    实体标识
     * @return 一个实体实例。
     */
    <T extends Entity> T get(Class<T> clazz, Serializable id);

    /**
     * 从仓储中装载指定类型、指定ID的实体
     * @param <T>   实体类型
     * @param clazz 实体的类
     * @param id    实体标识
     * @return 一个实体实例。
     */
    <T extends Entity> T load(Class<T> clazz, Serializable id);

    /**
     * 从仓储中获取entity参数所代表的未修改的实体
     * @param <T>    实体类型
     * @param clazz  实体的类
     * @param entity 要查询的实体
     * @return 参数entity在仓储中的未修改版本
     */
    <T extends Entity> T getUnmodified(Class<T> clazz, T entity);

    /**
     * 查找指定类型的所有实体
     * @param <T>   实体类型
     * @param clazz 实体的类
     * @return 符合条件的实体集合
     */
    <T extends Entity> List<T> findAll(Class<T> clazz);

    /**
     * 根据单一属性的值查找实体
     * @param <T>           要查询的实体的类型
     * @param clazz         要查询的实体的类
     * @param propertyName  要查询的属性
     * @param propertyValue 匹配的属性值
     * @return 类型为clazz的、属性propertyName的值等于propertyValue的实体的集合
     */
    <T extends Entity> List<T> findByProperty(Class<T> clazz, String propertyName, Object propertyValue);

    /**
     * 获取命名查询的查询字符串
     * @param queryName 命名查询的名字
     * @return 命名查询对应的JPQL字符串
     */
    String getQueryStringOfNamedQuery(String queryName);


    /**
     * 将内存中的持久化对象状态即时写入数据库
     */
    void flush();

    /**
     * 使用数据库中的最新数据更新实体的当前状态。实体中的任何已改变但未持久化的属性值将被数据库中的最新值覆盖。
     * @param entity 要刷新的实体
     */
    void refresh(Entity entity);

    /**
     * 清空持久化缓存
     */
    void clear();

    //////////////////////////

    int deleteByPrimaryKey(Long id);

    <T extends BaseEntity> int insert(T entity);

    <T extends BaseEntity> int insertSelective(T entity);

    <T extends BaseEntity> T selectByPrimaryKey(Long id);

    <T extends BaseEntity> int updateByPrimaryKeySelective(T entity);

    <T extends BaseEntity> int updateByPrimaryKey(T entity);
}
