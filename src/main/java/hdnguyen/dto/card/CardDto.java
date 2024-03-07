package hdnguyen.dto.card;

import hdnguyen.dto.TagDto;
import hdnguyen.entity.Card;
import lombok.*;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardDto {
    private String id;
    private String term;
    private String definition;
    private List<TagDto> tags;
    private String image;
    private String audio;
    private String extractInfo;
    private Date createAt;
    private Boolean isFavourite;
    private Boolean isRemembered;
    private String idDeck;


    public CardDto(Card card){
        this.id = card.getId();
        this.term = card.getTerm();
        this.definition = card.getDefinition();
        card.getTags().forEach(tag -> {
            this.tags.add(new TagDto(tag));
        });
        this.image = card.getImage();
        this.audio = card.getAudio();
        this.extractInfo = card.getExtractInfo();
        this.createAt = card.getCreateAt();
        this.isFavourite = card.getIsFavourite();
        this.isRemembered = card.getIsRemembered();
        this.idDeck = card.getDeck().getId();
    }
}


