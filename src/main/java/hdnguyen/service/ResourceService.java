package hdnguyen.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ResourceService {
    private final ResourceLoader resourceLoader;

    public String getResourceDirectory() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:/");

        return resource.getURL().getPath();
    }
}
