package com.example.vkpageobserver.configuration;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.ServiceActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.ServiceClientCredentialsFlowResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public VkApiClient vk() {
        return new VkApiClient(new HttpTransportClient());
    }
    @Bean
    public ServiceActor serviceActor() {
        try {
            Integer vkApiId = 51537876;
            String vkApiClientSecret= "vt6EgyadqkvZJB27sDvf";
            ServiceClientCredentialsFlowResponse authResponse = vk().oAuth()
                    .serviceClientCredentialsFlow(vkApiId, vkApiClientSecret)
                    .execute();
            return new ServiceActor(vkApiId, authResponse.getAccessToken());
        } catch (ApiException | ClientException e) {
            throw new RuntimeException(e);
        }
    }
}
