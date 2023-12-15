package hdnguyen.requestbody;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardStudy {
    private int id;
    private int q; // đại diện cho response
}
