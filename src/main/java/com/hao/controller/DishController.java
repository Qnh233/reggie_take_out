package com.hao.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hao.common.R;
import com.hao.domain.Category;
import com.hao.domain.Dish;
import com.hao.domain.DishFlavor;
import com.hao.domain.Setmeal;
import com.hao.dto.DishDto;
import com.hao.service.CategoryService;
import com.hao.service.DishFlavorService;
import com.hao.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author 必燃
 * @version 1.0
 * @create 2023-01-14 20:09
 */
@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto)
    {
        log.info(dishDto.toString());

        //优化入缓存，记得清理缓存
        redisTemplate.delete("dish_"+dishDto.getCategoryId()+"_1");

        dishService.saveWithFlavor(dishDto);

        return R.success("添加菜品成功");
    }

    /**
     * 分页条件查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> SelcetConditionPage(Integer page, Integer pageSize,String name)
    {
        log.info("分页查询 page={}，pagesize={}，name={}",page,pageSize,name);
        Page<Dish> dishPage = new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage = new Page<>();
        //条件构造器
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper();
        //加入模糊查询条件
        lambdaQueryWrapper.like(StringUtils.hasText(name),Dish::getName,name);
        //加入排序条件
        lambdaQueryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        dishService.page(dishPage,lambdaQueryWrapper);
        //dish to dishdto
        //第一次拷贝，将页面基本信息拷入，但不包括每条的基本信息，也就是列表records
        BeanUtils.copyProperties(dishPage,dishDtoPage,"records");
        List<Dish> records = dishPage.getRecords();
        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            //此次拷贝在循环中进行拷贝，将每个单行数据逐个拷贝到准备好的dishDto中。
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();
            Category byId = categoryService.getById(categoryId);
            String categoryname1 = byId.getName();
            dishDto.setCategoryName(categoryname1);
            return dishDto;
        }).collect(Collectors.toList());
        //然后将此list再装入dishDtoPage
        dishDtoPage.setRecords(list);
        return R.success(dishDtoPage);
    }

    @GetMapping("/{id}")
    public R<DishDto> getById(@PathVariable Long id)
    {
        //设计多表查询，使用mp，尽量在service层写方法
        log.info("根据id查询菜品信息,id={}",id);
        Dish byId = dishService.getById(id);
        Long categoryId = byId.getCategoryId();
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(byId,dishDto);
        Category CbyId = categoryService.getById(categoryId);
        String cbyIdName = CbyId.getName();
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(id!=null,DishFlavor::getDishId, id);
        List<DishFlavor> dishFlavors = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(dishFlavors);
        //赋值id就可以回显分类名
//        dishDto.setCategoryName(cbyIdName);
        return R.success(dishDto);
    }

    @PutMapping
    @Transactional
    public R<String> update(@RequestBody DishDto dishdto)
    {
        log.info("修改菜品:{}",dishdto.getName());
        //优化入缓存，记得清理缓存
        redisTemplate.delete("dish_"+dishdto.getCategoryId()+"_1");

        Dish dish = new Dish();
        BeanUtils.copyProperties(dishdto,dish);
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishdto.getId());
        log.info("删除原有数据");
        boolean remove = dishFlavorService.remove(queryWrapper);
        boolean b = dishService.removeById(dish.getId());
        if(remove&&b)
        {
            log.info("删除成功!");
            this.save(dishdto);
        }
        return R.success("更新成功");
        //设计多表查询，使用mp，尽量在service层写方法
    }


    @GetMapping("/list")
    public R<List<DishDto>> getByIdlist(Long categoryId,Integer status)
    {
        log.info("获取分类{}下菜品。",categoryId);
        List<DishDto> dishDtos=null;
        //动态拼接一个字符串key
        String key = "dish_" + categoryId + "_" + status;
        //加入缓存步骤，先从缓存中获取数据
        dishDtos= (List<DishDto>) redisTemplate.opsForValue().get(key);
        if(dishDtos!=null)
        {
            return R.success(dishDtos);
        }
        LambdaQueryWrapper<Dish> q = new LambdaQueryWrapper<>();
        q.eq(categoryId!=null,Dish::getCategoryId,categoryId).eq(Dish::getStatus,status);
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        List<Dish> list = dishService.list(q);
         dishDtos = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            //此次拷贝在循环中进行拷贝，将每个单行数据逐个拷贝到准备好的dishDto中。
            BeanUtils.copyProperties(item, dishDto);
            Category byId = categoryService.getById(categoryId);
            String categoryname1 = byId.getName();
            dishDto.setCategoryName(categoryname1);
            queryWrapper.eq(DishFlavor::getDishId,item.getId());
            List<DishFlavor> list1 = dishFlavorService.list(queryWrapper);
            queryWrapper.clear();
            dishDto.setFlavors(list1);
            return dishDto;
        }).collect(Collectors.toList());
         //运行到此处表示缓存中没有，查询数据库得到后再加入到缓存中。
        redisTemplate.opsForValue().set(key,dishDtos,60, TimeUnit.MINUTES);
        return R.success(dishDtos);
    }


    @PostMapping("/status/{status}")
    public R<String> changeStatus(@PathVariable Integer status,Long[] ids)
    {
        log.info("菜品更改状态id：{}",ids);

        for (int i = 0; i < ids.length; i++) {
            Dish byId = dishService.getById(ids[i]);
            if(byId==null)
            {
                return R.error("错误");
            }
            byId.setStatus(status);
            boolean b = dishService.updateById(byId);
        }
        return R.success("状态修改成功！");
    }
}
