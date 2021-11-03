package io.mybatis.example.springboot.controller;

import io.mybatis.common.core.DataResponse;
import io.mybatis.common.core.RowsResponse;

import io.mybatis.example.springboot.model.Order;
import io.mybatis.example.springboot.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * order - 
 *
 * @author liuzh
 */
@RestController
@RequestMapping("orders")
public class OrderController {

  @Autowired
  private OrderService orderService;

  @PostMapping
  public DataResponse<Order> save(@RequestBody Order order) {
    return DataResponse.ok(orderService.save( order));
  }

  @GetMapping
  public RowsResponse<Order> findList(@RequestBody Order order) {
    return RowsResponse.ok(orderService.findList( order));
  }

  @GetMapping(value = "/{id}")
  public DataResponse<Order> findById(@PathVariable("id") Long id) {
    return DataResponse.ok(orderService.findById(id));
  }

  @PutMapping(value = "/{id}")
  public DataResponse<Order> update(@PathVariable("id") Integer id, @RequestBody Order order) {
    order.setOrderId(id);
    return DataResponse.ok(orderService.update( order));
  }

  @DeleteMapping(value = "/{id}")
  public DataResponse<Boolean> deleteById(@PathVariable("id") Long id) {
    return DataResponse.ok(orderService.deleteById(id) == 1);
  }

}
