package io.mybatis.example.springboot.service;

import io.mybatis.example.springboot.mapper.BaseMapper;
import io.mybatis.service.AbstractService;

public abstract class AbstractIdService<T, M extends BaseMapper<T>> extends AbstractService<T, Long, M> {

}
