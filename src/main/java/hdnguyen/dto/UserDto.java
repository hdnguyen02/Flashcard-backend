package hdnguyen.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDto {
    private String email;
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthdate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createAt;
    private List<RoleDto> roles;
}
