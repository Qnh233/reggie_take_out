package com.hao.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.hao.common.BaseContext;
import com.hao.common.R;
import com.hao.domain.Dish;
import com.hao.domain.Setmeal;
import com.hao.domain.ShoppingCart;
import com.hao.service.DishService;
import com.hao.service.SetmealService;
import com.hao.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 必燃
 * @version 1.0
 * @create 2023-01-25 19:51
 */
@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @GetMapping("/list")
    public R<List<ShoppingCart>> getlist()
    {
        log.info("获取订单列表");
        LambdaQueryWrapper<ShoppingCart> sh = new LambdaQueryWrapper<>();
        sh.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        List<ShoppingCart> shoppingCarts = shoppingCartService.list(sh);
        return R.success(shoppingCarts);
    }

    @PostMapping("/add")
    public R<String> add(@RequestBody ShoppingCart shoppingCart)
    {
        log.info("添加订单。。。");
        //注意同一订单要加number
        //不考虑同一菜品不同规格情况
        LambdaUpdateWrapper<ShoppingCart> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.setSql("number = number + 1");
        boolean update;
        if(shoppingCart.getDishId()!=null)
        {
            //updateWrapper.setSql("amount = amount + "+shoppingCart.getAmount());
            updateWrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
            //.eq(ShoppingCart::getDishFlavor,shoppingCart.getDishFlavor());
            update= shoppingCartService.update(updateWrapper);
        }
        else
        {
            updateWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
            //.eq(ShoppingCart::getDishFlavor,shoppingCart.getDishFlavor());
            update= shoppingCartService.update(updateWrapper);
        }
        if(!update)
        {
            shoppingCart.setUserId(BaseContext.getCurrentId());
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
        }
        return R.success("添加成功！");
    }

    @PostMapping("/sub")
    public R<String> sub(@RequestBody ShoppingCart shoppingCart)
    {
        log.info("删除订单。。。");
        //注意同一订单要加number
        LambdaUpdateWrapper<ShoppingCart> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.setSql("number = number - 1");
        //查出菜品或者套餐价格
        long v;
        long v1;
        ShoppingCart one=shoppingCart;
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        if(shoppingCart.getDishId()!=null)
        {
            queryWrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
            one = shoppingCartService.getOne(queryWrapper);
            updateWrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
        }
        else
        {
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
            one = shoppingCartService.getOne(queryWrapper);
            updateWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());

        }
        if(one.getNumber()==1)
        {
            shoppingCartService.remove(queryWrapper);
            return R.success("删除成功！");
        }
//        v = one.getAmount().longValue();
//        v1 = v / one.getNumber();
//        updateWrapper.setSql("amount = amount - "+v1);
        boolean update = shoppingCartService.update(updateWrapper);
        return R.success("减少成功！");
    }


    @DeleteMapping("/clean")
    @Transactional
    public R<String> clean()
    {
        log.info("清空购物车...");
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,userId);
        shoppingCartService.remove(wrapper);
        return R.success("清空成功！");
    }


}
