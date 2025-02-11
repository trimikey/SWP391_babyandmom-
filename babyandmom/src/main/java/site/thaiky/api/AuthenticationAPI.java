package site.thaiky.api;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.thaiky.entity.User;
import site.thaiky.service.AuthenticationService;

@RestController
@RequestMapping("/api/authentication")
public class AuthenticationAPI {

    @Autowired
    AuthenticationService authenticationService;
    @PostMapping("/register")
    public ResponseEntity register(@Valid @RequestBody User user) {
        User newUser = authenticationService.register(user);
        return ResponseEntity.ok(newUser);
    }
}
