package com.diaryservice.webservice.domain.post;

import com.diaryservice.webservice.domain.BaseTimeEntity;
import com.diaryservice.webservice.domain.event.Event;
import com.diaryservice.webservice.dto.PostResponseDto;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@Entity
public class Post extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "EVENT_ID")
    private Event event;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false)
    private String author;

    private String mediaName;

    @Builder
    public Post(Event event, String title, String content, String author, String mediaName) {
        this.event = event;
        this.title = title;
        this.content = content;
        this.author = author;
        this.mediaName = mediaName;
    }

    public PostResponseDto toDto(){
        return PostResponseDto.builder()
                .id(id)
                .eventId(event.getId())
                .title(title)
                .content(content)
                .author(author)
                .mediaName(mediaName)
                .build();
    }
}
