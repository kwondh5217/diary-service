package com.diaryservice.webservice;


import com.diaryservice.webservice.config.auth.LoginUser;
import com.diaryservice.webservice.config.auth.dto.SessionUser;
import com.diaryservice.webservice.domain.event.Event;
import com.diaryservice.webservice.dto.EventResponseDto;
import com.diaryservice.webservice.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final EventService eventService;

    @GetMapping("/")
    public String index(Model model, HttpSession session, @LoginUser SessionUser user) {

        if(user != null){
            model.addAttribute("userName", user.getName());
            Optional<List<Event>> allByUserId = eventService.findAllByUserId(user.getId());
            Optional<List<Event>> allByInvitedToUser = eventService.findAllByInvitedToUser(user.getId());

            List<EventResponseDto> createdDtos = allByUserId.map(events ->
                    events.stream().map(Event::toDto).collect(Collectors.toList()))
                    .orElse(Collections.emptyList());
            List<EventResponseDto> invitedDtos = allByInvitedToUser.map(invitedEvents ->
                    invitedEvents.stream().map(Event::toDto).collect(Collectors.toList()))
                    .orElse(Collections.emptyList());

            model.addAttribute("events", createdDtos);
            model.addAttribute("invitedEvents", invitedDtos);
        }

        String errorMessage = (String) session.getAttribute("errorMessage");
        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
            session.removeAttribute("errorMessage"); // 에러 메시지 사용 후 세션에서 삭제
        }


        return "index";

    }



}
