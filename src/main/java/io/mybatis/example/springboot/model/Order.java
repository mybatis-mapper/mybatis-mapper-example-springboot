package io.mybatis.example.springboot.model;

import io.mybatis.provider.Entity;


/**
 * t_order1 
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

}
