package com.smhrd.board.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smhrd.board.entity.BoardEntity;
import com.smhrd.board.service.BoardService;

@RestController
public class BoardRestController {
	
	@Autowired
	BoardService boardService;
	
	@GetMapping("/board/search") 
	public List<BoardEntity> search(@RequestParam String type, @RequestParam String keyword) {
		// 필요한 데이터 : type, keyword -> RequestParam 이용해서 가져옴
		
		// 데이터 베이스 연결 => 서비스 객체 생성 : AutoWired
		// -> service 객체에 기능 구현해야 함
		return boardService.search(type, keyword);
	}
}
