package com.hao.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hao.common.R;
import com.hao.domain.Category;
import com.hao.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 必燃
 * @version 1.0
 * @create 2023-01-13 19:45
 */
@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody Category category)
    {
        log.info("新增分类{}",category);
        categoryService.save(category);
        return R.success("新增分类成功");
    }

    /**
     * 分页条件查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> SelcetConditionPage(Integer page, Integer pageSize)
    {
        log.info("分页查询 page={}，pagesize={}，name={}",page,pageSize);
        Page<Category> categoryPage = new Page<>(page,pageSize);
        //条件构造器
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper();
        //加入模糊查询条件
        //lambdaQueryWrapper.like(StringUtils.hasText(name),Category::getName,name);
        //加入排序条件
        lambdaQueryWrapper.orderByAsc(Category::getSort);

        categoryService.page(categoryPage,lambdaQueryWrapper);

        return R.success(categoryPage);
    }

    @DeleteMapping
    public R<String> DeleteCategory(Long ids)
    {
        log.info("删除分类id：{}",ids);
        categoryService.remove(ids);
        return R.success("分类信息删除成功");
    }

    @PutMapping
    public R<String> update(@RequestBody Category category)
    {
        log.info("修改分类信息：{}",category);
        categoryService.updateById(category);
        return R.success("修改分类信息成功");
    }

    @GetMapping("/list")
    public R<List<Category>> list (Category category)
    {
        LambdaQueryWrapper<Category> queryLambdaQueryWrapper = new LambdaQueryWrapper<>();

        queryLambdaQueryWrapper.eq(category.getType()!=null,Category::getType,category.getType());

        queryLambdaQueryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getCreateTime);

        List<Category> list = categoryService.list(queryLambdaQueryWrapper);

        return R.success(list);
    }
}
