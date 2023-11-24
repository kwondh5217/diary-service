package com.diaryservice.webservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class PostResponseDto {

    private Long id;
    private Long eventId;
    private String title;
    private String content;
    private String author;
    private String mediaName;

    @Builder
    public PostResponseDto(Long id, Long eventId, String title, String content, String author, String mediaName) {
        this.id = id;
        this.eventId = eventId;
        this.title = title;
        this.content = content;
        this.author = author;
        this.mediaName = mediaName;
    }
}
