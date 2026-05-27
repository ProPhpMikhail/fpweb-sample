package app.finplan.service;

import app.finplan.dto.notification.*;
import app.finplan.exception.BusinessException;
import app.finplan.exception.NotFoundException;
import app.finplan.exception.NotificationException;
import app.finplan.exception.UserException;
import app.finplan.mapper.NotificationMapper;
import app.finplan.model.*;
import app.finplan.repositories.NotificationRepository;
import app.finplan.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notRepo;
    private final NotificationMapper notMapper;
    private final UserRepository userRepo;

    public Page<NotificationDTO> list(int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit, Sort.by("createdAt").ascending());
        return notRepo.findAll(pageable).map(notMapper::map);
    }

    public Page<NotificationDTO> list(Long userId, int page, int limit, NotificationStatus status) {
        Pageable pageable = PageRequest.of(page, limit, Sort.by("createdAt").descending());
        if (status != null) {
            return notRepo.findByUserIdAndStatus(userId, status, pageable).map(notMapper::map);
        } else {
            return notRepo.findByUserId(userId, pageable).map(notMapper::map);
        }
    }

    @Transactional
    public NotificationDTO create(Long userId, NotificationCreateDTO dto) {
        User user = userRepo.findById(userId).orElseThrow(
                () -> new BusinessException(UserException.NOT_FOUND)
        );
        Notification not = new Notification();
        notMapper.create(dto, not);
        not.setStatus(NotificationStatus.NEW);
        not.setUser(user);
        not = notRepo.save(not);
        return notMapper.map(not);
    }

    public List<Long> markRead(Long userId) {
        List<Notification> nots = notRepo.findByUserIdAndStatus(userId, NotificationStatus.NEW);
        List<Long> ids = new ArrayList<>();
        nots.forEach((Notification not) -> {
            not.setStatus(NotificationStatus.READ);
            ids.add(not.getId());
            notRepo.save(not);
        });
        return ids;
    }

    @Transactional
    public NotificationDTO update(Long id, Long userId, NotificationUpdateDTO dto) {
        Notification not = notRepo.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new BusinessException(NotificationException.NOT_FOUND));
        notMapper.update(dto, not);
        not = notRepo.save(not);

        notRepo.save(not);

        return notMapper.map(not);
    }

    @Transactional
    public Long delete(Long userId, Long id) {
        Notification not = notRepo.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new BusinessException(NotificationException.NOT_FOUND));
        notRepo.delete(not);
        return id;
    }
}
