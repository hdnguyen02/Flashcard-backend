package hdnguyen.service;

import hdnguyen.common.Helper;
import hdnguyen.dao.CardDao;
import hdnguyen.dao.DeckDao;
import hdnguyen.dto.card.CardDto;
import hdnguyen.entity.Card;
import hdnguyen.entity.Deck;
import hdnguyen.entity.Tag;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class CardService {
    private final CardDao cardDao;
    private final DeckDao deckDao;
    private final Helper helper;
    private final FirebaseStorageService firebaseStorageService;

    @PersistenceContext
    private final EntityManager entityManager;

    public CardDto createCard(String idDeck, String term, String definition, String extractInfo,
                              MultipartFile image, MultipartFile audio, List<String> idTags) throws Exception {


        Optional<Deck> oDesk = deckDao.findById(idDeck);
        if (oDesk.isEmpty()) throw new Exception("Không tồn tại bộ thẻ!");
        Deck deck = oDesk.get();

        String imageUrl = image != null ? firebaseStorageService.save("image", image) : null;
        String audioUrl = audio != null ? firebaseStorageService.save("audio", audio) : null;

        List<Tag> tags = new ArrayList<>();
        idTags.forEach(idTag -> {
            tags.add(Tag.builder().id(idTag).build());
        });


        Card card = Card.builder()
                .id(helper.generateUUID())
                .term(term)
                .definition(definition)
                .extractInfo(extractInfo)
                .image(imageUrl)
                .audio(audioUrl)
                .tags(tags)
                .createAt(new Date(System.currentTimeMillis()))
                .isFavourite(false)
                .isRemembered(false)
                .deck(deck)
                .build();

        return new CardDto(cardDao.save(card));
    }

    public CardDto updateCard(String id, String idDeck,String term, String definition,String extractInfo,
                              MultipartFile image, MultipartFile audio, Boolean isFavourite,
                              Boolean isRemembered,List<String> idTags) throws Exception {

        Optional<Card>  oCard = cardDao.findById(id);
        if (oCard.isEmpty()) throw new Exception("Not found card!");
        Card card = oCard.get();
        if (idDeck != null) {
            Optional<Deck>  oDeck = deckDao.findById(idDeck);
            if (oDeck.isEmpty()) throw new Exception("Not found deck!");
            Deck deck = oDeck.get();
            card.setDeck(deck);
        }

        if (image != null ) {
            card.setImage(firebaseStorageService.save("image", image));
        }
        if (audio != null) {
            card.setAudio(firebaseStorageService.save("audio", audio));
        }

        if (idTags != null) {
            List<Tag> tags = new ArrayList<>();
            idTags.forEach(idTag -> {
                tags.add(Tag.builder().id(idTag).build());
            });
            card.setTags(tags);
        }

        if (term != null) {
            card.setTerm(term);
        }
        if (definition != null) {
            card.setDefinition(definition);
        }
        if (extractInfo != null) {
            card.setExtractInfo(extractInfo);
        }
        if (isRemembered != null) {
            card.setIsRemembered(isRemembered);
        }
        if (isFavourite != null) {
            card.setIsFavourite(isFavourite);
        }
        return new CardDto(cardDao.save(card));
    }

    public CardDto deleteCard(String id) throws Exception {
        Optional<Card> oCard = cardDao.findById(id);
        if (oCard.isEmpty()) throw new Exception("Not found card!");
        Card card = oCard.get();
        cardDao.delete(card);
        return new CardDto(card);
    }

    public CardDto getCardWithId(String id) throws Exception {
        Optional<Card> oCard = cardDao.findById(id);
        if (oCard.isEmpty()) throw new Exception("Not found card!");
        Card card = oCard.get();
        return new CardDto(card);
    }
}
