package com.lc.boot.oss.autoconfigure;

import com.aliyun.oss.OSSClient;
import com.lc.boot.oss.autoconfigure.core.AliOssClient;
import com.lc.boot.oss.autoconfigure.core.OssClient;
import com.lc.boot.oss.autoconfigure.core.OssProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@AllArgsConstructor
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "oss.aliyun", name = "endpoint")
@ConditionalOnClass(OSSClient.class)
public class AliYunOssConfiguration {

    @Bean
    public OssClient aliYunOssClient(OssProperties ossProperties) {
        OssProperties.AliYunProperties oss = ossProperties.getAliyun();
        log.info("oss init");
        return new AliOssClient(oss);
    }
}
