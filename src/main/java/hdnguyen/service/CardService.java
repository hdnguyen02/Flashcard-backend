package hdnguyen.service;

import hdnguyen.common.Helper;
import hdnguyen.common.TypeFile;
import hdnguyen.dao.CardDao;
import hdnguyen.dao.DeskDao;
import hdnguyen.dto.CardDto;
import hdnguyen.dto.ResponseObject;
import hdnguyen.dto.TagDto;
import hdnguyen.dto.WrapperCardDto;
import hdnguyen.entity.Card;
import hdnguyen.entity.Desk;
import hdnguyen.entity.Tag;
import hdnguyen.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class CardService {

    @PersistenceContext
    private EntityManager entityManager;
    private final CardDao cardDao;
    private final DeskDao deskDao;
    private final Helper helper;



    public ResponseObject addCard( String term,String definition,String image,String audio,String extractInfo, Integer idDeskAddCard, List<Integer> idTags) throws Exception {
        User user = helper.getCurentUser();
        Optional<Desk> deskAddCard = deskDao.findById(idDeskAddCard);
        if (deskAddCard.isEmpty()) throw new Exception("Không tồn tại bộ thẻ này!");
        if (!deskAddCard.get().getUser().getEmail().equals(user.getEmail())) {
            throw new Exception("Unauthorized!");
        }

        List<Tag> tags = new ArrayList<>();
        idTags.forEach(idTag -> {
            tags.add(Tag.builder()
                    .id(idTag)
                    .build());
        });


        Card addCard = Card.builder()
                .term(term).definition(definition)
                .image(image).audio(audio).extractInfo(extractInfo)
                .desk(deskAddCard.get())
                .tags(tags)
                .createAt(new Date(System.currentTimeMillis()))
                .repetitions(0)
                .lastStudyDate(null)
                .dueDate(null)
                .interval(1)
                .eFactor(2f)
                .build();
        try {
            cardDao.save(addCard);
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

    private List<CardDto> getCardsDto(List<Card> cards, String urlRoot) {

        List<CardDto> cardsDto = new ArrayList<>();

        cards.forEach(card -> {
            String urlImage = card.getImage();
            if (urlImage != null) {
                urlImage = urlRoot + "/card/" + TypeFile.image + "/" + urlImage;
            }

            String urlAudio = card.getAudio();
            if (urlAudio != null) {
                urlAudio = urlRoot + "/card/" + TypeFile.audio + "/" + urlAudio;
            }

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
                    .image(urlImage)
                    .audio(urlAudio)
                    .tags(tagsDto)
                    .createAt(card.getCreateAt())
                    .extractInfo(card.getExtractInfo())
                    .repetitions(card.getRepetitions())
                    .interval(card.getInterval())
                    .dueDate(card.getDueDate())
                    .eFactor(card.getEFactor())
                    .lastStudyDate(card.getLastStudyDate())
                    .build());
        });
        return cardsDto;
    }




    public ResponseObject getCardWithIdDesk (Integer deskId, HttpServletRequest request) throws Exception {

        String email = helper.getCurentUser().getEmail();
        if (!deskDao.existDeskWithEmail(deskId, email)) throw new Exception("Desk not found!");
        Optional<Desk> optionalDesk = deskDao.findById(deskId);
        if (optionalDesk.isEmpty()) {
            throw new Exception("Desk not found");
        }
        Desk desk = optionalDesk.get();
        Date lastDate = desk.getLastDate();
        int learnedCardNumber = desk.getLearnedCardNumber();
        int reviewedCardNumber = desk.getReviewedCardNumber();
        int studyCardNumber = desk.getStudyCardNumber();
        int reviewCardNumber = desk.getReviewCardNumber();
        if (lastDate != null && lastDate.compareTo(new Date()) == 0 ) {
            if (learnedCardNumber == studyCardNumber && reviewedCardNumber == reviewCardNumber) {
                return ResponseObject.builder()
                        .status("success")
                        .message("Bạn đã hoàn thành bộ thẻ")
                        .data(null)
                        .build();
            }
            else {
                studyCardNumber = studyCardNumber - learnedCardNumber;
                reviewCardNumber = reviewCardNumber - reviewedCardNumber;
            }
        }

        String strQueryStudyCard = "SELECT c FROM Card c WHERE c.desk.id = :deskId AND c.repetitions=0";
        TypedQuery<Card> queryStudyCard = entityManager.createQuery(strQueryStudyCard, Card.class);
        queryStudyCard.setParameter("deskId", deskId);
        queryStudyCard.setFirstResult(0);
        queryStudyCard.setMaxResults(studyCardNumber);
        List<Card> studyCards = queryStudyCard.getResultList();

        String strQueryReviewCard = "SELECT c FROM Card c WHERE c.desk.id = :deskId AND c.repetitions != 0 AND c.dueDate <= CURRENT_DATE";
        TypedQuery<Card> queryReviewCard = entityManager.createQuery(strQueryReviewCard, Card.class);
        queryReviewCard.setParameter("deskId", deskId);
        queryReviewCard.setFirstResult(0);
        queryReviewCard.setMaxResults(reviewCardNumber);
        List<Card> reviewCards = queryReviewCard.getResultList();

        String urlRoot = helper.getUrlRoot(request);
        List<CardDto> studyCardsDto = this.getCardsDto(studyCards, urlRoot);
        List<CardDto> reviewCardsDto = this.getCardsDto(reviewCards, urlRoot);

        WrapperCardDto wrapperCardDto = new WrapperCardDto(studyCardsDto, reviewCardsDto);

        return ResponseObject.builder()
                .status("success")
                .message("Truy vấn thành công")
                .data(wrapperCardDto)
                .build();
    }
}
