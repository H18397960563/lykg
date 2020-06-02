package com.leyou.service;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/*
@Service
public class UploadService {

    public String uploadImage(MultipartFile file) {
        String url = null;
        try {
            // 2、保存图片
            // 2.1、生成保存目录
            File dir = new File("D:\\upload");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            // 2.2、保存图片
            file.transferTo(new File(dir, file.getOriginalFilename()));

            // 2.3、拼接图片地址
            url = "http://image.leyou.com/" + file.getOriginalFilename();

            return url;


        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }
}*/
@Service
public class UploadService {
    //文件的上传对象
    @Autowired
    private FastFileStorageClient fastFileStorageClient;
    public String uploadImage(MultipartFile file) {
        String url = null;
        try {
            //图片的名称
            String originalFilename = file.getOriginalFilename();//asdfdsf.jpg
            //获取文件后缀名
            String ext = StringUtils.substringAfterLast(originalFilename,".");
            //上传
            StorePath storePath = fastFileStorageClient.uploadFile(file.getInputStream(), file.getSize(), ext, null);
            //返回完整路径
            return "http://image.leyou.com/"+storePath.getFullPath();

        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }
}
