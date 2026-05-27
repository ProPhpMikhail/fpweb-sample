package app.finplan.controller.v1;

import app.finplan.dto.notification.NotificationCreateDTO;
import app.finplan.dto.notification.NotificationDTO;
import app.finplan.exception.*;
import app.finplan.handler.ApiResponse;
import app.finplan.model.NotificationStatus;
import app.finplan.model.User;
import app.finplan.repositories.NotificationRepository;
import app.finplan.repositories.UserRepository;
import app.finplan.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notService;

    @Autowired
    private NotificationRepository notRepo;

    @Autowired
    private UserRepository userRepo;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<ApiResponse<Page<NotificationDTO>>> list(
            @RequestParam(defaultValue="1") int page,
            @RequestParam(defaultValue="20") int size,
            @RequestParam(required = false) NotificationStatus status,
            Principal principal
    ) {
        String userEmail = principal.getName();
        User user = userRepo.findByEmail(userEmail).orElseThrow(
                () -> new BusinessException(UserException.NOT_FOUND)
        );
        Page<NotificationDTO> list = notService.list(user.getId(), page-1, size, status);
        return ResponseEntity.ok(ApiResponse.ok(list));
    }

    @PostMapping("/add/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<ApiResponse<NotificationDTO>> create(
            @PathVariable Long userId,
            @Valid @RequestBody NotificationCreateDTO dto,
            Principal principal
    ) {
        User user = userRepo.findById(userId).orElseThrow(
                () -> new BusinessException(UserException.NOT_FOUND)
        );
        return ResponseEntity.ok(ApiResponse.ok(notService.create(user.getId(), dto)));
    }

    @PostMapping("/markRead")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<ApiResponse<List<Long>>> markRead(
            Principal principal
    ) {
        String userEmail = principal.getName();
        User user = userRepo.findByEmail(userEmail).orElseThrow(
                () -> new BusinessException(UserException.NOT_FOUND)
        );
        return ResponseEntity.ok(ApiResponse.ok(notService.markRead(user.getId())));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse<Long>> delete(
            @PathVariable Long id
    ) {
        Long userId = notRepo.findById(id).orElseThrow(
                () -> new BusinessException(NotificationException.NOT_FOUND)
        ).getUser().getId();
        return ResponseEntity.ok(ApiResponse.ok(notService.delete(userId, id)));
    }
}
