package com.smhrd.board;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/*
 * Spring boot 의 시작점
 
 * SpringBootApplication 이라는 annotation은 3개의 annotation의 집합
 * 1. SpringBootConfiguration
 * 	- Spring Boot 의 전반적인 환경 설정을 담당하는 annotation
 * 2. EnableAutoConfiguration
 * 	- pom.xml에 jar 파일을 작성하면 필요한 객체를 자동으로 생성 및 사용하도록 하는 annotation
 * 	- DB 접속을 위해 STS3에서 사용했던 HikariCP, DataSource 객체들을 DB 정보 작성 만으로 자동으로 생성
 * 3. ComponentScan
 * 	- @Controller같은 객체들을 자동으로 등록해주는 annotation
 * 
 * 결론 : @SpringBootApplication 이라는 annotation 을 활용한 것만으로 복잡한 설정 없이 라이브러리 추가 및 자동 등록된다 라고 생각하기
 * 		 위의 내용 외울 필요 X
 * 		 + 앞으로 여기 수정할 일 없음 스프링 부트가 돌아갈 수 있도록 해주는 공간임
 */
@SpringBootApplication
public class SpringBootBoardApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootBoardApplication.class, args);
	}

}
