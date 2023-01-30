package com.hao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hao.domain.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Xhao
* @description 针对表【employee(员工信息)】的数据库操作Mapper
* @createDate 2022-11-22 21:43:04
* @Entity domain.Employee
*/
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {


}
