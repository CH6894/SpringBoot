package com.smhrd.board.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.smhrd.board.config.BucketConfig;
import com.smhrd.board.config.FileUploadConfig;
import com.smhrd.board.entity.BoardEntity;
import com.smhrd.board.entity.UserEntity;
import com.smhrd.board.service.BoardService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/board") // Controller 에 requestMapping 진행 시 default url 변경(이 컨트롤러에서만 기본 경로 : ~~~/board/
							// ~~ 임
public class BoardController {
	@Autowired
	BoardService boardService;
	
	private final FileUploadConfig fileUploadConfig;
	private final BucketConfig bucketConfig;
	private final AmazonS3 amazonS3;

	BoardController(FileUploadConfig fileUploadConfig, BucketConfig bucketConfig, AmazonS3 amazonS3) {
		this.fileUploadConfig = fileUploadConfig;
		this.bucketConfig = bucketConfig;
		this.amazonS3 = amazonS3;
	}

	// 글쓰기 기능
	@PostMapping("/write")
	public String write(@RequestParam String title, @RequestParam String content, HttpSession session,
			@RequestParam MultipartFile image) {

		// 필요한거 -> 제목, 작성자, 내용, 이미지 (번호, 작성일 제외)
		// 작성자 -> session에 담긴 값을 가지고 오는 코드
		// 이미지
		// -> 이미지 파일을 가지고 와서 서버에 저장
		// -> 이미지를 서버에 저장시키기 위한 환경 설정 코드 필요
		String imgPath = "";
		// 이 if문의 용도
		// 1. 이미지 업로드 되면 C:/upload 폴더에 저장하기
		// 2. DB에 /uploads/파일명 으로 문자열 저장하기
		// *	만약 이미지 업로드 안됐으면 if문 안들어옴
		// **	DB에 저장할때 오류나면 오류 메세지 출력인듯
		if (!image.isEmpty()) {
			// *이미지의 이름 가져오기
			String img_name = image.getOriginalFilename();
			// -> getOriginalFilename 메서드 사용 : 이미지의 고유 이름을 가져와 줌 중복될 수도 있음
			// 이미지가 이름은 같은데 안에 담긴 그림은 다를 수 있으니까, 랜덤값을 만들어줌
			// java 안에 고유 번호를 만드는 객체가 존재함 -> UUID 객체
			String file_name = UUID.randomUUID() + "_" + img_name;
			// => random값_이미지이름, 예시) 123_1.jpg\
			
			try {
				ObjectMetadata metadata = new ObjectMetadata();
		        metadata.setContentLength(image.getSize());
		        metadata.setContentType(image.getContentType());

		        PutObjectRequest request = new PutObjectRequest(bucketConfig.getbucketName(), file_name, image.getInputStream(), metadata)
		                .withCannedAcl(CannedAccessControlList.PublicRead); // public 접근 허용

		        amazonS3.putObject(request);
		        imgPath = amazonS3.getUrl(bucketConfig.getbucketName(), file_name).toString();
		        
			}catch (Exception e) {
				e.printStackTrace();
			}
		}

		// DB 저장
		// DB에 접근하기 위한 객체는 service 객체, 게시판 관련 service 객체 만들기
		// BoardService -> BoardRepository
		// save()
		BoardEntity entity = new BoardEntity();
		entity.setTitle(title);
		entity.setContent(content);
		entity.setImgPath(imgPath);

		// writer
		UserEntity user = (UserEntity) session.getAttribute("user");
		// (UserEntity) 해준 거 : 다운 캐스팅
		// 작성자는 글쓰기에서 input 값이 아님! => 세션에서 가져오기 => Httpsession
		// session 에서 값을 꺼내면 항상 Object 형태로 반환 => 모든 클래스는 Object 의 자식
		// Object 에는 밑의 getUserId 와 같은 메서드는 없기 때문에 다운 캐스팅 해주기
		
		String writer = user.getUserId();

		entity.setWriter(writer);

		BoardEntity result = boardService.write(entity);
		if (result != null) {
			// 성공
			// 글이 작성될 시 index 페이지로 이동
			return "redirect:/";
		} else {
			return "redirect:/board/write";
		}

	}

	// 게시글 상세 페이지 이동
	@GetMapping("/detail/{id}") // {} : url에 있는 변수
	public String detail(@PathVariable Long id, Model model) {// url의 변수 가지고 오는 법
		System.out.println(id);
		// id를 바탕으로 select 진행
		// => 게시글의 상세 정보 출력
		// =>DB 접근하기 => service 객체에서 기능 구현하겠다는 의미
		Optional<BoardEntity> entity = boardService.detail(id);

		model.addAttribute("detail", entity.get());

		return "detail";
	}

	// 게시글 수정 페이지 이동
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable Long id, Model model) {

		// id를 바탕으로 데이터 select 하기 -> BoardService 에서 detail로 만들어 놓음 => 그래서 위의 게시글 상세 페이지
		// 이동의 코드랑 똑같음
		Optional<BoardEntity> entity = boardService.detail(id);

		model.addAttribute("edit", entity.get());

		return "edit";
	}

	@PostMapping("/update")
	public String update(@RequestParam Long id, @RequestParam String title, @RequestParam String content,
			@RequestParam String oldImgPath, @RequestParam MultipartFile image) {
		// 필요한 데이터 : title, id(글 번호), content, imgPath ,, 등등

		// 데이터 불러 오기
		Optional<BoardEntity> board = boardService.detail(id);
		BoardEntity entity = board.get();

		String uploadDir = fileUploadConfig.getUploaderDir();

		// 새로운 이미지가 선택된 경우에만 이미지 처리
		if (!image.isEmpty()) {
			// 기존 이미지가 있는지 여부 판단
			if (oldImgPath != null && !oldImgPath.isEmpty()) {
				// 기존 이미지 삭제 : 서버에서 이미지 삭제
				String oldFile = oldImgPath.replace("/uploads/", "");
				Path oldFilePath = Paths.get(uploadDir, oldFile);
				
				try {
					Files.deleteIfExists(oldFilePath);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			// 새로운 이미지 저장
			try {
				String newFileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
				Path newFilePath = Paths.get(uploadDir, newFileName);
				image.transferTo(newFilePath.toFile());
				entity.setImgPath("/uploads/" + newFileName);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		entity.setTitle(title);
		entity.setContent(content);
		
		// update 문 실행
		// JPA에서 update 가 없는 것이 아님. save() 함수가 update 문 실행까지 담당하고 있음
		// save() -> 2가지 기능(1. insert, 2. update)
		// - update 문을 실행하는 조건
		// -- findById()를 사용해서 데이터를 불러오는 상황 = select 문 사용 이후 데이터는 영속 상태(= 수정 상태)
		//		=> 이 상태일 때 save 함수 사용 시 update 문을 실행
		
		// 복잡한 update 문은 실행되지 않음 => 복잡한거 쓰려면?: @Query() 이용해서 update 실행
		
		boardService.write(entity);
        return "redirect:/board/detail/" + id;
	}

}
