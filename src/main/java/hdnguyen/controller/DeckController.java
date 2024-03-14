package hdnguyen.controller;

import hdnguyen.dto.deck.DeckDto;
import hdnguyen.dto.deck.LDeckDto;
import hdnguyen.rqbody.DeckRQBody;
import hdnguyen.dto.ResponseObject;
import hdnguyen.service.DeckService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@ResponseStatus(HttpStatus.OK)
@RequestMapping("${system.version}")
public class DeckController {

    private final DeckService deckService;


    @GetMapping("/decks")
    public ResponseObject getDecks() {
        List<LDeckDto> deckDtos = deckService.getDesks();
        return new ResponseObject(deckDtos);
    }

    @PostMapping("/decks")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseObject createDeck(@RequestBody DeckRQBody deckRQBody) throws Exception {
        DeckDto deckDto = deckService.createDeck(deckRQBody);
        return new ResponseObject(deckDto);
    }

    @DeleteMapping("/decks/{id}")
    public ResponseObject deleteDeck(@PathVariable String id) throws Exception {
        DeckDto deckDto = deckService.deleteDeck(id);
        return new ResponseObject(deckDto);
    }

    @PutMapping("/decks/{id}")
    public ResponseObject updateDeck(@PathVariable String id,@RequestBody DeckRQBody deckRQBody) throws  Exception {
        DeckDto deckDto = deckService.updateDeck(id, deckRQBody);
        return new ResponseObject(deckDto);
    }

    @GetMapping("decks/{id}")
    public ResponseObject getDeckWithId(@PathVariable String id) throws Exception{
        DeckDto deckDto = deckService.getDeckWithId(id);
        return new ResponseObject(deckDto);
    }
}
