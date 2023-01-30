package com.hao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hao.domain.AddressBook;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Xhao
* @description 针对表【address_book(地址管理)】的数据库操作Mapper
* @createDate 2022-11-22 21:37:16
* @Entity .domain.com.hao.domain.AddressBook
*/
@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {


}
