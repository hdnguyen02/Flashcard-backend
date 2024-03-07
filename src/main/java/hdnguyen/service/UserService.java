package hdnguyen.service;


import hdnguyen.common.ERole;
import hdnguyen.dao.UserDao;
import hdnguyen.dto.ResponseObject;
import hdnguyen.dto.UserDto;
import hdnguyen.entity.Role;
import hdnguyen.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;

    public UserDto addUser(String uid, String email) throws Exception {
        Optional<User> oUser = userDao.findById(uid);
        if (oUser.isPresent()) {
            throw new Exception("Đã tồn tại uid!");
        }
        Role roleStudent = new Role(ERole.STUDENT.name(), null);
        Set<Role> roles = new HashSet<>();
        roles.add(roleStudent);
        User user = User.builder().uid(uid).email(email).roles(roles).build();
        return new UserDto(userDao.save(user));
    }



}
