package ru.scriptrid.vkpageobserver.configuration;

import ru.scriptrid.vkpageobserver.properties.VkApiProperties;
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
    public ServiceActor serviceActor(VkApiProperties properties) {
        try {

            ServiceClientCredentialsFlowResponse authResponse = vk().oAuth()
                    .serviceClientCredentialsFlow(properties.getAppId(), properties.getSecretKey())
                    .execute();
            return new ServiceActor(properties.getAppId(), authResponse.getAccessToken());
        } catch (ApiException | ClientException e) {
            throw new RuntimeException(e);
        }
    }
}
