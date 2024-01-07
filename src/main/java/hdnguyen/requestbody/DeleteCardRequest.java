package hdnguyen.requestbody;

import lombok.*;


import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeleteCardRequest {
    private List<Integer> idCards;
}
