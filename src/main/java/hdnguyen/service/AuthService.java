package hdnguyen.service;

import hdnguyen.dao.UserDao;
import hdnguyen.dto.ResponseObject;
import hdnguyen.dto.auth.AuthenticateRequest;
import hdnguyen.dto.auth.AuthResponse;
import hdnguyen.dto.auth.RegisterRequest;
import hdnguyen.entity.Role;
import hdnguyen.entity.RoleEnum;
import hdnguyen.entity.User;
import hdnguyen.exception.RegisterException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserDao userDao;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;


    public ResponseObject register(RegisterRequest register) throws Exception {
        Set<Role> roles = new HashSet<>();
        roles.add(Role.builder().name(RoleEnum.STUDENT.toString()).build());
        Optional<User> checkUser =  userDao.findById(register.getEmail());
        if (checkUser.isPresent()) {
            Map<String, String> listError = new HashMap<>();
            listError.put("email", "Email đã được sử dụng");
            throw new RegisterException("Đăng ký tài khoản thất bại!",listError);
        }

        var user = User.builder()
                .email(register.getEmail())
                .password(passwordEncoder.encode(register.getPassword()))
                .name(register.getName())
                .birthdate(register.getBirthdate())
                .createAt(LocalDateTime.now())
                .isEnabled(true)
                .roles(roles)
                .build();

        userDao.save(user);
        String token = jwtService.generateToken(user);
        AuthResponse authResponse = AuthResponse.builder()
                .token(token)
                .build();

        return ResponseObject.builder()
                .status("success")
                .message("Tạo tài khoản thành công")
                .data(authResponse)
                .build();
    }


    public ResponseObject authenticate(AuthenticateRequest authRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authRequest.getEmail(), authRequest.getPassword()
        ));

        var user = userDao.findById(authRequest.getEmail()).orElseThrow();
        String token = jwtService.generateToken(user);
        AuthResponse authResponse = AuthResponse.builder()
                .token(token)
                .build();

        return ResponseObject.builder()
                .message("Đăng nhập thành công")
                .status("success")
                .data(authResponse)
                .build();
    }
}
