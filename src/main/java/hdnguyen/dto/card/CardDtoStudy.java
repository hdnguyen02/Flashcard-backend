package hdnguyen.dto.card;

import hdnguyen.dto.TagDto;
import lombok.*;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardDtoStudy {
    private String id;
    private String term;
    private String definition;
    private List<TagDto> tags;
    private Map<String, Integer> options;
    private String type;
//    private String extractInfo;
//    private String image;
//    private String audio;
//    private Date createAt;
//
//    private Integer repetitions;
//    private Date lastStudyDate;
//    private Integer interval;
//    private float easeFactor;
//    private Date dueDate;
}


