package hdnguyen.dto;

import hdnguyen.dto.auth.LabelDto;
import hdnguyen.entity.Label;
import jakarta.persistence.Column;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeskResponse {
    private Integer id;
    private String description;
    private Boolean isPublic;
    private String name;
    private List<LabelDto> labels;
    private Date createAt;
}
