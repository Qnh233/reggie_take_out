package com.hao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hao.domain.Dish;
import com.hao.domain.Setmeal;
import com.hao.exception.BusinessExcetption;
import com.hao.service.CategoryService;
import com.hao.mapper.CategoryMapper;
import com.hao.domain.Category;
import com.hao.service.DishService;
import com.hao.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author Xhao
* @description 针对表【category(菜品及套餐分类)】的数据库操作Service实现
* @createDate 2022-11-22 21:40:51
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService{

    @Autowired
    private SetmealService setmealService;
    @Autowired
    private DishService dishService;

    @Override
    public boolean remove(Long id) {

        //检查关联菜品与否
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int count = dishService.count(dishLambdaQueryWrapper);
        if(count>0)
        {
            //抛出业务异常
            throw new BusinessExcetption("分类下存在菜品，无法删除！",20020);
        }
        //检查关联套餐与否
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count1 = setmealService.count(setmealLambdaQueryWrapper);
        if(count1>0)
        {
            throw new BusinessExcetption("分类下存在关联套餐，无法删除！",20020);
        }
        return super.removeById(id);
    }
}
