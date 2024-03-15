package com.example.test.replace.DAO;

import java.time.LocalDateTime;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Repository
public class ReplaceReviewDAO {

	@Autowired
	SqlSessionTemplate sqlSessiontemplate ;
	
	public int insert(Map<String, Object> replaceCategory) {
		int result = this.sqlSessiontemplate.insert("replaceCategory.insert",replaceCategory);
		return result;
	}
	
	
//	@Id
//	@GeneratedValue
//	@Column(name = "Reivew_ID")
//	private Integer id;
//	
//	@ManyToOne
//	@Column(name = "Replace_ID")
//	private Integer replaceID;
//	
//	@NotNull
//	private Integer rating;
//	
//	@NotNull
//	private String content;
//
//	private LocalDateTime updateDate;
//
//	private LocalDateTime modifyDate;
}
