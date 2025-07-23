package com.smhrd.board.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.smhrd.board.entity.BoardEntity;
import com.smhrd.board.service.BoardService;



@Controller // 컨트롤러임을 annotation 으로 명시해주기
public class MainController {
	
	@Autowired
	BoardService boardService;
	
	// 메인 페이지 띄우기 기능
	// 1. 매핑할 메서드 만들기 -> boot 에서는 REST API를 지키기를 선호 => 무슨말?: Get, Post, Delete, patch 를 제대로 명시
	@GetMapping("/") // 원래 legacy 에서는 @RequestMapping 이었는데, GetMapping으로 바꿔줌 => 코드가 줄어듬 : legacy(value="~") -> boot("/") 
	public String inedx(Model model) {
		// legacy 에서 return 타입을 String 으로 정했었음
		// -> viewResolver 가 WEB/INF - view 폴더의 ~.jsp 파일을 실행하겠습니다 라는 의미
		// boot 에서 .jsp 실행하고 싶으면 properties 에서 설정해주면 됨
		
		// boot 에서 내장되어 있는 설정
		// resources - templates 폴더의 ~.html 파일을 실행하도록 설정되어 있음
		// 결론 : boot 에서는 inedx.html을 실행시키겠습니다. 라는 것
		
		// 게시글 모두 출력 후 index 페이지로 전달
		List<BoardEntity> list = boardService.show();
		// 게시글 출력할 땐 session 객체는 범위가 큼 => request 객체 사용
		// Spring 에서 request Scope 대신 사용하는 객체 -> Model 객체 사용
		model.addAttribute("boardList",list);
		
		
		return "index";
	}
	
	@GetMapping("/login")
	public String login() {
		
		return "login";
	}
	
	@GetMapping("/register")
	public String register() {
		
		return "register";
	}
	
	@GetMapping("/write")
	public String write() {
		return "write";
	}
	
}
