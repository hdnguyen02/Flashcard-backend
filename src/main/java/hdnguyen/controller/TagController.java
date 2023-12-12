package hdnguyen.controller;

import hdnguyen.dto.ResponseObject;
import hdnguyen.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.Map;


@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("api/v1") // sai
public class TagController {
    private final TagService tagService;

    @PostMapping("/tags")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseObject addTag(@RequestBody Map<String, String> requestBody) throws Exception {
        String name = requestBody.get("name");
        if (name == null) throw new Exception("Thiếu dữ liệu");
        return tagService.addTag(name);
    }

    @GetMapping("/tags") // thiếu => nên chỉ ra tag của ai /{idUser}/tags/{idTag}
    @ResponseStatus(HttpStatus.OK)
    public ResponseObject getAllTag()  {
        return tagService.getTags();
    }
}
