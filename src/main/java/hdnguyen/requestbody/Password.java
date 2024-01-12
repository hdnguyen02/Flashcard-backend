package hdnguyen.requestbody;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Password {
    private String oldPassword;
    private String newPassword;
    private String conformPassword;
}
