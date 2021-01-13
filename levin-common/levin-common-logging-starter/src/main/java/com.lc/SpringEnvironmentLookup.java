package com.lc;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.lookup.AbstractLookup;
import org.apache.logging.log4j.core.lookup.StrLookup;
import org.springframework.core.io.ClassPathResource;

import java.util.Properties;

import static org.apache.logging.log4j.util.Strings.isNotBlank;

/**
 * @description log4j2 上下文查找插件类
 */

@Plugin(name = SpringEnvironmentLookup.LOOK_UP_PREFIX, category = StrLookup.CATEGORY)
public class SpringEnvironmentLookup extends AbstractLookup {
    static final String LOOK_UP_PREFIX = "spring";
    private static final String PROFILE_PREFIX = "application";
    private static final String PROFILE_SUFFIX = ".properties";
    private static final String META_PROFILE = PROFILE_PREFIX + PROFILE_SUFFIX;
    private static final String SPRING_PROFILES_ACTIVE = "spring.profiles.active";
    private static final Properties properties;

    static {
        try {
            properties = new Properties();
            properties.load(new ClassPathResource(META_PROFILE).getInputStream());
            // 获取激活的properties
            String active = properties.getProperty(SPRING_PROFILES_ACTIVE);
            // 没有设置
            if (isNotBlank(active)) {
                String configName = PROFILE_PREFIX + "-" + active + PROFILE_SUFFIX;
                properties.load(new ClassPathResource(configName).getInputStream());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("SpringEnvironmentLookup initialize fail");
        }
    }

    @Override
    public String lookup(LogEvent event, String key) {
        return String.valueOf(properties.getOrDefault(key, "levin"));
    }

}