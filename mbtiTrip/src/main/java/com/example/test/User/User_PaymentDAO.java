package com.example.test.User;

import java.time.LocalDateTime;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Repository
public class User_PaymentDAO {

	@Autowired
	SqlSessionTemplate sqlSessiontemplate;
	
	public int insert(Map<String, Object> userPayment) {
		int result = this.sqlSessiontemplate.insert("userPayment.insert", userPayment);
		return result;
	};
	
//	@Id
//	@GeneratedValue
//	@Column(name = "UserPayment_ID")
//	private Integer id;
//	
//	@OneToMany(mappedBy = "Cart")
//	@Column(name = "UserCart_ID")
//	private Integer userCartID;
//
//	private String Card_info;
//	
//	private String Card_Num;
//	
//	private Integer price;
//	
//	private LocalDateTime paymentTime;
	
}

