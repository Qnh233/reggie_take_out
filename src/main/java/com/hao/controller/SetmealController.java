package com.hao.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hao.common.R;
import com.hao.domain.Category;
import com.hao.domain.Setmeal;
import com.hao.domain.SetmealDish;
import com.hao.dto.SetmealDto;
import com.hao.service.CategoryService;
import com.hao.service.SetmealDishService;
import com.hao.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author 必燃
 * @version 1.0
 * @create 2023-01-15
 */
@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisCacheManager cacheManager;

    @PostMapping
    @CacheEvict(value = "setmealCache", allEntries = true)
    public R<String> save(@RequestBody SetmealDto setmealDto)
    {
        log.info(setmealDto.toString());

        //优化入缓存，记得清理缓存
        redisTemplate.delete("dish_"+setmealDto.getCategoryId()+"_1");

        setmealService.saveWithDish(setmealDto);

        return R.success("添加套餐成功");
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
        Page<Setmeal> setmealPage = new Page<>(page,pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>();
        //条件构造器
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper();
        //加入模糊查询条件
        lambdaQueryWrapper.like(StringUtils.hasText(name),Setmeal::getName,name);
        //加入排序条件
        lambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(setmealPage,lambdaQueryWrapper);
        //dish to dishdto
        //第一次拷贝，将页面基本信息拷入，但不包括每条的基本信息，也就是列表records
        BeanUtils.copyProperties(setmealPage,setmealDtoPage,"records");
        List<Setmeal> records = setmealPage.getRecords();
        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            //此次拷贝在循环中进行拷贝，将每个单行数据逐个拷贝到准备好的dishDto中。
            BeanUtils.copyProperties(item, setmealDto);
            Long categoryId = item.getCategoryId();
            Category byId = categoryService.getById(categoryId);
            String categoryname1 = byId.getName();
            setmealDto.setCategoryName(categoryname1);
            return setmealDto;
        }).collect(Collectors.toList());
        //然后将此list再装入dishDtoPage
        setmealDtoPage.setRecords(list);
        return R.success(setmealDtoPage);
    }

    @GetMapping("/{id}")
    public R<SetmealDto> getById(@PathVariable Long id)
    {
        //设计多表查询，使用mp，尽量在service层写方法
        log.info("根据id查询套餐信息,id={}",id);
        Setmeal byId = setmealService.getById(id);
        Long categoryId = byId.getCategoryId();

        SetmealDto setmealDto = new SetmealDto();
        //拷贝基本信息，即不包含套餐菜品信息列表
        BeanUtils.copyProperties(byId, setmealDto);
        //分类信息
        Category CbyId = categoryService.getById(categoryId);
        String cbyIdName = CbyId.getName();
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(id!=null,SetmealDish::getSetmealId, id);
        //以id为条件查询出所有该套餐下菜品列表导入dto的list中
        List<SetmealDish> setmealDish = setmealDishService.list(queryWrapper);
        setmealDto.setSetmealDishes(setmealDish);
        //赋值id就可以回显分类名
//        setmealDto.setCategoryName(cbyIdName);
        return R.success(setmealDto);
    }
//
    @PutMapping
    @Transactional
    public R<String> update(@RequestBody SetmealDto setmealDto)
    {
        log.info("修改菜品:{}", setmealDto.getName());

        //优化入缓存，记得清理缓存
        redisTemplate.delete("dish_"+setmealDto.getCategoryId()+"_1");

        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDto, setmeal);
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, setmealDto.getId());
        log.info("删除原有套餐中菜品数据");
        boolean remove = setmealDishService.remove(queryWrapper);
        boolean b = setmealService.updateById(setmeal);
        Long id = setmeal.getId();
        if(remove)
        {
            log.info("删除成功!");
            List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
            setmealDishes.stream().map((item) ->{
                item.setSetmealId(id);
                return item;
            }).collect(Collectors.toList());
            setmealDishService.saveBatch(setmealDishes);
        }
        return R.success("更新成功");
        //设计多表查询，使用mp，尽量在service层写方法
    }

    @DeleteMapping
    @Transactional
    @CacheEvict(value = "setmealCache", allEntries = true)
    public R<String> delete(Long[] ids)
    {
        log.info("删除套餐ids：{}",ids);
        for (int i = 0; i < ids.length; i++) {
            Setmeal byId = setmealService.getById(ids[i]);
            if(byId.getStatus()==1)
            {
                return R.error("套餐正在售卖，不可删除！");
            }
            else
            {
                setmealService.removeById(ids[i]);
                LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(SetmealDish::getSetmealId, ids[i]);
                setmealDishService.remove(queryWrapper);
            }
        }
        return R.success("删除成功！");
    }

    @PostMapping("/status/{status}")
    public R<String> changeStatus(@PathVariable Integer status,Long[] ids)
    {
        log.info("套餐更改状态id：{}",ids);
        for (int i = 0; i < ids.length; i++) {
            Setmeal byId = setmealService.getById(ids[i]);
            if(byId==null)
            {
                return R.error("错误");
            }
            byId.setStatus(status);
            boolean b = setmealService.updateById(byId);
        }
        return R.success("状态修改成功！");
    }


    @GetMapping("/list")
    @Cacheable(value = "setmealCache",key = "#categoryId+'_'+#status")
    public R<List<Setmeal>> getByIdlist(Long categoryId,Integer status)
    {
        log.info("获取分类{}下套餐。",categoryId);
        List<Setmeal> list=null;
        String key="setmeal_"+categoryId+"_"+status;
        list = (List<Setmeal>) redisTemplate.opsForValue().get(key);
        if(list!=null)
        {
            return R.success(list);
        }
        LambdaQueryWrapper<Setmeal> q = new LambdaQueryWrapper<>();
        q.eq(categoryId!=null,Setmeal::getCategoryId,categoryId).eq(Setmeal::getStatus,status);
        list = setmealService.list(q);
        redisTemplate.opsForValue().set(key,list,60, TimeUnit.MINUTES);
        return R.success(list);
    }

    //获取对应套餐下的dish
    @GetMapping("/dish/{id}")
    public R<List<SetmealDish>> getDish(@PathVariable long id)
    {
        List<SetmealDish> list=null;
        String key="setmealdish_"+id;
        list = (List<SetmealDish>) redisTemplate.opsForValue().get(key);
        if(list!=null)
        {
            return R.success(list);
        }
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,id);
        list = setmealDishService.list(queryWrapper);
        redisTemplate.opsForValue().set(key,list,60, TimeUnit.MINUTES);
        return R.success(list);

    }

}
