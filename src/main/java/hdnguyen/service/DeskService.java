package hdnguyen.service;

import hdnguyen.dao.DeskDao;
import hdnguyen.dto.DeskDto;
import hdnguyen.dto.DeskResponse;
import hdnguyen.dto.ResponseObject;
import hdnguyen.dto.auth.LabelDto;
import hdnguyen.entity.Desk;
import hdnguyen.entity.Label;
import hdnguyen.entity.User;
import hdnguyen.exception.AddException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class DeskService {

    private final DeskDao deskDao;

    public ResponseObject addDesk(DeskDto deskDto) throws AddException {
        List<Label> labels = new ArrayList<>();
        deskDto.getIdLabels().forEach(idLabel -> {
            Label label = Label.builder()
                    .id(idLabel)
                    .build();
            labels.add(label);
        });

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User)authentication.getPrincipal();
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
        if (userDeskName.contains(addDesk.getName())) throw new AddException("Tên desk đã tồn tại");
        try {
            Desk deskAddSuccess = deskDao.save(addDesk);
            deskDto.setId(deskAddSuccess.getId());
        }
        catch (Exception e) {
            throw new AddException(e.getMessage());
        }
        return ResponseObject.builder()
                .status("success")
                .data(deskDto)
                .message("Thêm thành công!")
                .build();
    }

    public ResponseObject getAll(Boolean isPublic, String sortBy) {




        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User)authentication.getPrincipal();
        List<Desk> desks = user.getDesks();
        List<DeskResponse> deskResponses = new ArrayList<>();
        desks.forEach(desk -> {
            List<LabelDto> labelDtos = new ArrayList<>();
            desk.getLabels().forEach(label -> {
                labelDtos.add(new LabelDto(label.getId(), label.getName()));
            });
            deskResponses.add(
                    DeskResponse.builder()
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
                .message("All desk")
                .data(deskResponses)
                .build();
    }

}
