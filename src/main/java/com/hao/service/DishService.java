package com.hao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hao.domain.Category;
import com.hao.domain.Dish;
import com.hao.dto.DishDto;

/**
* @author Xhao
* @description 针对表【dish(菜品管理)】的数据库操作Service
* @createDate 2022-11-22 21:41:42
*/
public interface DishService extends IService<Dish> {

    void saveWithFlavor(DishDto d);

}
