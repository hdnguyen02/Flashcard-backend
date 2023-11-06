package hdnguyen.service;

import hdnguyen.common.Helper;
import hdnguyen.dao.DeskDao;
import hdnguyen.entity.Card;
import hdnguyen.requestbody.DeskRequestBody;
import hdnguyen.dto.DeskDto;
import hdnguyen.dto.ResponseObject;
import hdnguyen.dto.auth.LabelDto;
import hdnguyen.entity.Desk;
import hdnguyen.entity.Label;
import hdnguyen.entity.User;
import hdnguyen.requestbody.DeskUpdateBody;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class DeskService {

    private final DeskDao deskDao;
    private final Helper helper;
    @PersistenceContext
    private EntityManager entityManager;

    public ResponseObject addDesk(DeskRequestBody deskDto) throws Exception {
        List<Label> labels = new ArrayList<>();
        deskDto.getIdLabels().forEach(idLabel -> {
            Label label = Label.builder()
                    .id(idLabel)
                    .build();
            labels.add(label);
        });

        User user = helper.getCurentUser();
        Desk addDesk = Desk.builder()
                .name(deskDto.getName())
                .description(deskDto.getDescription())
                .isPublic(deskDto.getIsPublic())
                .user(user)
                .createAt(new Date(System.currentTimeMillis()))
                .labels(labels)
                .studyCardNumber(15)
                .reviewCardNumber(300)
                .learnedCardNumber(0)
                .reviewedCardNumber(0)
                .lastDate(null)
                .build();
        Set<String> userDeskName = new HashSet<>();
        user.getDesks().forEach(userDesk -> {
            userDeskName.add(userDesk.getName());
        });
        if (userDeskName.contains(addDesk.getName())) throw new Exception("Tên bộ thẻ đã tồn tại!");
        try {
            Desk deskAddSuccess = deskDao.save(addDesk);
            deskDto.setId(deskAddSuccess.getId());
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

    public ResponseObject getAll(String [] aliasLabels, String orderBy,String sortBy) {

        // trước tiên kiểm tra xem giá trị đưa lên
        // tiếp tục lọc ra xem.
        String strQuery = null;
        TypedQuery<Desk> queryDesk;
        String email = helper.getCurentUser().getEmail(); // lấy ra email hiện tại sau đó check xem.
        if (aliasLabels != null) {
            strQuery = "SELECT d from Desk d JOIN d.labels l WHERE d.user.email=:email AND l.alias IN :aliasLabels ORDER BY d." + orderBy + " " + sortBy;
            queryDesk = entityManager.createQuery(strQuery, Desk.class);
            queryDesk.setParameter("aliasLabels", Arrays.asList(aliasLabels));
            queryDesk.setParameter("email", email);
        }
        else { // không lọc theo labels
            strQuery = "SELECT d from Desk d d.user.email=:email ORDER BY d." + orderBy + " " + sortBy;
            queryDesk = entityManager.createQuery(strQuery, Desk.class);
            queryDesk.setParameter("email", email);
        }

        List<Desk> desks =queryDesk.getResultList();
        List<DeskDto> deskResponses = new ArrayList<>();
        desks.forEach(desk -> {
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

    public ResponseObject deleteDesk(int id) throws Exception {
        Desk desk = this.getDeskWithOfUser(id);
        try {
            deskDao.delete(desk); // xóa desk.
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


    private Desk getDeskWithOfUser(int id) throws Exception{
        Optional<Desk> optionalDesk = deskDao.findById(id);
        if (optionalDesk.isEmpty()) throw new Exception("Không tìm thấy bộ thẻ!");
        User user = helper.getCurentUser();
        Desk desk = optionalDesk.get();
        if (!desk.getUser().getEmail().equals(user.getEmail())) throw new Exception("Unauthorized!");
        return desk;
    }

    // update
    public ResponseObject updateDesk(int id, DeskUpdateBody deskUpdateBody) throws Exception {
        Desk desk = this.getDeskWithOfUser(id);
        desk.setName(deskUpdateBody.getName());
        desk.setDescription(deskUpdateBody.getDescription());
        desk.setIsPublic(deskUpdateBody.getIsPublic());
        List<Label> labels = new ArrayList<>();
        deskUpdateBody.getIdLabels().forEach(idLabel -> {
            labels.add(Label.builder().id(idLabel).build());
        });
        desk.setLabels(labels);
        try {
            Desk deskUpdate =  deskDao.save(desk);

            List<Label> labelsUpdate= deskUpdate.getLabels();
            List<LabelDto> labelsDto = new ArrayList<>();
            labelsUpdate.forEach(labelUpdate -> {
                labelsDto.add(LabelDto.builder().id(labelUpdate.getId()).name(labelUpdate.getName()).build());
            });

            DeskDto deskDto = DeskDto.builder()
                    .id(deskUpdate.getId())
                    .name(deskUpdate.getName())
                    .isPublic(deskUpdate.getIsPublic())
                    .description(deskUpdate.getDescription())
                    .createAt(deskUpdate.getCreateAt())
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

    public ResponseObject getDeskWithId(int id) throws Exception {

        Desk desk = this.getDeskWithOfUser(id);
        List<Label> labels = desk.getLabels();
        List<LabelDto> labelDtos = new ArrayList<>();
        labels.forEach(label -> {
            labelDtos.add(LabelDto.builder()
                    .name(label.getName())
                    .id(label.getId())
                    .build());
         });

        DeskDto deskResponse = DeskDto.builder()
                .id(desk.getId())
                .name(desk.getName())
                .description(desk.getDescription())
                .cardNumber(desk.getCards().size())
                .isPublic(desk.getIsPublic())
                .labels(labelDtos)
                .createAt(desk.getCreateAt())
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
