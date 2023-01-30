package com.hao.controller;

import com.hao.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * @author 必燃
 * @version 1.0
 * @create 2023-01-14 17:10
 * 文件的上传与下载
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;


    @PostMapping("/upload")
    //参数名一致
    public R<String> upload(MultipartFile file)
    {
        //file是临时文件，只存在于此次请求，需要手动转存到磁盘里。
        log.info("上传文件{}",file.toString());
        //获取原文件名
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf('.'));
        //使用UUID重新生成文件名，防止重复，且有格式
        String filename = UUID.randomUUID().toString()+suffix;
        //创建目录对象
        File dir = new File(basePath);
        //检查目录是否存在
        if(!dir.exists())
        {
            dir.mkdirs();
        }

        try {
            //转存到指定位置
            file.transferTo(new File(basePath+filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(filename);
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response)
    {
        //输入流，通过输入流读取文件内容
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));
            //输出流，写回浏览器
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");
            int len = 0;
            byte[] bytes = new byte[1024];
            while((len = fileInputStream.read(bytes))!=-1)
            {
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            outputStream.close();
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
