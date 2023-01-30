package com.hao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hao.domain.Category;
import com.hao.domain.Setmeal;
import com.hao.dto.SetmealDto;


/**
* @author Xhao
* @description 针对表【setmeal(套餐)】的数据库操作Service
* @createDate 2022-11-22 21:43:24
*/
public interface SetmealService  extends IService<Setmeal> {
    public void saveWithDish(SetmealDto setmealDto);
}
