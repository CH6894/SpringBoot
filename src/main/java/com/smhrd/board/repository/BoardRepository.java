package com.smhrd.board.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.smhrd.board.entity.BoardEntity;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
	
	// 게시글 역순으로 출력하는 기능
	List<BoardEntity> findAllByOrderByIdDesc();
	
	// 제목으로 검색 기능 구현 - Containing : like 문
	List<BoardEntity> findByTitleContaining(String keyword);
	// = select * from ~~ where title like %keyword%
	
	
	// 내용으로 검색 기능 구현
	// 	sql 문 작성법은 알겠으나, JPA 메소드로 만들기 힘든경우
	// 	sql 문 삽입하는 방법
	//	 1. @Query 활용하여 작성(* 사용 안됨)
	// 	 2. 테이블 명 작성 시, 첫 글자와 _ 글자는 대문자로 작성
	// 	 3. :keyword -> :를 사용하면 변수로 사용하겠다는 의미
	// 	 4. 메소드 명은 자유롭게 작성
	@Query("select b from BoardEntity b where b.content like %:keyword%")
	List<BoardEntity> searchContent(String keyword);
	
	// 작성자 이름으로 검색 기능 구현
	List<BoardEntity> findByWriter(String keyword);
}
