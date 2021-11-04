# mybatis-mapper spring-boot sharding-jdbc 示例

## https://mapper.mybatis.io

## https://shardingsphere.apache.org

## 项目依赖

当前项目依赖中，主要包含了:
```xml
<dependency>
  <groupId>org.mybatis.spring.boot</groupId>
  <artifactId>mybatis-spring-boot-starter</artifactId>
  <version>2.2.0</version>
</dependency>
<dependency>
  <groupId>io.mybatis</groupId>
  <artifactId>mybatis-service</artifactId>
  <version>1.0.3</version>
</dependency>
<dependency>
  <groupId>org.apache.shardingsphere</groupId>
  <artifactId>sharding-jdbc-spring-boot-starter</artifactId>
  <version>4.1.1</version>
</dependency>
```

## 数据库脚本

两个库，每个库上有两个表结构，每个表各有两个分片，每个库相当于有4个表。

```sql
DROP DATABASE IF EXISTS db_0;
DROP DATABASE IF EXISTS db_1;

CREATE DATABASE db_0;
CREATE DATABASE db_1;

CREATE TABLE db_0.t_order0 (order_id INT NOT NULL, user_id INT NOT NULL, status VARCHAR(45) NULL, PRIMARY KEY (order_id));
CREATE TABLE db_0.t_order1 (order_id INT NOT NULL, user_id INT NOT NULL, status VARCHAR(45) NULL, PRIMARY KEY (order_id));
CREATE TABLE db_0.t_order_item0 (item_id INT NOT NULL, order_id INT NOT NULL, user_id INT NOT NULL, status VARCHAR(45) NULL, creation_date DATE, PRIMARY KEY (item_id));
CREATE TABLE db_0.t_order_item1 (item_id INT NOT NULL, order_id INT NOT NULL, user_id INT NOT NULL, status VARCHAR(45) NULL, creation_date DATE, PRIMARY KEY (item_id));
CREATE INDEX order_index_t_order ON db_0.t_order0 (order_id);
CREATE INDEX order_index_t_order ON db_0.t_order1 (order_id);

CREATE TABLE db_1.t_order0 (order_id INT NOT NULL, user_id INT NOT NULL, status VARCHAR(45) NULL, PRIMARY KEY (order_id));
CREATE TABLE db_1.t_order1 (order_id INT NOT NULL, user_id INT NOT NULL, status VARCHAR(45) NULL, PRIMARY KEY (order_id));
CREATE TABLE db_1.t_order_item0 (item_id INT NOT NULL, order_id INT NOT NULL, user_id INT NOT NULL, status VARCHAR(45) NULL, creation_date DATE, PRIMARY KEY (item_id));
CREATE TABLE db_1.t_order_item1 (item_id INT NOT NULL, order_id INT NOT NULL, user_id INT NOT NULL, status VARCHAR(45) NULL, creation_date DATE, PRIMARY KEY (item_id));
CREATE INDEX order_index_t_order ON db_1.t_order0 (order_id);
CREATE INDEX order_index_t_order ON db_1.t_order1 (order_id);
```

## 配置文件

主要是分片规则（参考官方示例），剩下的是 mybatis xml 配置文件路径的配置。

```yaml
spring:
  shardingsphere:
    datasource:
      names: ds0,ds1
      ds0:
        driver-class-name: com.mysql.jdbc.Driver
        password: ''
        type: com.zaxxer.hikari.HikariDataSource
        jdbc-url: jdbc:mysql://localhost:3306/db_0
        username: root
      ds1:
        driver-class-name: com.mysql.jdbc.Driver
        password: ''
        type: com.zaxxer.hikari.HikariDataSource
        jdbc-url: jdbc:mysql://localhost:3306/db_1
        username: root
    sharding:
      default-database-strategy:
        inline:
          algorithm-expression: ds$->{user_id % 2}
          sharding-column: user_id
      tables:
        t_order:
          actual-data-nodes: ds$->{0..1}.t_order$->{0..1}
          table-strategy:
            inline:
              algorithm-expression: t_order$->{order_id % 2}
              sharding-column: order_id
        t_order_item:
          actual-data-nodes: ds$->{0..1}.t_order_item$->{0..1}
          table-strategy:
            inline:
              algorithm-expression: t_order_item$->{order_id % 2}
              sharding-column: order_id
mybatis:
  mapper-locations: classpath*:mappers/*.xml
```
分片规则中，先根据用户 `user_id` 分库，再根据订单 `order_id` 分表，这个配置文件根据自己的需要进行配置即可。

## 代码生成器

只有上面一个启动类的情况下，这个项目没什么可用的内容，所有代码都可以通过代码生成器自动生成。

代码生成器在 `generator` 目录中：

