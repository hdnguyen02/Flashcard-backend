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

    public ResponseObject getTags() {
        List<TagDto> tagDtos = new ArrayList<>();
        List<Tag> tags = helper.getCurentUser().getTags();
        tags.forEach(tag -> {
            tagDtos.add(
                    TagDto.builder()
                            .id(tag.getId())
                            .name(tag.getName())
                            .build()
            );
        });


        return ResponseObject.builder()
                .status("success")
                .message("Truy vấn thành công")
                .data(tagDtos)
                .build();
    }

    public ResponseObject addTag(String name) throws Exception {
        User user = helper.getCurentUser();
        List<Tag> tags = user.getTags();

        Set<String> UserTagName = new HashSet<>();
        tags.forEach(tag -> {
            UserTagName.add(tag.getName());
        });
        if (UserTagName.contains(name)) throw new Exception("Đã tồn tại tag!");

        Tag tag;
        try {
            tag = tagDao.save( Tag.builder().name(name).user(user).build());
        }
        catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        TagDto tagDto = TagDto.builder()
                .id(tag.getId())
                .name(tag.getName())
                .build();

        return ResponseObject.builder()
                .status("success")
                .message("Thêm tag thành công")
                .data(tagDto)
                .build();
    }
}
