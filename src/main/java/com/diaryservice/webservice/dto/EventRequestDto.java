package com.diaryservice.webservice.dto;

import com.diaryservice.webservice.domain.event.Event;
import com.diaryservice.webservice.domain.user.User;
import com.diaryservice.webservice.domain.event.Status;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@NoArgsConstructor
@Getter @Setter
public class EventRequestDto {

    private User user;

    private Status status;
    private String eventName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate activationDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate deActivationDate;

    @Builder
    public EventRequestDto(User user, Status status, String eventName, LocalDate activationDate, LocalDate deActivationDate) {
        this.user = user;
        this.status = status;
        this.eventName = eventName;
        this.activationDate = activationDate;
        this.deActivationDate = deActivationDate;
    }

    public void setUser(User user){
        this.user = user;
    }

    public Event toEntity(){

        return Event.builder()
                .user(user)
                .status(status)
                .eventName(eventName)
                .activationDate(activationDate)
                .deactivationDate(deActivationDate)
                .build();
    }
}
