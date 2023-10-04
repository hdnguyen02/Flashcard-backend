package hdnguyen.service;

import hdnguyen.dao.TopicDao;
import hdnguyen.dto.ResponseObject;
import hdnguyen.dto.TopicDto;
import hdnguyen.dto.auth.LabelDto;
import hdnguyen.entity.Topic;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Service
public class TopicService {
    private final TopicDao topicDao;

    public ResponseObject getTopics() {
        List<Topic> topics = topicDao.findAll();
        List<TopicDto> topicDtos = new ArrayList<>();
        topics.forEach(topic -> {
            List<LabelDto> labels = new ArrayList<>();
            topic.getLabels().forEach(label -> labels.add(new LabelDto(label.getId(),label.getName())));
            topicDtos.add(TopicDto.builder()
                    .name(topic.getName())
                    .labels(labels)
                    .build());
        });
        return ResponseObject.builder()
                .status("success")
                .message("Truy vấn thành công")
                .data(topicDtos)
                .build();
    }

}
