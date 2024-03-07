package hdnguyen.service;

import hdnguyen.common.Helper;
import hdnguyen.dao.DeckDao;
import hdnguyen.dto.deck.DeckDto;
import hdnguyen.rqbody.DeckRQBody;
import hdnguyen.dto.deck.LDeckDto;
import hdnguyen.entity.Deck;
import hdnguyen.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class DeckService {

    private final DeckDao deckDao;
    private final Helper helper;

    public DeckDto createDeck(DeckRQBody deckRQBody) throws Exception {
        User user = helper.getUser();
        Deck deck = Deck.builder()
                .id(helper.generateUUID())
                .name(deckRQBody.getName())
                .description(deckRQBody.getDescription())
                .createAt((new Date()))
                .cards(new ArrayList<>())
                .user(user)
                .build();
        return new DeckDto(deckDao.save(deck));
    }


    public List<LDeckDto> getDesks() {
        User user = helper.getUser();
        List<Deck> decks = user.getDecks();
        List<LDeckDto> deckDtos = new ArrayList<>();
        decks.forEach(deck -> {
            deckDtos.add(new LDeckDto(deck));
        });
        return deckDtos;
    }


    public DeckDto deleteDeck(String id) throws Exception {
        Deck deck = this.getDeck(id);
        deckDao.delete(deck);
        return new DeckDto(deck);
    }

    private Deck getDeck(String id) throws Exception {
        Optional<Deck> oDeck = deckDao.findById(id);
        if (oDeck.isEmpty()) throw new Exception("Not found!");
        Deck deck = oDeck.get();
        String uid = deck.getUser().getUid();
        if (!uid.equals(helper.getUid())) {
            throw new Exception("Unauthorized!");
        }
        return deck;
    }

    public DeckDto updateDeck(String id, DeckRQBody deckRQBody) throws Exception {
        Deck deck = this.getDeck(id);
        deck.setName(deckRQBody.getName());
        deck.setDescription(deckRQBody.getDescription());
        return new DeckDto(deckDao.save(deck));
    }

    public DeckDto getDeckWithId(String id) throws Exception {
        Deck deck = this.getDeck(id);
        return new DeckDto(deck);
    }
}
