package com.hao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.hao.domain.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Xhao
* @description 针对表【dish_flavor(菜品口味关系表)】的数据库操作Mapper
* @createDate 2022-11-22 21:42:46
* @Entity domain.DishFlavor
*/
@Mapper
public interface DishFlavorMapper extends BaseMapper<DishFlavor> {


}
