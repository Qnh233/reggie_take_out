package com.hao.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hao.common.BaseContext;
import com.hao.common.R;
import com.hao.domain.AddressBook;
import com.hao.domain.Category;
import com.hao.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 必燃
 * @version 1.0
 * @create 2023-01-24 14:28
 */
@RestController
@Slf4j
@RequestMapping("/addressBook")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    @GetMapping("/list")
    public R<List<AddressBook>> Selcetaddresslist()
    {
        log.info("获取地址列表....");
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId,BaseContext.getCurrentId());
        List<AddressBook> list = addressBookService.list(queryWrapper);
        return R.success(list);
    }

    @GetMapping("/{id}")
    public R<AddressBook> getbyid(@PathVariable Long id)
    {
        log.info("获取地址信息..id:{}",id);
        AddressBook byId = addressBookService.getById(id);
        return R.success(byId);
    }


    @PutMapping
    public R<String> update(@RequestBody AddressBook addressBook)
    {
//        addressBook.setUpdateUser();
        boolean b = addressBookService.updateById(addressBook);
        if(b)
        {
            return R.success("修改成功！");
        }
        else
        {
            return R.error("修改失败");
        }
    }


    @PostMapping
    public R<String> add(@RequestBody AddressBook addressBook) {
        log.info("添加地址...");
        addressBook.setUserId(BaseContext.getCurrentId());
        boolean save = addressBookService.save(addressBook);

        if (save) {
            return R.success("添加成功！");
        } else
        {
            return R.error("添加失败！");
        }

    }

    @PutMapping("/default")
    public R<String> setDefault(@RequestBody AddressBook addressBook)
    {
        log.info("修改id{}为默认地址...");
        LambdaUpdateWrapper<AddressBook> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        wrapper.set(AddressBook::getIsDefault,0);
        addressBookService.update(wrapper);

        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);
        return R.success("修改成功！");
    }

    @GetMapping("/default")
    public R<AddressBook> getDefault()
    {
        log.info("获取默认地址...");
        LambdaQueryWrapper<AddressBook> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId()).eq(AddressBook::getIsDefault,1);
        AddressBook one = addressBookService.getOne(wrapper);
        return R.success(one);
    }
}
