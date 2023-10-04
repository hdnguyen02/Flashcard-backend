package hdnguyen.service;

import hdnguyen.common.Helper;
import hdnguyen.common.TypeFile;
import hdnguyen.dao.CardDao;
import hdnguyen.dao.DeskDao;
import hdnguyen.dto.CardDto;
import hdnguyen.dto.ResponseObject;
import hdnguyen.entity.Card;
import hdnguyen.entity.Desk;
import hdnguyen.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardDao cardDao;
    private final DeskDao deskDao;
    private final Helper helper;

    public ResponseObject addCard( String term,String definition,String image,String audio,String extractInfo, Integer idDeskAddCard) throws Exception {
        User user = helper.getCurentUser();
        Optional<Desk> deskAddCard = deskDao.findById(idDeskAddCard);
        if (deskAddCard.isEmpty()) throw new Exception("Không tồn tại bộ thẻ này!");
        if (!deskAddCard.get().getUser().getEmail().equals(user.getEmail())) {
            throw new Exception("Unauthorized!");
        }
        Card addCard = Card.builder()
                .term(term).definition(definition)
                .image(image).audio(audio).extractInfo(extractInfo)
                .desk(deskAddCard.get())
                .createAt(new Date(System.currentTimeMillis()))
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

    public ResponseObject getCardWithIdDesk (Integer idDesk, HttpServletRequest request) throws Exception {
        String urlRoot = helper.getUrlRoot(request);
        User user = helper.getCurentUser();
        Optional<Desk> optionalDesk = deskDao.findById(idDesk);
        if (optionalDesk.isEmpty()) throw new Exception("Desk not found!");
        Desk desk = optionalDesk.get();
        final String emailUser = user.getEmail();
        if (!emailUser.equals(desk.getUser().getEmail())) throw new Exception("Unauthorized!");


        List<Card> cards = desk.getCards();
        List<CardDto>  cardDtos = new ArrayList<>();
        cards.forEach(card -> {
            String urlImage = card.getImage();
            if (urlImage != null){
                 urlImage = urlRoot + "/card/"+ TypeFile.image + "/" + urlImage;
            }

            String urlAudio = card.getAudio();
            if (urlAudio != null){
                urlAudio = urlRoot + "/card/"+ TypeFile.audio + "/" + urlAudio;
            }

            cardDtos.add(CardDto.builder()
                            .id(card.getId())
                            .term(card.getTerm())
                            .definition(card.getDefinition())
                            .image(urlImage)
                            .audio(urlAudio)
                    .build());
        });

        return ResponseObject.builder()
                .status("success")
                .message("Truy vấn thành công")
                .data(cardDtos)
                .build();
    }
}
