package com.hao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hao.domain.AddressBook;
import com.hao.mapper.AddressBookMapper;
import com.hao.service.AddressBookService;
import org.springframework.stereotype.Service;

/**
* @author Xhao
* @description 针对表【address_book(地址管理)】的数据库操作Service实现
* @createDate 2022-11-22 21:37:16
*/
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper,AddressBook> implements AddressBookService{



}
