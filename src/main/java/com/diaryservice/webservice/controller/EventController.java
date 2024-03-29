package com.diaryservice.webservice.controller;

import com.diaryservice.webservice.config.auth.LoginUser;
import com.diaryservice.webservice.config.auth.dto.SessionUser;
import com.diaryservice.webservice.domain.event.Event;
import com.diaryservice.webservice.domain.user.User;
import com.diaryservice.webservice.service.EventService;
import com.diaryservice.webservice.dto.EventRequestDto;
import com.diaryservice.webservice.service.UserService;
import com.diaryservice.webservice.dto.EventResponseDto;
import com.diaryservice.webservice.validate.ValidateUserAccess;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;


@RequiredArgsConstructor
@Controller
public class EventController {


    private final EventService eventService;
    private final UserService userService;

    @GetMapping("/event/form")
    public String createFormEvent(@LoginUser SessionUser sessionUser){

        return "/event/create-event";
    }

    @ValidateUserAccess
    @PostMapping("/event/{id}/delete")
    public String deleteEvent(@PathVariable Long id, @LoginUser SessionUser sessionUser){
        eventService.delete(id);

        return "redirect:/";
    }

    @ValidateUserAccess
    @GetMapping("/event/{id}")
    public String viewEvent(@PathVariable Long id, @LoginUser SessionUser sessionUser, Model model){

        return getString(id, sessionUser, model);
    }

    @PostMapping("/event/save")
    public String createEvent(@ModelAttribute EventRequestDto eventRequestDto, @LoginUser SessionUser sessionUser,
                                RedirectAttributes redirectAttributes){

        eventRequestDto.setUser(userService.findById(sessionUser.getId()));
        Long save = eventService.save(eventRequestDto.toEntity());

        redirectAttributes.addAttribute("id", save);

        return "redirect:/event/{id}";
    }

    @ValidateUserAccess
    @ResponseBody
    @PostMapping("event/{eventId}/invite-user")
    public ResponseEntity<String> inviteUser(@PathVariable Long eventId, @LoginUser SessionUser sessionUser,
                                             @RequestParam("email") String email) {

        try {
            User invitedUser = userService.findByEmail(email);
            Event event = eventService.findEventById(eventId);

            if(eventService.inviteUser(event, invitedUser)){
                return ResponseEntity.ok("성공적으로 추가되었습니다.");
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 추가된 친구입니다.");


        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유효하지 않은 이메일입니다.");
        }
    }

    private String getString(@PathVariable Long eventId, @LoginUser SessionUser sessionUser, Model model) {

        EventResponseDto responseDto = eventService.findEventById(eventId).toDto();


        model.addAttribute("event", responseDto);
        model.addAttribute("user", sessionUser);

        return "event/view-event"; // 같은 페이지에 오류 메시지와 함께 유지
    }


}
