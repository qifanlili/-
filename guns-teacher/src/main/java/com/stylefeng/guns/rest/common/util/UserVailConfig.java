package com.stylefeng.guns.rest.common.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "ali-valid")
public class UserVailConfig {

    private String host;
    private String path;
    private String method;
    private String appcode;
}
