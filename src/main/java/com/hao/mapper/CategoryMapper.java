package com.hao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hao.domain.Category;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Xhao
* @description 针对表【category(菜品及套餐分类)】的数据库操作Mapper
* @createDate 2022-11-22 21:40:51
* @Entity .com.hao.domain.Category
*/
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {


}
