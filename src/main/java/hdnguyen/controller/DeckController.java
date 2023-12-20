package hdnguyen.controller;

import hdnguyen.requestbody.DeskRequestBody;
import hdnguyen.dto.ResponseObject;
import hdnguyen.requestbody.DeckUpdateBody;
import hdnguyen.service.DeckService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

// sau này có cả tính năng share bộ desks.
@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class DeckController {
    private final DeckService deckService;
    @PostMapping("/decks")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseObject createDeck(@RequestBody DeskRequestBody deskDto) throws Exception {
        System.out.println(deskDto.toString());
       return deckService.createDeck(deskDto);
    }

    @DeleteMapping("/decks/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseObject deleteDeck(@PathVariable Integer id) throws Exception {
        return deckService.deleteDeck(id);
    }

    @PutMapping("/decks/{id}") // sai
    @ResponseStatus(HttpStatus.OK)
    public ResponseObject updateDeck(@PathVariable Integer id, @RequestBody DeckUpdateBody deckUpdateBody) throws  Exception {
        return deckService.updateDeck(id, deckUpdateBody);
    }


    @GetMapping("/decks")
    @ResponseStatus(HttpStatus.OK)
    public ResponseObject getDesks(@RequestParam(required = false, defaultValue = "asc") String sortBy,
                                   @RequestParam(required = false, defaultValue = "name") String orderBy,
                                   @RequestParam(required = false) String labels ) {

        // String [] aliasLabels = labels.split(",");
        return deckService.getDecks(null, orderBy, sortBy);
    }

    @GetMapping("decks/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseObject getDeckWithId(@PathVariable Integer id) throws Exception {
        return deckService.getDeckWithId(id);
    }
    }
