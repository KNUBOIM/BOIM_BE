package BOIM_Project.boim.service;

import BOIM_Project.boim.auth.security.JwtTokenProvider;
import BOIM_Project.boim.dto.TokenDto;
import BOIM_Project.boim.dto.request.LoginRequest;
import BOIM_Project.boim.dto.request.RefreshTokenRequest;
import BOIM_Project.boim.dto.request.SignUpRequest;
import BOIM_Project.boim.entity.RefreshToken;
import BOIM_Project.boim.entity.User;
import BOIM_Project.boim.repository.RefreshTokenRepository;
import BOIM_Project.boim.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.time.Instant;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository,
        PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public void signUp(SignUpRequest request) {
        if (userRepository.existsByUserAccount(request.userAccount())) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다."); // ->  message constants로 변경
        }

        User user = request.toUserEntity();
        userRepository.save(user);
    }

    public TokenDto login(LoginRequest request) {
        User user = userRepository.findByUserAccount(request.userAccount())
            .orElseThrow(() -> new IllegalArgumentException("아이디 혹은 비밀번호가 틀림"));
        if (!passwordEncoder.matches(request.userPassword(), user.getUserPassword())) {
            throw new IllegalArgumentException("아이디 혹은 비밀번호가 틀림");
        }

        String access = jwtTokenProvider.createAccessToken(user.getUserAccount(), user.getUserRole());
        String refresh = jwtTokenProvider.createRefreshToken(user.getUserAccount());

        refreshTokenRepository.deleteByUser(user);
        RefreshToken entity = RefreshToken.createEntity(user, refresh,
            Instant.now().plusMillis(jwtTokenProvider.getRefreshExpMs()));
        refreshTokenRepository.save(entity);

        return TokenDto.create(access, refresh);
    }

    public TokenDto refresh(RefreshTokenRequest request) {
        var claims = jwtTokenProvider.validateToken(request.refreshToken());
        String userAccount = claims.getBody().getSubject();

        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.refreshToken())
            .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 리프레시 토큰"));
        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new IllegalArgumentException("리프레시 토큰이 만료되었음");
        }

        String newAccess = jwtTokenProvider.createAccessToken(userAccount,
            refreshToken.getUser().getUserRole());
        String newRefresh = jwtTokenProvider.createRefreshToken(userAccount);

        refreshToken.setToken(newRefresh);
        refreshToken.setExpiryDate(Instant.now().plusMillis(jwtTokenProvider.getRefreshExpMs()));
        refreshTokenRepository.save(refreshToken);
        
        return TokenDto.create(newAccess, newRefresh);
    }
}
