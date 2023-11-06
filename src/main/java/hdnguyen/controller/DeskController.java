package hdnguyen.controller;

import hdnguyen.requestbody.DeskRequestBody;
import hdnguyen.dto.ResponseObject;
import hdnguyen.requestbody.DeskUpdateBody;
import hdnguyen.service.DeskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

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
    @PutMapping("edit/{id}")
    @ResponseStatus(HttpStatus.OK)
                public ResponseObject updateDesk(@PathVariable Integer id, @RequestBody DeskUpdateBody deskUpdateBody) throws  Exception {
        return deskService.updateDesk(id, deskUpdateBody);
    }


    @GetMapping("all")
    @ResponseStatus(HttpStatus.OK)
    public ResponseObject getAll(@RequestParam(required = false, defaultValue = "name") String sortBy,
                                 @RequestParam(required = false, defaultValue = "asc") String orderBy,
                                 @RequestParam(required = false) String labels
                                )
    {

        String [] aliasLabels = labels.split(",");
        return deskService.getAll(aliasLabels, orderBy, sortBy);
    }

    @GetMapping("detail/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseObject getDeskWithId(@PathVariable Integer id) throws Exception {
        return deskService.getDeskWithId(id);
    }
}
