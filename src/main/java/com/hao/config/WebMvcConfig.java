package com.hao.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.hao.common.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

/**
 * @author 必燃
 * @version 1.0
 * @create 2022-11-29 18:21
 */
@Slf4j
@Configuration
@EnableSwagger2
@EnableKnife4j
public class WebMvcConfig extends WebMvcConfigurationSupport {
    /**
     * 静态资源加载设置
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("静态映射。。");
//        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/static/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/static/front/");
    }

    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("扩展消息转换器");
        //创建消息转换器
        MappingJackson2HttpMessageConverter mjhmc =new MappingJackson2HttpMessageConverter();
        //设置
        mjhmc.setObjectMapper(new JacksonObjectMapper());
        //追加在首位
        converters.add(0,mjhmc);
    }
    @Bean
    public Docket createRestApi() {
        // 文档类型
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.hao.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("瑞吉外卖")
                .version("1.0")
                .description("瑞吉外卖接口文档")
                .build();
    }
}
//
//
//    @Override
//    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
//        log.info("静态映射。。");
//        registry.addResourceHandler("/static/backend/**").addResourceLocations("classpath:/static/backend/**");
//        registry.addResourceHandler("/static/front/**").addResourceLocations("classpath:/static/front/**");
//        super.addResourceHandlers(registry);
//    }
//
//    @Override
//    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
//        log.info("扩展消息转换器");
//        //创建消息转换器
//        MappingJackson2HttpMessageConverter mjhmc =new MappingJackson2HttpMessageConverter();
//        //设置
//        mjhmc.setObjectMapper(new JacksonObjectMapper());
//        //追加在首位
//        converters.add(0,mjhmc);
//    }
//}
