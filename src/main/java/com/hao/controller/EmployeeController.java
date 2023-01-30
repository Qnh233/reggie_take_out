package com.hao.controller;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hao.common.R;
import com.hao.domain.Employee;
import com.hao.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * @author 必燃
 * @version 1.0
 * @create 2022-11-27 15:19
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     * @param employee
     * @param request 用于将用户存放至Session
     */
    @PostMapping("/login")
    public R<Employee> login(@RequestBody Employee employee, HttpServletRequest request)
    {
        String password = employee.getPassword();
        password= DigestUtils.md5DigestAsHex(password.getBytes());

        //根据用户名查询数据
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(lambdaQueryWrapper);

        if (emp==null)
        {
            return R.error("登录失败（用户名不存在）");
        }

        if(!emp.getPassword().equals(password))
        {
            return R.error("登录失败（密码错误）");
        }

        //看员工状态
        if(emp.getStatus()==0)
        {
            return R.error("账号已禁用");
        }

        HttpSession session = request.getSession();
        session.setAttribute("employee",emp.getId());
        return R.success(emp);
    }


    /**
     * 员工退出
     * @param request
     */
    @PostMapping("/logout")
    public R<String> Logout(HttpServletRequest request)
    {

        request.getSession().removeAttribute("employee");

        return R.success("退出成功");
    }

    /**
     * 分页条件查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> SelcetConditionPage(Integer page, Integer pageSize,String name)
    {
        log.info("分页查询 page={}，pagesize={}，name={}",page,pageSize,name);
        Page<Employee> employeePage = new Page<>(page,pageSize);
        //条件构造器
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper();
        //加入模糊查询条件
        lambdaQueryWrapper.like(StringUtils.hasText(name),Employee::getName,name);
        //加入排序条件
        lambdaQueryWrapper.orderByDesc(Employee::getUpdateTime);

        Page<Employee> emps = employeeService.page(employeePage,lambdaQueryWrapper);

        return R.success(emps);
    }

    /**
     * 添加用户
     * @param employee
     * @param request
     * @return
     */
    @PostMapping
    public R<String> AddEmployee(@RequestBody Employee employee,HttpServletRequest request)
    {
        long id = Thread.currentThread().getId();
        log.info("线程ID为：{}",id);
        log.info("尝试添加用户");
        //前端给到的数据没有密码和创建日期

        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
//        employee.setCreateTime(new Date());
//        employee.setUpdateTime(new Date());
//        Long creater = (Long) request.getSession().getAttribute("employee");
//        employee.setCreateUser(creater);
//        employee.setUpdateUser(creater);
        boolean save;
        save = employeeService.save(employee);
        if (save)
        {
            log.info("添加成功");
            return R.success("添加成功");
        }
        else
        {
            log.info("添加失败");
            return R.error("添加失败");
        }
    }


    /**
     * 用户数据修改
     * @param request
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee)
    {
        log.info("尝试修改用户数据");
//        LambdaQueryWrapper<Employee> employeeLambdaQueryWrapper = new LambdaQueryWrapper<>();
//        employeeLambdaQueryWrapper.eq(Employee::getId,id);

//        employee.setUpdateUser((Long) request.getSession().getAttribute("employee"));
//        employee.setUpdateTime(new Date());

        employeeService.updateById(employee);
        return R.success("修改成功");
    }

    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id)
    {
        log.info("根据id查询员工信息");
        Employee byId = employeeService.getById(id);
        return R.success(byId);
    }

}
