package com.example.test.User.Service;

import java.util.Map;

import org.springframework.context.annotation.Bean;

public interface adminService {

	public boolean userBaned(String username); 
	
	//오늘 방문자 수 
	public int rating(String userName);
	
	//총 사용자 수 
	public Map<String, Integer> userCnt();
	
	//총 게시글 수 
	public int postCnt();
	//총 숙소 수 
	public int replaceCnt();
	//총 adventure 수 
	public int adventureCnt();
	//각각의 detail 
	@Bean
	public void dailyrating(); 
}