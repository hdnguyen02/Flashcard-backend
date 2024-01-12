package hdnguyen.service;

import hdnguyen.common.Helper;
import hdnguyen.dao.UserDao;
import hdnguyen.dto.ResponseObject;
import hdnguyen.dto.RoleDto;
import hdnguyen.dto.UserDto;
import hdnguyen.entity.Role;
import hdnguyen.entity.User;
import hdnguyen.requestbody.Password;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserDao userDao;
    private final Helper helper;
    private final PasswordEncoder passwordEncoder;

    public ResponseObject updateUser(UserDto userDto) {


        User user = helper.getUser();
        user.setName(userDto.getName());
        user.setBirthdate(userDto.getBirthdate());
        userDao.save(user);

        return ResponseObject.builder()
                .message("Update thành công!")
                .status("success")
                .data(userDto)
                .build();
    }


    public ResponseObject getUser() {
        User user = helper.getUser();
        Set<Role> roles =  user.getRoles();
        List<RoleDto> rolesDto = new ArrayList<>();
        for (Role role : roles) {
            rolesDto.add(new RoleDto(role.getName(), role.getDescription()));
        }

        UserDto userDto = UserDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .roles(rolesDto)
                .birthdate(user.getBirthdate())
                .createAt(user.getCreateAt())
                .build();

        return ResponseObject.builder()
                .data(userDto)
                .message("Truy vấn thành công!")
                .status("success")
                .build();
    }
    public ResponseObject updatePassword(Password password) throws Exception {
        User user = helper.getUser();
        if(!checkPassword(password.getOldPassword(),user.getPassword())) {
            throw new Exception("Sai mật khẩu!");
        }
        if (!password.getNewPassword().equals(password.getConformPassword())) {
            throw new Exception("New password và conform password không giống nhau!");
        }
        user.setPassword(passwordEncoder.encode(password.getNewPassword()));
        userDao.save(user);
        return ResponseObject.builder()
                .status("success")
                .message("Update password thành công!")
                .data(null)
                .build();
    }
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
