package io.mybatis.example.springboot.mapper;

import io.mybatis.mapper.Mapper;

import io.mybatis.example.springboot.model.Order;
import io.mybatis.mapper.base.EntityProvider;
import io.mybatis.provider.Caching;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Lang;
import org.apache.ibatis.annotations.Options;

/**
 * order - 
 *
 * @author liuzh
 */
@org.apache.ibatis.annotations.Mapper
public interface OrderMapper extends Mapper<Order, Long> {

  /**
   * 保存实体，默认主键自增，并且名称为 id
   * <p>
   * 这个方法是个示例，你可以在自己的接口中使用相同的方式覆盖父接口中的配置
   *
   * @param entity 实体类
   * @return 1成功，0失败
   */
  @Override
  @Lang(Caching.class)
  @Options(useGeneratedKeys = true, keyProperty = "order_id")
  @InsertProvider(type = EntityProvider.class, method = "insert")
  int insert(Order entity);

  /**
   * 保存实体中不为空的字段，默认主键自增，并且名称为 id
   * <p>
   * 这个方法是个示例，你可以在自己的接口中使用相同的方式覆盖父接口中的配置
   *
   * @param entity 实体类
   * @return 1成功，0失败
   */
  @Override
  @Lang(Caching.class)
  @Options(useGeneratedKeys = true, keyProperty = "order_id")
  @InsertProvider(type = EntityProvider.class, method = "insertSelective")
  int insertSelective(Order entity);
}