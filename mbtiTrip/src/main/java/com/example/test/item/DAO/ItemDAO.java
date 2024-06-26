package com.example.test.item.DAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.test.GCSDTO.GCSDTO;
import com.example.test.POST.DTO.PostDTO;
import com.example.test.User.Service.UserHistoryService;
import com.example.test.item.DTO.ItemDTO;

import com.example.test.paging.Page;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Repository
public class ItemDAO {
	/**@version 1.0
	 * @author Shin_SungJin 
	 * Adventure와 replace 통합 DAO입니다.
	 * 
	 * */
	
	
	
	@Autowired
	private SqlSessionTemplate sqlSessiontemplate;
	
	@Autowired
	private UserHistoryService userHistoryService;
	
	private List<String> likedUser = new ArrayList<>();
	
	public List<ItemDTO> searchLocation(String location){
		//List<ItemDTO> result = this.sqlSessiontemplate.selectList("itemByLocation", location);
		List<ItemDTO> result = this.sqlSessiontemplate.selectList("item.locationSerch", location);
		return result;
	}
	
	
	public int create(ItemDTO itemdto){
		 int result = this.sqlSessiontemplate.insert("item.create", itemdto);
		 log.info("message - > viewCreateItem 실행 전 ");
		 userHistoryService.ViewCreateItem();
		 return result;
	}
	
	public List<ItemDTO> replaceList(Page page){
		List<ItemDTO> result = this.sqlSessiontemplate.selectList("item.replaceList", page);
		 return result;
	}
	
	public List<ItemDTO> adventureList(Page page){
		List<ItemDTO> result = this.sqlSessiontemplate.selectList("item.adventure", page);
		return result;
	}
	
	public int myitem(String userName){
		int result = this.sqlSessiontemplate.selectOne("item.myitem", userName);
		return result;
	}
	
	public int itemByMbti(ItemDTO itemdto){
		int result = this.sqlSessiontemplate.selectOne("item.itemByMbti", itemdto);
		return result;
	}
	
	public int itemByLocation(ItemDTO itemdto){
		int result = this.sqlSessiontemplate.selectOne("item.itemByLocation", itemdto);
		return result;
	}
	
	public int mbtiAndLocation(ItemDTO itemdto){
		int result = this.sqlSessiontemplate.selectOne("item.mbtiAndLocation", itemdto);
		return result;
	}
	
	public int deleteItem(Integer itemId){
		  int result = this.sqlSessiontemplate.delete("item.deleteItem", itemId);
		return result;
	}



	public ItemDTO findById(Integer itemid) {
		// TODO Auto-generated method stub
		return this.sqlSessiontemplate.selectOne("item.findById", itemid);
	}



	

	public int getTotal(String keyword) {
		// TODO Auto-generated method stub
		return this.sqlSessiontemplate.selectOne("item.getTotalByKeyword", keyword);
	}
	
	public Integer totalCount() {
		// TODO Auto-generated method stub
		return this.sqlSessiontemplate.selectOne("item.getTotalFull");
	}

	public int update(ItemDTO itemdto) {
		// TODO Auto-generated method stub
		return this.sqlSessiontemplate.update("item.update", itemdto);
	}

	public int createImg(GCSDTO img) {
		// TODO Auto-generated method stub
		return this.sqlSessiontemplate.insert("item.createImg",img);
	}

	public List<String> getUrl(int itemID) {
		// TODO Auto-generated method stub
		return this.sqlSessiontemplate.selectList("item.getUrl",itemID);
	}

	public List<ItemDTO> search(String keyword) {
		// TODO Auto-generated method stub
		return this.sqlSessiontemplate.selectOne("item.searchByKeyword", keyword);
	}

	
	public List<ItemDTO> search(Page page) {
		// TODO Auto-generated method stub
		return this.sqlSessiontemplate.selectOne("item.searchByPage", page);
	}


	public List<ItemDTO> listWithPage(Page page) {
		// TODO Auto-generated method stub
		return this.sqlSessiontemplate.selectList("item.listWithPage", page);
	}
	
	
	public int itemLike(Integer itemId,String usrerName) {
		// TODO Auto-generated method stub
		int result = 0;
		if(likedUser.contains(usrerName)) {
			/*throw ~~~ 이미 좋아요 누른 아이템 입니다. */
		}
		else {
			result = this.sqlSessiontemplate.update("item.likedUser", itemId);
			if(result != 0) {
				likedUser.add(usrerName);
			}
		}
		/*아직 sql 구현 안함 */
		
		return result;
	}

}
