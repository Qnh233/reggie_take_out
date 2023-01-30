package com.hao.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hao.common.BaseContext;
import com.hao.common.R;
import com.hao.domain.*;
import com.hao.exception.BusinessExcetption;
import com.hao.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author 必燃
 * @version 1.0
 * @create 2023-01-27 14:07
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressBookService addressBookService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private OrdersService ordersService;

    @GetMapping("/userPage")
    public R<Page> SelcetConditionPage(Integer page, Integer pageSize)
    {
        log.info("分页查询 page={}，pagesize={}",page,pageSize);
        Page<Orders> ordersPage = new Page<>(page,pageSize);
        //条件构造器
        LambdaQueryWrapper<Orders> lambdaQueryWrapper = new LambdaQueryWrapper();
//        //加入模糊查询条件
//        lambdaQueryWrapper.like(StringUtils.hasText(name),Dish::getName,name);
        //加入排序条件
        lambdaQueryWrapper.orderByDesc(Orders::getOrderTime);

        ordersService.page(ordersPage,lambdaQueryWrapper);
        return R.success(ordersPage);
    }

//    @PostMapping("/submit")
//    public R<String> add(@RequestBody Orders orders)
//    {
//        log.info("尝试生成订单...");
//        Long id = BaseContext.getCurrentId();
//        orders.setUserId(id);
//        orders.setUserName(userService.getById(id).getName());
//        orders.setOrderTime(LocalDateTime.now());
//        orders.setCheckoutTime(LocalDateTime.now());
//        ordersService.save(orders);
//        return R.success("保存成功！");
//    }


    /**
     * 用户下单
     * @param orders
     */
    @Transactional
    @PostMapping("/submit")
    public void submit(@RequestBody Orders orders) {
        //获得当前用户id
        Long userId = BaseContext.getCurrentId();

        //查询当前用户的购物车数据
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,userId);
        List<ShoppingCart> shoppingCarts = shoppingCartService.list(wrapper);

        if(shoppingCarts == null || shoppingCarts.size() == 0){
            throw new BusinessExcetption("购物车为空，不能下单",20010);
        }

        //查询用户数据
        User user = userService.getById(userId);

        //查询地址数据
        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = addressBookService.getById(addressBookId);
        if(addressBook == null){
            throw new BusinessExcetption("用户地址信息有误，不能下单", 20010);
        }

        long orderId = IdWorker.getId();//订单号

        AtomicInteger amount = new AtomicInteger(0);

        List<OrderDetail> orderDetails = shoppingCarts.stream().map((item) -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setAmount(item.getAmount());
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());


        orders.setId(orderId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);
        orders.setAmount(new BigDecimal(amount.get()));//总金额
        orders.setUserId(userId);
        orders.setNumber(String.valueOf(orderId));
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));
        //向订单表插入数据，一条数据
        ordersService.save(orders);

        //向订单明细表插入数据，多条数据
        orderDetailService.saveBatch(orderDetails);

        //清空购物车数据
        shoppingCartService.remove(wrapper);
    }
}
