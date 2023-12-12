package hdnguyen.requestbody;

import lombok.*;


import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeckUpdateBody {
    private String name;
    private String description;
    private Boolean isPublic;
    private List<Integer> idLabels;
}
