package ${package};

import io.mybatis.provider.Entity;

<#list it.importJavaTypes as javaType>
import ${javaType};
</#list>

/**
 * ${it.name} <#if it.comment?has_content>- ${it.comment}</#if>
 *
 * @author ${SYS['user.name']}
 */
@Entity.Table(value = "${it.name}", <#if it.comment?has_content>remark = "${it.comment}", </#if>autoResultMap = true)
public class ${it.name.className} extends BaseId {
  <#list it.columns as column>
  <#if !column.pk>
  @Entity.Column(value = "${column.name}", remark = "${column.comment}"<#if column.tags.jdbcType>, jdbcType = org.apache.ibatis.type.JdbcType.${column.jdbcType}</#if>)
  private ${column.javaType} ${column.name.fieldName};
  </#if>
  </#list>

  <#list it.columns as column>
  <#if !column.pk>
  /**
   * 获取 ${column.comment}
   *
   * @return ${column.name.fieldName} - ${column.comment}
   */
  public ${column.javaType} get${column.name.className}() {
    return ${column.name.fieldName};
  }

  /**
   * 设置${column.comment}
   *
   * @param ${column.name.fieldName} ${column.comment}
   */
  public void set${column.name.className}(${column.javaType} ${column.name.fieldName}) {
    this.${column.name.fieldName} = ${column.name.fieldName};
  }

  </#if>
  </#list>
}
