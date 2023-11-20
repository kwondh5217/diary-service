package com.diaryservice.webservice.service;


import com.diaryservice.webservice.domain.user.User;
import com.diaryservice.webservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public User findById(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원 입니다."));

        return user;
    }

    public void deleteById(Long id){
        userRepository.deleteById(id);
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("유효하지 않은 이메일입니다."));
    }
}
