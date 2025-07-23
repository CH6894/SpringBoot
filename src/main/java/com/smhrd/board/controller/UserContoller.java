package com.smhrd.board.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smhrd.board.entity.UserEntity;
import com.smhrd.board.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserContoller {
	
	@Autowired
	UserService userService; // userService 객체 생성
	
	// 회원 가입 기능 구현
	@PostMapping("/register.do")
	public String register(@RequestParam String id, @RequestParam String pw, @RequestParam String name, @RequestParam int age) {
		// boot의 경우, @RequestParam("id") 이런 식으로 안해줘도 됨
		// -> input의 name 값과 변수 명을 일치시켜주면 됨. => @RequestParam String id : id 라는 변수를 가져오는 것
		
		// 필요한 데이터 -> 회원 가입을 위한 input 태그 데이터
		System.out.println("id : " + id);
		System.out.println("pw : " + pw);
		System.out.println("name : " + name);
		System.out.println("age : " + age);
				
		// userEntity 매개변수로 받기 --> lombok(getter, setter 사용)
		UserEntity entity = new UserEntity();
		entity.setUserId(id);
		entity.setPw(pw);
		entity.setName(name);
		entity.setAge(age);
		
		userService.register(entity);
		
		return "login";
	}
	
	
	// 로그인 기능 구현
	// 1. 매핑할 메소드 만들어주기
	@PostMapping("/login.do")
	public String login(@RequestParam String id, @RequestParam String pw, HttpSession session) {
		// 2. 필요한 데이터 확인 : login.html에 있는 id와 pw의 name값 가져오기 + 3.4 만든 후, 로그인 정보 저장을 위해 session 추가
		
		// 3. DB 접근하기
		// 3.1 service 객체가 이 컨트롤러에 생성되었는지 여부를 판단 -> Autowired 확인
		// 3.2 service 에 기능 구현하기, 3.3 : UserService
		
		// 3.4 서비스 기능 실행
		Optional<UserEntity> entity = userService.login(id, pw);
		
		// Optional 객체에 .isPresent() => 데이터가 있으면 True, 없으면 False 를 return 해주는 함수
		if(entity.isPresent()) {
			// entity는 현재 Optional 객체
			// UserEntity 내장 함수 사용
			// 	-> .get()을 사용하여 optional 에 있는 데이터 가지고 오기
			session.setAttribute("user", entity.get());
			return "redirect:/"; // 로그인 성공하면 index(메인) 페이지로 이동
		}else {
			// 로그인 실패 시 alert 창 띄우기
			// queryparameter 전송 가능
			
			return "redirect:/login?error=true";
		}
		
	}
	
	
	
	// 로그 아웃 기능
	// 1. 매핑할 메소드 만들어주기
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		// 2. 필요한 데이터 -> 세션값만 지우면 됨 
		
		session.removeAttribute("user");
		
		return "redirect:/";
	}
	
	
}
