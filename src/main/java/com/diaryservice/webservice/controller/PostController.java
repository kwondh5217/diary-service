package com.diaryservice.webservice.controller;

import com.diaryservice.webservice.config.auth.LoginUser;
import com.diaryservice.webservice.config.auth.dto.SessionUser;
import com.diaryservice.webservice.domain.event.Event;
import com.diaryservice.webservice.service.EventService;
import com.diaryservice.webservice.domain.event.Status;
import com.diaryservice.webservice.service.PostService;
import com.diaryservice.webservice.dto.PostRequestDto;
import com.diaryservice.webservice.dto.PostResponseDto;
import com.diaryservice.webservice.validate.ValidateUserAccess;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;


@RequiredArgsConstructor
@Controller
public class PostController {

    private final PostService postService;
    private final EventService eventService;

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleInvalidEventException(IllegalArgumentException e, HttpSession session) {
        session.setAttribute("errorMessage", "잘못된 접근입니다.");
        return "redirect:/"; // 인덱스 페이지로 리다이렉트
    }


    @ValidateUserAccess
    @PostMapping("/event/{eventId}/post")
    public String savePost(@PathVariable Long eventId, @LoginUser SessionUser sessionUser,
                           @RequestPart("author") String author,
                           @RequestPart("title") String title, @RequestPart("content") String content,
                           @RequestPart("media") MultipartFile media, RedirectAttributes redirectAttributes) throws IOException {


        Event eventById = eventService.findEventById(eventId);
        if(eventById.getStatus() != Status.ACTIVE){
            throw new IllegalArgumentException("비활성화된 이벤트입니다.");
        }

        Optional<String> optionalMediaName = (media.getSize() != 0) ? Optional.of(postService.uploadFile(media)) : Optional.empty();


        PostRequestDto postRequestDto = PostRequestDto.builder()
                .event(eventById)
                .title(title)
                .content(content)
                .author(author)
                .mediaName(optionalMediaName.orElse(null))
                .build();


        redirectAttributes.addAttribute("postId", postService.save(postRequestDto));

        return "redirect:/event/{eventId}/post/{postId}";
    }

//    게시글 삭제할 때 s3버킷 내 파일 삭제 구현 해야함
    @ValidateUserAccess
    @PostMapping("/event/{eventId}/post/{postId}/delete")
    public @ResponseBody String deletePost(@PathVariable Long eventId,
                                           @LoginUser SessionUser sessionUser, @PathVariable Long postId) throws IOException {

        PostResponseDto byId = postService.findById(postId);

        postService.deleteFile(byId.getMediaName());
        postService.deleteById(postId);


        return "ok";
    }

    @ValidateUserAccess
    @GetMapping("/event/{eventId}/post/{postId}")
    public String getPost(@PathVariable Long eventId,
                          @LoginUser SessionUser sessionUser,
                          @PathVariable Long postId, Model model) {

        PostResponseDto byId = postService.findById(postId);
        model.addAttribute("post", byId);

        return "/post/view";
    }

}
