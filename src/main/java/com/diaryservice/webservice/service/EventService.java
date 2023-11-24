package com.diaryservice.webservice.service;

import com.diaryservice.webservice.domain.event.Event;
import com.diaryservice.webservice.domain.event.Status;
import com.diaryservice.webservice.domain.user.User;
import com.diaryservice.webservice.dto.PostResponseDto;
import com.diaryservice.webservice.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class EventService {

    private final EventRepository eventRepository;
    private final PostService postService;

    @Transactional
    public Long save(Event event){
        LocalDate currentDate = LocalDate.now();
        if (currentDate.isEqual(event.getActivationDate()) || (currentDate.isAfter(event.getActivationDate()) && currentDate.isBefore(event.getDeactivationDate()))) {
            event.setStatus(Status.ACTIVE);
        } else {
            event.setStatus(Status.INACTIVE);
        }

        return eventRepository.save(event).getId();
    }

    @Transactional
    public void delete(Long id){
        eventRepository.deleteById(id);
    }
    @Transactional(readOnly = true)
    public Event findEventById(Long id){
        return eventRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("유효하지 않은 이벤트입니다."));

    }

    @Transactional(readOnly = true)
    public List<PostResponseDto> findAllPostsByEventId(Long id){
        return postService.findAllByEvent(findEventById(id));
    }

    @Transactional(readOnly = true)
    public Optional<List<Event>> findAllByUserId(Long id){
        return eventRepository.findAllByUserId(id);
    }

    @Transactional(readOnly = true)
    public Optional<List<Event>> findAllByInvitedToUser(Long id){
        return eventRepository.findByInvitedUsersId(id);
    }

    @Transactional(readOnly = true)
    public Long findUserIdById(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(()-> new IllegalArgumentException("유효하지 않은 이벤트입니다."));

        return event.getUser().getId();
    }

    @Transactional
    public boolean inviteUser(Event event, User user){
        if(!event.isInvitedUser(user)){
            event.addInvitedUser(user);
            return true;
        }

        return false;
    }
    @Transactional
    @Scheduled(cron = "0 0 0 * * *") // 스케줄링 표현식: 매일 자정
    public void updateEventStatusAtMidnight() {
        LocalDate currentDate = LocalDate.now();
        List<Event> events = eventRepository.findAll();

        for (Event event : events) {
            if (currentDate.isEqual(event.getActivationDate()) || (currentDate.isAfter(event.getActivationDate()) && currentDate.isBefore(event.getDeactivationDate()))) {
                event.setStatus(Status.ACTIVE);
            } else {
                event.setStatus(Status.INACTIVE);
            }
        }

        eventRepository.saveAll(events);
    }
}
