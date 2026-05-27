package app.finplan.service;

import app.finplan.dto.personal.*;
import app.finplan.exception.UserException;
import app.finplan.exception.BusinessException;
import app.finplan.mapper.UserMapper;
import app.finplan.model.User;
import app.finplan.model.UserRole;
import app.finplan.repositories.UserRepository;
import app.finplan.security.CustomUserDetailsService;
import app.finplan.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonalService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final MailService mailService;
    private final UserMapper userMapper;

    @Value("${app.base-url}")
    private String baseUrl;

    public void register(RegisterRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.email());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getRole() == UserRole.UNCONFIRMED) {
                throw new BusinessException(UserException.EMAIL_NOT_CONFIRMED);
            } else {
                throw new BusinessException(UserException.EMAIL_EXISTS);
            }
        }

        User user = new User();
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(UserRole.UNCONFIRMED);
        String code = generateCode();
        user.setCode(code);
        userRepository.save(user);

        mailService.sendConfirmationCode(user.getEmail(), code);
    }

    public void confirmEmail(ConfirmEmailRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BusinessException(UserException.NOT_FOUND));

        if (user.getCode() == null || !user.getCode().equals(request.code())) {
            throw new BusinessException(UserException.INVALID_CONFIRMATION_CODE);
        }

        user.setCode(null);
        user.setRole(UserRole.USER);
        userRepository.save(user);
    }

    public void sendResetLinkEmail(RecoverEmailRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BusinessException(UserException.NOT_FOUND));

        String rawToken = TokenUtil.generateRawToken();
        String tokenHash = TokenUtil.sha256Hex(rawToken);
        user.setRecoverTokenHash(tokenHash);
        userRepository.save(user);

        String link = baseUrl + "/recover/" + rawToken;
        mailService.sendResetLinkEmail(request.email(), link);
    }

    public Boolean validResetToken(String token) {
        String tokenHash = TokenUtil.sha256Hex(token);
        Optional<User> user = userRepository.findByRecoverTokenHash(tokenHash);
        return user.isPresent();
    }

    public void resetPassword(String token, ResetPasswordRequest request) {
        String tokenHash = TokenUtil.sha256Hex(token);
        User user = userRepository.findByRecoverTokenHash(tokenHash)
                .orElseThrow(() -> new BusinessException(UserException.NOT_FOUND));

        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRecoverTokenHash("");
        userRepository.save(user);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email()).orElseThrow(() ->
            new BusinessException(UserException.NOT_FOUND)
        );
        if (user.getRole() == UserRole.UNCONFIRMED) {
            throw new BusinessException(UserException.EMAIL_NOT_CONFIRMED);
        }

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(request.email());

        if (!passwordEncoder.matches(request.password(), userDetails.getPassword())) {
            throw new BusinessException(UserException.INVALID_CREDENTIALS);
        }

        user.setRecoverTokenHash("");
        userRepository.save(user);

        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(
                token,
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhone(),
                user.getRole()
        );
    }

    public void changePassword(User user, ChangePasswordRequest request) {
        if (user.getRole() == UserRole.UNCONFIRMED) {
            throw new BusinessException(UserException.EMAIL_NOT_CONFIRMED);
        }

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(user.getEmail());

        if (user.getPassword() != null && !passwordEncoder.matches(request.password(), userDetails.getPassword())) {
            throw new BusinessException(UserException.INVALID_CREDENTIALS);
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }

    public UserInfo setUserInfo(User user, UserInfoCreate dto) {
        userMapper.create(dto, user);
        userRepository.save(user);
        return userMapper.map(user);
    }

    public UserInfo getUserInfo(User user) {
        return userMapper.map(user);
    }

    public void resendCode(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(UserException.NOT_FOUND));

        if (user.getRole() != UserRole.UNCONFIRMED) {
            throw new BusinessException(UserException.EMAIL_ALREADY_CONFIRMED);
        }

        String code = generateCode();
        user.setCode(code);
        userRepository.save(user);

        mailService.sendConfirmationCode(user.getEmail(), code);
    }

    private String generateCode() {
        int code = (int) (Math.random() * 90_00) + 10_00;
        return String.valueOf(code);
    }
}
