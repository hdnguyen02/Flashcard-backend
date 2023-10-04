package hdnguyen.requestbody;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeskRequestBody {
    private Integer id; // có thể nhận lấy null.
    private String name;
    private String description;
    private Boolean isPublic;
    private List<Integer> idLabels; // chứa dánh sách id label.
}
