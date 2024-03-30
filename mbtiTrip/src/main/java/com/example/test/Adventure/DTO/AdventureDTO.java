package com.example.test.Adventure.DTO;



import java.time.LocalDateTime;
import java.util.Set;

import com.example.test.User.DTO.UserDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class AdventureDTO {

	
	private Integer adventureID; //게시한 담당자아이디?
	
	private Integer adventureCategoryID; // 어드벤쳐 카테고리 id
	
	private Integer mbtiID;
	
	private Integer cityID;
	
	private Integer postCategoryID; // 게시판 카테고리(판매, 리뷰)?
	
	private Integer adventureTypeId; // 어드벤쳐 카테고리랑 타입이랑 무슨차이?
	
	private String adventureLocation; // 해당 어드벤쳐 위치
	
	private String adventureName; // 어드벤쳐 이름
	
	private Integer adventurePrice; // 어드벤쳐 가격
	
	private String adventureContent; // 어드벤쳐 내용
	
	private String tel; // 어드벤쳐 번호
	
	private String adventureAdmin; // 어드벤쳐 관리자 이름?
	
	private UserDTO Author; // 작성자
	
	private double ratingAvg; // 해당 어드벤쳐 평점
	
	private Set<UserDTO> voter; // 추천수
	
	private Adventure_CategoryDTO adCategory; 
	
	private LocalDateTime updateDate;
	
	private LocalDateTime modifyDate;
	
	private Integer views; // 조회수
	
}
