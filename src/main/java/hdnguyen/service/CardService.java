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


//        List<Tag> tags = new ArrayList<>();
//        idTags.forEach(idTag -> {
//            tags.add(Tag.builder()
//                    .id(idTag)
//                    .build());
//        });
////        if (extractInfo.equals("null") || extractInfo.equals("")) {
////            extractInfo = null;
////        }
//        Card card = Card.builder()
//                .term(term).definition(definition)
//                .deck(desk.get())
//                .tags(tags)
//                .createAt(new Date(System.currentTimeMillis()))
//                .build();
//        try {
//            cardDao.save(card);
//        }
//        catch (Exception e) {
//            throw new Exception(e.getMessage());
//        }
//        return null;
    }





    //    public ResponseObject updateCard( CardDeckDto cardDto) throws Exception {
    //        String idDeck = cardDto.getDeck().getId();
    //        String email = helper.getEMail();
    //        Optional<Deck> oDeck = deckDao.findById(cardDto.getDeck().getId());
    //        if (oDeck.isEmpty()) throw new Exception("Không hợp lệ!");
    //        if (deckDao.existDeckWithEmail(idDeck, email)) throw new Exception("Unauthorized!");
    //        Optional<Card> oCard = cardDao.findById(cardDto.getId());
    //        if (oCard.isEmpty()) throw new Exception("Not found Card!");
    //        Card card  = oCard.get();
    //        card.setTerm(cardDto.getTerm());
    //        card.setDefinition(cardDto.getDefinition());
    //        card.setDeck(Deck.builder().id(cardDto.getDeck().getId()).build());
    //
    //        List<Tag> tags = new ArrayList<>();
    //        cardDto.getTags().forEach(tagDto -> {
    //            tags.add(Tag.builder()
    //                    .id(tagDto.getId())
    //                    .name(tagDto.getName())
    //                    .user(helper.getUser())
    //                    .build()
    //            );
    //        });
    //        card.setTags(tags);
    //        cardDao.save(card);
    //        return ResponseObject.builder()
    //                .data(null)
    //                .status("success")
    //                .message("Update thành công!")
    //                .build();
    //    }
    //    public ResponseObject deleteCard(String idCard) {
    //        cardDao.deleteById(idCard);
    //        return ResponseObject.builder()
    //                .status("success")
    //                .data(null)
    //                .message("Delete thành công!")
    //                .build();
    //    }
    //
    //
    //    public ResponseObject getCards(String filter, String value) throws Exception {
    //        String query;
    //        TypedQuery<Card> queryCard;
    //        String email = helper.getEMail();
    //
    //        if (filter == null) {
    //            query = "SELECT c from Card c WHERE c.deck.user.email =: email";
    //            queryCard =  entityManager.createQuery(query, Card.class);
    //            queryCard.setParameter("email", email);
    //        }
    //        else if (filter.equals("id-deck")) {
    //            int idDeck = Integer.parseInt(value);
    //            query = "SELECT c from Card c WHERE c.deck.user.email =: email AND c.deck.id = :idDeck";
    //            queryCard =  entityManager.createQuery(query, Card.class);
    //            queryCard.setParameter("email", email);
    //            queryCard.setParameter("idDeck", idDeck);
    //        }
    //        else if (filter.equals("tags")){
    //            String [] nameTags = value.split(",");
    //            query = "SELECT c from Card c JOIN c.tags t WHERE c.deck.user.email =: email AND t.name IN :nameTags";
    //            queryCard =  entityManager.createQuery(query, Card.class);
    //            queryCard.setParameter("email", email);
    //            queryCard.setParameter("nameTags", Arrays.asList(nameTags));
    //        }
    //        else if (filter.equals("type")) {
    //            query = "SELECT c from Card c WHERE c.deck.user.email =: email AND c.type = :type";
    //            queryCard =  entityManager.createQuery(query, Card.class);
    //            queryCard.setParameter("email", email);
    //            queryCard.setParameter("type", value);
    //        }
    //        else throw new Exception("params không hợp lệ!");
    //        List<Card> cards = queryCard.getResultList();
    //        List<CardDeckDto> cardsDto = new ArrayList<>();
    //        cards.forEach(card -> {
    //            List<TagDto> tagsDto = new ArrayList<>();
    //            List<Tag> lTags = card.getTags();
    //            lTags.forEach(tag -> {
    //                tagsDto.add(TagDto.builder()
    //                        .name(tag.getName())
    //                        .id(tag.getId())
    //                        .build());
    //            });
    //
    //            DeckCardDto deckDto = DeckCardDto.builder()
    //                    .id(card.getDeck().getId())
    //                    .name(card.getDeck().getName())
    //                    .build();
    //
    //            cardsDto.add(CardDeckDto.builder()
    //                    .id(card.getId())
    //                    .term(card.getTerm())
    //                    .definition(card.getDefinition())
    //                    .tags(tagsDto)
    //                    .deck(deckDto)
    //                    .build());
    //        });
    //        return ResponseObject.builder()
    //                .status("success")
    //                .message("Truy vấn thành công")
    //                .data(cardsDto)
    //                .build();
    //    }

}
