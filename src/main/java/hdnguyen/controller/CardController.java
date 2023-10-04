package hdnguyen.controller;

import hdnguyen.common.TypeFile;
import hdnguyen.dto.ResponseObject;
import hdnguyen.exception.AddException;
import hdnguyen.exception.ForbiddenException;
import hdnguyen.exception.StorageException;
import hdnguyen.service.CardService;
import hdnguyen.service.StorageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/api/v1/card")
public class CardController {
    private final StorageService storageService;
    private final CardService cardService;

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseObject addCard(
            @RequestParam(name="term") String term, @RequestParam(name="definition") String definition,
            @RequestParam(name="extractInfo",required = false) String extractInfo,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "audio", required = false) MultipartFile audio,
            @RequestParam("idDeskAddCard") Integer idDeskAddCard
            ) throws StorageException, ForbiddenException, AddException {

        String imageName = storageService.save(image, TypeFile.image);
        String audioName = storageService.save(audio, TypeFile.audio);

        return cardService.addCard(term, definition, imageName, audioName, extractInfo, idDeskAddCard);
    }

    @GetMapping("/desk/{idDesk}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseObject getCardWithIdDesk(@PathVariable Integer idDesk, HttpServletRequest request) throws Exception {
        return cardService.getCardWithIdDesk(idDesk, request);
    }
}
