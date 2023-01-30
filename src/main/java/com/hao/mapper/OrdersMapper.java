package com.hao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hao.domain.Orders;
import org.apache.ibatis.annotations.Mapper;


/**
* @author Xhao
* @description 针对表【orders(订单表)】的数据库操作Mapper
* @createDate 2022-11-22 21:43:21
* @Entity .domain.Orders
*/
@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {

}
