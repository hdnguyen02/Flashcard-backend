package hdnguyen.controller;

import hdnguyen.requestbody.DeskRequestBody;
import hdnguyen.dto.ResponseObject;
import hdnguyen.requestbody.DeskUpdateBody;
import hdnguyen.service.DeskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("api/v1/desk")
public class DeskController {

    private final DeskService deskService;

    @PostMapping("add")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseObject addDesk(@RequestBody DeskRequestBody deskDto) throws Exception {
       return deskService.addDesk(deskDto);
    }

    // xóa bộ desk
    @DeleteMapping("delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseObject deleteDesk(@PathVariable Integer id) throws Exception {
        return deskService.deleteDesk(id); // xóa bộ thẻ.
    }

    // cập nhập dữ liệu.
    @PutMapping("update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseObject updateDesk(@PathVariable Integer id, @RequestBody DeskUpdateBody deskUpdateBody) throws  Exception {
        return deskService.updateDesk(id, deskUpdateBody);
    }


    @GetMapping("all")
    @ResponseStatus(HttpStatus.OK)
    public ResponseObject getAll(@RequestParam(required = false) Boolean isPublic,@RequestParam(required = false) String sortBy) {
        return deskService.getAll(isPublic, sortBy);
    }

    @GetMapping("detail/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseObject getDeskWithId(@PathVariable Integer id) throws Exception {
        return deskService.getDeskWithId(id);
    }



}
