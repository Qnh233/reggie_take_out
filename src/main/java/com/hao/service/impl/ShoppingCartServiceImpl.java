package com.hao.service.impl;



import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hao.domain.ShoppingCart;
import com.hao.mapper.ShoppingCartMapper;
import com.hao.service.ShoppingCartService;
import org.springframework.stereotype.Service;

/**
* @author Xhao
* @description 针对表【shopping_cart(购物车)】的数据库操作Service实现
* @createDate 2022-11-22 21:43:31
*/
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart>
implements ShoppingCartService{


}
