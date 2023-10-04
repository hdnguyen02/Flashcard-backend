package hdnguyen.dto;

import lombok.*;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardDto {
    private Integer id;
    private String term;
    private String definition;
    private String extractInfo;
    private String image;
    private String audio;
    private Date createAt;
    private Integer idDesk;
}
