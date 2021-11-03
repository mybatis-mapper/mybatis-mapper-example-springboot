package io.mybatis.example.springboot.controller;

import io.mybatis.common.core.DataResponse;
import io.mybatis.common.core.RowsResponse;

import io.mybatis.example.springboot.model.OrderItem;
import io.mybatis.example.springboot.service.OrderItemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * order_item - 
 *
 * @author liuzh
 */
@RestController
@RequestMapping("orderItems")
public class OrderItemController {

  @Autowired
  private OrderItemService orderItemService;

  @PostMapping
  public DataResponse<OrderItem> save(@RequestBody OrderItem orderItem) {
    return DataResponse.ok(orderItemService.save( orderItem));
  }

  @GetMapping
  public RowsResponse<OrderItem> findList(@RequestBody OrderItem orderItem) {
    return RowsResponse.ok(orderItemService.findList( orderItem));
  }

  @GetMapping(value = "/{id}")
  public DataResponse<OrderItem> findById(@PathVariable("id") Long id) {
    return DataResponse.ok(orderItemService.findById(id));
  }

  @PutMapping(value = "/{id}")
  public DataResponse<OrderItem> update(@PathVariable("id") Integer id, @RequestBody OrderItem orderItem) {
    orderItem.setItemId(id);
    return DataResponse.ok(orderItemService.update( orderItem));
  }

  @DeleteMapping(value = "/{id}")
  public DataResponse<Boolean> deleteById(@PathVariable("id") Long id) {
    return DataResponse.ok(orderItemService.deleteById(id) == 1);
  }

}
