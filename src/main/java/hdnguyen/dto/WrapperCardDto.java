package hdnguyen.dto;

import hdnguyen.dto.card.CardDtoStudy;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WrapperCardDto {
    private List<CardDtoStudy> studyCards;
    private List<CardDtoStudy> reviewCards;
}
