package hdnguyen.service;

import hdnguyen.algorithm.InputSm2;
import hdnguyen.algorithm.OutputSm2;
import hdnguyen.algorithm.Sm2;
import hdnguyen.common.CardType;
import hdnguyen.common.Helper;
import hdnguyen.common.Response;
import hdnguyen.component.CardQuery;
import hdnguyen.dao.CardDao;
import hdnguyen.dao.DeckDao;
import hdnguyen.dto.CardDto;
import hdnguyen.dto.ResponseObject;
import hdnguyen.dto.TagDto;
import hdnguyen.entity.Card;
import hdnguyen.entity.Deck;
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

        Optional<Card> oCard = cardDao.findById(cardStudy.getId());
        if (oCard.isEmpty()) throw new Exception("Không tồn tại thẻ này!");
        Card card = oCard.get();
        if (cardStudy.getQ() == Response.AGAIN) {
            card.setType(String.valueOf(CardType.LEARNING));
            card.setRepetition(0);
            card.setInterval(0);
            card.setEf(2.5f);
        } else {
            card.setType(String.valueOf(CardType.REVIEW));
            InputSm2 inputSm2 = InputSm2.builder()
                    .q(cardStudy.getQ())
                    .n(card.getRepetition())
                    .i(card.getInterval())
                    .ef(card.getEf())
                    .build();
            OutputSm2 outputSm2 = Sm2.calc(inputSm2);
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

            inputSm2.setQ(Response.GOOD); // GOOD
            OutputSm2 outputGOOD = Sm2.calc(inputSm2);


            if (cardUpdate.getRepetition() != 0 && cardUpdate.getRepetition() != 1) {
                inputSm2.setQ(Response.EASY); // EASY
                OutputSm2 outputEASY = Sm2.calc(inputSm2);
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
                    .message("Update thành công!")
                    .data(cardDto)
                    .build();
        } catch (Exception e) {
            throw  new Exception(e.getMessage());
        }



    }
