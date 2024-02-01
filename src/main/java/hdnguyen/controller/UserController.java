package hdnguyen.controller;


import hdnguyen.dto.ResponseObject;
import hdnguyen.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseObject register(@RequestBody Map<String, String> info) throws Exception {
        System.out.println("vào nè");
        return userService.register(info.get("uid"), info.get("email"));
    }




}
