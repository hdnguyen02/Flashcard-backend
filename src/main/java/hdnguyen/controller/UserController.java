package hdnguyen.controller;


import hdnguyen.dto.ResponseObject;
import hdnguyen.dto.UserDto;
import hdnguyen.requestbody.Password;
import hdnguyen.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class UserController {
    private final UserService userService;
    @GetMapping("user")
    public ResponseObject getUser () {
        return userService.getUser();
    }

    @PutMapping("user")
    public ResponseObject updateProfile(@RequestBody UserDto userDto) {
        return userService.updateUser(userDto);
    }
    @PutMapping("password")
    public ResponseObject updatePassword(@RequestBody Password password) throws Exception {
        return userService.updatePassword(password);
    }
}
