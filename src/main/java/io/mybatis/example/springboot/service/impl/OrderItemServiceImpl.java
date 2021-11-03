package io.mybatis.example.springboot.service.impl;

import io.mybatis.service.AbstractService;

import io.mybatis.example.springboot.service.OrderItemService;
import io.mybatis.example.springboot.mapper.OrderItemMapper;
import io.mybatis.example.springboot.model.OrderItem;
import org.springframework.stereotype.Service;

/**
 * order_item - 
 *
 * @author liuzh
 */
@Service
public class  OrderItemServiceImpl extends AbstractService<OrderItem, Long, OrderItemMapper> implements OrderItemService {

}
