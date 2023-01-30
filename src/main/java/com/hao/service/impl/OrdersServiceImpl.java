package com.hao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hao.domain.Orders;
import com.hao.mapper.OrdersMapper;
import com.hao.service.OrdersService;

import org.springframework.stereotype.Service;

/**
* @author Xhao
* @description 针对表【orders(订单表)】的数据库操作Service实现
* @createDate 2022-11-22 21:43:21
*/
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService{

}
