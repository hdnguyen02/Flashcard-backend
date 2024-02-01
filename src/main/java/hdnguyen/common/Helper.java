package hdnguyen.common;


import hdnguyen.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;

@Component
public class Helper {
    public String getUrlRoot(HttpServletRequest request) {
        return ServletUriComponentsBuilder.fromRequestUri(request)
                .replacePath(null)
                .build()
                .toString();
    }
    public User getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User)authentication.getPrincipal();
    }
    public String getEMail() {
        User user = this.getUser();
        return user.getEmail();
    }
    public String generateUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

}
