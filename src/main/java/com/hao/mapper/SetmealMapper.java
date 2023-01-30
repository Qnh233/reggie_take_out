package com.hao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hao.domain.Setmeal;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Xhao
* @description 针对表【setmeal(套餐)】的数据库操作Mapper
* @createDate 2022-11-22 21:43:24
* @Entity .domain.Setmeal
*/
@Mapper
public interface SetmealMapper extends BaseMapper<Setmeal> {
}
