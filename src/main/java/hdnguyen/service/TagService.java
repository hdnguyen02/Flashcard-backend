package hdnguyen.service;


import hdnguyen.common.Helper;
import hdnguyen.dao.TagDao;
import hdnguyen.dto.ResponseObject;
import hdnguyen.dto.TagDto;
import hdnguyen.entity.Tag;
import hdnguyen.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagDao tagDao;
    private final Helper helper;

    public List<TagDto> getTags() {
        List<TagDto> tagDtos = new ArrayList<>();
        List<Tag> tags = helper.getUser().getTags();
        tags.forEach(tag -> {
            tagDtos.add(new TagDto(tag));
        });
        return tagDtos;
    }

    public TagDto createTag(String name) throws Exception {
        User user = helper.getUser();
        List<Tag> tags = user.getTags();
        Set<String> UserTagName = new HashSet<>();
        tags.forEach(tag -> {
            UserTagName.add(tag.getName());
        });
        if (UserTagName.contains(name)) throw new Exception("Đã tồn tại tag!");
        Tag tag = Tag.builder().name(name).user(user).build();
        return new TagDto(tagDao.save(tag));
    }
}
