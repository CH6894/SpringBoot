package com.smhrd.board.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity // DB 테이블 처럼 쓰겠다라는 의미. (Entity 역할을 하는 클래스 파일) 
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
	// 클래스의 구성 요소 : 필드, 메서드
	// 이제 작성할 필드들 : DB의 컬럼이 될 예정
	// ★★★필수 조건★★★ : 반드시 pk가 존재해야 함
	// --> 주로 숫자(long)타입으로 지정, 필수는 아니지만 가급적 숫자로.
	
	@Id // PK 지정하는 annotation, jakarta.persistence 를 import 해주기 jpa니까
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 자동으로 숫자 증가하도록 설정해주기, auto_increase 라는 명령어와 동일
	private Long id;
	
	// 컬럼 설정하는 방법(unique, not null, length 등등 제한거는 법)
	// @Column 를 각 변수마다 지정해주기  
	@Column(unique = true, nullable = false) // unique 설정, not null 설정(false : null 안됨)
	private String userId; // user_id로 해도 상관은 없으나, jpa가 카멜 기법을 선호하기 때문에, 스네이크 기법으로 작성되면 오류가 발생할 가능성이 있음
	
	@Column(length = 100)
	private String pw;
	
	private String name;
	private int age;
	
}
