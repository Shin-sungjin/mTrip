package com.example.test.User.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.test.Adventure.DTO.AdventureDTO;
import com.example.test.User.DAO.UserCartDAO;
import com.example.test.User.DAO.UserDAO;
import com.example.test.User.DTO.UserCartDTO;
import com.example.test.item.DAO.ItemDAO;
import com.example.test.item.DTO.ItemDTO;
import com.example.test.replace.DTO.ReplaceDTO;

import com.example.testExcepion.Cart.CartException;
import com.example.testExcepion.Cart.CartExceptionEnum;
import com.example.testExcepion.Insert.InsertException;
import com.example.testExcepion.Insert.InsertExceptionEnum;
import com.example.testExcepion.Utile.UserNotFoundExcepiton;
import com.example.testExcepion.Utile.UtileExceptionCode;
import com.google.api.client.http.HttpResponse;
import com.google.api.services.storage.Storage.BucketAccessControls.Insert;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class UserCartServiceImpl implements UserCartService{

	
	@Autowired
	UserCartDAO userCartDAO;
	
	@Autowired
	UserDAO userDAO;
	@Autowired
	ItemDAO itemDao;
	
	
	/*아이템 myCart에 삽입*/
	@Override
	public boolean insertItem(UserCartDTO userCartDTO, Principal principal) throws CartException{
		//4.3 test끝 	
		//URL  = /replace/cart Post
		int result = 0;
		boolean ck;
		//로그인 된 사용자인지 검사 
		if(principal.getName() == null) {
			throw new UserNotFoundExcepiton(UtileExceptionCode.USER_NOT_FOUND_EXCEPTION);
		}
		//Cart에 올바르게 정보가 들어오는지 validation검사진행
		CartValidationCK(userCartDTO);
		
		//Cart에 아이템 정보와 , User 정보 세팅
		userCartDTO.setUserName(userDAO.getUserNameByuserID(principal.getName()));
		userCartDTO.setPayments(false);
		log.info("message {}", userCartDTO.toString());
		//아이템 삽입
		result = userCartDAO.insertItem(userCartDTO);
		//insert 된 결과가 없다면, 예외처리 
		if(result == 0) {
			throw new InsertException(InsertExceptionEnum.INSERT_SERVER_ERROR);
		}
		
		ck = (result != 0) ? true : false;
		
		return ck;
	}

	@Override
	public List<HashMap<String, Object>> detail(UserCartDTO usercartdto ,Principal principal) {
		//url = mypage/ myCart
		//payments get false;
		
		//user정보 조회 
		String userName = userDAO.getUserNameByuserID(principal.getName());
		usercartdto.setUserName(userName);
		
		//user의 결제 되지 않은 장바구니 내역 불러오기 
		List<HashMap<String, Object>> userCart = this.userCartDAO.detail(usercartdto);
		for(HashMap<String, Object> item :userCart) {
			int itemID = (Integer) item.get("itemId");
			List<String> url = itemDao.getUrl(itemID);
			 //등록된 이미지가 없을 경우
			 if(url.isEmpty()) {
				 url.add("0");
			 }
			 String[] ImgeUrl = url.toArray(new String[0]); // 리스트를 배열로 변환
		     item.put("ImgeUrl", ImgeUrl); // 아이템에 이미지 URL 배열 추가
		}
		//최종 가격 삽입
		usercartdto.setFinalPrice(sumPrice(userCart));
		return userCart;
	}
	
	
	@Override
	public Integer sumPrice(List<HashMap<String, Object>> userCart) {
		Integer result = 0;
		for(HashMap<String, Object> item : userCart) {
			result += (int) item.get("price");
		}		
		return result;
	}

	
	
	//결제 된 정보 표시 
	@Override
	public List<HashMap<String, Object>>  detail_Pay(UserCartDTO usercartdto ,Principal principal) {
		//url = mypage/ myPayments
		//payments get true;
		String userName = userDAO.getUserNameByuserID(principal.getName());
		usercartdto.setUserName(userName);
		List<HashMap<String, Object>> userCart = this.userCartDAO.detail_pay(usercartdto);
		return userCart;
	}

	
	
	/*결제가 성공메소드 */
	@Override
	public boolean updatePaymentsSuccess(Principal prince) throws CartException {
		String userName = userDAO.getUserNameByuserID(prince.getName());
		boolean ck = (userCartDAO.updatePaymentsSuccess(userName) != 0) ? true : false ;
		if(!ck) {
			throw new CartException(CartExceptionEnum.PAYMENTS_FAIL);
		}
		//ck가 false일 때 예외처리
		return ck;
	}

	/*결제 취소 메소드 */
	@Override
	public boolean updatePaymentFalse(Principal prince)throws CartException {
		String userName =userDAO.getUserNameByuserID(prince.getName());
		boolean ck = (userCartDAO.updatePaymentFalse(userName) == 1) ? true : false;
		//ck가 false일 때 예외처리
		if(!ck) {
			throw new CartException(CartExceptionEnum.PAYMENTS_DROPFAIL);
		}
		return ck;
	}

	/*장바구니 삭제 */
	@Override
	public boolean deleteItem(Principal principal, Integer itemID) {
		UserCartDTO userCart = new UserCartDTO();
		String userName = userDAO.getUserNameByuserID(principal.getName());
		userCart.setUserName(userName);
		userCart.setItemId(itemID);
		Integer result = userCartDAO.deleteItem(userCart);
		boolean ck = (result == 1) ? true : false; 		
		//ck가 false일 때 예오ㅓㅣ처리
		return ck;
	}

	/*장바구니 내역 전체 삭제 */
	@Override
	public boolean deleteALL(Principal principal) {
		UserCartDTO userCart = new UserCartDTO();
		String userName = userDAO.getUserNameByuserID(principal.getName());
		userCart.setUserName(userName);
		Integer result = userCartDAO.deleteALL(userCart);
		boolean ck = (result == 1) ? true : false; 		
		//ck가 false일 때 예오ㅓㅣ처리 
		return ck;
	}
	
	
	/*BisMypage에 사용 될 예약 된 정보 불러오기 */
	@Override
	public List<HashMap<String, Object>> reservationInfo(Principal principal) {
		String adminName = userDAO.getUserNameByuserID(principal.getName());
		//예약 된 사용자 조회
		List<HashMap<String, Object>> userReservationInfo 
					= userCartDAO.reservationInfo(adminName);
		
		return userReservationInfo;
	}

	

	//validationCk
	private void CartValidationCK(UserCartDTO userCart) {
		if(userCart.getItemId() == null) {
			throw new CartException(CartExceptionEnum.CART_NOTFOUND_ITEM);
		}
		
		if(userCart.getStartDate() == null) {
			throw new CartException(CartExceptionEnum.CART_STARTDATE_NULL);
			}
		
		if(userCart.getEndDate() == null) {
			throw new CartException(CartExceptionEnum.CART_ENDDATE_NULL);
			}
		
		if(userCart.getStartDate().isBefore(LocalDate.now())){
			throw new CartException(CartExceptionEnum.CART_STARTDATE_MISMATCH);
		}
		
	    if(userCart.getEndDate().isBefore(userCart.getStartDate())) {
			throw new CartException(CartExceptionEnum.CART_ENDDATE_MISMATCH2);
		}
	}

}
