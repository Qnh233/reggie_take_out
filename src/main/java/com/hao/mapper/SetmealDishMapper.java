package com.hao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hao.domain.SetmealDish;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Xhao
* @description 针对表【setmeal_dish(套餐菜品关系)】的数据库操作Mapper
* @createDate 2022-11-22 21:43:27
* @Entity .domain.SetmealDish
*/
@Mapper
public interface SetmealDishMapper extends BaseMapper<SetmealDish> {
}
