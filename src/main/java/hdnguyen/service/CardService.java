package hdnguyen.service;

import hdnguyen.algorithm.InputSm2;
import hdnguyen.algorithm.OutputSm2;
import hdnguyen.algorithm.ScheduleSM2;
import hdnguyen.common.CardType;
import hdnguyen.common.Helper;
import hdnguyen.common.Response;
import hdnguyen.component.CardQuery;
import hdnguyen.dao.CardDao;
import hdnguyen.dao.DeckDao;
import hdnguyen.dao.HistoryDao;
import hdnguyen.dto.card.CardDto;
import hdnguyen.dto.card.CardDtoStudy;
import hdnguyen.dto.ResponseObject;
import hdnguyen.dto.TagDto;
import hdnguyen.dto.deck.DeckCardDto;
import hdnguyen.entity.Card;
import hdnguyen.entity.Deck;
import hdnguyen.entity.History;
import hdnguyen.entity.Tag;
import hdnguyen.requestbody.CardStudy;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class CardService {
    private final CardDao cardDao;
    private final DeckDao deckDao;
    private final HistoryDao historyDao;
    private final Helper helper;
    private final CardQuery cardQuery;

    @PersistenceContext
    private final EntityManager entityManager;

    public ResponseObject createCard(String term, String definition, Integer idDeck, List<Integer> idTags) throws Exception {
        Optional<Deck> desk = deckDao.findById(idDeck);
        if (desk.isEmpty()) throw new Exception("Không tồn tại bộ thẻ!");
        if (!deckDao.existDeckWithEmail(idDeck, helper.getEMail())) throw new Exception("Unauthorized!");

        List<Tag> tags = new ArrayList<>();
        idTags.forEach(idTag -> {
            tags.add(Tag.builder()
                    .id(idTag)
                    .build());
        });
//        if (extractInfo.equals("null") || extractInfo.equals("")) {
//            extractInfo = null;
//        }
        Card card = Card.builder()
                .term(term).definition(definition)
                .deck(desk.get())
                .tags(tags)
                .createAt(new Date(System.currentTimeMillis()))
                .type(String.valueOf(CardType.FRESH))
                .interval(0)
                .repetition(0)
                .ef(2.5f)
                .build();
        try {
            cardDao.save(card);
        }
        catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return ResponseObject.builder()
                .status("success")
                .message("Thêm card thành công")
                .data(null)
                .build();
    }

    public ResponseObject calcCardStudy(int idDeck, CardStudy cardStudy) throws Exception {

        Optional<Deck> oDeck = deckDao.findById(idDeck);
        Optional<Card> oCard = cardDao.findById(cardStudy.getId());
        if (oDeck.isEmpty()) throw new Exception("Không tồn tại b thẻ này!");
        if (oCard.isEmpty()) throw new Exception("Không tồn tại thẻ này!");
        Deck deck = oDeck.get();
        Card card = oCard.get();
        if (cardStudy.getQ() < Response.GOOD) { // 0 1 2 > 3
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Optional<History> oHistory = historyDao.findByIdDeckAndDate(deck.getId(), dateFormat.format(new Date()));

            History history;
            history = oHistory.orElseGet(() -> History.builder()
                    .idDeck(deck.getId())
                    .totalStudiedNew(0)
                    .totalStudiedReview(0)
                    .date(dateFormat.format(new Date()))
                    .build());



            if (card.getType().equals(String.valueOf(CardType.FRESH))) {
                history.setTotalStudiedNew(history.getTotalStudiedNew() + 1);
            }
            else if (card.getType().equals(String.valueOf(CardType.REVIEW))) {
                history.setTotalStudiedReview(history.getTotalStudiedReview() + 1);
            }

            historyDao.save(history);
            card.setType(String.valueOf(CardType.LEARNING));
            card.setRepetition(0);
            card.setInterval(0);
            card.setEf(2.5f);

            // đồng thời kiểm tra xem trước đó là card gì cập nhập lại số lượng.

        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Optional<History> oHistory = historyDao.findByIdDeckAndDate(deck.getId(), dateFormat.format(new Date()));

            History history;
            history = oHistory.orElseGet(() -> History.builder()
                    .idDeck(deck.getId())
                    .totalStudiedNew(0)
                    .totalStudiedReview(0)
                    .date(dateFormat.format(new Date()))
                    .build());



            if (card.getType().equals(String.valueOf(CardType.FRESH))) {
                history.setTotalStudiedNew(history.getTotalStudiedNew() + 1);
            }
            else if (card.getType().equals(String.valueOf(CardType.REVIEW))) {
                history.setTotalStudiedReview(history.getTotalStudiedReview() + 1);
            }
            card.setType(String.valueOf(CardType.REVIEW));
            historyDao.save(history);
            InputSm2 inputSm2 = InputSm2.builder()
                    .q(cardStudy.getQ())
                    .n(card.getRepetition())
                    .i(card.getInterval())
                    .ef(card.getEf())
                    .build();
            OutputSm2 outputSm2 = ScheduleSM2.calc(inputSm2);
            card.setRepetition(outputSm2.getN());
            card.setInterval(outputSm2.getI());
            card.setEf(outputSm2.getEf());

            // DUE
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_MONTH, card.getInterval());
            card.setDue(calendar.getTime());
        }

        try {
            Card cardUpdate = cardDao.save(card);
            Map<String, Integer> options = new HashMap<>();
            InputSm2 inputSm2 = InputSm2.builder()
                    .n(cardUpdate.getRepetition())
                    .ef(cardUpdate.getEf())
                    .i(cardUpdate.getInterval())
                    .build();

            inputSm2.setQ(Response.GOOD);
            OutputSm2 outputGOOD = ScheduleSM2.calc(inputSm2);

            if (cardUpdate.getRepetition() != 0 && cardUpdate.getRepetition() != 1) {
                inputSm2.setQ(Response.EASY); // EASY
                OutputSm2 outputEASY = ScheduleSM2.calc(inputSm2);
                options.put("EASY", outputEASY.getI());
            }

            options.put("AGAIN", 0);
            options.put("GOOD", outputGOOD.getI());

            List<TagDto> tagDtos = new ArrayList<>();

            cardUpdate.getTags().forEach(tag -> {
                tagDtos.add(TagDto.builder().id(tag.getId()).name(tag.getName()).build());
            });


            CardDtoStudy cardDtoStudy = CardDtoStudy.builder()
                    .id(cardUpdate.getId())
                    .term(cardUpdate.getTerm())
                    .definition(cardUpdate.getDefinition())
                    .tags(tagDtos)
                    .type(cardUpdate.getType())
                    .options(options)
                    .build();
            return ResponseObject.builder()
                    .status("success")
                    .message("Update thành công")
                    .data(cardDtoStudy)
                    .build();
        } catch (Exception e) {
            throw  new Exception(e.getMessage());
        }
    }


    public ResponseObject getCardsToStudy(int idDeck, HttpServletRequest request) throws Exception {
        String email = helper.getEMail();
        Optional<Deck> oDeck = deckDao.findById(idDeck);
        if (oDeck.isEmpty()) throw new Exception("Desk not found");
        if (!deckDao.existDeckWithEmail(idDeck, email)) throw new Exception("Unauthorized!");
        Deck deck = oDeck.get();
        int newLimit = deck.getNewLimit();
        int reviewLimit = deck.getReviewLimit();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Optional<History> oHistory = historyDao.findByIdDeckAndDate(deck.getId(), dateFormat.format(new  Date())); // check lịch sử của hôm nay
        if (oHistory.isPresent()) {
            History history = oHistory.get();
                newLimit = newLimit - history.getTotalStudiedNew();
                reviewLimit = reviewLimit - history.getTotalStudiedReview();
        }

        List<Card> cards = cardQuery.getCardsToStudy(idDeck, newLimit, reviewLimit);
        List<CardDtoStudy> cardsDto = new ArrayList<>();
        cards.forEach(card -> {

            Map<String, Integer> options = new HashMap<>();
                InputSm2 inputSm2 = InputSm2.builder()
                        .n(card.getRepetition())
                        .ef(card.getEf())
                        .i(card.getInterval())
                        .build();

                inputSm2.setQ(3); // GOOD
                OutputSm2 outputGOOD = ScheduleSM2.calc(inputSm2);


                if (card.getRepetition() != 0 && card.getRepetition() != 1) {
                    inputSm2.setQ(5); // EASY
                    OutputSm2 outputEASY = ScheduleSM2.calc(inputSm2);
                    options.put("EASY",outputEASY.getI());
                }

                options.put("AGAIN",0);
                options.put("GOOD",outputGOOD.getI());

                List<TagDto> tagsDto = new ArrayList<>();
                List<Tag> tags = card.getTags();
                tags.forEach(tag -> {
                    tagsDto.add(TagDto.builder()
                            .name(tag.getName())
                            .id(tag.getId())
                            .build());
                });

                cardsDto.add(CardDtoStudy.builder()
                        .id(card.getId())
                        .term(card.getTerm())
                        .definition(card.getDefinition())
                        .type(card.getType())
                        .tags(tagsDto)
                        .options(options)
                        .build());
            });

        return ResponseObject.builder()
                .status("success")
                .message("Truy vấn thành công")
                .data(cardsDto)
                .build();
    }

    public ResponseObject updateCard( CardDto cardDto) throws Exception {
        int idDeck = cardDto.getDeck().getId();
        String email = helper.getEMail();
        Optional<Deck> oDeck = deckDao.findById(cardDto.getDeck().getId());
        if (oDeck.isEmpty()) throw new Exception("Không hợp lệ!");
        if (!deckDao.existDeckWithEmail(idDeck, email)) throw new Exception("Unauthorized!");
        Optional<Card> oCard = cardDao.findById(cardDto.getId());
        if (oCard.isEmpty()) throw new Exception("Not found Card!");
        Card card  = oCard.get();
        card.setTerm(cardDto.getTerm());
        card.setDefinition(cardDto.getDefinition());
        card.setDeck(Deck.builder().id(cardDto.getDeck().getId()).build());
        cardDao.save(card);
        return ResponseObject.builder()
                .data(null)
                .status("success")
                .message("Update thành công!")
                .build();
    }
    public ResponseObject deleteCard(int idCard) {
        cardDao.deleteById(idCard);
        return ResponseObject.builder()
                .status("success")
                .data(null)
                .message("Delete thành công!")
                .build();
    }


    public ResponseObject getCards(String filter, String value) throws Exception {
        String query;
        TypedQuery<Card> queryCard;
        String email = helper.getEMail();

        if (filter == null) {
            query = "SELECT c from Card c WHERE c.deck.user.email =: email";
            queryCard =  entityManager.createQuery(query, Card.class);
            queryCard.setParameter("email", email);
        }
        else if (filter.equals("id-deck")) {
            int idDeck = Integer.parseInt(value);
            query = "SELECT c from Card c WHERE c.deck.user.email =: email AND c.deck.id = :idDeck";
            queryCard =  entityManager.createQuery(query, Card.class);
            queryCard.setParameter("email", email);
            queryCard.setParameter("idDeck", idDeck);
        }
        else if (filter.equals("tags")){
            String [] nameTags = value.split(",");
            query = "SELECT c from Card c JOIN c.tags t WHERE c.deck.user.email =: email AND t.name IN :nameTags";
            queryCard =  entityManager.createQuery(query, Card.class);
            queryCard.setParameter("email", email);
            queryCard.setParameter("nameTags", Arrays.asList(nameTags));
        }
        else if (filter.equals("type")) {
            query = "SELECT c from Card c WHERE c.deck.user.email =: email AND c.type = :type";
            queryCard =  entityManager.createQuery(query, Card.class);
            queryCard.setParameter("email", email);
            queryCard.setParameter("type", value);
        }
        else throw new Exception("params không hợp lệ!");
        List<Card> cards = queryCard.getResultList();
        List<CardDto> cardsDto = new ArrayList<>();
        cards.forEach(card -> {
            List<TagDto> tagsDto = new ArrayList<>();
            List<Tag> lTags = card.getTags();
            lTags.forEach(tag -> {
                tagsDto.add(TagDto.builder()
                        .name(tag.getName())
                        .id(tag.getId())
                        .build());
            });

            DeckCardDto deckDto = DeckCardDto.builder()
                    .id(card.getDeck().getId())
                    .name(card.getDeck().getName())
                    .build();

            cardsDto.add(CardDto.builder()
                    .id(card.getId())
                    .term(card.getTerm())
                    .definition(card.getDefinition())
                    .type(card.getType())
                    .tags(tagsDto)
                    .deck(deckDto)
                    .build());
        });
        return ResponseObject.builder()
                .status("success")
                .message("Truy vấn thành công")
                .data(cardsDto)
                .build();
    }

}
