package hdnguyen.service;

import hdnguyen.common.TypeFile;
import hdnguyen.exception.StorageException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
public class StorageService {

    public String save( MultipartFile file, TypeFile typeFile) throws StorageException {
        if (file == null) return null;
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddmmHH");
        String formattedDateTime = now.format(formatter);
        String fileName =  formattedDateTime + file.getOriginalFilename();
        String pathSave = System.getProperty("user.dir") + "/src/main/resources/static/card/" + typeFile.name();
        try {
            file.transferTo(new File(pathSave + "/" + fileName));
            return fileName;
        } catch (Exception e) {
            throw new StorageException("Lưu file (" + typeFile.name() +  ") không thành công");
        }
    }
}