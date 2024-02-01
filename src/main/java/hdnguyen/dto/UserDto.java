package hdnguyen.dto;

import hdnguyen.entity.Role;
import hdnguyen.entity.User;
import lombok.*;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String uid;
    private String email;
    private Set<Role> roles;

    public UserDto(User user) {
        this.uid = user.getUid();
        this.email = user.getEmail();
        this.roles = user.getRoles();
    }
}

