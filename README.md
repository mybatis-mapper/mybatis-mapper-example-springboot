# mybatis-mapper spring-boot 封装定制示例

> https://mapper.mybatis.io

mybatis-mapper 可以直接使用。也推荐有自己基础框架的做一定的封装后使用。

封装的目的是对自己现有框架规则的固化，简化模板代码，方便后续对基类的调整，让框架更方便使用。

>当前分支是一个示例，这个分支只有一个规则：
> 
>**所有表都有一个 bigint 类型（对应 Java Long）的自增主键 id**

所以一切的开头都定义在 `io.mybatis.example.springboot.model.BaseId` 中：
```java
package io.mybatis.example.springboot.model;

import io.mybatis.provider.Entity;

public class BaseId {
  @Entity.Column(id = true, remark = "主键, bigint类型", updatable = false, insertable = false)
  private Long id;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
```
由此衍生的代码模板中 ` extends BaseId `：
```ftl
package ${package};

import io.mybatis.provider.Entity;
import io.mybatis.example.springboot.model.BaseId;

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
```

主键类型确认后，mybatis-mapper 中所有主键泛型字段都可以用自己的默认基类替代，例如 `BaseIdService`:
```java
package io.mybatis.example.springboot.service;

import io.mybatis.service.BaseService;

public interface BaseIdService<T> extends BaseService<T, Long> {

}
```
对应的接口模板：
```ftl
package ${package};

import ${project.attrs.basePackage}.model.${it.name.className};

/**
 * ${it.name} - ${it.comment}
 *
 * @author ${SYS['user.name']}
 */
public interface ${it.name.className}Service extends BaseIdService<${it.name.className}> {

}
```
其他基础类和模板也是相同方式的实现。

## 生成的代码示例

表结构:
```sql
CREATE TABLE `country` (
  `Id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `countryname` varchar(255) DEFAULT NULL COMMENT '名称',
  `countrycode` varchar(255) DEFAULT NULL COMMENT '代码',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=188 DEFAULT CHARSET=utf8 COMMENT='国家和地区信息';
```

在当前 `BaseId` 的封装形式下，一套生成的代码如下。

### 实体类
```java
package io.mybatis.example.springboot.model;

import io.mybatis.provider.Entity;


/**
 * country - 国家和地区信息
 *
 * @author liuzh
 */
@Entity.Table(value = "country", remark = "国家和地区信息", autoResultMap = true)
public class Country extends BaseId {
  @Entity.Column(value = "countryname", remark = "名称")
  private String countryname;
  @Entity.Column(value = "countrycode", remark = "代码")
  private String countrycode;

  /**
   * 获取 名称
   *
   * @return countryname - 名称
   */
  public String getCountryname() {
    return countryname;
  }

  /**
   * 设置名称
   *
   * @param countryname 名称
   */
  public void setCountryname(String countryname) {
    this.countryname = countryname;
  }

  /**
   * 获取 代码
   *
   * @return countrycode - 代码
   */
  public String getCountrycode() {
    return countrycode;
  }

  /**
   * 设置代码
   *
   * @param countrycode 代码
   */
  public void setCountrycode(String countrycode) {
    this.countrycode = countrycode;
  }

}

```

### Mapper接口
```java
package io.mybatis.example.springboot.mapper;

import io.mybatis.example.springboot.model.Country;
import org.apache.ibatis.annotations.Mapper;

/**
 * country - 国家和地区信息
 *
 * @author liuzh
 */
@Mapper
public interface CountryMapper extends BaseMapper<Country> {

}
```

### Mapper.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="io.mybatis.example.springboot.mapper.CountryMapper">
  <resultMap id="baseResultMap" type="io.mybatis.example.springboot.model.Country">
    <id property="id" column="Id" jdbcType="BIGINT"/>
    <result property="countryname" column="countryname" jdbcType="VARCHAR"/><!-- 名称 -->
    <result property="countrycode" column="countrycode" jdbcType="VARCHAR"/><!-- 代码 -->
  </resultMap>
</mapper>
```

### Service 接口
```java
package io.mybatis.example.springboot.service;

import io.mybatis.example.springboot.model.Country;

/**
 * country - 国家和地区信息
 *
 * @author liuzh
 */
public interface CountryService extends BaseIdService<Country> {

}
```

### Service 实现类
```java
package io.mybatis.example.springboot.service.impl;

import io.mybatis.example.springboot.service.AbstractIdService;

import io.mybatis.example.springboot.service.CountryService;
import io.mybatis.example.springboot.mapper.CountryMapper;
import io.mybatis.example.springboot.model.Country;
import org.springframework.stereotype.Service;

/**
 * country - 国家和地区信息
 *
 * @author liuzh
 */
@Service
public class  CountryServiceImpl extends AbstractIdService<Country, CountryMapper> implements CountryService {

}
```

### Controller
```java
package io.mybatis.example.springboot.controller;

import io.mybatis.common.core.DataResponse;
import io.mybatis.common.core.RowsResponse;

import io.mybatis.example.springboot.model.Country;
import io.mybatis.example.springboot.service.CountryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * country - 国家和地区信息
 *
 * @author liuzh
 */
@RestController
@RequestMapping("countries")
public class CountryController {

  @Autowired
  private CountryService countryService;

  @PostMapping
  public DataResponse<Country> save(@RequestBody Country country) {
    return DataResponse.ok(countryService.save( country));
  }

  @GetMapping
  public RowsResponse<Country> findList(@RequestBody Country country) {
    return RowsResponse.ok(countryService.findList( country));
  }

  @GetMapping(value = "/{id}")
  public DataResponse<Country> findById(@PathVariable("id") Long id) {
    return DataResponse.ok(countryService.findById(id));
  }

  @PutMapping(value = "/{id}")
  public DataResponse<Country> update(@PathVariable("id") Long id, @RequestBody Country country) {
    country.setId(id);
    return DataResponse.ok(countryService.update( country));
  }

  @DeleteMapping(value = "/{id}")
  public DataResponse<Boolean> deleteById(@PathVariable("id") Long id) {
    return DataResponse.ok(countryService.deleteById(id) == 1);
  }

}
```

### HTTP 测试
```http request
### 新增
POST http://localhost:8080/countries
Content-Type: application/json

{
  "countryname": "Test",
  "countrycode": "TES"
}

### 查询指定ID
GET http://localhost:8080/countries/184

### 条件查询
GET http://localhost:8080/countries
Content-Type: application/json

{
  "countrycode": "CN"
}

### 更新
PUT http://localhost:8080/countries/184
Content-Type: application/json

{
  "countrycode": "TST"
}

### 删除指定的 id
DELETE http://localhost:8080/countries/184
```