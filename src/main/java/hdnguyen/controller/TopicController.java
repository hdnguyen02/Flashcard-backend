package hdnguyen.controller;


import hdnguyen.dto.ResponseObject;
import hdnguyen.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("api/v1") // sai
public class TopicController {

    private final TopicService topicService;

    @GetMapping("/topics")
    @ResponseStatus(HttpStatus.OK)
    public ResponseObject getTopics(){
        return topicService.getTopics();
    }
}
