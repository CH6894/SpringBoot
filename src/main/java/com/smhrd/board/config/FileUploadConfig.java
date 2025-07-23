package com.smhrd.board.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration // 환경설정 클래스 파일 지정
// 파일(이미지)이 저장될 경로를 지정하기 위한 config 파일
public class FileUploadConfig {
	
	@Value("${file.upload-dir}") // application.properties 에 있는 file.upload-dir 를 참고해서 값을 넣겠다는 뜻
	private String uploadDir; // C:/upload 폴더 라는 경로를 uploadDir 이라는 변수에다가 담아뒀음
							  // => 왜?? : 유지보수를 위해서 => 어떻게?? : file.upload-dir을 value annotation 을 했기 때문
							  // => 그게뭐임?? : application.properties 에 file.upload-dir = C:/upload 이 부분만 수정해 주면 모든 코드에서 적용되니까
							  // => 원래라면 그냥 경로 자체를 String uploadDir = "C:/upload"; 라고만 해도 되는데, 경로가 수정되면 모든 코드에서 바꿔줘야 함
	
	public String getUploaderDir() {
		return this.uploadDir;
	}
}
