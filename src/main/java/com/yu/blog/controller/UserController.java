package com.yu.blog.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yu.blog.model.KakaoProfile;
import com.yu.blog.model.OAuthToken;
import com.yu.blog.model.User;
import com.yu.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

// 인증이 안 된 사용자들이 출입할 수 있는 경로를 /auth/*로 허용
// 그냥 주소가 /이면 index.jsp 허용
// static 이하에 있는 /js/*, /css/*, /image/* 허용
@Controller
public class UserController {

    @Value("${yu.key}")
    private String yuKey;

    @Value("${yu.client_id}")
    private String client_id;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @GetMapping("/auth/joinForm")
    public String joinForm(){
        return "user/joinForm";
    }

    @GetMapping("/auth/loginForm")
    public String loginForm(){
        return "user/loginForm";
    }

    @GetMapping("/user/updateForm")
    public String updateForm(){
        return "/user/updateForm";
    }

    @GetMapping("/auth/kakao/callback")
    public String kakaoCallback(String code){ // Data를 리턴해주는 컨트롤러 함수
        // 쿼리 스트링 내의 code 값을 받을 수 있다.
        // Post 방식으로 key=value 형태의 데이터를 요청(카카오 쪽으로)
        // Retrofit2, OkHttp, RestTemplate 등 여러 라이브러리가 있다.
        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", client_id);
        params.add("redirect_uri", "http://localhost:8000/auth/kakao/callback");
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest, // 데이터
                String.class // 리턴 타입
        );

        // Gson, Json Simple, ObjectMapper <- json을 오브젝트로 관리(Token 정보)
        ObjectMapper objectMapper = new ObjectMapper();
        OAuthToken oAuthToken = null;
        try{
             oAuthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
        } catch (JsonMappingException e){
            e.printStackTrace();
        } catch(JsonProcessingException e){
            e.printStackTrace();
        }

        // access token을 통해 사용자 정보 가져오기
        RestTemplate rt2 = new RestTemplate();
        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("Authorization", "Bearer "+oAuthToken.getAccess_token());
        headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers2);
        ResponseEntity<String> response2 = rt2.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest, // 데이터
                String.class // 리턴 타입
        );

        // 사용자의 정보를 오브젝트로 관리 : username, password, email정보 필요함
        ObjectMapper objectMapper2 = new ObjectMapper();
        KakaoProfile kakaoProfile = null;
        try{
            kakaoProfile = objectMapper2.readValue(response2.getBody(), KakaoProfile.class);
        } catch (JsonMappingException e){
            e.printStackTrace();
        } catch(JsonProcessingException e){
            e.printStackTrace();
        }

        User kakaoUser = User.builder()
                .username(kakaoProfile.getKakao_account().getEmail() + "_" + kakaoProfile.getId())
                .password(yuKey) // yml로부터 주입받은 값
                .email(kakaoProfile.getKakao_account().getEmail())
                .oauth("kakao")
                .build();

        // 가입자 혹은 비가입자 체크. 비가입자면 회원가입 후 로그인 처리.
        User user = userService.회원찾기(kakaoUser.getUsername());

        if(user.getUsername() == null){
            userService.회원가입(kakaoUser);
        }
        // 강제 로그인!
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(kakaoUser.getUsername(), yuKey));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        return "redirect:/";
    }
}
