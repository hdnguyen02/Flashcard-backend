package hdnguyen.service;


import hdnguyen.common.ERole;
import hdnguyen.dao.RoleDao;
import hdnguyen.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleDao roleDao;

    public void initRole() {
        List<Role> roles = roleDao.findAll();
        if (roles.size() != 0) return;
        Role roleStudent = new Role(ERole.STUDENT.toString(), null);
        Role roleTeacher = new Role(ERole.TEACHER.name(), null);
        Role roleAdmin = new Role(ERole.ADMIN.name(), null);
        roles.add(roleStudent);
        roles.add(roleTeacher);
        roles.add(roleAdmin);
        roleDao.saveAll(roles);
    }
}
