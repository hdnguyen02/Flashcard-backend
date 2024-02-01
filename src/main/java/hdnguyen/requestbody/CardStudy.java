package hdnguyen.requestbody;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardStudy {
    private String id;
    private int q; // đại diện cho response
}
