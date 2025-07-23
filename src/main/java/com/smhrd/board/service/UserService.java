package com.smhrd.board.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smhrd.board.entity.UserEntity;
import com.smhrd.board.repository.UserRepository;

@Service
public class UserService {
	/*
	 * Spring(legacy, boot) 에서는 3계층 구조를 띔
	 * Controller - Service - Repository(mapper) 의 3계층 구조
	 * 
	 * Controller : mapping 처리 => Http 요청 처리 + 사용자와 직접적인 상호 작용(RequestParam)
	 * Service : 비지니스 로직 담당, Repository(mapper) 연결하는 역할
	 * Repository : DB와 직접 통신
	 * 
	 * Controller 와 Repository 가 직접적인 연결이 되면
	 * -> Controller 의 역할 많아짐 + 가독성이 떨어짐 + Repository 와의 결합도 올라감
	 * => 유지 보수가 힘들어짐
	 * => 여러 개의 쿼리문 사용이 힘들어짐
	 * 
	 * Service 객체를 사용하는 이유
	 * -> 유지보수성 향상
	 * 결론 : Controller -연결-> Service -연결-> Repository
	 */
	@Autowired // Repository 가 인터페이스기 때문에 객체 생성이 안되기 때문에 연결해준 annotation
	private UserRepository userRepository;
	
	
	// 기능 구현 -> 회원가입 기능 => Service 와 Repository 연결함
	public void register(UserEntity entity) {
		userRepository.save(entity);
	}
	
	// 아이디 중복 체크 기능 구현
	public boolean checkId(String user_id) {
		return userRepository.existsByUserId(user_id);
		
	}
	
	// 로그인 기능 구현
	public Optional<UserEntity> login(String id, String pw) {
		// 아이디와 패스워드 받기
		// 3.3 repository 에 적절한 메소드가 있는지 여부를 판단 -> sql문 판단
		// select * from 테이블명 where userId = id and pw = pw
		// 메소드 없으면 만든 후 실행  => UserRepository 에서 만듬
		Optional<UserEntity> entity = userRepository.findByUserIdAndPw(id, pw);		
		return entity;
	}
	
	
}
