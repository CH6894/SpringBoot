package com.smhrd.board.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class BoardEntity {
	// pk가 존재해야 함
	
	@Id // pk 지정
	@GeneratedValue(strategy = GenerationType.IDENTITY) // auto increase
	private Long id; // 글 번호 
	
	// 각 컬럼들에게 not null 설정(imgPath 제외)
	@Column(nullable = false)
	private String title;
	
	@Column(nullable = false)
	private String writer;
	
	@Column(nullable = false, columnDefinition = "TEXT") // 내용의 경우 길이가 길어야 하니까 columnDefinition 속성 설정
	private String content; 
	
	private String imgPath;
	// img 파일을 넣는 것이 아니라, 경로를 DB에 저장
	// DB 서버에 직접적으로 이미지와 같은 파일을 저장하지 않음.(가능은 하지만, DB서버가 무거워지고, DB를 불러오는 데 오래걸림)
	// -> 이미지는 서버에 저장, 서버의 주소를 DB에 저장
	
	@Column(nullable = false, updatable = false) // DB에 저장 시, insert 는 가능하지만, update 는 불가능하게 설정
	private LocalDate time;
	
	// 글 작성 시 자동으로 writeDay가 입력 되도록 코드 작성
	// entity가 생성될 때 실행하는 코드
	@PrePersist
	protected void onCreate() {
		this.writeDay = LocalDate.now();
	}
	
}
