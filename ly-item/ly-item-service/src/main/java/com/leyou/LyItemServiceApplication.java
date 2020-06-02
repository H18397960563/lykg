package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.leyou.item.mapper")
public class LyItemServiceApplication {//你这样的话就是启动类和这个mapper评级了我觉得应该大他有
    public static void main(String[] args) {
        SpringApplication.run(LyItemServiceApplication.class,args);
    }
}
