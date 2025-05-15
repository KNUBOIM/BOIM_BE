package BOIM_Project.boim.controller;

import BOIM_Project.boim.dto.TokenDto;
import BOIM_Project.boim.dto.request.LoginRequest;
import BOIM_Project.boim.dto.request.RefreshTokenRequest;
import BOIM_Project.boim.dto.request.SignUpRequest;
import BOIM_Project.boim.service.AuthService;
import org.antlr.v4.runtime.Token;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody SignUpRequest request) {
        authService.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginRequest request) {
        TokenDto tokenDto = authService.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(tokenDto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenDto> refresh(@RequestBody RefreshTokenRequest request) {
        TokenDto tokenDto = authService.refresh(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(tokenDto);
    }
}