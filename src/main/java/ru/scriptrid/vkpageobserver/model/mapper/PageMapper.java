package ru.scriptrid.vkpageobserver.model.mapper;

import com.vk.api.sdk.objects.users.responses.GetResponse;
import org.springframework.stereotype.Component;
import ru.scriptrid.vkpageobserver.model.dto.ChangeDto;
import ru.scriptrid.vkpageobserver.model.dto.ObservingPageDto;
import ru.scriptrid.vkpageobserver.model.entity.PageEntity;
import ru.scriptrid.vkpageobserver.model.entity.UserEntity;

import java.util.Comparator;

@Component
public class PageMapper {
    public PageEntity toEntity(GetResponse response) {
        PageEntity page = new PageEntity();
        page.setId(response.getId());
        page.setFirstName(response.getFirstName());
        page.setLastName(response.getLastName());
        page.setBirthDate(response.getBdate());
        if (response.getCity() == null) {
            page.setLocation(null);
        } else {
            page.setLocation(response.getCity().getTitle());
        }
        return page;
    }

    public PageEntity toEntity(GetResponse response, UserEntity user) {
        PageEntity page = toEntity(response);
        page.addUser(user);
        return page;
    }

    public ObservingPageDto toDto(PageEntity page) {
        return new ObservingPageDto(
                page.getFirstName(),
                page.getLastName(),
                page.getBirthDate(),
                page.getLocation(),
                page.getChanges()
                        .stream()
                        .map(c -> new ChangeDto(
                                c.getTimeOfChange(),
                                c.getBefore(),
                                c.getAfter()
                        ))
                        .sorted(Comparator.comparing(ChangeDto::timeOfChange).reversed())
                        .toList()
        );

    }
}
