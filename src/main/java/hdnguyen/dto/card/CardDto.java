package hdnguyen.dto.card;

import hdnguyen.dto.TagDto;
import hdnguyen.dto.deck.DeckCardDto;
import lombok.*;
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
    private List<TagDto> tags; // có gửi list tag mà.
    private String type;
    private DeckCardDto deck;
 //   private Integer idDeck;
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


