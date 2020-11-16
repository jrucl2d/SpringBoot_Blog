package com.yu.blog.test;

import com.yu.blog.model.RoleType;
import com.yu.blog.model.User;
import com.yu.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DummyControllerTest {

    @Autowired // DummyControllerTest가 메모리에 뜰 때 이것도 같이 메모리에 뜬다.(의존성 주입, DI)
    private UserRepository userRepository;

    // insert test
    @PostMapping("/dummy/join")
    public String join(String username, String password, String email) {
        // @RequestBody를 적지 않으면 url-encoded form data(?key=value&)형태를 전달받을 수 있다.
        System.out.println(username + password + email);
        return "회원가입 완료";
    }
    @PostMapping("/dummy/join2")
    public String join2(User user) {
        // 객체를 넣어주면 알아서 파싱해줌
        System.out.println(user.getUsername() + user.getPassword() + user.getEmail());

        user.setRole(RoleType.USER);
        userRepository.save(user); // 실제 회원가입이 됨(DB에 저장)
        return "회원가입 완료";
    }

    // select test
    // /dummy/user/3 -> {id} 주소로 파라미터 전달받을 수 있음
    @GetMapping("/dummy/user/{id}")
    public User detail(@PathVariable int id){
        // findById의 리턴 자료형은 Optional. null일 경우를 대비해서.

        // get -> null일 일이 없다고 판단하고 그냥 리턴(위험함)
        // User user = userRepository.findById(id).get();

        // orElseGet(other) -> 없으면 빈 User 객체를 넣어줌. 그래도 null은 아니다.
//        User user = userRepository.findById(id).orElseGet(new Supplier<User>() {
//            @Override
//            public User get() {
//                return new User();
//            }
//        });

        // throw exception -> 가장 권장되는 방식, Supplier 타입의 리턴형을 람다식으로 생략할 수 있음
        User user = userRepository.findById(id).orElseThrow(()->{
            return new IllegalArgumentException("해당 유저는 없습니다. id : " + id);
        });

        // 요청은 웹 브라우저. user 객체는 자바 오브젝트. 변환이 필요하다(json으로)
        // 스프링부트는 MessageConverter가 응답시에 자동으로 Jackson 라이브러리 호출해서 json으로 변경해줌
        return user;
    }
    @GetMapping("/dummy/user")
    public List<User> list(){
        return userRepository.findAll();
    }
    // 한 페이지당 2건의 데이터를 리턴받기
    @GetMapping("/dummy/user/page") // user/page?page=0이 첫 번째 페이지. ?page=1이 두 번재 페이지.
    public List<User> pageList(@PageableDefault(size=2, sort="id", direction = Sort.Direction.DESC) Pageable pageable){
        Page<User> usersPage = userRepository.findAll(pageable);
        List<User> users = usersPage.getContent();
        return users;
    }

    // update test
    @PutMapping("/dummy/user/{id}") // @RequestBody로 json 데이터 받기
    public User updateUser(@PathVariable int id, @RequestBody User requestUser){
        User user = userRepository.findById(id).orElseThrow(()-> {
            return new IllegalArgumentException("수정에 실패하였습니다.");
        });
        user.setPassword(requestUser.getPassword());
        user.setEmail(requestUser.getEmail());
        userRepository.save(user);
        return user;
    }
    @Transactional // save 없이도 더티 체킹을 통해 업데이트 가능, 함수 종료시에 자동 commit
    @PutMapping("/dummy/user2/{id}") // @RequestBody로 json 데이터 받기
    public User updateUser2(@PathVariable int id, @RequestBody User requestUser){
        User user = userRepository.findById(id).orElseThrow(()-> {
            return new IllegalArgumentException("수정에 실패하였습니다.");
        }); // 이 순간에 db에서 영속성 컨텍스트로 가져옴(영속화 됨)
        user.setPassword(requestUser.getPassword());
        user.setEmail(requestUser.getEmail());
        return user; // 종료시 commit할 때 영속화 된 오브젝트의 값이 바뀌었음을 인식하고 알아서 commit 해줌
    }

    // delete test
    @DeleteMapping("/dummy/user/{id}")
    public String delete(@PathVariable int id){
    try{
        userRepository.deleteById(id);
    } catch(EmptyResultDataAccessException e){ // 귀찮으면 그냥 Exception으로 해도 되긴 한다.
        return "삭제에 실패했습니다. 해당 ID는 존재하지 않습니다.";
    }
    return "삭제되었습니다. ID : " + id;
    }
}
