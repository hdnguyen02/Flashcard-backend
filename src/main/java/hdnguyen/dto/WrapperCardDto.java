package hdnguyen.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WrapperCardDto {
    private List<CardDto> studyCards;
    private List<CardDto> reviewCards;
}
