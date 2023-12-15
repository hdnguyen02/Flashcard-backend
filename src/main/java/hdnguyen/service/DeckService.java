package hdnguyen.service;

import hdnguyen.common.Helper;
import hdnguyen.dao.DeckDao;
import hdnguyen.requestbody.DeskRequestBody;
import hdnguyen.dto.DeskDto;
import hdnguyen.dto.ResponseObject;
import hdnguyen.dto.auth.LabelDto;
import hdnguyen.entity.Deck;
import hdnguyen.entity.Label;
import hdnguyen.entity.User;
import hdnguyen.requestbody.DeckUpdateBody;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class DeckService {

    private final DeckDao deckDao;
    private final Helper helper;
    @PersistenceContext
    private EntityManager entityManager;

    public ResponseObject createDeck(DeskRequestBody deskDto) throws Exception {
        List<Label> labels = new ArrayList<>();
        deskDto.getIdLabels().forEach(idLabel -> {
            Label label = Label.builder()
                    .id(idLabel)
                    .build();
            labels.add(label);
        });

        User user = helper.getUser();
        Deck addDeck = Deck.builder()
                .name(deskDto.getName())
                .isPublic(deskDto.getIsPublic())
                .description(deskDto.getDescription())
                .createAt((new Date()))
                .newLimit(10)
                .reviewLimit(30)
                .user(user)
                .labels(labels)


                .build();
        Set<String> userDeskName = new HashSet<>();
        user.getDecks().forEach(userDesk -> {
            userDeskName.add(userDesk.getName());
        });
        if (userDeskName.contains(addDeck.getName())) throw new Exception("Tên bộ thẻ đã tồn tại!");
        try {
            Deck deckAddSuccess = deckDao.save(addDeck);
            deskDto.setId(deckAddSuccess.getId());
        }
        catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return ResponseObject.builder()
                .status("success")
                .data(deskDto)
                .message("Thêm bộ thẻ thành công")
                .build();
    }

    public ResponseObject getDecks(String [] aliasLabels, String orderBy, String sortBy) {

        String strQuery = null;
        TypedQuery<Deck> queryDeck;
        String email = helper.getUser().getEmail();
        if (aliasLabels != null) {
            strQuery = "SELECT d from Deck d JOIN d.labels l WHERE d.user.email=:email AND l.alias IN :aliasLabels ORDER BY d." + orderBy + " " + sortBy;
            queryDeck = entityManager.createQuery(strQuery, Deck.class);
            queryDeck.setParameter("aliasLabels", Arrays.asList(aliasLabels));
            queryDeck.setParameter("email", email);
        }
        else { // không lọc theo labels
            strQuery = "SELECT d from Deck d WHERE d.user.email=:email ORDER BY d." + orderBy + " " + sortBy;
            queryDeck = entityManager.createQuery(strQuery, Deck.class);
            queryDeck.setParameter("email", email);

        }

        List<Deck> decks = queryDeck.getResultList();
        List<DeskDto> deskResponses = new ArrayList<>();
        decks.forEach(desk -> {
            List<LabelDto> labelDtos = new ArrayList<>();
            desk.getLabels().forEach(label -> {
                labelDtos.add(new LabelDto(label.getId(), label.getName()));
            });
            deskResponses.add(
                    DeskDto.builder()
                            .id(desk.getId())
                            .name(desk.getName())
                            .description(desk.getDescription())
                            .isPublic(desk.getIsPublic())
                            .createAt(desk.getCreateAt())
                            .cardNumber(desk.getCards().size())
                            .labels(labelDtos)
                            .build()
            );
        });
        return ResponseObject.builder()
                .status("success")
                .message("Truy vấn thành công")
                .data(deskResponses)
                .build();
    }

    public ResponseObject deleteDeck(int id) throws Exception {
        Deck deck = this.getDeckWithOfUser(id);
        try {
            deckDao.delete(deck); // xóa desk.
        }
        catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return ResponseObject.builder()
                .message("delete thành công")
                .status("success")
                .data(null)
                .build();
    }


    private Deck getDeckWithOfUser(int id) throws Exception{
        Optional<Deck> optionalDesk = deckDao.findById(id);
        if (optionalDesk.isEmpty()) throw new Exception("Không tìm thấy bộ thẻ!");
        User user = helper.getUser();
        Deck deck = optionalDesk.get();
        if (!deck.getUser().getEmail().equals(user.getEmail())) throw new Exception("Unauthorized!");
        return deck;
    }

    // update
    public ResponseObject updateDeck(int id, DeckUpdateBody deckUpdateBody) throws Exception {
        Deck deck = this.getDeckWithOfUser(id);
        deck.setName(deckUpdateBody.getName());
        deck.setDescription(deckUpdateBody.getDescription());
        deck.setIsPublic(deckUpdateBody.getIsPublic());
        List<Label> labels = new ArrayList<>();
        deckUpdateBody.getIdLabels().forEach(idLabel -> {
            labels.add(Label.builder().id(idLabel).build());
        });
        deck.setLabels(labels);
        try {
            Deck deckUpdate =  deckDao.save(deck);

            List<Label> labelsUpdate= deckUpdate.getLabels();
            List<LabelDto> labelsDto = new ArrayList<>();
            labelsUpdate.forEach(labelUpdate -> {
                labelsDto.add(LabelDto.builder().id(labelUpdate.getId()).name(labelUpdate.getName()).build());
            });

            DeskDto deskDto = DeskDto.builder()
                    .id(deckUpdate.getId())
                    .name(deckUpdate.getName())
                    .isPublic(deckUpdate.getIsPublic())
                    .description(deckUpdate.getDescription())
                    .createAt(deckUpdate.getCreateAt())
                    .labels(labelsDto)
                    .build();

            return ResponseObject.builder()
                    .status("success")
                    .message("update thành công")
                    .data(deskDto)
                    .build();
        }
        catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public ResponseObject getDeckWithId(int id) throws Exception {

        Deck deck = this.getDeckWithOfUser(id);
        List<Label> labels = deck.getLabels();
        List<LabelDto> labelDtos = new ArrayList<>();
        labels.forEach(label -> {
            labelDtos.add(LabelDto.builder()
                    .name(label.getName())
                    .id(label.getId())
                    .build());
         });

        DeskDto deskResponse = DeskDto.builder()
                .id(deck.getId())
                .name(deck.getName())
                .description(deck.getDescription())
                .cardNumber(deck.getCards().size())
                .isPublic(deck.getIsPublic())
                .labels(labelDtos)
                .createAt(deck.getCreateAt())
                .build();

        return ResponseObject.builder()
                    .status("success")
                    .message("Truy vấn thành công")
                    .data(deskResponse)
                    .build();
    }

    // tìm kiếm, lọc desk. card
    // cho lọc thẻ + sắp xếp


}
