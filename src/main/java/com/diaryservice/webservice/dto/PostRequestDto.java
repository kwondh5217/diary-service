package com.diaryservice.webservice.dto;

import com.diaryservice.webservice.domain.event.Event;
import com.diaryservice.webservice.domain.post.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class PostRequestDto {

    private Event event;

    private String title;

    private String content;

    private String author;
    private String mediaName;


    @Builder
    public PostRequestDto(Event event, String title, String content, String author, String mediaName) {
        this.event = event;
        this.title = title;
        this.content = content;
        this.author = author;
        this.mediaName = mediaName;
    }


    public Post toEntity(){
        return Post.builder()
                .event(event)
                .title(title)
                .content(content)
                .author(author)
                .mediaName(mediaName)
                .build();
    }

}
