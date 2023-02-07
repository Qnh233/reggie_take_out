package com.hao.filter;

import com.alibaba.fastjson.JSON;
import com.hao.common.BaseContext;
import com.hao.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @author 必燃
 * @version 1.0
 * @create 2022-11-28 14:23
 */
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {

    //进行路径比较的类
    public static final AntPathMatcher PATH_MATCHER=new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request=(HttpServletRequest) servletRequest;
        HttpServletResponse respond= (HttpServletResponse) servletResponse;

        String requestURI = request.getRequestURI();
//        log.info("拦截到请求：{}",(requestURI));
        String[] urls = new String[]
                {
                        "/employee/login",
                        "/employee/logout",
                        "/backend/**",
                        "/front/**",
                        "/user/**",
//                        "/addressBook/**",
//                        "/order/**"
                        "/user/login",
                        "/user/sendMsg",
                        "/doc.html",
                        "/webjars/**",
                        "/swagger-resources",
                        "/v2/api-docs",
                        "/favicon.ico"
                };

        if(check(urls,requestURI))
        {
            log.info("拦截到请求：{} 不需要处理",(requestURI));
            filterChain.doFilter(request,respond);
            return;
        }
        else
        {

            if (request.getSession().getAttribute("employee")!=null||request.getSession().getAttribute("user")!=null) {
                log.info("拦截到请求：{} 用户已经登录 id为 employee{}+user{}",(requestURI),request.getSession().getAttribute("employee"),request.getSession().getAttribute("user"));
                long id = Thread.currentThread().getId();
                log.info("线程ID为：{}",id);
                Long currentId = (Long)request.getSession().getAttribute("employee");
                if (currentId==null)
                {
                    currentId = (Long)request.getSession().getAttribute("user");
                }
                BaseContext.setCurrentId(currentId);

                filterChain.doFilter(request,respond);
            }
            else
            {
                //做出响应 前端有对于拦截器的监听
                log.info("拦截到请求：{} 用户未登录!",requestURI);
                respond.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
            }
        }

    }

    public boolean check(String[] urls,String URL)
    {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, URL);
            if(match)
            {
                return match;
            }
        }
        return false;
    }
}
