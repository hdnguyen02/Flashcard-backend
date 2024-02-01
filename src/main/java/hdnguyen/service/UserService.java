package hdnguyen.service;


import hdnguyen.dao.UserDao;
import hdnguyen.dto.ResponseObject;
import hdnguyen.dto.UserDto;
import hdnguyen.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;

    public ResponseObject register(String uid, String email) throws Exception {
        User user = User.builder().uid(uid).email(email).build();
        try {
            return ResponseObject.builder()
                    .status("success").message("Đăng ký tài khoản thành công!")
                    .data(new UserDto(userDao.save(user)))
                    .build();
        }
        catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }



}
