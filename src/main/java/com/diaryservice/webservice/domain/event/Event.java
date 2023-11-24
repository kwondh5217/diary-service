package com.diaryservice.webservice.domain.event;

import com.diaryservice.webservice.domain.BaseTimeEntity;
import com.diaryservice.webservice.domain.post.Post;
import com.diaryservice.webservice.domain.user.User;
import com.diaryservice.webservice.dto.EventResponseDto;
import com.diaryservice.webservice.dto.PostResponseDto;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@Entity
public class Event extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String eventName;

    private LocalDate activationDate;
    private LocalDate deactivationDate;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<Post> posts;

    @ManyToMany
    @JoinTable(
            name = "event_invited_users",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"),
            uniqueConstraints = {@UniqueConstraint(columnNames = {"event_id", "user_id"})}
    )
    private List<User> invitedUsers;


    @Builder
    public Event(User user, Status status, String eventName, LocalDate activationDate, LocalDate deactivationDate) {
        this.user = user;
        this.status = status;
        this.eventName = eventName;
        this.activationDate = activationDate;
        this.deactivationDate = deactivationDate;
    }

    public void addInvitedUser(User user){
        if(invitedUsers == null){
            invitedUsers = new ArrayList<>();
        }
        invitedUsers.add(user);
    }

    public boolean isInvitedUser(User user){
        return invitedUsers.stream().anyMatch(invited -> invited.getId().equals(user.getId()));
    }

    public EventResponseDto toDto(){

        List<PostResponseDto> postResponseDtos = posts.stream()
                .map(Post::toDto)
                .collect(Collectors.toList());


        return EventResponseDto.builder()
                .id(id)
                .userId(user.getId())
                .status(status)
                .eventName(eventName)
                .activationDate(activationDate)
                .deactivationDate(deactivationDate)
                .posts(postResponseDtos)
                .build();
    }

}