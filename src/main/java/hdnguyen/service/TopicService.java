package hdnguyen.service;

import hdnguyen.dao.TopicDao;
import hdnguyen.dto.ResponseObject;
import hdnguyen.dto.TopicDto;
import hdnguyen.dto.auth.LabelDto;
import hdnguyen.entity.Label;
import hdnguyen.entity.Topic;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@RequiredArgsConstructor
@Service
public class TopicService {
    private final TopicDao topicDao;

    public ResponseObject getTopics() {
        List<Topic> topics = topicDao.findAll();
        List<TopicDto> topicDtos = new ArrayList<>();
        topics.forEach(topic -> {
            // lấy ra toàn bộ lable

            List<LabelDto> labels = new ArrayList<>();
            topic.getLabels().forEach(label -> labels.add(new LabelDto(label.getId(),label.getName())));
            topicDtos.add(TopicDto.builder()
                    .name(topic.getName())
                    .labels(labels)
                    .build());
        });
        return ResponseObject.builder()
                .status("success")
                .message("all topic")
                .data(topicDtos)
                .build();
    }

}
