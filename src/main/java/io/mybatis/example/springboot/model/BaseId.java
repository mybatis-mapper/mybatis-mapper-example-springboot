package io.mybatis.example.springboot.model;

import io.mybatis.provider.Entity;

public class BaseId {
  @Entity.Column(id = true, remark = "主键,bigint类型", updatable = false, insertable = false)
  private Long id;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
