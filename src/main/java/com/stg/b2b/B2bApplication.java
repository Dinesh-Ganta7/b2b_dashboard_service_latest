package com.stg.b2b;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableEncryptableProperties
@EnableAsync
@SpringBootApplication
public class B2bApplication {
    public static void main(String[] args) {
        SpringApplication.run(B2bApplication.class, args);
    }

}
