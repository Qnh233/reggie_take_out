package com.hao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hao.domain.User;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Xhao
* @description 针对表【user(用户信息)】的数据库操作Mapper
* @createDate 2022-11-22 21:43:36
* @Entity .domain.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
