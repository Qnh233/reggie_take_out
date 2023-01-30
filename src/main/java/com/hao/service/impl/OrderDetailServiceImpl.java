package com.hao.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hao.domain.OrderDetail;
import com.hao.mapper.OrderDetailMapper;
import com.hao.service.OrderDetailService;
import org.springframework.stereotype.Service;

/**
* @author Xhao
* @description 针对表【order_detail(订单明细表)】的数据库操作Service实现
* @createDate 2022-11-22 21:43:16
*/
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail>
implements OrderDetailService {

}
