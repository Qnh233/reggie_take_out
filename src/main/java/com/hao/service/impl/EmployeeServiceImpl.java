package com.hao.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hao.domain.Employee;
import com.hao.mapper.EmployeeMapper;
import com.hao.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
* @author Xhao
* @description 针对表【employee(员工信息)】的数据库操作Service实现
* @createDate 2022-11-22 21:43:04
*/
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

}