//
//    private List<CardDto> convertCardsDto(List<Card> cards) {
//
//        List<CardDto> cardsDto = new ArrayList<>();
//        cards.forEach(card -> {
//            List<TagDto> tagsDto = new ArrayList<>();
//            List<Tag> tags = card.getTags();
//            tags.forEach(tag -> {
//                tagsDto.add(TagDto.builder()
//                        .name(tag.getName())
//                        .id(tag.getId())
//                        .build());
//            });
//
//            cardsDto.add(CardDto.builder()
//                    .id(card.getId())
//                    .term(card.getTerm())
//                    .definition(card.getDefinition())
//                    .tags(tagsDto)
//                    .build());
//        });
//        return cardsDto;
//    }

    public ResponseObject getCardsToStudy(int idDeck, HttpServletRequest request) throws Exception {

        String email = helper.getEMail();
        Optional<Deck> oDeck = deckDao.findById(idDeck);
        if (oDeck.isEmpty()) throw new Exception("Desk not found");
        if (!deckDao.existDeckWithEmail(idDeck, email)) throw new Exception("Unauthorized!");
        Deck deck = oDeck.get();

        int newLimit = deck.getNewLimit();
        int reviewLimit = deck.getReviewLimit();


        if (deck.getRecentAlter() != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String strRecentAlter = dateFormat.format(deck.getRecentAlter());
            String strNow =  dateFormat.format(new Date(    ));

            if (strRecentAlter.equals(strNow)) {
                int totalStudiedNew = deck.getTotalStudiedNew() != null ? deck.getTotalStudiedNew() : 0;
                int totalStudiedReview = deck.getTotalStudiedReview() != null ? deck.getTotalStudiedReview() : 0;
                newLimit = newLimit - totalStudiedNew;
                reviewLimit = reviewLimit - totalStudiedReview;
            }
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
                OutputSm2 outputGOOD = Sm2.calc(inputSm2);


                if (card.getRepetition() != 0 && card.getRepetition() != 1) {
                    inputSm2.setQ(5); // EASY
                    OutputSm2 outputEASY = Sm2.calc(inputSm2);
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



//        cards.forEach((type, cards) -> {
//
//            List<CardDto> cardsDto = new ArrayList<>();
//            cards.forEach(card -> {
//                Map<String, Integer> options = new HashMap<>();
//                InputSm2 inputSm2 = InputSm2.builder()
//                        .n(card.getRepetition())
//                        .ef(card.getEf())
//                        .i(card.getInterval())
//                        .build();
//
//                inputSm2.setQ(3); // GOOD
//                OutputSm2 outputGOOD = Sm2.calc(inputSm2);
//
//
//
//
//                if (card.getRepetition() != 0 && card.getRepetition() != 1) {
//                    inputSm2.setQ(5); // EASY
//                    OutputSm2 outputEASY = Sm2.calc(inputSm2);
//                    options.put("EASY",outputEASY.getI());
//                }
//
//                options.put("AGAIN",0);
//                options.put("GOOD",outputGOOD.getI());
//
//                List<TagDto> tagsDto = new ArrayList<>();
//                List<Tag> tags = card.getTags();
//                tags.forEach(tag -> {
//                    tagsDto.add(TagDto.builder()
//                            .name(tag.getName())
//                            .id(tag.getId())
//                            .build());
//                });
//
//                cardsDto.add(CardDto.builder()
//                        .id(card.getId())
//                        .term(card.getTerm())
//                        .definition(card.getDefinition())
//                        .tags(tagsDto)
//                        .options(options)
//                        .build());
//            });
//
//            dtoCards.put(type, cardsDto);
//        });

        return ResponseObject.builder()
                .status("success")
                .message("Truy vấn thành công")
                .data(cardsDto)
                .build();
    }



//    @Transactional
//    public ResponseObject updateCardsStudyAndReview(WrapperCardDto wrapperCardDto, int deskId) throws Exception {
//
////        List<CardDto> studyCards = wrapperCardDto.getStudyCards();
////        List<CardDto> reviewCards = wrapperCardDto.getReviewCards();
////
////        Optional<Deck> optionalDesk = deckDao.findById(deskId);
////        if (optionalDesk.isEmpty()) throw new Exception("Không tồn tại bộ thẻ!");
////        Deck deck = optionalDesk.get();
////        if (studyCards.size() == 0 && reviewCards.size() == 0) {return null;}
////        deck.setLastDate(new Date());
////        deck.setReviewedCardNumber(reviewCards.size());
////        deck.setLearnedCardNumber(studyCards.size());
////        try {
////            deckDao.save(deck);
////        }
////        catch (Exception e) {
////            throw new Exception(e.getMessage());
////        }
////        for (CardDto studyCard : studyCards) {
////            studyCard.setLastStudyDate(new Date());
////            Calendar calendar = Calendar.getInstance();
////            calendar.setTime(studyCard.getLastStudyDate());
////            calendar.add(Calendar.DAY_OF_MONTH, studyCard.getInterval());
////            Date dueDate = calendar.getTime();
////            studyCard.setDueDate(dueDate);
////            Card card = this.cardDtoChangeToCard(studyCard, deskId);
////            try {
////                cardDao.save(card);
////            } catch (Exception e) {
////                throw new Exception(e.getMessage());
////            }
////        }
////        for (CardDto reviewCard : reviewCards) {
////            reviewCard.setLastStudyDate(new Date());
////            Calendar calendar = Calendar.getInstance();
////            calendar.setTime(reviewCard.getLastStudyDate());
////            calendar.add(Calendar.DAY_OF_MONTH, reviewCard.getInterval());
////            Date dueDate = calendar.getTime();
////            reviewCard.setDueDate(dueDate);
////            Card card = this.cardDtoChangeToCard(reviewCard, deskId);
////            try {
////                cardDao.save(card);
////            } catch (Exception e) {
////                throw new Exception(e.getMessage());
////            }
////        }
////        return ResponseObject.builder()
////                .status("success")
////                .message("Cập nhập cards thành công!")
////                .data(wrapperCardDto)
////                .build();
//        return null;
//    }
//    private Card cardDtoChangeToCard(CardDto cardDto, int deskId) {
//        List<Tag> tags = new ArrayList<>();
//        cardDto.getTags().forEach(tagDto -> {
//            tags.add(Tag.builder().id(tagDto.getId()).build());
//        });
//        return Card.builder()
//                .id(cardDto.getId())
//                .term(cardDto.getTerm())
//                .definition(cardDto.getDefinition())
////                .image(cardDto.getImage())
////                .audio(cardDto.getAudio())
////                .extractInfo(cardDto.getExtractInfo())
////                .createAt(cardDto.getCreateAt())
//                .deck(Deck.builder().id(deskId).build())
//                .tags(tags)
////                .repetitions(cardDto.getRepetitions())
////                .interval(cardDto.getInterval())
////                .ef(cardDto.getEaseFactor())
////                .due(cardDto.getDueDate())
//                .build();
//    }
}
