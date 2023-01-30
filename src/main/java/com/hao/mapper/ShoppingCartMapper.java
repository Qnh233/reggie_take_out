package com.hao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hao.domain.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Xhao
* @description 针对表【shopping_cart(购物车)】的数据库操作Mapper
* @createDate 2022-11-22 21:43:31
* @Entity .domain.ShoppingCartController
*/
@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {


}
