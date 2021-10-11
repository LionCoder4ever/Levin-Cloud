package com.lc.boot.oss.autoconfigure.core;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * oss properties configuration
 */
@Data
@ConfigurationProperties(prefix = OssProperties.OSS_PREFIX)
public class OssProperties {

    static final String OSS_PREFIX = "oss";

    /**
     * enable oss save
     */
    private boolean enabled;

    private AliYunProperties aliyun;

    /**
     * 阿里云oss配置
     */
    @Data
    @NoArgsConstructor
    public static class AliYunProperties {
        private String endPoint;

        private String accessKeyId;

        private String accessKeySecret;

        private String bucketName;

        // bucketName prefix endPoint
        private String fileUrl;

        // file root path
        private String rootPath;
    }
}
