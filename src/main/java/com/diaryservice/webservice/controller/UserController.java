package com.diaryservice.webservice.controller;

import com.diaryservice.webservice.config.auth.LoginUser;
import com.diaryservice.webservice.config.auth.dto.SessionUser;
import com.diaryservice.webservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;

    @GetMapping("/my-page")
    public String viewUserInfo(@LoginUser SessionUser sessionUser, Model model){
        model.addAttribute("user", sessionUser);

        return "/user/info";
    }

    @PostMapping("/my-page/{id}/delete")
    public String deleteUser(@PathVariable Long id, @LoginUser SessionUser sessionUser){
        userService.deleteById(id);

        return "redirect:/logout";
    }

}
