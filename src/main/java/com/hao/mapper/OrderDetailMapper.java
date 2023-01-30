package com.hao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hao.domain.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Xhao
* @description 针对表【order_detail(订单明细表)】的数据库操作Mapper
* @createDate 2022-11-22 21:43:16
* @Entity .domain.OrderDetail
*/
@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {


}
