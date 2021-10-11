package com.lc.samples;

import com.lc.boot.oss.autoconfigure.core.OssClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class Application implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    @Autowired
    private OssClient ossClient;

    @Override
    public void run(String... args) throws Exception {
       log.info("bucket exit check result: " + ossClient.exit("cdcs"));
    }
}
