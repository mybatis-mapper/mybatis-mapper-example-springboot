package io.mybatis.example.springboot.service.impl;

import io.mybatis.service.AbstractService;

import io.mybatis.example.springboot.service.OrderService;
import io.mybatis.example.springboot.mapper.OrderMapper;
import io.mybatis.example.springboot.model.Order;
import org.springframework.stereotype.Service;

/**
 * t_order1 - 
 *
 * @author liuzh
 */
@Service
public class  OrderServiceImpl extends AbstractService<Order, Long, OrderMapper> implements OrderService {

}
