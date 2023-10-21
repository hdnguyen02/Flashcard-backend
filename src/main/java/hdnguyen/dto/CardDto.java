package hdnguyen.dto;

import lombok.*;
import java.util.Date;
import java.util.List;

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
    private List<TagDto> tags;
    private Integer repetitions;
    private Date lastStudyDate;
    private Integer interval;
    private float eFactor;
    private Date dueDate;
}


