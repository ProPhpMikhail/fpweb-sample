package app.finplan.controller.v1;

import app.finplan.dto.personal.*;
import app.finplan.exception.BusinessException;
import app.finplan.exception.NotFoundException;
import app.finplan.exception.UserException;
import app.finplan.handler.ApiResponse;
import app.finplan.model.User;
import app.finplan.repositories.UserRepository;
import app.finplan.service.PersonalService;
import app.finplan.service.MailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/personal")
@RequiredArgsConstructor
public class PersonalController {

    @Autowired
    private final PersonalService personalService;

    @Autowired
    MailService mailService;

    @Autowired
    UserRepository userRepo;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody RegisterRequest request) {
        personalService.register(request);
    }

    @PostMapping("/confirm")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void confirm(@RequestBody ConfirmEmailRequest request) {
        personalService.confirmEmail(request);
    }

    @PostMapping("/recover")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void recover(@RequestBody RecoverEmailRequest request) {
        personalService.sendResetLinkEmail(request);
    }

    @GetMapping("/recover/{token}")
    public ResponseEntity<ApiResponse<Boolean>> validToken(@PathVariable String token) {
        return ResponseEntity.ok(ApiResponse.ok(personalService.validResetToken(token)));
    }

    @PostMapping("/recover/{token}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void recoverChangePassword(
            @PathVariable String token,
            @RequestBody ResetPasswordRequest request
    ) {
        personalService.resetPassword(token, request);
    }

    @PostMapping("/resend")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resend(@RequestBody ResendCodeRequest request) {
        personalService.resendCode(request.email());
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(personalService.login(request)));
    }

    @GetMapping("/isNonePassword")
    public ResponseEntity<ApiResponse<Boolean>> isNonePassword(
            Principal principal
    ) {
        String userEmail = principal.getName();
        User user = userRepo.findByEmail(userEmail).orElseThrow(
                () -> new BusinessException(UserException.NOT_FOUND)
        );
        return ResponseEntity.ok(ApiResponse.ok(user.getPassword() == null || user.getPassword().isEmpty()));
    }

    @PostMapping("/changePassword")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            Principal principal
    ) {
        String userEmail = principal.getName();
        User user = userRepo.findByEmail(userEmail).orElseThrow(
                () -> new BusinessException(UserException.NOT_FOUND)
        );
        personalService.changePassword(user, request);
    }

    @GetMapping("/userinfo")
    public ResponseEntity<ApiResponse<UserInfo>> getUserInfo(
            Principal principal
    ) {
        String userEmail = principal.getName();
        User user = userRepo.findByEmail(userEmail).orElseThrow(
                () -> new BusinessException(UserException.NOT_FOUND)
        );
        return ResponseEntity.ok(ApiResponse.ok(personalService.getUserInfo(user)));
    }

    @PatchMapping("/userinfo")
    public ResponseEntity<ApiResponse<UserInfo>> setUserInfo(
            @Valid @RequestBody UserInfoCreate request,
            Principal principal
    ) {
        String userEmail = principal.getName();
        User user = userRepo.findByEmail(userEmail).orElseThrow(
                () -> new BusinessException(UserException.NOT_FOUND)
        );
        return ResponseEntity.ok(ApiResponse.ok(personalService.setUserInfo(user, request)));
    }

    @GetMapping("/valid")
    public ResponseEntity<ApiResponse<Boolean>> valid(Principal principal) {
        String userEmail = principal.getName();
        Optional<User> user = userRepo.findByEmail(userEmail);
        return ResponseEntity.ok(ApiResponse.ok(user.isPresent()));
    }
}

