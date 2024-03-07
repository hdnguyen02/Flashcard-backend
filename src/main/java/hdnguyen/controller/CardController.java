package hdnguyen.controller;

import hdnguyen.dto.ResponseObject;
import hdnguyen.dto.card.CardDto;
import hdnguyen.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;


@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@ResponseStatus(HttpStatus.OK)
@RequestMapping("api/v1")
public class CardController {
    private final CardService cardService;


    @PostMapping("cards")
    public ResponseObject createCard(
            @RequestParam String idDeck,
            @RequestParam String term,
            @RequestParam String definition,
            @RequestParam (required = false) String extractInfo,
            @RequestParam (required = false) MultipartFile image,
            @RequestParam (required = false) MultipartFile audio,
            @RequestParam (required = false) List<String> idTags) throws  Exception {

        CardDto cardDeckDto = cardService.createCard(idDeck, term, definition, extractInfo, image, audio, idTags);
        return new ResponseObject(cardDeckDto);
    }

//    @GetMapping("cards") // lọc cards
//
//    public ResponseObject getCards(@RequestParam(required = false) String filter, @RequestParam(required = false) String value) throws Exception {
//        return cardService.getCards(filter, value);
//    }
//
//    @PutMapping("cards")
//    @ResponseStatus(HttpStatus.OK)
//    public ResponseObject updateCard( @RequestBody CardDeckDto cardDto) throws Exception {
//        return cardService.updateCard(cardDto);
//    }
//
//    @DeleteMapping("cards/{id}")
//    @ResponseStatus(HttpStatus.OK)
//    public ResponseObject deleteCards(@PathVariable String id) {
//        return cardService.deleteCard(id);
//    }
}
