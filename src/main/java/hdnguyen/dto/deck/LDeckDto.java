package hdnguyen.dto.deck;

import hdnguyen.entity.Deck;
import lombok.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LDeckDto {
    private String id;
    private String description;
    private String name;
    private Integer numberCard;
    private Date createAt;

    public LDeckDto(Deck deck) {
        this.id = deck.getId();
        this.name = deck.getName();
        this.description = deck.getDescription();
        this.numberCard = deck.getCards() == null ? 0 : deck.getCards().size();
        this.createAt = deck.getCreateAt();
    }
}
