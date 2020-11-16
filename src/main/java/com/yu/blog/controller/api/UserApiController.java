package com.yu.blog.controller.api;

import com.yu.blog.config.auth.PrincipalDetail;
import com.yu.blog.dto.ResponseDto;
import com.yu.blog.model.RoleType;
import com.yu.blog.model.User;
import com.yu.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController // ajax 요청용
public class UserApiController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/auth/joinProc") // 회원가입 요청 주소 변경
    public ResponseDto<Integer> save(@RequestBody User user){
        userService.회원가입(user); // 오류가 나면 globalExceptionHandler가 오류 처리해줌
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1); // 자바 오브젝트를 JSON으로 변환해서 리턴(Jackson)
    }
    // 기존 로그인은 삭제했음

    @PutMapping("/user") // 회원정보 수정
    public ResponseDto<Integer> update(@RequestBody User user){
        userService.회원수정(user);

        // 강제 로그인!
        // 여기서는 트랜잭션이 종료되기 때문에 DB의 값은 변경되지만 세션 값은 변경되지 않아서 화면에 보여지는 값은 그대로.
        // 강제로 세션 값을 변경해줘야 함. Authentication을 직접 생성해서 authenticationManager가 세션 값을 변경하도록
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1); // 자바 오브젝트를 JSON으로 변환해서 리턴(Jackson)
    }
}
