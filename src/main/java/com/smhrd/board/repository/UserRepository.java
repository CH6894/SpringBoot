package com.smhrd.board.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smhrd.board.entity.UserEntity;

@Repository // 0. annotation 으로 Repository 임을 알려줘야 함 -> 역할 : 데이터 베이스 연동
			// 이 annotation 은 안써도 됨 -> extends JpaRepository 를 해줬기 때문!!
public interface UserRepository extends JpaRepository<UserEntity, Long>{
	// 1. 인터페이스 "상속" 받기 * 필수
	// 인터페이스간의 "상속"은 extends 사용함
	
	// <> : 제너릭 
	// -> 객체의 타입을 지정해주는 역할을 함(Spring boot 한정이 아님 일반적으로 그럼)
	// -> 레퍼런스 타입만 타입 지정 가능
	//     => 기본 타입 8가지는 들어갈 수 없음 => 들어가려면? : Wrapper Class 이용(기본 타입 8가지를 레퍼런스 타입으로 사용할 수 있게 해준 Class)

	// JpaRepository<T, ID>
	// - T : entity(= VO) 객체 -> Class 파일로 구성 시 테이블 생성(VO 객체로 만듬)
	// - ID : @Id로 지정한 필드의 타입을 넣어주기 => 우리는 Long 타입으로 id 를 선언 해줌, => Long 작성
	//			= entity 가 가지고 있는 pk 필드의 타입

	// jpa 메소드
	// 기본 내장 메소드 소개
	// 1. save(entity)
	// --> insert into entity명(= table) values(매개변수로 넣은 entity)

	// 2. delete(매개변수)
	// --> delete from entity명 where pk명 = 매개변수
	
	// 3. findAll()
	// --> select * from entity명
	
	// 4. findById(매개변수) -> 여기서의 Id : pk를 말하는 것
	// --> select * from entity명 where id = 매개변수

	// 5. existsBy컬럼명(매개변수) : 데이터 존재 유무를 알려주는 함수
	// --> select * from 테이블명 where 컬럼명 = 매개변수
	
	/*
	커스텀 메소드
	 - 예시) select * from 테이블명 where userId = ?? and pw = ??;
	 - 규칙 : select 는 find
			 where 는 by
			 컬럼 명 작성
			 and, or, order by 등의 키워드는 그대로 작성하기
	★★★★★★★★★ 단어 사이가 이어질 시 무조건 카멜 기법 => 대문자 작성, 언더바 안됨 ★★★★★★★★★★
	
	findByUserIdAndPw(매개변수1, 매개변수2)
	
	* userId가 있는지 없는지
	* existByUserId 라는 함수는 없음
	* -> 커스텀으로 만들어야 함
	*/
	boolean existsByUserId(String user_id);
	// user_id를 바탕으로 데이터가 존재하는 지 유무를 알려주는 기능을 하는 메소드를 만듬
	
	
	// 로그인 기능 구현
	// Optional -> npe(null pointer exception)를 피하기 위해 사용
	// null 이 왔을 때 종료의 위험을 막아줌 -> 값이 없을 시 empty를 return 해줌. / 원래는 null 이 반환되면 종료될 수도 있음
	// 결론 : 결과가 없을 때 "null"이 아니라 "없다"를 보여주기 위한 객체
	Optional<UserEntity> findByUserIdAndPw(String id, String pw);
	
}