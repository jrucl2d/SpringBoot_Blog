package com.yu.blog.config.auth;

import com.yu.blog.model.User;
import com.yu.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service // 빈에 등록됨
public class PrincipalDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    // 스프링 시큐리티가 로그인 요청 가로챌 때 username, password 두 개 가로챔
    // password는 알아서 처리해줌. 따라서 username이 DB에 존재하는지만 확인해주면 됨
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User principal = userRepository.findByUsername(username)
                .orElseThrow(()->{
                    return new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다 : " + username);
                });
        return new PrincipalDetail(principal); // 생성자에 User 객체를 넣어서 UserDetails 객체를 리턴(시큐리티 세션에 유저 정보가 저장됨)
    }
}
