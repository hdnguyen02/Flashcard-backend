package hdnguyen.dto;

import hdnguyen.dto.auth.LabelDto;
import lombok.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeskDto {
    private Integer id;
    private String description;
    private Boolean isPublic;
    private String name;
    private List<LabelDto> labels;
    private Integer cardNumber; 
    private Date createAt;
}
