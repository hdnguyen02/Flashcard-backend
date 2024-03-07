package hdnguyen.service;

import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.StorageClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FirebaseStorageService {

    private final FirebaseApp firebaseApp;

    public String save(String folder, MultipartFile file) throws IOException {
        Storage storage = StorageClient.getInstance(firebaseApp).bucket().getStorage();
        String fileName =  System.currentTimeMillis() + "-" + file.getOriginalFilename();
        Bucket bucket = storage.get("flashcard-d55bd.appspot.com");
        bucket.create(folder + "/" + fileName, file.getInputStream(), file.getContentType());
        String postUrl = "https://firebasestorage.googleapis.com/v0/b/" + bucket.getName() + "/o/";
        return postUrl + folder + "%2F" + fileName + "?alt=media";
    }
}
