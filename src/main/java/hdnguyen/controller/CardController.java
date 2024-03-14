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
@RequestMapping("${system.version}")
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

        CardDto cardDto = cardService.createCard(idDeck, term, definition, extractInfo, image, audio, idTags);
        return new ResponseObject(cardDto);
    }

    @PutMapping("cards/{id}")
    public ResponseObject updateCard(@PathVariable String id,  @RequestParam (required = false) String idDeck,
                                     @RequestParam (required = false) String term,
                                     @RequestParam (required = false) String definition,
                                     @RequestParam (required = false) String extractInfo,
                                     @RequestParam (required = false) MultipartFile image,
                                     @RequestParam (required = false) MultipartFile audio,
                                     @RequestParam (required = false) Boolean isFavourite,
                                     @RequestParam (required = false) Boolean isRemembered,
                                     @RequestParam (required = false) List<String> idTags) throws Exception {
        CardDto cardDto = cardService.updateCard(id ,idDeck, term, definition,
                extractInfo,image,audio,isFavourite, isRemembered,idTags);
        return new ResponseObject(cardDto);
    }

    @DeleteMapping("cards/{id}")
    public ResponseObject deleteCard(@PathVariable String id) throws Exception {
        CardDto cardDto = cardService.deleteCard(id);
        return new ResponseObject(cardDto);
    }
    @GetMapping("cards/{id}")
    public ResponseObject getCardWithId(@PathVariable String id) throws Exception {
        CardDto cardDto = cardService.getCardWithId(id);
        return new ResponseObject(cardDto);
    }
}
