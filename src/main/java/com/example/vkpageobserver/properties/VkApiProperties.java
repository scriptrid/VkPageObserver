package com.example.vkpageobserver.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "vk-api")
public class VkApiProperties {

    private Integer appId;
    private String secretKey;
}
