package com.hao.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hao.domain.DishFlavor;
import com.hao.domain.Setmeal;
import com.hao.domain.SetmealDish;
import com.hao.dto.SetmealDto;
import com.hao.mapper.SetmealMapper;
import com.hao.service.SetmealDishService;
import com.hao.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author Xhao
* @description 针对表【setmeal(套餐)】的数据库操作Service实现
* @createDate 2022-11-22 21:43:24
*/
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    @Override
    public void saveWithDish(SetmealDto setmealDto)
    {

        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,setmealDto.getId());

        //保存菜品的基本信息到套餐表setmeal
        this.save(setmealDto);
        Long setmealid = setmealDto.getId();

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();

        //处理一下，加入id数据
        setmealDishes.stream().map((item) ->{
            item.setSetmealId(setmealid);
            return item;
        }).collect(Collectors.toList());
        //菜品口味到setmealdish表中
        setmealDishService.saveBatch(setmealDishes);

    }

}
