package com.diaryservice.webservice.domain.user;


import com.diaryservice.webservice.domain.event.Event;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true)
    private String email;

    @Column
    private String picture;

    @Enumerated(EnumType.STRING)
    @Column
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Event> events;




    @Builder
    public User(Long id, String name, String email, String picture, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
    }

    public User update(String name, String picture) {
        this.name = name;
        this.picture = picture;

        return this;
    }
    public String getRoleKey() {
        return this.role.getKey();
    }

}
