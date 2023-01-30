package com.hao.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hao.domain.Dish;
import com.hao.domain.DishFlavor;
import com.hao.dto.DishDto;
import com.hao.mapper.DishMapper;
import com.hao.service.DishFlavorService;
import com.hao.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author Xhao
* @description 针对表【dish(菜品管理)】的数据库操作Service实现
* @createDate 2022-11-22 21:41:42
*/
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService{

    @Autowired
    private DishFlavorService dishFlavorService;
    /**
     * 新增菜品，对应口味数据
     */
    @Override
    public void saveWithFlavor(DishDto dishDto)
    {
        //保存菜品的基本信息到菜品表dish
        this.save(dishDto);
        Long dishid = dishDto.getId();

        List<DishFlavor> flavors = dishDto.getFlavors();

        //处理一下，加入id数据
        flavors.stream().map((item) ->{
            item.setDishId(dishid);
            return item;
        }).collect(Collectors.toList());
        //菜品口味到口味表中
        dishFlavorService.saveBatch(flavors);
    }
}
