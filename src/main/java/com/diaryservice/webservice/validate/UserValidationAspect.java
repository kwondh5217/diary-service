package com.diaryservice.webservice.validate;

import com.diaryservice.webservice.config.auth.dto.SessionUser;
import com.diaryservice.webservice.domain.event.Event;
import com.diaryservice.webservice.service.EventService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Aspect
@Component
public class UserValidationAspect {

    private final EventService eventService;

    @Before("@annotation(validateUserAccess)")
    public void validateUserAccess(JoinPoint joinPoint, ValidateUserAccess validateUserAccess) {
        Object[] args = joinPoint.getArgs();

        if (args.length < 2) {
            throw new IllegalArgumentException("인자의 개수가 부족합니다.");
        }

        Long eventId = (Long) args[0];
        SessionUser sessionUser = (SessionUser) args[1];

        if (eventId == null || sessionUser == null || sessionUser.getId() == null) {
            throw new IllegalArgumentException("잘못된 인자 값입니다.");
        }

        Event eventById = eventService.findEventById(eventId);
        if (eventById == null) {
            throw new IllegalArgumentException("해당 이벤트를 찾을 수 없습니다.");
        }

        boolean isValid = eventById.getInvitedUsers().stream()
                .anyMatch(user -> user.getId().equals(sessionUser.getId()))
                || eventById.getUser().getId().equals(sessionUser.getId());

        if (!isValid) {
            throw new IllegalArgumentException("접근 권한이 없는 유저입니다.");
        }
    }

}
