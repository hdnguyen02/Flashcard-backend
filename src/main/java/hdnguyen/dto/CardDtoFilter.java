package hdnguyen.dto;

import lombok.*;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardDtoFilter {
    private Integer id;
    private String term;
    private String definition;
    private List<TagDto> tags;
    private String type;
    private Integer idDeck;
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


