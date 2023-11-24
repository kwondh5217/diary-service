package com.diaryservice.webservice.service;

import com.diaryservice.webservice.domain.event.Event;
import com.diaryservice.webservice.domain.post.Post;
import com.diaryservice.webservice.dto.PostRequestDto;
import com.diaryservice.webservice.dto.PostResponseDto;
import com.diaryservice.webservice.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final S3Service s3Service;

    @Transactional
    public Long save(PostRequestDto postRequestDto){
        return postRepository.save(postRequestDto.toEntity()).getId();
    }

    @Transactional
    public void deleteById(Long id){
        postRepository.deleteById(id);
    }
    @Transactional
    public String uploadFile(MultipartFile multipartFile) throws IOException {
         return s3Service.upload(multipartFile);
    }

    @Transactional
    public void deleteFile(String fileName) throws IOException {
        s3Service.delete(fileName);
    }

    @Transactional(readOnly = true)
    public PostResponseDto findById(Long id){
        Post byId = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        return PostResponseDto.builder()
                .id(byId.getId())
                .eventId(byId.getEvent().getId())
                .title(byId.getTitle())
                .content(byId.getContent())
                .author(byId.getAuthor())
                .mediaName(byId.getMediaName())
                .build();
    }

    @Transactional(readOnly = true)
    public List<PostResponseDto> findAllByEvent(Event event) {
        List<Post> allByEvent = postRepository.findAllByEvent(event)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        List<PostResponseDto> postResponseDtos = new ArrayList<>();

        for (Post post : allByEvent) {
            PostResponseDto postResponseDto = PostResponseDto.builder()
                    .id(post.getId())
                    .eventId(post.getEvent().getId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .author(post.getAuthor())
                    .mediaName(post.getMediaName())
                    .build();

            postResponseDtos.add(postResponseDto);
        }

        return postResponseDtos;
    }


}
