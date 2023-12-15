package hdnguyen.controller;

import hdnguyen.dto.ResponseObject;
import hdnguyen.requestbody.CardStudy;
import hdnguyen.service.CardService;
import hdnguyen.service.StorageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class CardController {
    private final StorageService storageService;
    private final CardService cardService;

    @GetMapping("/decks/{id-deck}/cards/study")
    @ResponseStatus(HttpStatus.OK)
    public ResponseObject getCardsToStudy(@PathVariable(name = "id-deck") Integer idDeck, HttpServletRequest request) throws Exception {
        return cardService.getCardsToStudy(idDeck, request);
    }

    @PutMapping("/decks/{id-deck}/cards/study")
    @ResponseStatus(HttpStatus.OK)
    public ResponseObject calcCardStudy(@PathVariable(name = "id-deck") Integer idDeck, @RequestBody CardStudy cardStudy) throws Exception {
        return cardService.calcCardStudy(idDeck, cardStudy);
    }


    @PostMapping("decks/{id-deck}/cards")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseObject createCard(
            @PathVariable(name = "id-deck") Integer idDeck,
            @RequestParam(value = "term") String term, @RequestParam String definition,
            @RequestParam(value = "idTags", required = false) List<Integer> idTags
//            @RequestParam(required = false) String extractInfo,
//            @RequestParam(value = "image", required = false) MultipartFile image,
//            @RequestParam(value = "audio", required = false) MultipartFile audio
            ) throws  Exception {

//        String imageName = storageService.save(image, TypeFile.image);
//        String audioName = storageService.save(audio, TypeFile.audio);
        return cardService.createCard(term, definition, idDeck,idTags);
    }

    // gửi đến card thẻ học.


}
