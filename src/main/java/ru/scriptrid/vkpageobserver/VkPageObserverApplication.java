package ru.scriptrid.vkpageobserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class VkPageObserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(VkPageObserverApplication.class, args);
    }

}
