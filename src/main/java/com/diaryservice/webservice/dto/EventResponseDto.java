package com.diaryservice.webservice.dto;

import com.diaryservice.webservice.domain.event.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Getter @Setter
public class EventResponseDto {

    private Long id;
    private Long userId;
    private String eventName;
    private Status status;
    private LocalDate activationDate;
    private LocalDate deactivationDate;
    private List<PostResponseDto> posts;


    @Builder
    public EventResponseDto(Long id, Long userId, String eventName, Status status, LocalDate activationDate, LocalDate deactivationDate, List<PostResponseDto> posts) {
        this.id = id;
        this.userId = userId;
        this.eventName = eventName;
        this.status = status;
        this.activationDate = activationDate;
        this.deactivationDate = deactivationDate;
        this.posts = posts;
    }
}
