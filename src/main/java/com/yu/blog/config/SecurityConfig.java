package com.yu.blog.config;

import com.yu.blog.config.auth.PrincipalDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// 아래 세 개 어노테이션은 security 사용시 필수
@Configuration // 빈 등록(IoC관리, 스프링 컨테이너에서 객체를 관리)
@EnableWebSecurity // security 필터 추가 : security 활성화, 그 설정을 해당 파일에서 한다는 의미
@EnableGlobalMethodSecurity(prePostEnabled = true) // 특정 주소로의 접근시 권한 및 인증을 미리 체크한다는 의미
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Bean // 빈 등록, IoC가 됨
    public BCryptPasswordEncoder encodePWD(){
        return new BCryptPasswordEncoder();
    }
    @Autowired
    private PrincipalDetailService principalDetailService;
    
    // spring security가 대신 로그인해줄 때 가로챈 password가 어떤걸로 해싱 되어 있는지 알아야 함(이게 있어야 pw 비교)
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(principalDetailService).passwordEncoder(encodePWD());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean(); // AuthenticationManager을 빈에 등록
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable() // csrf 토큰 비활성화(테스트시에는 걸어두는게 좋음)
                .authorizeRequests()
                .antMatchers("/", "/auth/**", "/js/**", "/css/**", "/image/**")
                .permitAll() // 위 주소들은 인증 없이 누구나
                .anyRequest() // 다른 주소들은 인증 필요
                .authenticated()
            .and()
                .formLogin()
                .loginPage("/auth/loginForm") // 로그인 폼을 보여주고
                .loginProcessingUrl("/auth/loginProc") // 스프링 시큐리티가 이 주소로 요청오는 로그인을 가로채고 대신 로그인
                .defaultSuccessUrl("/");
    }
}
