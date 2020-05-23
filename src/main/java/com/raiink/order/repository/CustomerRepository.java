package com.raiink.order.repository;

import org.springframework.data.repository.CrudRepository;

import com.raiink.order.dto.Customer;

/**
 * @description: 客户信息
 * @author: hulei
 * @create: 2020-05-13 22:43:26
 */
public interface CustomerRepository extends CrudRepository<Customer, Integer> {}
