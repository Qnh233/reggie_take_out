package com.hao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hao.domain.Dish;

import com.hao.domain.Dish;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Xhao
* @description 针对表【dish(菜品管理)】的数据库操作Mapper
* @createDate 2022-11-22 21:41:42
* @Entity .com.hao.domain.Dish
*/
@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
