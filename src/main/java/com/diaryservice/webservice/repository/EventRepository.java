package com.diaryservice.webservice.repository;

import com.diaryservice.webservice.domain.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    Optional<List<Event>> findAllByUserId(Long userId);

    Optional<List<Event>> findByInvitedUsersId(Long userId);
}
