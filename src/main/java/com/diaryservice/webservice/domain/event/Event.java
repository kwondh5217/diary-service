package com.diaryservice.webservice.domain.event;

import com.diaryservice.webservice.domain.BaseTimeEntity;
import com.diaryservice.webservice.domain.post.Post;
import com.diaryservice.webservice.domain.user.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
            inverseJoinColumns = @JoinColumn(name = "user_id")
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
}