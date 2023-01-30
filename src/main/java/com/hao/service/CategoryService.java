package com.hao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hao.domain.Category;
import com.hao.domain.Employee;


/**
* @author Xhao
* @description 针对表【category(菜品及套餐分类)】的数据库操作Service
* @createDate 2022-11-22 21:40:51
*/
public interface CategoryService extends IService<Category> {

    boolean remove(Long id);
}
