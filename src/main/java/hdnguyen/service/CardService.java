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
import hdnguyen.dto.CardDto;
import hdnguyen.dto.ResponseObject;
import hdnguyen.dto.TagDto;
import hdnguyen.entity.Card;
import hdnguyen.entity.Deck;
import hdnguyen.entity.History;
import hdnguyen.entity.Tag;
import hdnguyen.requestbody.CardStudy;
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


            CardDto cardDto = CardDto.builder()
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
                    .data(cardDto)
                    .build();
        } catch (Exception e) {
            throw  new Exception(e.getMessage());
        }
    }


    public ResponseObject getCardsToStudy(int idDeck, HttpServletRequest request) throws Exception {
//        Optional<History> oHistory = historyDao.findByIdDeckAndDate(2, new Date());
//        System.out.println(historyDao.findAll());
//        System.out.println(new Date());

        String email = helper.getEMail();
        Optional<Deck> oDeck = deckDao.findById(idDeck);
        if (oDeck.isEmpty()) throw new Exception("Desk not found");
        if (!deckDao.existDeckWithEmail(idDeck, email)) throw new Exception("Unauthorized!");
        Deck deck = oDeck.get();


//        if (oHistory.isPresent()) {
//            System.out.println("Có tồn tại");
//        }
//        return null;


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
        List<CardDto> cardsDto = new ArrayList<>();
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

                cardsDto.add(CardDto.builder()
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
}
