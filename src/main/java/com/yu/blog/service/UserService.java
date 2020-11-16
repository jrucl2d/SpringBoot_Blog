package com.yu.blog.service;

import com.yu.blog.model.RoleType;
import com.yu.blog.model.User;
import com.yu.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 스프링이 컴포넌트 스캔을 통해서 Bean에 자동으로 등록을 해줌. IoC를 해줌.
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BCryptPasswordEncoder encoder; // 해싱용

    @Transactional // 회원가입 트랜잭션 따로
    public int 회원가입(User user){
        String rawPassword = user.getPassword();
        String encPassword = encoder.encode(rawPassword);
        user.setPassword(encPassword); // 해싱
        user.setRole(RoleType.USER); // 유일하게 자동으로 안 들어가는 롤타입 default 값 설정
        try {
            userRepository.save(user);
            return 1;
        } catch (Exception e) {
            return -1;
        }
    }

    @Transactional
    public void 회원수정(User user){
        // 영속성 컨텍스트에 User 오브젝트를 영속화하고, 영속화된 User 오브젝트를 수정하면 더티체킹.
        User persistence = userRepository.findById(user.getId()).orElseThrow(()->{
            return new IllegalArgumentException("회원 찾기 실패");
        });

        // Validation 체크 : kakao 회원가입 한 사람은 oauth가 비어있지 않으므로 절대 수정 불가
        if(persistence.getOauth() == null || persistence.getOauth().equals("")){
            String rawPassword = user.getPassword();
            String encPassword = encoder.encode(rawPassword);
            persistence.setPassword(encPassword);
        }
        persistence.setEmail(user.getEmail());
    }

    @Transactional(readOnly = true)
    public User 회원찾기(String username){
        User user = userRepository.findByUsername(username).orElseGet(()->{
            return new User(); // 없으면 빈 객체 리턴
        });
        return user;
    }
}
