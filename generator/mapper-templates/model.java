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
<#-- 逻辑表名使用 t_ 前缀 -->
@Entity.Table(value = "t_${it.name}", <#if it.comment?has_content>remark = "${it.comment}", </#if>autoResultMap = true)
public class ${it.name.className} {
  <#-- 下面数组中的字段是分片字段，不允许修改值 -->
  <#assign updateables = ["user_id", "order_id", "item_id"]>
  <#list it.columns as column>
  <#if column.pk>
  @Entity.Column(value = "${column.name}", id = true, remark = "${column.comment}", updatable = false)
  <#else>
  @Entity.Column(value = "${column.name}", remark = "${column.comment}"<#if column.tags.jdbcType>, jdbcType = org.apache.ibatis.type.JdbcType.${column.jdbcType}</#if><#if updateables?seq_index_of(column.name) gt -1>, updatable = false</#if>)
  </#if>
  private ${column.javaType} ${column.name.fieldName};

  </#list>

  <#list it.columns as column>
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

  </#list>
}
