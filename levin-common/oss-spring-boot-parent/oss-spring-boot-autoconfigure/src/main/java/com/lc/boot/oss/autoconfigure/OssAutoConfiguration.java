package com.lc.boot.oss.autoconfigure;

import com.lc.boot.oss.autoconfigure.core.OssProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(OssProperties.class)
@Import(AliYunOssConfiguration.class)
public class OssAutoConfiguration {
}
