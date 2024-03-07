package hdnguyen.dto.deck;

import hdnguyen.dto.card.CardDto;
import hdnguyen.entity.Deck;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeckDto {
    private String id;
    private String description;
    private String name;
    private Date createAt;
    private List<CardDto> cards = new ArrayList<>();

    public DeckDto(Deck deck) {
        this.id = deck.getId();
        this.description = deck.getDescription();
        this.name = deck.getName();
        this.createAt = deck.getCreateAt();
        deck.getCards().forEach(card -> {
            this.cards.add(new CardDto(card));
        });
    }
}



