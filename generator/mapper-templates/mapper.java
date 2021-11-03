package ${package};

import io.mybatis.mapper.Mapper;

import ${project.attrs.basePackage}.model.${it.name.className};
import io.mybatis.mapper.base.EntityProvider;
import io.mybatis.provider.Caching;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Lang;
import org.apache.ibatis.annotations.Options;

/**
 * ${it.name} - ${it.comment}
 *
 * @author ${SYS['user.name']}
 */
@org.apache.ibatis.annotations.Mapper
public interface ${it.name.className}Mapper extends Mapper<${it.name.className}, Long> {

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
  <#list it.columns as column>
  <#if column.pk>
  @Options(useGeneratedKeys = true, keyProperty = "${column.name}")
  </#if>
  </#list>
  @InsertProvider(type = EntityProvider.class, method = "insert")
  int insert(${it.name.className} entity);

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
  <#list it.columns as column>
  <#if column.pk>
  @Options(useGeneratedKeys = true, keyProperty = "${column.name}")
  </#if>
  </#list>
  @InsertProvider(type = EntityProvider.class, method = "insertSelective")
  int insertSelective(${it.name.className} entity);
}