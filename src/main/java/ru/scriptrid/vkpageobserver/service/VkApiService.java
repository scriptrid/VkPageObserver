package ru.scriptrid.vkpageobserver.service;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.ServiceActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.users.Fields;
import com.vk.api.sdk.objects.users.responses.GetResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VkApiService {

    private static final List<Fields> SEARCHING_FIELDS = List.of(
            Fields.FIRST_NAME_NOM,
            Fields.LAST_NAME_NOM,
            Fields.BDATE,
            Fields.CITY
    );
    private final VkApiClient vk;
    private final ServiceActor serviceActor;

    public VkApiService(VkApiClient vk, ServiceActor serviceActor) {
        this.vk = vk;
        this.serviceActor = serviceActor;
    }

    public GetResponse requestPage(String pageId) {
        return requestPages(List.of(pageId)).get(0);
    }

    public List<GetResponse> requestPages(List<String> pageIds) {
        try {
            return vk
                    .users()
                    .get(serviceActor)
                    .userIds(pageIds)
                    .fields(SEARCHING_FIELDS)
                    .execute();
        } catch (ApiException | ClientException e) {
            throw new RuntimeException(e);
        }
    }
}
