package hdnguyen.service;

import hdnguyen.common.Helper;
import hdnguyen.dao.DeskDao;
import hdnguyen.requestbody.DeskRequestBody;
import hdnguyen.dto.DeskDto;
import hdnguyen.dto.ResponseObject;
import hdnguyen.dto.auth.LabelDto;
import hdnguyen.entity.Desk;
import hdnguyen.entity.Label;
import hdnguyen.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class DeskService {

    private final DeskDao deskDao;
    private final Helper helper;

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

    public ResponseObject getAll(Boolean isPublic, String sortBy) {

        User user = helper.getCurentUser();
        List<Desk> desks = user.getDesks();
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

    public ResponseObject getDeskWithId(Integer id) throws Exception {

        Optional<Desk> optionalDesk = deskDao.findById(id);
        if (optionalDesk.isEmpty()) throw new Exception("Không tìm thấy bộ thẻ!");
        User user = helper.getCurentUser();
        Desk desk = optionalDesk.get();
        if (!desk.getUser().getEmail().equals(user.getEmail())) throw new Exception("Unauthorized!");

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
}
