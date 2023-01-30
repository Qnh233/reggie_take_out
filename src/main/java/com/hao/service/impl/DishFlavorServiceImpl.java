package com.hao.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hao.domain.DishFlavor;
import com.hao.dto.DishDto;
import com.hao.mapper.DishFlavorMapper;
import com.hao.service.DishFlavorService;
import org.springframework.stereotype.Service;

/**
* @author Xhao
* @description 针对表【dish_flavor(菜品口味关系表)】的数据库操作Service实现
* @createDate 2022-11-22 21:42:46
*/
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper,DishFlavor> implements DishFlavorService{

}
