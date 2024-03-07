package hdnguyen.controller;

import hdnguyen.dto.ResponseObject;
import hdnguyen.dto.UserDto;
import hdnguyen.rqbody.UserRQBody;
import hdnguyen.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class UserController {
    private final UserService userService;

    @PostMapping("/users")
    public ResponseObject register(@RequestBody UserRQBody userRQBody) throws Exception {
        String email = userRQBody.getEmail();
        String uid = userRQBody.getUid();
        UserDto userDto = userService.addUser(uid, email);
        return new ResponseObject(userDto);
    }
}
