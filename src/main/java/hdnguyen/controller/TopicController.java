package hdnguyen.controller;


import hdnguyen.dto.ResponseObject;
import hdnguyen.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin("http://localhost:5173")
@RequiredArgsConstructor
@RequestMapping("/api/v1/topic")
public class TopicController {

    private final TopicService topicService;

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public ResponseObject getTopics(){
        return topicService.getTopics();
    }
}