- lib: jar包目录，代码生成器所有依赖的 jar 都可以放到该目录
  - mysql-connector-java-5.1.49.jar: 当前项目使用的 MySQL 驱动，如果连接其他数据库，可以使用其他驱动
  - rui-cli.jar: 代码生成器
- mapper-templates: 代码模板
- project.yaml: 代码生成器配置文件
- run.bat: windows执行脚本
- run.sh: linux,mac执行脚本

>代码生成器完整文档看这里: https://mapper.mybatis.io/docs/7.generator.html

当前代码生成器已经配置好，需要修改的地方是数据库连接信息和需要生成代码的表，对应下面配置：
```yaml
database:
  # 数据库连接配置
  jdbcConnection:
    # 使用方言，默认为 JDBC 方式，可能会取不到表或字段注释
    # 后续介绍如何通过字典匹配注释值
    dialect: MYSQL
    # jdbc驱动
    driver: com.mysql.jdbc.Driver
    # jdbc连接地址
    url: jdbc:mysql://localhost:3306/db_0?useSSL=false
    # 用户名
    user: root
    # 密码
    password:
  tableRules:
    - name: t%1
      search: t_(?<name>.*)1
      replace: ${name}
```

这里使用了 `tableRules` 配置获取表的规则，由于存在同一个表结构的多个分片，因此这里只筛选出序号 1 的表。并且通过正则表达式保留了中间部分（如 `t_order1 => order`）。

```yaml
  tableRules:
    - name: t%1
      search: t_(?<name>.*)1
      replace: ${name}
```

配置完成后，在控制台执行上面的 `run.xx` 脚本，脚本就一行命令 `java -cp "lib/*" io.mybatis.rui.cli.Main -p project.yaml`。

## 代码生成器中几个特殊地方

### Controller 中设置 id

```ftl
  @PutMapping(value = "/{id}")
  public DataResponse<${it.name.className}> update(@PathVariable("id") Integer id, @RequestBody ${it.name.className} ${it.name.fieldName}) {
    <#list it.columns as column>
    <#if column.pk>
    ${it.name.fieldName}.set${column.name.className}(id);
    </#if>
    </#list>
    return DataResponse.ok(${it.name.fieldName}Service.update( ${it.name.fieldName}));
  }
```
每个表的主键都不一样，因此这里set需要筛选出主键字段名。

### Mapper 接口重写两个 insert 接口
```ftl
  @Override
  @Lang(Caching.class)
  <#list it.columns as column>
  <#if column.pk>
  @Options(useGeneratedKeys = true, keyProperty = "${column.name}")
  </#if>
  </#list>
  @InsertProvider(type = EntityProvider.class, method = "insert")
  int insert(${it.name.className} entity);
```
和Controller类似，由于主键不同，这里需要单独设置 `keyProperty`。

### 实体类
```ftl
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
```
第一个是 `value = "t_${it.name}"`，由于代码生成器中配置会让 `t_order1` 变成 `order`，所以这里逻辑表名手工加了个 `t_` 前缀。

其次是 `<#assign updateables = ["user_id", "order_id", "item_id"]>` 和 下面的 `<#if updateables?seq_index_of(column.name) gt -1>, updatable = false</#if>`，分片键不能更新字段值，
所以这里通过特殊的方式设置字段 `updatable = false`。

## 运行项目

生成代码后，启动服务，可以通过HTTP简单测试。

```http request
### 新增
POST http://localhost:8080/orders
Content-Type: application/json

{
  "orderId": 1,
  "userId": 1,
  "status": "UP"
}

### 查询单个订单
GET http://localhost:8080/orders/1

### 查询全部
GET http://localhost:8080/orders
Content-Type: application/json

{}

### 修改
PUT http://localhost:8080/orders/1
Content-Type: application/json

{
  "status": "DOWN"
}

### 删除
DELETE http://localhost:8080/orders/1
```

## 主要代码

可以根据生成的代码简单了解 mybatis-mapper，使用 mybatis-mapper 只需要两个主要的类，其他都是可选的辅助，主要的两个类，
一个是实体类，例如：

```java
/**
 * order 
 *
 * @author liuzh
 */
@Entity.Table(value = "t_order", autoResultMap = true)
public class Order {
  @Entity.Column(value = "order_id", id = true, remark = "", updatable = false)
  private Integer orderId;

  @Entity.Column(value = "user_id", remark = "", updatable = false)
  private Integer userId;

  @Entity.Column(value = "status", remark = "")
  private String status;

  //省略 getter,setter
}
```

还有一个 Mapper 接口:
```java
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
```

由于通用 Mapper 接口和 `@org.apache.ibatis.annotations.Mapper` 同名，所以注解带上的包名，当使用注解标记接口时，
MyBatis 不需要 `@MapperScan` 注解即可使用。