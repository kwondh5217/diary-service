package com.diaryservice.webservice.repository;

import com.diaryservice.webservice.domain.event.Event;
import com.diaryservice.webservice.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<List<Post>> findAllByEvent(Event event);
}
