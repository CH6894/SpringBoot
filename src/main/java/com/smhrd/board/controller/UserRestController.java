package com.smhrd.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smhrd.board.service.UserService;

@RestController
public class UserRestController {
	@Autowired
	UserService userService;
	
	// RestController 란?
	// 일반 Controller는 view(html)을 반환함
	// RestController는 데이터를 반환함!! => Controller와의 차이점은 반환하는 타입만 다르고 문법이나 나머지는 모두 동일함!
	//	-> 데이터란?? : String, int, Json 등등
	// legacy -> Controller + ResponseBody 를 이용해서 구현
	// boot는 ResponsBody는 불필요
	
	// 아이디 중복 체크 기능
	// 1. 매핑할 매소드 만들기
	@GetMapping("/member/check-id")
	public boolean checkId(@RequestParam String id) {
		// 2. 필요한 데이터 파악 -> @RequestParam으로 설정
		// 3. DB접근
		return  userService.checkId(id);
	}
	
}
