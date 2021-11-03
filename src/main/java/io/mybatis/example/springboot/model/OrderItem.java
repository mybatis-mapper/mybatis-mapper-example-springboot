package io.mybatis.example.springboot.model;

import io.mybatis.provider.Entity;

import java.util.Date;

/**
 * t_order_item1 
 *
 * @author liuzh
 */
@Entity.Table(value = "t_order_item", autoResultMap = true)
public class OrderItem {
  @Entity.Column(value = "item_id", id = true, remark = "", updatable = false)
  private Integer itemId;

  @Entity.Column(value = "order_id", remark = "", updatable = false)
  private Integer orderId;

  @Entity.Column(value = "user_id", remark = "", updatable = false)
  private Integer userId;

  @Entity.Column(value = "status", remark = "")
  private String status;

  @Entity.Column(value = "creation_date", remark = "", jdbcType = org.apache.ibatis.type.JdbcType.DATE)
  private Date creationDate;


  /**
   * 获取 
   *
   * @return itemId - 
   */
  public Integer getItemId() {
    return itemId;
  }

  /**
   * 设置
   *
   * @param itemId 
   */
  public void setItemId(Integer itemId) {
    this.itemId = itemId;
  }

  /**
   * 获取 
   *
   * @return orderId - 
   */
  public Integer getOrderId() {
    return orderId;
  }

  /**
   * 设置
   *
   * @param orderId 
   */
  public void setOrderId(Integer orderId) {
    this.orderId = orderId;
  }

  /**
   * 获取 
   *
   * @return userId - 
   */
  public Integer getUserId() {
    return userId;
  }

  /**
   * 设置
   *
   * @param userId 
   */
  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  /**
   * 获取 
   *
   * @return status - 
   */
  public String getStatus() {
    return status;
  }

  /**
   * 设置
   *
   * @param status 
   */
  public void setStatus(String status) {
    this.status = status;
  }

  /**
   * 获取 
   *
   * @return creationDate - 
   */
  public Date getCreationDate() {
    return creationDate;
  }

  /**
   * 设置
   *
   * @param creationDate 
   */
  public void setCreationDate(Date creationDate) {
    this.creationDate = creationDate;
  }

}
